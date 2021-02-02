package com.dagf.uweyt3.iptv

import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.dagf.uweyt3.R
import com.dagf.uweyt3.utils.UtilsIPTV
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAdListener
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.*

class ExoPlayerActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null

    private lateinit var playerView: PlayerView
    private lateinit var progressBar: TashieLoader
    private lateinit var layoutContainer: RelativeLayout
    private lateinit var btnBack: ImageView
    private lateinit var channelName: TextView
    private lateinit var errorMessage: TextView
    private lateinit var btnClose: ImageView
    private lateinit var btnExternalPlayer: ImageView
    private var mInterstitialAd: InterstitialAd?=null
    private var mInterstitialAdFacebook: com.facebook.ads.InterstitialAd?=null

    companion object {
        public var ad_unit = "ca-app-pub-3940256099942544/6300978111"
        public var ad_inters_unit = "ca-app-pub-3940256099942544/1033173712"
        public var key_data = "JASKKWWWWAA"
    }

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

       //     Log.e("MAIN", "uh "+(ad_view_container!=null))
            var adWidthPixels = ad_view_container!!.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }


    private var ad_view_container:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)

     //   ad_unit = intent!!.getStringExtra(key_data)

        mInterstitialAd = InterstitialAd(this)
        ad_inters_unit = UtilsIPTV.ad_inters_admob
        ad_unit = UtilsIPTV.ad_banner
        mInterstitialAd!!.adUnitId = ad_inters_unit
        btnBack = findViewById(R.id.btnBack)
        if(UtilsIPTV.typeAd == TypeAd.ADMOB) {

            MobileAds.initialize(this) {}
            var mAdView = AdView(this)
            mAdView.adSize = AdSize.BANNER
            mAdView.adUnitId = UtilsIPTV.ad_banner;
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            mInterstitialAd!!.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    startPlayer()
                    val adRequest = AdRequest.Builder().build()
                    mAdView.loadAd(adRequest)
                }

                override fun onAdFailedToLoad(p0: Int) {
                    super.onAdFailedToLoad(p0)
                    val adRequest = AdRequest.Builder().build()
                    mAdView.loadAd(adRequest)
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    pausePlayer()
                    mInterstitialAd!!.show()
                }
            }

            mInterstitialAd!!.loadAd(AdRequest.Builder().build())

            ad_view_container!!.addView(mAdView)

        }else {


            val mAdView = com.facebook.ads.AdView(this, UtilsIPTV.ad_banner, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            //   Log.e("MAIN", "Es "+ChannelCategoryActivity.ad_facebook_banner + " Ta cargando ya")

            mInterstitialAdFacebook = com.facebook.ads.InterstitialAd(this, UtilsIPTV.ad_inters_admob)

            mInterstitialAdFacebook!!.loadAd(mInterstitialAdFacebook!!.buildLoadAdConfig().withAdListener(object : InterstitialAdListener{
                override fun onError(p0: Ad?, p1: AdError?) {
                    ad_view_container!!.removeAllViews()
                    val mAdView = com.facebook.ads.AdView(this@ExoPlayerActivity, UtilsIPTV.ad_banner, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
                    mAdView.loadAd()
                    ad_view_container!!.addView(mAdView)
                }

                override fun onAdLoaded(p0: Ad?) {
                    pausePlayer()
                    mInterstitialAdFacebook!!.show()
                }

                override fun onAdClicked(p0: Ad?) {

                }

                override fun onLoggingImpression(p0: Ad?) {

                }

                override fun onInterstitialDisplayed(p0: Ad?) {

                }

                override fun onInterstitialDismissed(p0: Ad?) {
                    startPlayer()
                    ad_view_container!!.removeAllViews()
                    val mAdView = com.facebook.ads.AdView(this@ExoPlayerActivity, UtilsIPTV.ad_banner, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
                    mAdView.loadAd()
                    ad_view_container!!.addView(mAdView)
                }

            }).build())

            mAdView.loadAd()
            ad_view_container!!.addView(mAdView)
        }




        btnClose = findViewById(R.id.btnClose)
        channelName = findViewById(R.id.channelName)
        errorMessage = findViewById(R.id.errorMessage)
        progressBar = findViewById(R.id.progressBar)
        playerView = findViewById(R.id.simpleExoPlayerView)
        layoutContainer = findViewById(R.id.layoutContainer)
        btnExternalPlayer = findViewById(R.id.btnExternalPlayer)

        btnExternalPlayer.visibility = View.GONE
        btnBack.setOnClickListener {
            finish()
        }

        btnClose.setOnClickListener {
            finish()
        }

        btnExternalPlayer.setOnClickListener {
         /*   val url = intent.getParcelableExtra<M3UItem>("CHANNEL_DETAILS")?.tvURL
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "video")
            startActivity(intent)
            finish()*/
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            val channelDetails = intent.getParcelableExtra<M3UItem>("CHANNEL_DETAILS")
            channelName.text = channelDetails?.tvName

            // 1. Create a default TrackSelector
            val loadControl = DefaultLoadControl()
            val bandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(this,
                    DefaultRenderersFactory(this),
                    trackSelector,
                    loadControl
            )
            playerView.player = player

            playerView.setControllerVisibilityListener { visibility ->
                layoutContainer.visibility = visibility
            }
            player?.addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED || !playWhenReady) {
                        playerView.keepScreenOn = false
                    } else {
                        // This prevents the screen from getting dim/lock
                        playerView.keepScreenOn = true
                        if (playbackState == Player.STATE_BUFFERING) {
                            progressBar.visibility = View.VISIBLE
                        } else if (playbackState == Player.STATE_READY) {
                            progressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    //btnExternalPlayer.visibility = View.VISIBLE
                    Toast.makeText(
                            this@ExoPlayerActivity,
                            "Currently this channel is not working",
                            Toast.LENGTH_LONG
                    ).show()
                    //    Log.e("MAIN", error.message + " "+error.cause.toString())
                }
            })

            buildMediaSource(Uri.parse(channelDetails?.tvURL))
        }
    }

    private fun buildMediaSource(uri: Uri) {

        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(this, null, UtilsIPTV.createDataSourceFactory(this, bandwidthMeter));
        // This is the MediaSource representing the media to be played.
     //   Log.e("MAIN", "url $uri")
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        // Prepare the player with the source.
        player?.prepare(mediaSource)
        player?.playWhenReady = true
      }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

    private fun pausePlayer() {
        player?.playWhenReady = false
    }

    private fun startPlayer() {
        player?.playWhenReady = true
    }

    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
        startPlayer()
    }

    public override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    public override fun onStop() {
        super.onStop()
        pausePlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        intent.removeExtra("CHANNEL_DETAILS")
        super.onDestroy()
    }
}
