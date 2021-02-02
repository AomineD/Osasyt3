package com.dagf.uweyt3.iptv

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FileDownloader(context: Context, test: Boolean) {

    private var mContext: Context
    private var mListener: FileDownloadListener? = null
    private lateinit var mListener2: FileDownloadListenerExt
    private var bo = false;

    init {
        mContext = context

  bo = test;
        try{
            mListener2 = context as FileDownloadListenerExt
        }catch (e: Exception){
            mListener = context as FileDownloadListener
        }
    }

    fun getIPTVFile(baseUrl: String, fileName: String) {
        val apiInterface = ApiClient().getFile(baseUrl)?.create(ApiInterface::class.java)
        val file = apiInterface!!.fetchFile(fileName)
        //file.request().url().toString();
        if(bo)
      Log.e("MAIN", "url "+ file.request().url.toString());
        file.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    val `is` = response.body()!!.byteStream()
                    val parser = M3UParser()
                    val playlist = parser.parseFile(`is`)
                    if(bo)
                    Log.e("MAIN", playlist.get(0).tvURL);
try {
    mListener2.onFileDownloaded(playlist)
}catch (e: Exception) {
    mListener!!.onFileDownloaded(playlist)
}
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t is NetworkErrorException)
                    Toast.makeText(mContext, "No internet connectivity found", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
if(mListener2 != null){
    mListener2.onError(t.message!!)
}
                }
            }
        })
    }

    interface FileDownloadListener {
        fun onFileDownloaded(playlist: ArrayList<M3UItem>)
    }

    interface FileDownloadListenerExt {
        fun onFileDownloaded(playlist: ArrayList<M3UItem>)
        fun onError(erno: String)
    }
}