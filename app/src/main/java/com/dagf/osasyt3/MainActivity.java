package com.dagf.osasyt3;

import android.Manifest;
import android.content.Context;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.dagf.uweyt3.Ytmp3;

public class MainActivity extends AppCompatActivity {

    private Ytmp3 ytmp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      ytmp3 = new Ytmp3(this, "", new Ytmp3.ApiListener() {
            @Override
            public void OnSuccessLoad(String url) {
                Toast.makeText(MainActivity.this, "SI ALFIN", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnInitialized() {
                ytmp3.executeApi("l7COUDNU80");
            }

            @Override
            public void onFailedLoad(String error) {
                Toast.makeText(MainActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });


    }


}
