package dhairyapandya.com.qrcodeapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
Button Generate,Scan,Send;
ImageView QRcode;
EditText Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0FD5F8"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        Generate= findViewById(R.id.generate);
        Scan=findViewById(R.id.scan);
        Send=findViewById(R.id.send);
        QRcode=findViewById(R.id.qrimage);
        Code=findViewById(R.id.code);
        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = Code.getText().toString();
                if(data.isEmpty())
                {
                    Code.setError("Value Required");
                }
                else
                {
                    QRGEncoder qrgEncoder=new QRGEncoder(data,null, QRGContents.Type.TEXT,500);
                    try {
                        Bitmap qrBits = qrgEncoder.getBitmap();
                        QRcode.setImageBitmap(qrBits);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),scanner.class));
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable=QRcode.getDrawable();
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"pic.jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");

                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
        });
    }
}