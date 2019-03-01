package com.dagf.osasyt3;

import android.Manifest;
import android.content.Context;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
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

      ytmp3 = new Ytmp3(this, "8HduqvJn6In4DgmtigLl", new Ytmp3.ApiListener() {


            @Override
            public void OnInitialized() {
                ytmp3.executeApi("Nce39E8VXaw", new Ytmp3.GetListener() {
                    @Override
                    public void OnSuccessLoad(String url) {
                        Log.e("MAIN", "OnSuccessLoad: "+url);
                    }

                    @Override
                    public void onFailedLoad(String error) {

                    }
                });
            }

            @Override
            public void onFailedLoad(String error) {
                Toast.makeText(MainActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });


    }


}
