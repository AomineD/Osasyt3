package com.dagf.uweyt3.iptv

import am.appwise.components.ni.NoInternetDialog
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.dagf.uweyt3.R
import com.dagf.uweyt3.utils.UtilsIPTV
import com.facebook.ads.*
import com.google.android.gms.ads.AdRequest

public class ChannelCategoryActivity : AppCompatActivity(), FileDownloader.FileDownloadListener {

    private var mPlaylist: ArrayList<M3UItem>? = null
    private var rvChannelCategory: RecyclerView? = null
    private var channelBy: String? = null
    private var gotIPTVFile: Boolean = false;
    private var adapter: ChannelAdapter? = null
    private var progressBar: TashieLoader? = null
    private var textNoChannelFound: TextView? = null
    private var adContainer: LinearLayout? = null
    private var searchView: SearchView? = null
    private var noInternetDialog: NoInternetDialog? = null
    private lateinit var fileDownloader: FileDownloader
    companion object{
        public var ad_facebook_banner = UtilsIPTV.ad_banner
        public var channelBystat = ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_category)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressBar = findViewById(R.id.progressBar)
        adContainer = findViewById(R.id.adContainer)
        rvChannelCategory = findViewById(R.id.rvChannelCategory)
        textNoChannelFound = findViewById(R.id.textNoChannelFound)
        ad_facebook_banner = UtilsIPTV.ad_banner
        progressBar?.visibility = View.VISIBLE
        rvChannelCategory?.visibility = View.GONE
        if(intent.getStringExtra("CHANNEL_BY") != null)
        channelBy = intent.getStringExtra("CHANNEL_BY")

        supportActionBar?.title = "Channel $channelBy"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getResources()
                .getColor(R.color.colorPrimary)));

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rvChannelCategory?.addItemDecoration(dividerItemDecoration)
        fileDownloader = FileDownloader(this, false)

        when(channelBy) {
            "By Country" -> fileDownloader.getIPTVFile(ApiClient.IPTV_FILE_URL, "index.country.m3u")
            "By Category" -> fileDownloader.getIPTVFile(ApiClient.IPTV_FILE_URL, "index.category.m3u")
            "By Language" -> fileDownloader.getIPTVFile(ApiClient.IPTV_FILE_URL, "index.language.m3u")
        }

        if(UtilsIPTV.typeAd == TypeAd.ADMOB){
            var mAdView = com.google.android.gms.ads.AdView(this)
            mAdView.adSize = com.google.android.gms.ads.AdSize.BANNER
            mAdView.adUnitId = UtilsIPTV.ad_banner;
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            adContainer!!.addView(mAdView)
        }else {
      //      AdSettings.setDebugBuild(true)
        //    Log.e("MAIN", "onCreate: "+ ad_facebook_banner )
            val adView = AdView(this, ad_facebook_banner, AdSize.BANNER_HEIGHT_50)


            adView.loadAd(adView.buildLoadAdConfig().withAdListener(object : AdListener {
                override fun onError(p0: Ad?, p1: AdError?) {
                    Log.e("MAIN", "onError: " + p1!!.errorMessage)
                }

                override fun onAdLoaded(p0: Ad?) {
                    Log.e("MAIN", "onAdLoaded: " )
                }

                override fun onAdClicked(p0: Ad?) {

                }

                override fun onLoggingImpression(p0: Ad?) {

                }

            }).build())

            adContainer!!.addView(adView)
        }
       /* adsManager = AdsManager(this)
        val prefsManager = PrefsManager(this)
        if (prefsManager.getPrefs("display_ads").equals("ADMOB", true)) {
            adsManager?.loadGoogleBannerAd(adContainer!!, prefsManager.getPrefs("admob_banner_id"))
        } else if (prefsManager.getPrefs("display_ads").equals("FACEBOOK", true)) {
            adsManager?.loadFacebookBannerAd(adContainer!!, prefsManager.getPrefs("fb_banner_id"))
        } else if (prefsManager.getPrefs("display_ads").equals("ADCOLONY", true)) {
            adsManager?.loadAdColonyBannerAd(adContainer!!, prefsManager.getPrefs("adcolony_banner_id"))
        } else if (prefsManager.getPrefs("display_ads").equals("IRONSRC", true)) {
            adsManager?.loadAdIronSrcBannerAd(adContainer!!)
        }*/
    }

    override fun onFileDownloaded(playlist: ArrayList<M3UItem>) {
        gotIPTVFile = true
        mPlaylist = playlist

        if(mPlaylist?.size == 0) {
            progressBar?.visibility = View.GONE
            rvChannelCategory?.visibility = View.GONE
            textNoChannelFound?.visibility = View.VISIBLE
        }
        else {
            progressBar?.visibility = View.GONE
            rvChannelCategory?.visibility = View.VISIBLE
            when (channelBy) {
                "By Country" -> adapter =
                    ChannelAdapter(this@ChannelCategoryActivity, mPlaylist!!, false)
                "By Category", "By Language" -> adapter =
                    ChannelAdapter(this@ChannelCategoryActivity, playlist)
            }

            rvChannelCategory?.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(mPlaylist != null && mPlaylist?.size != 0) {
                    val filterChannels = mutableListOf<M3UItem>()

                    for (item: M3UItem in mPlaylist!!) {
                        if (item.tvName.contains(newText, true)) {
                            filterChannels.add(item)
                        }
                    }

                    adapter?.filterChannels(filterChannels)
                }

                return false
            }
        })

        searchView?.setOnCloseListener {
            adapter?.filterChannels(mPlaylist!!)
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun internetDialog() {
        if(noInternetDialog == null) {
            noInternetDialog = NoInternetDialog.Builder(this).apply {
                setCancelable(false)
                setBgGradientStart(Color.parseColor("#303030"))
                setBgGradientCenter(Color.parseColor("#202020"))
                setBgGradientEnd(Color.parseColor("#151515"))
            }.build()

            noInternetDialog?.setOnDismissListener{
                if(!gotIPTVFile) {
                    when(channelBy) {
                        "By Country" -> fileDownloader.getIPTVFile(ApiClient.IPTV_FILE_URL, "index.country.m3u")
                        "By Category" -> fileDownloader.getIPTVFile(ApiClient.IPTV_FILE_URL, "index.category.m3u")
                        "By Language" -> fileDownloader.getIPTVFile(ApiClient.IPTV_FILE_URL, "index.language.m3u")
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        /*if (Util.SDK_INT < 24)
            adsManager?.pauseAds()*/
        super.onPause()
    }

    override fun onStop() {
        //adsManager?.pauseAds()
        searchView?.onActionViewCollapsed()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        internetDialog()
       // adsManager?.resumeAds()
    }

    override fun onDestroy() {
        if(mPlaylist?.size != 0)
            mPlaylist?.clear()

        //adsManager?.destroyAds()
        noInternetDialog?.onDestroy()
        intent.removeExtra("CHANNEL_BY")
        super.onDestroy()
    }
}
