package com.dagf.osasyt3;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dagf.uweyt3.DonationDialog;
import com.dagf.uweyt3.Ytmp4;
import com.dagf.uweyt3.iptv.ChannelCategoryActivity;
import com.dagf.uweyt3.iptv.ExoPlayerActivity;
import com.dagf.uweyt3.iptv.TypeAd;
import com.dagf.uweyt3.livestreaming.LiveStreamingFragment;
import com.dagf.uweyt3.utils.UtilsIPTV;
import com.facebook.ads.AdSettings;
import com.google.android.exoplayer2.C;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.dagf.osasyt3.R;

public class MainActivity extends AppCompatActivity {

 //   private Ytmp3 ytmp3;

    String rr = "nada";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdSettings.setDebugBuild(true);

    UtilsIPTV.startViewIPTV(this, TypeAd.AUDIENCEFB,"By Category", "359004844525143_855073358251620", new UtilsIPTV.onClickChannel() {
        @Override
        public void onCliked(Bundle bundle) {
            bundle.putBoolean("is_iptv", true);
            bundle.putBoolean("is_new", true);
           OpenPlayerAgain(bundle);
        }

        @Override
        public void clickCountry() {
            Toast.makeText(MainActivity.this, "clicked country", Toast.LENGTH_SHORT).show();
        }
    });

/*

       ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 103);


       Ytmp4.realtimeDataViewVideo(this, "https://www.youtube.com/watch?v=HXQSTcK8jkk", 4,new Ytmp4.onLoadViewInterface() {
            @Override
            public void onGetView(String v) {
                Log.e("MAIN", v);
            }
        });

        LiveStreamingFragment fragment = new LiveStreamingFragment();

        fragment.setAppCompatActivity(this, new LiveStreamingFragment.LiveStreamingListener() {
            @Override
            public void onShareChat() {
                Toast.makeText(MainActivity.this, "Share chat", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onShare() {
                Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDonate() {
                //Toast.makeText(MainActivity.this, "Donation", Toast.LENGTH_SHORT).show();

                DonationDialog donationDialog = new DonationDialog(MainActivity.this);

                donationDialog.setListener(new DonationDialog.onDonateListener() {
                    @Override
                    public void onTryDonate(int value) {
                        Toast.makeText(MainActivity.this, "VALOR "+value+" PB", Toast.LENGTH_SHORT).show();
                  fragment.sendDonateMessage(value);
                    }
                });

                donationDialog.show();
            }

            @Override
            public void onSendMessage() {
                Toast.makeText(MainActivity.this, "send", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFollow(TextView clicked, boolean following) {
               fragment.changeFollowStatus();
            }

            @Override
            public void onBack() {
                Toast.makeText(MainActivity.this, "Backing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickUser() {

            }
        });
        fragment.setName("Diego");

        fragment.setNameCh("Diego's Channel");
fragment.setUrlTo("https://www.youtube.com/watch?v=HXQSTcK8jkk");
fragment.setIdentifier("Diego/LAN");
fragment.setDiasMaximos(1);
fragment.setUrrPhoto("https://www.salsalol.com/images/champions/Illaoi.png?v=32?1569462185");
fragment.setMaxMessage(8);
fragment.setNameP("Diego", new LiveStreamingFragment.onBanUser() {
    @Override
    public void whenBanUser(String username) {

    }
});
fragment.setWithAds(true, "2505373932857364_2505378202856937");
fragment.setDebg(true);
//fragment.isAdminSender = true;
getSupportFragmentManager().beginTransaction().replace(R.id.layad, fragment).commitAllowingStateLoss();


//fragment.sendDonateMessage(90);
        //   videoView.start();

       /* Ytmp4.getUrlOf(this, "https://www.youtube.com/watch?v=WQVghd0gil4", Ytmp4.Calidad.alta, new Ytmp4.onGetUrl() {
            @Override
            public void onCompleteGot(String url) {
                Log.e("MAIN", "onCompleteGot: "+url );
            }

            @Override
            public void onFail(String cause) {
                Log.e("MAIN", "onFail: "+cause );
            }
        });*/

    }




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
    }

    public void OpenPlayerAgain(Bundle b) {

            String packageName = "net.media.invegas";//Tools.players.get(0).packageName;
            try {
                Intent mx = new Intent(Intent.ACTION_VIEW);
                mx.setPackage("net.media.invegas");//Tools.players.get(0).packageName);
                mx.setDataAndType(Uri.parse("https://srv-file6.gofile.io/download/DInk05/View_From_A_Blue_Moon_Trailer-720p.mp4"), "video/*");
                mx.putExtras(b);


                // mx.putExtra("from_start", false);

                startActivity(mx);
            }  catch (Exception e) {
                Log.e("MAIN", "OpenWithMX: " + e.getMessage());

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (ActivityNotFoundException ee) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }

            }
        }

}
