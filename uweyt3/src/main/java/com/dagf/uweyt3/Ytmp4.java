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

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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



    public static void playLiveVideo(final AppCompatActivity activity, String videourl, LinearLayout id_player){

        videourl = videourl.replace("https://youtu.be/", "");

        final YouTubePlayerView youtubePlayerView = new YouTubePlayerView(activity);

        id_player.addView(youtubePlayerView);

        activity.getLifecycle().addObserver(youtubePlayerView);
        youtubePlayerView.getPlayerUiController().enableLiveVideoUi(true);
        //  youtubePlayerView.getpla

        youtubePlayerView.getPlayerUiController().showDuration(false);
        youtubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youtubePlayerView.setVisibility(View.GONE);
        youtubePlayerView.getPlayerUiController().showVideoTitle(false);
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
}
