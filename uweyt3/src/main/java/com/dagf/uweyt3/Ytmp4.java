package com.dagf.uweyt3;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class Ytmp4 {

    public interface onGetUrl{
        void onCompleteGot(String url);
        void onFail(String cause);
    }
    public interface onGetAllUrl{
        void onLoadAll(ArrayList<String> urrs);
        void onFails(String erno);
    }


    private static ArrayList<String> urls = new ArrayList<>();

    public static void getAllUrls(Context context, final ArrayList<String> s, Calidad calidad, final onGetAllUrl alllistener){
        final ArrayList<String> urlsActual = new ArrayList<>();
        try{
            for(int i=0; i < s.size(); i++){
                final int finalI = i;
                getUrlOf(context, s.get(i), calidad, new onGetUrl() {
                    @Override
                    public void onCompleteGot(String url) {
                   //     Log.e("MAIN", "onCompleteGot: "+url+ " id "+finalI );
                        if(urlsActual.size() > finalI)
                        urlsActual.add(finalI, url);
                        else
                            alllistener.onFails("Error en algun link");
                        if(finalI == s.size() - 1){
                            alllistener.onLoadAll(urlsActual);
                        }
                    }

                    @Override
                    public void onFail(String cause) {
alllistener.onFails("En el video: "+ finalI +" error => "+cause);
                    }
                });
            }
        }
catch (Exception e){
            alllistener.onFails(e.getMessage());
}
    }

    public enum Calidad{
        alta,
        baja,
        media
    }

    public static void getUrlOf(Context context, String url, final Calidad quali, final onGetUrl listener){
   //     Log.e("MAIN", "getUrlOf: "+url );
        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = 18;
                    switch (quali) {
                        case alta:
itag = 137;
                            break;
                        case media:
itag = 136;
                            break;
                        case baja:
itag = 134;
                            break;
                    }
                    String downloadUrl = ytFiles.get(itag).getUrl();
                //    Log.e("YOUTUBE::", String.valueOf(downloadUrl));

                    try {

                     //   MediaSource mediaSource = mediaSource(Uri.parse(downloadUrl));
                      //  player.prepare(mediaSource, true, false);

                        listener.onCompleteGot(downloadUrl);

                    }catch (Exception e){
listener.onFail(e.getMessage());
                    }


                }else{
                    listener.onFail("Nulo no hay nada");
                }
            }
        }.extract(url, true, true);


    }


    public interface onLoadLiveCorrect{
        void onLoad();
    }

    public static void playLiveVideo(final AppCompatActivity activity, String videourl, YouTubePlayerView youtubePlayerView, onLoadLiveCorrect liveCorrect){

        videourl = videourl.replace("https://www.youtube.com/watch?v=", "");

       /* final YouTubePlayerView youtubePlayerView = new YouTubePlayerView(activity);

        id_player.addView(youtubePlayerView);*/

        activity.getLifecycle().addObserver(youtubePlayerView);
        youtubePlayerView.getPlayerUiController().enableLiveVideoUi(true);
        //  youtubePlayerView.getpla

        youtubePlayerView.getPlayerUiController().showDuration(false);
        youtubePlayerView.getPlayerUiController().showYouTubeButton(false);
    //    youtubePlayerView.setVisibility(View.GONE);
        youtubePlayerView.getPlayerUiController().showVideoTitle(false);
        youtubePlayerView.getPlayerUiController().showFullscreenButton(false);
        youtubePlayerView.setEnableAutomaticInitialization(false);
       // youtubePlayerView.getPlayerUiController().showUi(false);
        final String finalVideourl = videourl;
        youtubePlayerView.initialize(new YouTubePlayerListener() {
            @Override
            public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

            }

            @Override
            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {
if(playerState == PlayerConstants.PlayerState.UNSTARTED){
   // Log.e("MAIN", "NO EMPEZADO");
}else if(playerState == PlayerConstants.PlayerState.PLAYING && playingFirst){
    youtubePlayerView.setVisibility(View.VISIBLE);
    liveCorrect.onLoad();
    youTubePlayer.play();
    playingFirst = false;
}
            }

            @Override
            public void onReady(@NotNull final YouTubePlayer youTubePlayer) {
                String videoId = finalVideourl;
                youTubePlayer.loadVideo(videoId, 0);
                playingFirst = true;
            }

            @Override
            public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

            }

        }, true);
    }


    private static boolean playingFirst = true;


    public static void realtimeDataViewVideo(Activity activity, String url_live, long secs,onLoadViewInterface viewInterface){

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDataView(activity, url_live, viewInterface);
                    }
                });

            }
        }, 1, secs * 1000);

    }


    private static void getDataView(Context context, String url_live, onLoadViewInterface viewInterface){

       final RequestQueue queue = Volley.newRequestQueue(context);
        final String key = "\"text\":\"";
        StringRequest request = new StringRequest(Request.Method.GET, url_live, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] r = response.split(key);
              //  Log.e("MAIN", "CONTAINS "+response.contains(key));
              //  Log.e("MAIN", "onResponse: "+r.length);
                String cero = r[2];

                cero = cero.split("w")[0];

                viewInterface.onGetView(cero.replace(" ", ""));
             //   Log.e("MAIN", "onResponse: "+cero);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MAIN", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(request);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
              //  Log.e("MAIN", "onRequestFinished: ");
                queue.getCache().clear();
            }
        });
    }

    public interface onLoadViewInterface{
        void onGetView(String v);
    }
}
