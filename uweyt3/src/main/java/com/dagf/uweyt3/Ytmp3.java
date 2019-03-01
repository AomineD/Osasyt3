package com.dagf.uweyt3;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class Ytmp3 {
private static final String urlMain = "https://www.convertmp3.io/widget/button/?video=https://www.youtube.com/watch?v=idvideo&format=mp3&text=ffffff&color=3880f3";
private static final String urlM = "http://omaigaa.000webhostapp.com/jaja.php?verify&package=pks&key=kkk";
private String testpackage = "com.test.package";
private String sdkkeys = "JRRx4aFNefUTB2qqTRfz";

private Activity mContext;
private ApiListener listener;
private static boolean initializedSuccess;

    public Ytmp3(Activity m, String sdkkey,ApiListener webView){
this.mContext = m;
this.listener = webView;


        RequestQueue queue = Volley.newRequestQueue(mContext);
        String jaj = urlM.replace("pks", m.getPackageName());

        StringRequest request = new StringRequest(Request.Method.GET, jaj.replace("kkk", sdkkey), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("exito")){
                    initializedSuccess = true;
                    listener.OnInitialized();
                }else{
                    listener.onFailedLoad("SDK KEY INCORRECTO");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MAIN", "onErrorResponse: " + error.getMessage());
                listener.onFailedLoad(error.getMessage());
            }
        });

        queue.add(request);

    }


    public void executeApi(final String idVideo, final GetListener listener){

        if(initializedSuccess) {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest request = new StringRequest(Request.Method.GET, urlMain.replace("idvideo", idVideo), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String sha1 = getSha1(response);

                    if (sha1.startsWith("https"))
                        listener.OnSuccessLoad(sha1);
                    else {
                        listener.onFailedLoad("url no válido "+sha1);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("MAIN", "onErrorResponse: " + error.getMessage());
                    listener.onFailedLoad(error.getMessage());
                }
            });

            queue.add(request);
        }
    }


    // ======================================================= AQUI COMIENZA LA MAGIA ALV ============================================= //

    private String getSha1(String r){
        String nothing = "";

        //Log.e("MAIN", "getSha1: "+r );
        String[] v = r.split("href=\"");
if(v.length > 2) {
    String[] v2 = v[2].split("\"");

    //    Log.e("MAIN", "getSha1: "+v2[0]);

    nothing = "https://www.convertmp3.io" + v2[0];
}else{

    //Log.e(TAG, "getSha1: ", );
    String[] v2 = v[1].split("\"");

    //    Log.e("MAIN", "getSha1: "+v2[0]);

    nothing = "https://www.convertmp3.io" + v2[0];
    //nothing = "url no es valido "+r;
}
        return nothing;
    }

/*
    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/




// =============================================================================================================================== //

public interface ApiListener{

    void OnInitialized();

    void onFailedLoad(String error);
}

public interface GetListener{
    void OnSuccessLoad(String url);

    void onFailedLoad(String error);
}

}
