package com.zanganeh.ronagh

object BillingConfig {
    // Public RSA key of the app from the Cafe Bazaar developer console (not a secret).
    const val RSA_PUBLIC_KEY =
        "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC7BwonJfcUqvjntjmZZW6m2D78l+IjdNi6n/ABzITGMKmy+pyO77CFjYpE2JrNxq9uWb9Pvyu2gQ0+Vh/PYrB9R/UvH7BeeI8Gn7fV7XKX0B05yvgE1C/ksv34+rr3OQ+cOT/2jf6gDykp40h1rJE6lIZ2XyR93t6L5iX5zwWiPftHFTmmwoddz8f214jzmBzoJVXn3WMiMjC1KndUB5z49XslabhlH9sp1S6q8xkCAwEAAQ=="

    // SKU (product ID in Bazaar console) -> number of invoices it adds.
    // Must match the PACKS list in the web app.
    val PACKS: Map<String, Int> = mapOf(
        "ronagh_invoices_10" to 10,
        "ronagh_invoices_30" to 30,
        "ronagh_invoices_50" to 50,
        "ronagh_invoices_100" to 100
    )
}
