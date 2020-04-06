package com.dagf.uweyt3.utils;

import android.animation.Animator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.dagf.uweyt3.R;
import com.dagf.uweyt3.livestreaming.LiveStreamingFragment;
import com.dagf.uweyt3.livestreaming.MessageSend;
import com.google.firebase.database.ServerValue;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class UtilsFullScreen {
    private View generalView;
    private LiveStreamingFragment fragment;
    public UtilsFullScreen(View v, LiveStreamingFragment liveStreamingFragment){
        generalView = v;
        this.fragment = liveStreamingFragment;
    }

    private View grande, like, donate;
    private  View heart;
    private static boolean isSized = false;
    public void configFull(){

        grande = generalView.findViewById(R.id.lay_bottom);
        heart = generalView.findViewById(R.id.heart);
        like = generalView.findViewById(R.id.like);
        donate = generalView.findViewById(R.id.donate_inm);
timer = new Timer();
     //   heart.setProgress(0.7f);
   grande.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

openUp();

       }
   });



    }

    private void regenerateTimer(){
        timer.cancel();
        timer.purge();
        timer = new Timer();

        timer.schedule(reverseSize(), 6000);
    }

    private void cancelAbsolute(){
        timer.cancel();
        timer.purge();
        timer = new Timer();

        timer.schedule(reverseSize(), 6);
      //  Log.e("MAIN", "cancelAbsolute: hagalee" );
    }

    private Timer timer;

    private ResizeAnimation resizeAnimGrande;
    private ResizeAnimation resizeAnimation1;
    private ResizeAnimation resizeAnimation2;
    private ResizeAnimation resizeAnimation3;

    private TimerTask reverseSize(){
        return new TimerTask() {
            @Override
            public void run() {
                //ResizeAnimation resizeAnimation = new ResizeAnimation(grande, grande.getHeight(), grande.getWidth());

                resizeAnimGrande.reverseIt = true;
                resizeAnimation1.reverseIt = true;
                resizeAnimation2.reverseIt = true;
                resizeAnimation3.reverseIt = true;
            //    resizeAnimation.setDuration(1800);

                resizeAnimGrande.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        resetSize();
isSized = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                grande.startAnimation(resizeAnimGrande);

                heart.startAnimation(resizeAnimation1);
                like.startAnimation(resizeAnimation2);
                donate.startAnimation(resizeAnimation3);

            }
        };
    }


    private void playHeart(View view) {
        View heart_ic = view.findViewById(R.id.heart);
        fragment.sendAnyMessage("❤️");
        buttonEffect(heart_ic);
        cancelAbsolute();
resetSize();
    }

    private void playLike(View view) {
        View like_ic = view.findViewById(R.id.like);
        fragment.sendAnyMessage("\uD83D\uDC4D");
        buttonEffect(like_ic);
        cancelAbsolute();
        resetSize();
    }


    private void resetSize(){
        generalView.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUp();
            }
        });

        generalView.findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUp();
            }
        });
        generalView.findViewById(R.id.donate_inm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUp();
            }
        });

        // TOUCH

        generalView.findViewById(R.id.like).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        generalView.findViewById(R.id.heart).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        generalView.findViewById(R.id.donate_inm).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    /** ANIMATION ON **/


    private int sizeInDp = 6;
    private void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(fragment.getContext() != null) {
                            float scale = fragment.getContext().getResources().getDisplayMetrics().density;
                            int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
                            v.setPadding(0, dpAsPixels, 0, 0);
                            Log.e("MAIN", "onTouch: "+dpAsPixels + " / "+v.getPaddingTop());
                            v.postInvalidate();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.setPadding(0, 0, 0, 0);
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    /** Likes and heart **/

int distanceY = -300;
    public void createViewAnimate(int type){
        if(fragment.getActivity() == null){
            return;
        }

        AnimationSet an = new AnimationSet(true);


        View v = null;
        RelativeLayout main = generalView.findViewById(R.id.inflaterContainerLL);

        int randis = randomGetXs();
        if(type == 0){

            Animation trAnimation = new TranslateAnimation(0,  randis, 0, distanceY);

       //     trAnimation.setRepeatMode(Animation.REVERSE); // This will make the view translate in the reverse direction

            an.addAnimation(trAnimation);
            Animation anim = new AlphaAnimation(1.0f, 0.0f);
            an.addAnimation(anim);

            v =  LayoutInflater.from(fragment.getActivity()).inflate(R.layout.reaction_like, (ViewGroup) generalView, false);
        }else{
            v =  LayoutInflater.from(fragment.getActivity()).inflate(R.layout.reaction_heart, (ViewGroup) generalView, false);
            Animation trAnimation = new TranslateAnimation(0, randis, 0, distanceY);

    //        trAnimation.setRepeatMode(Animation.REVERSE); // This will make the view translate in the reverse direction

            an.addAnimation(trAnimation);
            Animation anim = new AlphaAnimation(1.0f, 0.0f);
            an.addAnimation(anim);
        }



        main.addView(v);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                generalView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main.removeViewAt(0);
                    }
                }, 5);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(v.getLayoutParams());
        int ran = randomGet();


        params.setMargins(ran, 0, 0,0);
       //Log.e("MAINU", "random: "+randis );
        lastRandom = ran + 18;

        if(lastRandom >= max){
           // Log.e("MAIN", "createViewAnimate: "+lastRandom );
            lastRandom = 0;
        }

        v.setLayoutParams(params);
        an.setDuration(3000);
        v.startAnimation(an);
    }

    int max = 145;
    int min = 0;
    private int lastRandom = 0;
    private int randomGet(){
        Random r = new Random();
      //  Log.e("MAIN", "randomGet: "+lastRandom );
        int i1 = r.nextInt(max - lastRandom) + lastRandom;
    return i1;
    }

    int anotherMax = 145;
    private int randomGetXs(){

        Random r = new Random();
        int i1 = r.nextInt(anotherMax - min) + min;
        min += 40;
        if(anotherMax == 0){
            anotherMax = max;
        }

        if(min >= max){
            min = -145;
            anotherMax = 0;
    //        Log.e("MAINU", "randomGetXs cambio: "+min );
        }
     //   Log.e("MAINU", "randomGetXs: RANDOM "+i1 + " "+min );
        return i1;
    }

    /** OPEEEEEEEEEEEEEEEEEEEEEN **/

    void openUp(){
        if(!isSized) {

            resizeAnimGrande = new ResizeAnimation(grande, grande.getHeight(), grande.getWidth());
            resizeAnimation1 = new ResizeAnimation(heart, heart.getHeight(), heart.getWidth());
            resizeAnimation2 = new ResizeAnimation(like, like.getHeight(), like.getWidth());
            resizeAnimation3 = new ResizeAnimation(donate, donate.getHeight(), donate.getWidth());

            resizeAnimGrande.setDuration(1800);
            resizeAnimation1.setDuration(1800);
            resizeAnimation2.setDuration(1800);
            resizeAnimation3.setDuration(1800);

            grande.startAnimation(resizeAnimGrande);
            heart.startAnimation(resizeAnimation1);
            like.startAnimation(resizeAnimation2);
            donate.startAnimation(resizeAnimation3);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playLike(v);
                    //  regenerateTimer();
                }
            });



            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playHeart(v);
                    // regenerateTimer();
                }
            });

            donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment.streamingListener.onDonate();
                    regenerateTimer();
                }
            });
            isSized = true;

            timer.schedule(reverseSize(), 6000);
        }
    }

}
