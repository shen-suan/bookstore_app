package com.example.bst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.google.zxing.ResultPoint;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Build;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InlineScanActivity extends AppCompatActivity {
    CaptureManager captureManager;
    DecoratedBarcodeView barcodeView;
    Button button;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_scan);
        barcodeView = findViewById(R.id.barcodeView);
        captureManager = new CaptureManager(this, barcodeView);
        captureManager.initializeFromIntent(getIntent(),savedInstanceState);
        captureManager.decode();

        //button = findViewById(R.id.btnScan);
        //button.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
                txtResult = findViewById(R.id.txtResult);
                txtResult.setText("scaning...");
                barcodeView = findViewById(R.id.barcodeView);
                barcodeView.decodeSingle((new BarcodeCallback() {
                    @Override
                    public void barcodeResult(BarcodeResult result) {
                        if(result.getText() == null || result.getText().equals("")) {
                            // Prevent duplicate scans
                            return;
                        }
                        txtResult = findViewById(R.id.txtResult);
                        txtResult.setText(result.getText());

                        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        if(vib != null && vib.hasVibrator()){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                                // void vibrate (VibrationEffect vibe)
                                vib.vibrate(
                                        VibrationEffect.createOneShot(
                                                100,
                                                // The default vibration strength of the device.
                                                VibrationEffect.DEFAULT_AMPLITUDE
                                        )
                                );
                            }else{
                                // This method was deprecated in API level 26
//                                vib.vibrate(100);
                            }
                        }
                    }
                    @Override
                    public void possibleResultPoints(List<ResultPoint> resultPoints) {
                    }
                }));

            //}
        //});

    }
    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }
}
