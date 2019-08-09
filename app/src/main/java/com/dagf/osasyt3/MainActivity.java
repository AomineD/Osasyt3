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
import com.dagf.uweyt3.Ytmp4;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Ytmp3 ytmp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> urlYoutube = new ArrayList<>();

        urlYoutube.add("https://www.youtube.com/watch?v=KtzJgiHW7Bc");
        urlYoutube.add("https://www.youtube.com/watch?v=6bMmhKz6KXg");
        urlYoutube.add("https://www.youtube.com/watch?v=VlM8BXylDoQ");


        Ytmp4.getAllUrls(this, urlYoutube, Ytmp4.Calidad.media, new Ytmp4.onGetAllUrl() {
            @Override
            public void onLoadAll(ArrayList<String> urrs) {
                for(int i=0; i < urrs.size(); i++){
                    Log.e("MAIN", "onLoadAll: "+urrs.get(i) );
                }
            }

            @Override
            public void onFails(String erno) {
                Log.e("MAIN", "onFails:error "+erno );
            }
        });


    }


}
