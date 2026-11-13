# Ronagh (رونق) — Android project

Invoice and accounting app for shops. The app itself is the web page in `app/src/main/assets/www/index.html`;
this project only wraps it in a WebView and adds Cafe Bazaar in-app purchases for invoice packs.

## Build
1. Open this folder in Android Studio (Hedgehog or newer).
2. Build > Generate Signed App Bundle / APK. Create a keystore once and keep it safe.
   Losing it means you cannot publish updates.
3. Install the APK on a device with Cafe Bazaar, or upload it to the Bazaar developer console.

## Billing setup (Cafe Bazaar console)
- Set the RSA public key in `BillingConfig.kt` (already filled with the key you sent).
- Create four **managed (consumable) products** with exactly these IDs:
  - `ronagh_invoices_10`  → 10 invoices
  - `ronagh_invoices_30`  → 30 invoices
  - `ronagh_invoices_50`  → 50 invoices
  - `ronagh_invoices_100` → 100 invoices
- Prices in the console are usually in **Rial**: 20,000 Toman = 200,000 Rial, 60,000 Toman = 600,000 Rial,
  100,000 Toman = 1,000,000 Rial, 200,000 Toman = 2,000,000 Rial. Check the console's unit before publishing.
- Test purchases with a Bazaar test account before going live.

## Credits (how it works)
- Each user starts with 10 free invoices (saved in the app's local storage).
- Each saved invoice (sales or purchase) uses one credit.
- When credits reach 0 the app shows the pack list.
- A successful purchase adds the pack's invoices. Each purchase token is used only once.

## Known limits
- Credits are stored on the device only. Clearing app data or switching phones loses them, and a rooted
  device could edit them. For real money protection, verify purchases on your own server.
- Bazaar purchases only work inside the Android app, not in the web version.
