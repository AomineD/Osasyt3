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

import com.dagf.uweyt3.VmeoGetter;
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

        urlYoutube.add("82747747");
        urlYoutube.add("326897828");
        urlYoutube.add("237046615");


        VmeoGetter.getAllVimeoUrl(this, urlYoutube, VmeoGetter.QualityVimeo.alta, new VmeoGetter.allLoadListener() {
            @Override
            public void onLoadSuccess(ArrayList<VmeoGetter.VimeoObject> vimeoVids) {
                for(int i=0; i < vimeoVids.size(); i++){
                    Log.e("MAIN", "onLoadSuccess: "+vimeoVids.get(i).thumb_url+ " = "+vimeoVids.get(i).url);
                }
            }

            @Override
            public void onFailed(String erno) {
                Log.e("MAIN", "onFailed: "+erno );
            }
        });


    }


}
