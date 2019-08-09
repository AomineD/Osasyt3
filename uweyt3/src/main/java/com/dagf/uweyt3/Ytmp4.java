package com.dagf.uweyt3;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

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

}
