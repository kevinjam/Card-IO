package com.kevinjanvier.latterglory.cardverification;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class MainActivity extends AppCompatActivity {
    private Button scanButton;
    private TextView resultTextView;

    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    protected static final String TAG = "";
    private boolean autotestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        set the Button and text view
        scanButton = (Button) findViewById(R.id.scanButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("----Message---: ", "Card Click");
                // This method is set up as an onClick handler in the layout xml
                // e.g. android:onClick="onScanPress"

                Intent scanIntent = new Intent(MainActivity.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true); //
                scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_RESULT, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);

                scanIntent.putExtra(CardIOActivity.UI_MODE_SERVICE, true);
                scanIntent.putExtra(CardIOActivity.LAYOUT_INFLATER_SERVICE, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME,true);
                scanIntent.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, true);


                // hides the manual entry button
                // if set, developers should provide their own manual entry mechanism in the app
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false

                // matches the theme of your application
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CardIOActivity.canReadCardWithCamera()) {
            scanButton.setText("Scan a credit card");
        }
        else {
            scanButton.setText("Enter credit card information");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

            Log.d("Card Number---" +scanResult.getRedactedCardNumber(), "Card");

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }

            if (scanResult.cardholderName != null) {
                resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n";
            }
//
//            Intent intent = new Intent(MainActivity.this, ThankYou.class);
//            intent.putExtra("Card Number", resultStr);
//            startActivity(intent);
//            finish();
        }
        else {
            Log.i(TAG, "Set result: " + resultCode);
            resultStr = "Scan was canceled.";
        }
        resultTextView.setText(resultStr);

    }
}
