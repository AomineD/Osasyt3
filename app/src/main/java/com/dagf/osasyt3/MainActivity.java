package com.dagf.osasyt3;

import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.dagf.uweyt3.PornBi;
import com.dagf.uweyt3.Ytmp3;
import com.dagf.uweyt3.Ytmp4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Ytmp3 ytmp3;
    String rr = "nada";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



     //   Ytmp4.playLiveVideo(this, "https://youtu.be/05K00cYTFO8", R.id.youtube_player);




        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    PornBi b = new PornBi();

                    ArrayList<String> sr = new ArrayList<>(b.getCategories().values());


                    Log.e("MAIN", "onCreate: "+sr.size() + " count "+b);

              List<String> jaja = b.getViewUrls(sr.get(0));


                    Log.e("MAIN", "run: "+jaja.size());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MAIN", "PROBLEM "+e.getMessage() );

                }
            }
        });

     /*   final ArrayList<String> urlYoutube = new ArrayList<>();

        urlYoutube.add("https://www.youtube.com/watch?v=rKWgmeqOi8k");
      //  urlYoutube.add("326897828");
       // urlYoutube.add("237046615");

final VideoView videoView = findViewById(R.id.videow);
   Ytmp4.getUrlOf(this, "https://www.youtube.com/watch?v=rKWgmeqOi8k", Ytmp4.Calidad.media, new Ytmp4.onGetUrl() {
       @Override
       public void onCompleteGot(String url) {

           try {
               //String link="http://s1133.photobucket.com/albums/m590/Anniebabycupcakez/?action=view&amp; current=1376992942447_242.mp4";
               VideoView videoView = findViewById(R.id.videow);
               MediaController mediaController = new MediaController(MainActivity.this);
               mediaController.setAnchorView(videoView);
               Uri video = Uri.parse(url);
               videoView.setMediaController(mediaController);
               videoView.setVideoURI(video);
               videoView.start();
           } catch (Exception e) {
               // TODO: handle exception
               Toast.makeText(MainActivity.this, "Error connecting", Toast.LENGTH_SHORT).show();
           }
       }

       @Override
       public void onFail(String cause) {
           Log.e("MAIN", "onFail: "+cause );
       }
   });*/


    }


}
