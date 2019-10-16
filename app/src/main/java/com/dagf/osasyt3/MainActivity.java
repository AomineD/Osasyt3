package com.dagf.osasyt3;

import android.Manifest;
import android.content.Context;
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
import com.dagf.uweyt3.livestreaming.LiveStreamingFragment;
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


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 103);


     /*  Ytmp4.realtimeDataViewVideo(this, "https://www.youtube.com/watch?v=VDtNamYUi-c", 4,new Ytmp4.onLoadViewInterface() {
            @Override
            public void onGetView(String v) {
                Log.e("MAIN", v);
            }
        });*/

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
fragment.setUrlTo("https://www.youtube.com/watch?v=gVH6WvzwGeM");
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
}
