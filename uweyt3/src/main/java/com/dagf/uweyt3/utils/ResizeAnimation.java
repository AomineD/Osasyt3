package com.dagf.uweyt3.utils;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    final int targetHeight;
    View view;
    int startHeight;

    final int targetWidth;
    int startWidth;
    boolean reverseIt = false;
    private int porcentajeTamaño = 85;
    public ResizeAnimation(View view, int startHeight, int startWidth) {
        this.view = view;
        this.targetHeight = (startHeight * porcentajeTamaño) / 100;
        this.startHeight = startHeight;
        this.targetWidth = (startWidth * porcentajeTamaño) / 100;

     //   Log.e("MAIN", "ResizeAnimation: H: "+startHeight+ ", NH: "+targetHeight +  ", W: "+startWidth+", NW: "+ targetWidth);
        this.startWidth = startWidth;
    }

    public void setReverseIt(boolean isrev)
    {
        this.reverseIt = isrev;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = 0;
        int newWidth = 0;
        if(!reverseIt) {

            newHeight = (int) (startHeight + targetHeight * interpolatedTime);
             newWidth = (int) (startWidth + targetWidth * interpolatedTime);
        }else{
            //Log.e(TAG, "applyTransformation: "+startHeight );
            newHeight = (int) (startHeight - targetHeight * interpolatedTime);
            newWidth = (int) (startWidth - targetWidth * interpolatedTime);
        }
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
      //  Log.e("MAIN", "applyTransformation: "+newHeight+" "+newWidth );
        view.getLayoutParams().height = newHeight;
        view.getLayoutParams().width = newWidth;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        if(reverseIt){
          startHeight =  view.getLayoutParams().height;
          startWidth = view.getLayoutParams().width;
        }
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
