package com.dagf.uweyt3.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dagf.uweyt3.iptv.ChannelAdapter;
import com.dagf.uweyt3.iptv.ChannelCategoryActivity;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;


public class UtilsIPTV {
    public static String ad_banner;

    public static String banner_audience = "";
    public static String ad_inters_admob = "ca-app-pub-3940256099942544/1033173712";
public static onClickChannel clickChannel;


    public static void startViewIPTV(Context context, String forwhat, String ad_banners, onClickChannel onClickChannel){
Intent intent = new Intent(context, ChannelCategoryActivity.class);
        intent.putExtra("CHANNEL_BY", forwhat);

        ad_banner = ad_banners;
        context.startActivity(intent);
        clickChannel = onClickChannel;
    }

    public interface onClickChannel{
        void onCliked(Bundle bundle);

        void clickCountry();
    }



    public static DefaultDataSourceFactory createDataSourceFactory(Context context, TransferListener listener) {
        // Default parameters, except allowCrossProtocolRedirects is true

        String userAgent = Util.getUserAgent(context, context.getApplicationInfo().name);

        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                listener,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                context,
                listener,
                httpDataSourceFactory
        );

        return dataSourceFactory;
    }



    public static void goUfo(){}
}
