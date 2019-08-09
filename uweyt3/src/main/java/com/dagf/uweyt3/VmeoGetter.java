package com.dagf.uweyt3;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VmeoGetter {

    public interface LoadListener{
        void onLoadSuccess(String url, String thumb);

        void onFailed(String erno);
    }

    public interface allLoadListener{
        void onLoadSuccess(ArrayList<VimeoObject> vimeoVids);

        void onFailed(String erno);
    }


    public enum QualityVimeo{
        baja,
        media,
        alta
    }

    public static class VimeoObject
    {
        public VimeoObject(){

        }
        public String url;
        public String thumb_url;
    }

    public static void getAllVimeoUrl(Context c, final ArrayList<String> ids, final QualityVimeo calidad, final allLoadListener allLoadListener){
        final ArrayList<VimeoObject> rrarai = new ArrayList<>();
        for(int i=0; i < ids.size(); i++){
            final int finalI = i;
            getVimeoUrl(c, ids.get(i), calidad, new LoadListener() {
                @Override
                public void onLoadSuccess(String url, String thumb) {
                    VimeoObject ob = new VimeoObject();
                    ob.url = url;
                    ob.thumb_url = thumb;
                    rrarai.add(ob);
                    if(ids.size() - 1 == finalI){
                        allLoadListener.onLoadSuccess(rrarai);
                    }
                }

                @Override
                public void onFailed(String erno) {
allLoadListener.onFailed(erno);
                }
            });


        }
    }

    public static void getVimeoUrl(Context c, String id, final QualityVimeo calidad, final LoadListener listener){

        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.GET, "https://player.vimeo.com/video/"+id+"/config", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                  getConfigureUrl(response, calidad, listener);
                  //  listener.onCompleteGot(urlneed);
                } catch (JSONException e) {
                    listener.onFailed(e.getMessage());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
listener.onFailed("Error response: "+error.getMessage());
            }
        });



        queue.add(request);


    }

    private static String getConfigureUrl(String response, QualityVimeo calidad, LoadListener listener) throws JSONException {
        String ur = "";

        String thumb = "";

        JSONObject mainob = new JSONObject(response);

        /** ============================================== **/
        if(mainob.has("video")){

            JSONObject obvideo = mainob.getJSONObject("video");

            if(obvideo.has("thumbs")) {

                JSONObject thmb = obvideo.getJSONObject("thumbs");

                switch (calidad){
                    case baja:
                        thumb = thmb.getString("640");
                        break;
                    case media:
                        thumb = thmb.getString("960");
                        break;
                    case alta:
                        thumb = thmb.getString("1280");
                        break;
                }

            }
        }

        /** ==================================== **/


        if(mainob.has("request")){
            JSONObject requestob = mainob.getJSONObject("request");


            if(requestob.has("files")){
                JSONObject files = requestob.getJSONObject("files");

                JSONArray progresive = files.getJSONArray("progressive");

                for(int i =0; i < progresive.length(); i++){
                    JSONObject obpro = progresive.getJSONObject(i);
                    switch (calidad){
                        case baja:

                            if(obpro.has("quality") && obpro.getString("quality").equals("360p"))
                            {
                                ur = obpro.getString("url");
                                break;
                            }else if(obpro.has("quality") && obpro.getString("quality").equals("540p")){
                                ur = obpro.getString("url");
                                break;
                            }
                            break;
                        case media:
                            if(obpro.has("quality") && obpro.getString("quality").equals("540p"))
                            {
                                ur = obpro.getString("url");
                                break;
                            }else if(obpro.has("quality") && obpro.getString("quality").equals("360p")){
                                ur = obpro.getString("url");
                                break;
                            }
                            break;
                        case alta:
                            if(obpro.has("quality") && obpro.getString("quality").equals("1080p"))
                            {
                                ur = obpro.getString("url");
                                break;
                            }else if(obpro.has("quality") && obpro.getString("quality").equals("720p")){
                                ur = obpro.getString("url");
                                break;
                            }
                            break;
                            default:
                                ur = obpro.getString("url");
                                break;
                    }
                }
            }


        }



        listener.onLoadSuccess(ur, thumb);

        return ur;
    }

}
