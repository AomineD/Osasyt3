package com.dagf.uweyt3.livestreaming;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.dagf.uweyt3.R;
import com.dagf.uweyt3.Ytmp4;
import com.dagf.uweyt3.utils.UtilsFullScreen;
import com.facebook.ads.AdSettings;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;




/**
 * A simple {@link Fragment} subclass.
 */
public class LiveStreamingFragment extends Fragment {

    public interface LiveStreamingListener{
        void onShareChat();

        void onShare();

        void onDonate();

        void onSendMessage();

        void onFollow(TextView clicked, boolean isFollowing);

        void onBack();

        void onClickUser();
    }



    public LiveStreamingListener streamingListener;

    public LiveStreamingFragment() {
        // Required empty public constructor
    }


    private String urlTo = "";
    private String name  = "";
    public boolean following = false;

    public void setAppCompatActivity(AppCompatActivity appCompatActivity, LiveStreamingListener liveStreamingListener) {
        this.appCompatActivity = appCompatActivity;
        this.streamingListener = liveStreamingListener;
    }

    private AppCompatActivity appCompatActivity;

    public void setUrlTo(String urlTo) {
        this.urlTo = urlTo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
        if(database != null){
            database = null;
            messageArrayList.clear();
            adapter.notifyDataSetChanged();
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(nameCh);
        referenceBan = database.getReference("bans");

        if(adapter == null){
            adapter = new MessageAdapter(getActivity(), messageArrayList, isAdminSender);

        }
        SetupRef();

        if(nameChView != null){
            nameChView.setText(nameCh);
        }
    }

    private String nameCh = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(getActivity() == null){
            return null;
        }


        View v = inflater.inflate(R.layout.fragment_live_streaming, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        SetupViews(v);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() == null){
            return;
        }

        if(rot == 1) {
        //    getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    private TextView nameChView;
    private TextView nameVid;
    private TextView views;
    private TextView follow_btn;

    public void changeFollowStatus(){
        if(following) {
            follow_btn.setText(getString(R.string.follow));
        }else{
            follow_btn.setText(getString(R.string.following));
        }
        following = !following;
    }

    private FloatingActionsMenu menu;

    private static int rot = 0;

    UtilsFullScreen yfull;
    private void SetupViews(View v) {
        yfull = new UtilsFullScreen(v, this);
      //  AdSettings.setDebugBuild(true);
// FULL SCREEN
        if(rot == 1){
            v.findViewById(R.id.backbuttn).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SourceLockedOrientationActivity")
                @Override
                public void onClick(View v) {
                    appCompatActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rot = 0;
                }
            });




            yfull.configFull();

        }else{
            v.findViewById(R.id.backbuttn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() != null)
                        getActivity().finish();
                }
            });
            donate_watch = v.findViewById(R.id.donate_watch);

            donate_watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAdapter();
                }
            });
        }

        v.findViewById(R.id.full_screen).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                if(rot == 0){
                    rot = 1;
                    appCompatActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }else if(rot == 1){
                    appCompatActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rot = 0;
                }
            }
        });



        menu = v.findViewById(R.id.menufloating);
        v.findViewById(R.id.share_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamingListener.onShareChat();
                menu.collapse();
            }
        });

        v.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamingListener.onShare();
            }
        });

     follow_btn = v.findViewById(R.id.follow_btn);
     if(following){
         follow_btn.setText(getString(R.string.following));
     }
     follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamingListener.onFollow((TextView)v, following);
            }
        });

        v.findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamingListener.onDonate();
                menu.collapse();
            }
        });

        YouTubePlayerView playerView = v.findViewById(R.id.player_view);
        nameChView = v.findViewById(R.id.title_ch);
        views = v.findViewById(R.id.views_player);
        list_mesg = v.findViewById(R.id.rec_chat);

        if(!urlTo.isEmpty() && !nameCh.isEmpty()) {
            Ytmp4.playLiveVideo(appCompatActivity, urlTo, playerView, new Ytmp4.onLoadLiveCorrect() {
                @Override
                public void onLoad() {
v.findViewById(R.id.loading_relay).setVisibility(View.GONE);
                }
            });
        }else
            return;

        nameChView.setText(nameCh);

        Ytmp4.realtimeDataViewVideo(appCompatActivity, urlTo, 10, new Ytmp4.onLoadViewInterface() {
            @Override
            public void onGetView(String v) {
                try {
                    if(v.contains(",")){
                        v = v.replace(",", "");

                        v = v.replace(" ", "");
                      //   Log.e("MAIN", "onGetView: "+v );
                    }

                    Number nu = Integer.parseInt(v);

                    views.setText(prettyCount(nu));
                }catch (Exception e){
                    views.setText("Error");
                    Log.e("MAIN", "ERROR "+e.getMessage());
                }
            }
        });


        /** CHAT **/
        message = v.findViewById(R.id.message_edt);
        send_msg = v.findViewById(R.id.send_btn);


        nameChView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamingListener.onClickUser();
            }
        });



        if(isDebug)
            Log.e("MAIN", "onCreateView: BASE DE DATOS => "+databaseReference.getRef().toString());
        adapter = new MessageAdapter(getContext(), messageArrayList, isAdminSender);

        adapter.setOnClickMessage(new MessageAdapter.onClickListener() {
            @Override
            public void clickingMessage(final String name) {
                final BanModel model = new BanModel();

                model.name = name;
                //  Toast.makeText(getContext(), "si "+name, Toast.LENGTH_SHORT).show();

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("Banear usuario").setMessage("¿Seguro que quieres banear a "+name+" del chat/sorteos?").setCancelable(false).create();

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Si, banear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        referenceBan.push().setValue(model);
                        Toast.makeText(getContext(), "Baneado con éxito", Toast.LENGTH_SHORT).show();
                        OnBanUserList.whenBanUser(name);
                    }
                });

                alertDialog.show();
            }
        });


        LinearLayoutManager ll = new LinearLayoutManager(getContext());

        list_mesg.setLayoutManager(ll);
        list_mesg.setAdapter(adapter);

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clikS != null){
                    clikS.OnSend();
                }
                streamingListener.onSendMessage();
                sendMessage(false);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                list_mesg.scrollToPosition(adapter.getItemCount() - 1);
            }
        });

        SetupadapteR();

    }

    private ImageView donate_watch;
    private void changeAdapter() {

        ordenedPbDonation.clear();

        String type = "3";
        for(int i=0; i < messageArrayList.size(); i++){
         //   Log.e("MAIN", "changeAdapter: "+messageArrayList.get(i).getType_mensaje() );
            if(messageArrayList.get(i).isAd){
                ordenedPbDonation.add(messageArrayList.get(i));
            }else
            if(messageArrayList.get(i).getType_mensaje().equals(type)){
                ordenedPbDonation.add(messageArrayList.get(i));
            }
        }

        type = "2";
        for(int i=0; i < messageArrayList.size(); i++){
            if(messageArrayList.get(i).isAd){
                ordenedPbDonation.add(messageArrayList.get(i));
            }else
            if(messageArrayList.get(i).getType_mensaje().equals(type)){
                ordenedPbDonation.add(messageArrayList.get(i));
            }
        }

        type = "1";
        for(int i=0; i < messageArrayList.size(); i++){
            if(messageArrayList.get(i).isAd){
                ordenedPbDonation.add(messageArrayList.get(i));
            }else
            if(messageArrayList.get(i).getType_mensaje().equals(type)){
                ordenedPbDonation.add(messageArrayList.get(i));
            }
        }



        if(ordenedPbDonation.size() < 1){
            Toast.makeText(appCompatActivity, "No donations :(", Toast.LENGTH_SHORT).show();
return;
        }else{
            list_mesg.scrollToPosition(0);
        }

            adapter.setNewList(ordenedPbDonation);


        donate_watch.setImageDrawable(getResources().getDrawable(R.drawable.ic_backi));
        donate_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setNewList(messageArrayList);
                donate_watch.setImageDrawable(getResources().getDrawable(R.drawable.ic_donate));

                donate_watch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAdapter();
                    }
                });
                list_mesg.scrollToPosition(adapter.getItemCount() - 1);

            }
        });

    }


    /** =========== ANIMATIONS ON FULL SCREEN ============= **/
    public void sendAnyMessage(String ms){
        MessageSend mss = new MessageSend();

        mss.setIsAdmin(isAdminSender ? "true" : "false");
        mss.setHora(ServerValue.TIMESTAMP);
        String men = ms;//"❤️";

        mss.setType_mensaje("8");
        mss.setMesg(men);

        mss.setSnap(identifier);
        mss.setName_of(nameF);
        mss.setUrlProfilePic(urrPhoto);
        databaseReference.push().setValue(mss);
    }


    /** COSAS COMO UTILS **/

     public interface OnClickingSend {


        void OnSend();


    }

    public interface OnClickmedia{
        void onClickMedia(String data);
    }

        public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    private boolean getTimeInInteger(Long pastvalue) {

        // long pastvalue = Long.parseLong(codigoHora);
        // Default time zone.
        DateTime zulu = DateTime.now(DateTimeZone.UTC);


        long dias = TimeUnit.MILLISECONDS.toDays(zulu.toDate().getTime() - pastvalue);


        return dias >= diasMax;




    }


    private boolean isLongTo(Long pastvalue, long max) {

        // long pastvalue = Long.parseLong(codigoHora);
        // Default time zone.
        DateTime zulu = DateTime.now(DateTimeZone.UTC);


        long dias = TimeUnit.MILLISECONDS.toDays(zulu.toDate().getTime() - pastvalue);


        return dias > max;




    }

    private ArrayList<String> nameBanneds = new ArrayList<>();
    private String mediamess;
    private String url_media;
    private String mediainfo;
    private String actionn;
    private String cl;
    public boolean isAdminSender = false;
    private void sendMessage(boolean isMedia) {
        if (database != null && (!message.getText().toString().isEmpty() || isMedia)) {
            if(nameBanneds.contains(nameF)){
                Toast.makeText(getContext(), "Has sido baneado por algun admin", Toast.LENGTH_SHORT).show();
                return;
            }
            MessageSend mss = new MessageSend();

            mss.setIsAdmin(isAdminSender ? "true" : "false");
            mss.setHora(ServerValue.TIMESTAMP);
            if(!isMedia) {
                String men = message.getText().toString();
/*
if(isRudness(men)){
    men = transform(men);
}
*/
                mss.setType_mensaje("0");
                mss.setMesg(men);
            }else{

                mss.setMesg(mediamess);
                mss.setDescmedia(mediainfo);
                mss.setUrl_img_media(url_media);
                mss.setAction(actionn);
                mss.setDataToClick(cl);
                mss.setType_mensaje("1");
            }
            mss.setSnap(identifier);
            mss.setName_of(nameF);
            mss.setUrlProfilePic(urrPhoto);
            message.setText("");
            databaseReference.push().setValue(mss);
            if (isDebug)
                Log.e("MAIN", "SendMessage: MENSAJE ENVIADO = " + mss);
            //adapter.addMessage(mss);
        }else if(message.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Mensaje vacío? oh no", Toast.LENGTH_SHORT).show();
        }
    }


    public void setUrrPhoto(String urrPhoto) {
        this.urrPhoto = urrPhoto;
    }

    private String urrPhoto = "";
    private RecyclerView list_mesg;
    private EditText message;
  //  private CircleImageView profile_pic;
  //  private TextView profile_name;
    private FloatingActionButton send_msg;
    private MessageAdapter adapter;

    private onBanUser OnBanUserList;
    public interface onBanUser{
        void whenBanUser(String username);
    }
    private boolean isDebug = false;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<MessageReceive> messageArrayList = new ArrayList<>();
    private ArrayList<MessageReceive> ordenedPbDonation = new ArrayList<>();

    public void setDebg (boolean ss){
        isDebug = ss;
    }
    public String keyactual;
    private int fr;
    public static final int mansi = 3;

    ArrayList<DataSnapshot> dataSnapshots = new ArrayList<>();
    private void SetupRef() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageReceive m = dataSnapshot.getValue(MessageReceive.class);




                if(m == null){
                    return;
                }else {
                    if(getTimeInInteger(m.getHora())){
                        dataSnapshot.getRef().removeValue();
                    }
                }



                if(dataSnapshot.getKey().equals(keyactual)){
                    return;
                }

                if(m.getType_mensaje().equals("8") && m.getMesg().contains("\uD83D\uDC4D") && rot == 1){
                    Log.e("MAIN", "onChildAdded: "+(yfull != null) );
                    yfull.createViewAnimate(0);
                }else if(m.getType_mensaje().equals("8") && m.getMesg().contains("❤️") && rot == 1){
                    yfull.createViewAnimate(1);
                }

                keyactual = dataSnapshot.getKey();


                if(isDebug){
                    Log.e("MAIN", "onChildAdded: m es null = "+dataSnapshot.getKey()+(" mensaje  = ") + (m!=null ? m.getMesg(): "ES NULL PAPA"));
                }



                dataSnapshots.add(dataSnapshot);

                if(dataSnapshots.size() >= saizMax){


                      //  dataSnapshot.getRef().removeValue();
                    while (dataSnapshots.size() >= saizMax){
                        dataSnapshots.get(0).getRef().removeValue();
                        dataSnapshots.remove(0);
                        messageArrayList.remove(0);
                    }

                    if(adapter != null)
                        adapter.notifyDataSetChanged();

                }else if(isLongTo(m.getHora(), 1)){
                     // dataSnapshot.getRef().removeValue();

                  }else{
                    if(withAds) {
                        if (fr >= mansi) {
                            MessageReceive mw = new MessageReceive();
                            mw.isAd = true;
                            adapter.addMessage(mw);
                            fr = 0;
                        } else {
                            fr++;
                        }
                    }
                }


                if(!getTimeInInteger(m.getHora()) && adapter != null) {
                    // Log.e("MAIN", "onChildAdded: "+adapter.getItemCount());
                    adapter.addMessage(m);
                }else if(adapter == null){
                    //  Log.e("MAIN", "onChildAdded: 0pp" );
                    messageArrayList.add(m);
                }



              //   Log.e("MAIN", "onChildAdded: "+messageArrayList.size() + " saizmax "+saizMax);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        referenceBan.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BanModel model = dataSnapshot.getValue(BanModel.class);

                if(model != null){
                    nameBanneds.add(model.name);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private boolean colorAssigned;

    private DatabaseReference referenceBan;
    private ArrayList<String> blockeds = new ArrayList<>();

    public void setNameP(String n, onBanUser onBanUser){
        this.nameF = n;
        this.OnBanUserList = onBanUser;
    }

    private void SetupadapteR() {

        if(ListenerMedia != null)
            adapter.setClickmedia(ListenerMedia);

        if(colorAssigned) {
            //Log.e("MAIN", "SetupadapteR: "+cc[1]);
            adapter.setAnother_cards(cc[1]);
            adapter.setCard_main(cc[2]);

            adapter.setCard_backcolor(cc[0]);

            adapter.setActiomtextcolor(cc[3]);
            adapter.setTextColorM(cc[4]);
            adapter.setTextColorMa(cc[5]);
        }

        if(withAds){
            adapter.setIdBanner(id_banner);
        }

        adapter.setIdMain(identifier);
    }

    public int[] cc = new int[6];

    public void setColors(int cardMain, int cardanother, int cardBackaction, int actioncolor, int textcolor, int textcoloran){

        cc[0] = cardBackaction;
        cc[1] = cardanother;
        cc[2] = cardMain;
        cc[3] = actioncolor;
        cc[4] = textcolor;
        cc[5] = textcoloran;
        colorAssigned = true;

    }

    private String identifier;

    public void setIdentifier(String j){
        identifier = j;
    }


    public void setListenerMedia(OnClickmedia listenerMedia) {
        ListenerMedia = listenerMedia;
    }

    private OnClickmedia ListenerMedia;



    private OnClickingSend clikS;

  /*  public void setClickSendListener(OnClickingSend sendListener){
        clikS = sendListener;
    }
*/

  public void sendDonateMessage(int quantity){


if(databaseReference == null){
   Log.e("MAIN", "Thats null");
    return;
}
      if(nameBanneds.contains(nameF)){
          Toast.makeText(getContext(), "Has sido baneado por algun admin", Toast.LENGTH_SHORT).show();
          return;
      }
      MessageSend mss = new MessageSend();

      mss.setIsAdmin(isAdminSender ? "true" : "false");
      mss.setHora(ServerValue.TIMESTAMP);

        String men = "¡Ha donado "+quantity+" PB!";
/*
if(isRudness(men)){
    men = transform(men);
}
*/
if(quantity < 25) {
    mss.setType_mensaje("1");
}else if(quantity < 70){
    mss.setType_mensaje("2");
}else{
    mss.setType_mensaje("3");
}
        mss.setMesg(men);
      mss.setSnap(identifier);
      mss.setName_of(nameF);
      mss.setUrlProfilePic(urrPhoto);
     // message.setText("");
      databaseReference.push().setValue(mss);

  }
    private String nameF = "NO NAME";

    public void setWithAds(boolean withAds, String adbanner) {
        this.withAds = withAds;
        this.id_banner = adbanner;
    }

    private boolean withAds;
    private String id_banner;
    private long diasMax = 5;
    private int saizMax = 1000;

    /**
     Si pasa de estos días sera borrado el mensaje
     **/
    public void setDiasMaximos(long diasmax){
        this.diasMax = diasmax;
    }


    public void setMaxMessage(int max){
        this.saizMax = max;
    }
}
