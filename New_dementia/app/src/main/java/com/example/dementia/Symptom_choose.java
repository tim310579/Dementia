package com.example.dementia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Symptom_choose extends AppCompatActivity {

    private TextView tv_show_number, tv_show_name, tv_show_time;
    private String name, time;
    private String number;
    private Button bn_back, bn_next, bn_check_history;

    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5,
            checkbox6, checkbox7, checkbox8, checkbox9, checkbox10,
            checkbox11, checkbox12, checkbox13;
    //private String all_symptom="";
    private int[] symptom_array = {0,0,0,0,0,0,0,0,0,0,0,0,0}; //1 or 0 -> checked or not
    private String[] all_symptom = {"妄想", "幻覺", "激動/攻擊性", "憂鬱/情緒不佳",
            "焦慮", "昂然自得/欣快感", "冷漠/毫不在意", "言行失控", "暴躁易怒/情緒易變",
            "怪異動作", "睡眠/夜間行為", "食慾/飲食行為改變", "跌倒"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_choose);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        //number = gv.get_number();
        //name = gv.get_name();

        tv_show_number = (TextView) findViewById(R.id.tv_show_number);
        tv_show_name = (TextView) findViewById(R.id.tv_show_name);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);

        tv_show_number.setText(gv.get_number());
        tv_show_name.setText(gv.get_name());
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
        //System.out.println(timeStamp);
        tv_show_time.setText(timeStamp);

        bn_back = (Button) findViewById(R.id.bn_back);
        bn_back.setOnClickListener(new ButtonClickListener());
        bn_next = (Button) findViewById(R.id.bn_next);
        bn_next.setOnClickListener(new ButtonClickListener());

        checkbox1 = (CheckBox)findViewById(R.id.chk1);
        checkbox2 = (CheckBox)findViewById(R.id.chk2);
        checkbox3 = (CheckBox)findViewById(R.id.chk3);
        checkbox4 = (CheckBox)findViewById(R.id.chk4);
        checkbox5 = (CheckBox)findViewById(R.id.chk5);
        checkbox6 = (CheckBox)findViewById(R.id.chk6);
        checkbox7 = (CheckBox)findViewById(R.id.chk7);
        checkbox8 = (CheckBox)findViewById(R.id.chk8);
        checkbox9 = (CheckBox)findViewById(R.id.chk9);
        checkbox10 = (CheckBox)findViewById(R.id.chk10);
        checkbox11 = (CheckBox)findViewById(R.id.chk11);
        checkbox12 = (CheckBox)findViewById(R.id.chk12);
        checkbox13 = (CheckBox)findViewById(R.id.chk13);

        checkbox1.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox2.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox3.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox4.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox5.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox6.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox7.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox8.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox9.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox10.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox11.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox12.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkbox13.setOnCheckedChangeListener(checkBoxOnCheckedChange);


        //-----------------------------------------------------

        bn_check_history = (Button) findViewById(R.id.bn_check_history);

        bn_check_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Symptom_choose.this);
                dialog.setContentView(R.layout.popup_unsaved_notice);

                Button bn_back= (Button) dialog.findViewById(R.id.bn_back);
                Button bn_check_to_leave = (Button) dialog.findViewById(R.id.bn_check_to_leave);
                bn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bn_check_to_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(Symptom_choose.this  , Check_history.class);
                        startActivity(intent);
                    }
                });

                dialog.show();
            }

        });

    }
    //確定那些symptom被選取
    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if(isChecked)//等於 buttonView.isChecked()
                    {
                        for (int i = 0; i < 13; i++){
                            if (all_symptom[i].equals(buttonView.getText().toString())){
                                symptom_array[i] = 1;
                            }
                        }
                        //all_symptom = all_symptom + buttonView.getText().toString() + " ";
                    }
                    else{
                        for (int i = 0; i < 13; i++){
                            if (all_symptom[i].equals(buttonView.getText().toString())){
                                symptom_array[i] = 0;
                            }
                        }
                    }

                }
            };

    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bn_back:
                    back_page();
                    break;

                case R.id.bn_next:
                    next_page();
                    break;
                default:
                    break;
            }
        }
    }
    private void back_page(){new back_page().start();}
    private void next_page(){new next_page().start();}
    class back_page extends Thread{
        public back_page(){
        }
        @Override
        public void run(){
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            if(gv.is_admin > 0) { //admin or 工作人員, 回受試者清單
                Intent intent;
                intent = new Intent();
                intent.setClass(Symptom_choose.this  , Subject_list.class);
                startActivity(intent);
            }
            else {
                Intent intent;
                intent = new Intent();
                //intent.setClass(Symptom_choose.this, MainActivity.class);
                intent.setClass(Symptom_choose.this, home.class);
                startActivity(intent);
            }
            //finish();
        }
    }
    class next_page extends Thread{
        public next_page(){

        }
        @Override
        public void run(){
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            gv.set_symptom_array(symptom_array);
            gv.set_now_problem_page(0);

            for (int i =0; i < 13; i++){
                if (symptom_array[i]==1){
                    if (i == 12){ // 只填跌倒
                        gv.set_now_problem_page(i);
                        Intent intent = new Intent();
                        intent.setClass(Symptom_choose.this  , Problem_item_fall_down.class);
                        startActivity(intent);
                    }
                    else {
                        gv.set_now_problem_page(i);
                        Intent intent = new Intent();
                        intent.setClass(Symptom_choose.this, Problem_item_fill_in.class);
                        startActivity(intent);
                    }
                    break;
                }
            }
            //finish();
        }
    }
}