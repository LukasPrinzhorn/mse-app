package com.example.weihnachtmaerkte;

import androidx.annotation.Nullable;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.journeyapps.barcodescanner.CaptureActivity;

public class QRScannerActivity extends CaptureActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_q_r_scanner);
    }
}
