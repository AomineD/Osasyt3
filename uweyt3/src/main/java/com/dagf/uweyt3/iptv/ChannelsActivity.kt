package com.dagf.uweyt3.iptv

import am.appwise.components.ni.NoInternetDialog
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.dagf.uweyt3.R

class ChannelsActivity : AppCompatActivity(), FileDownloader.FileDownloadListener {

    private var mPlaylist: ArrayList<M3UItem>? = null
    private var adapter: ChannelAdapter? = null
    private var rvChannelCategory: RecyclerView? = null
    private var progressBar: TashieLoader? = null
    private var textNoChannelFound: TextView? = null
    private var adContainer: LinearLayout? = null
    private var searchView: SearchView? = null
    private var channels: M3UItem? = null
    private var gotIPTVFile: Boolean = false;
    private var noInternetDialog: NoInternetDialog? = null
    private lateinit var fileDownloader: FileDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressBar = findViewById(R.id.progressBar)
        rvChannelCategory = findViewById(R.id.rvChannelCategory)
        textNoChannelFound = findViewById(R.id.textNoChannelFound)
        adContainer = findViewById(R.id.adContainer)

        progressBar?.visibility = View.VISIBLE
        rvChannelCategory?.visibility = View.GONE

        channels = intent.getParcelableExtra<M3UItem>("CHANNELS")
        supportActionBar?.title = "${channels?.tvName} Channels"

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rvChannelCategory?.addItemDecoration(dividerItemDecoration)
        fileDownloader = FileDownloader(this)
        fileDownloader.getIPTVFile(ApiClient.IPTV_URL, channels?.tvURL ?: "")

        /*adsManager = AdsManager(this)
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
            adapter = ChannelAdapter(this@ChannelsActivity, mPlaylist!!, false, true)
            rvChannelCategory?.adapter = adapter
        }
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
                if(!gotIPTVFile)
                    fileDownloader.getIPTVFile(ApiClient.IPTV_URL, channels?.tvURL ?: "")
            }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
    /*    if (Util.SDK_INT < 24)
            adsManager?.pauseAds()*/
        super.onPause()
    }

    override fun onStop() {
       // adsManager?.pauseAds()
        searchView?.onActionViewCollapsed()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        internetDialog()
        //adsManager?.resumeAds()
    }

    override fun onDestroy() {
        if(mPlaylist?.size != 0)
            mPlaylist?.clear()

       // adsManager?.destroyAds()
        noInternetDialog?.onDestroy()
        intent.removeExtra("CHANNELS")
        super.onDestroy()
    }
}
