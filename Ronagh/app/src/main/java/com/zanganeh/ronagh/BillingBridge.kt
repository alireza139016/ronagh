package com.zanganeh.ronagh

import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.PurchaseRequest
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import org.json.JSONObject

/**
 * Bridge between the web app (window.RonaghBilling.buy(sku)) and Cafe Bazaar (Poolakey).
 * Results are sent to the page as window.onBillingResult({ok, sku, token, msg}).
 * Follows the official Poolakey guide: connect -> purchaseProduct -> consumeProduct.
 */
class BillingBridge(private val activity: ComponentActivity, private val webView: WebView) {

    private val payment = Payment(
        context = activity,
        config = PaymentConfiguration(
            localSecurityCheck = SecurityCheck.Enable(rsaPublicKey = BillingConfig.RSA_PUBLIC_KEY)
        )
    )

    private var paymentConnection: ir.cafebazaar.poolakey.Connection? = null
    private var connected = false

    fun start() {
        paymentConnection = payment.connect {
            connectionSucceed {
                connected = true
                restorePurchases()
            }
            connectionFailed { _ -> connected = false }
            disconnected { connected = false }
        }
    }

    fun stop() {
        paymentConnection?.disconnect()
        paymentConnection = null
        connected = false
    }

    @JavascriptInterface
    fun buy(sku: String) {
        if (!BillingConfig.PACKS.containsKey(sku)) {
            send(false, sku, "", "بسته نامعتبر است")
            return
        }
        activity.runOnUiThread {
            if (!connected) {
                send(false, sku, "", "اتصال به بازار برقرار نیست؛ اینترنت و ورود به حساب بازار را بررسی کنید")
                return@runOnUiThread
            }
            payment.purchaseProduct(
                registry = activity.activityResultRegistry,
                request = PurchaseRequest(productId = sku, payload = "ronagh")
            ) {
                purchaseFlowBegan { }
                failedToBeginFlow { _ -> send(false, sku, "", "باز کردن صفحهٔ پرداخت ممکن نشد") }
                purchaseSucceed { entity ->
                    send(true, entity.productId, entity.purchaseToken, "")
                    consume(entity.purchaseToken)
                }
                purchaseCanceled { send(false, sku, "", "خرید لغو شد") }
                purchaseFailed { _ -> send(false, sku, "", "خطا در خرید") }
            }
        }
    }

    /** Credits purchases that were paid but not yet handed to the page (e.g. app closed mid-way). */
    private fun restorePurchases() {
        payment.getPurchasedProducts {
            querySucceed { purchasedProducts ->
                purchasedProducts.forEach { info ->
                    val entity = info.purchaseEntity ?: return@forEach
                    if (BillingConfig.PACKS.containsKey(entity.productId)) {
                        send(true, entity.productId, entity.purchaseToken, "")
                        consume(entity.purchaseToken)
                    }
                }
            }
            queryFailed { _ -> }
        }
    }

    private fun consume(purchaseToken: String) {
        payment.consumeProduct(purchaseToken) {
            consumeSucceed { }
            consumeFailed { _ -> }
        }
    }

    private fun send(ok: Boolean, sku: String, token: String, msg: String) {
        val json = JSONObject()
            .put("ok", ok)
            .put("sku", sku)
            .put("token", token)
            .put("msg", msg)
            .toString()
        activity.runOnUiThread {
            webView.evaluateJavascript("if(window.onBillingResult){window.onBillingResult($json)}", null)
        }
    }
}
