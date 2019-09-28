package com.dagf.uweyt3;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class DonationDialog extends AlertDialog {
    public DonationDialog(Context context) {
        super(context);
    }

    protected DonationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private RadioGroup radioGroup;
    private View customValue;
    private EditText valuec;


    public interface onDonateListener{
        void onTryDonate(int value);
    }

    private onDonateListener listener;

    public void setListener(onDonateListener s){
        this.listener = s;
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.donation_dialog);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(getContext(), R.color.tran));

        radioGroup = findViewById(R.id.radioGroup);

customValue = findViewById(R.id.valueCustom);
valuec = findViewById(R.id.valuec);
RadioButton bt1 = findViewById(R.id.radioButton1);
            RadioButton bt2 = findViewById(R.id.radioButton2);
            RadioButton bt3 = findViewById(R.id.radioButton3);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == bt3.getId()){
customValue.setVisibility(View.VISIBLE);
                }else{
                    customValue.setVisibility(View.GONE);
                }

                Log.e("MAIN", "onCheckedChanged: "+checkedId);
            }
        });

    findViewById(R.id.cancel_donate).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    });

    findViewById(R.id.kk).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           if(bt1.getId() == radioGroup.getCheckedRadioButtonId()) {
               listener.onTryDonate(15);

           }else if(bt2.getId() == radioGroup.getCheckedRadioButtonId())

                    listener.onTryDonate(40);
           else if(bt3.getId() == radioGroup.getCheckedRadioButtonId()) {
               int value = Integer.parseInt(valuec.getText().toString());

               if (value > 0) {
                   listener.onTryDonate(value);
               } else {
                   listener.onTryDonate(0);
               }
           }else{
                Toast.makeText(getContext(), "No value", Toast.LENGTH_SHORT).show();
            }

            dismiss();

        }
    });

    }




}
