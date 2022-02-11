package dhairyapandya.com.qrcodeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class scanner extends AppCompatActivity {
CodeScannerView Scanner;
TextView COde;
CodeScanner codescanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        COde=findViewById(R.id.gettingtext);
        Scanner=findViewById(R.id.scannerview);
        codescanner=new CodeScanner(this,Scanner);
        codescanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            COde.setText(result.getText());
                    }
                });
            }
        });

        Scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codescanner.startPreview();
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
//        codescanner.startPreview();
        requestforcamera();
    }

    private void requestforcamera() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        codescanner.startPreview();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(scanner.this, "Camera Permission is Required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    @Override
    protected void onPause() {
        codescanner.releaseResources();
        super.onPause();
    }

}