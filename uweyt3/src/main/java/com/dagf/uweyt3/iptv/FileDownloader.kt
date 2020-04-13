package com.dagf.uweyt3.iptv

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FileDownloader(context: Context) {

    private var mContext: Context
    private var mListener: FileDownloadListener

    init {
        mContext = context
        mListener = context as FileDownloadListener
    }

    fun getIPTVFile(baseUrl: String, fileName: String) {
        val apiInterface = ApiClient().getFile(baseUrl)?.create(ApiInterface::class.java)
        val file = apiInterface!!.fetchFile(fileName)
        //file.request().url().toString();
      //  Log.e("MAIN", "url "+ file.request().url.toString());
        file.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    val `is` = response.body()!!.byteStream()
                    val parser = M3UParser()
                    val playlist = parser.parseFile(`is`)
                    Log.e("MAIN", playlist.get(0).tvURL);
                    mListener.onFileDownloaded(playlist)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t is NetworkErrorException)
                    Toast.makeText(mContext, "No internet connectivity found", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface FileDownloadListener {
        fun onFileDownloaded(playlist: ArrayList<M3UItem>)
    }
}