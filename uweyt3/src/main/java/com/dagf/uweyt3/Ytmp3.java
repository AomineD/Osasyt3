package com.dagf.uweyt3;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Ytmp3 {
private static final String urlMain = "https://www.convertmp3.io/widget/button/?video=https://www.youtube.com/watch?v=idvideo&format=mp3&text=ffffff&color=3880f3";
private Activity mContext;
private ApiListener listener;
    public Ytmp3(Activity m, ApiListener webView){
this.mContext = m;
this.listener = webView;
    }


    public void executeApi(final String idVideo){

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.GET, urlMain.replace("idvideo", idVideo), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
String sha1 = getSha1(response);

if(sha1.startsWith("https"))
listener.OnSuccessLoad(sha1);
else{
    listener.onFailedLoad("url no válido");
}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MAIN", "onErrorResponse: "+error.getMessage());
                listener.onFailedLoad(error.getMessage());
            }
        });

        queue.add(request);
    }


    // ======================================================= AQUI COMIENZA LA MAGIA ALV ============================================= //

    private String getSha1(String r){
        String nothing = "";

        //Log.e("MAIN", "getSha1: "+r );
        String[] v = r.split("href=\"");

        String[] v2 = v[2].split("\"");

    //    Log.e("MAIN", "getSha1: "+v2[0]);

        nothing = "https://www.convertmp3.io"+v2[0];
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
    void OnSuccessLoad(String url);

    void onFailedLoad(String error);
}

}
