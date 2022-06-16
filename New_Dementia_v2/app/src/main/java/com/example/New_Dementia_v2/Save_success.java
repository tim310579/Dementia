package com.example.New_Dementia_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Save_success extends AppCompatActivity {
    private TextView tv_show_number, tv_show_name, tv_show_time;

    private Button bn_check_history, bn_record_another, bn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_success);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_show_number = (TextView) findViewById(R.id.tv_show_number);
        tv_show_name = (TextView) findViewById(R.id.tv_show_name);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);

        tv_show_number.setText(gv.get_number());
        tv_show_name.setText(gv.get_name());

        String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
        //System.out.println(timeStamp);
        tv_show_time.setText(timeStamp);

        bn_check_history = (Button) findViewById(R.id.bn_check_history);
        bn_check_history.setOnClickListener(new ButtonClickListener());
        bn_record_another = (Button) findViewById(R.id.bn_record_another);
        bn_record_another.setOnClickListener(new ButtonClickListener());
        bn_logout = (Button) findViewById(R.id.bn_logout);
        bn_logout.setOnClickListener(new ButtonClickListener());

        if(gv.is_admin > 0){ //admin是回到受試者清單
            bn_logout.setText("回到受試者清單");
        }
        else{
            bn_logout.setText("回主畫面");
        }
    }
    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bn_check_history:
                    goto_history();
                    break;

                case R.id.bn_record_another:
                    goto_symptom_choose();
                    break;
                case R.id.bn_logout:
                    logout();
                    break;
                default:
                    break;
            }
        }
    }
    private void goto_history(){new goto_history().start();}
    private void goto_symptom_choose(){new goto_symptom_choose().start();}
    private void logout(){new logout().start();}

    class goto_history extends Thread{
        public goto_history(){
        }
        @Override
        public void run(){
            Intent intent;
            intent = new Intent();
            intent.setClass(Save_success.this  , Check_history.class);
            startActivity(intent);
            //finish();
        }
    }
    class goto_symptom_choose extends Thread{
        public goto_symptom_choose(){
        }
        @Override
        public void run(){
            Intent intent;
            intent = new Intent();
            intent.setClass(Save_success.this  , Symptom_choose.class);
            startActivity(intent);
            //finish();
        }
    }
    class logout extends Thread{
        public logout(){
        }
        @Override
        public void run(){
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            if (gv.is_admin > 0) {
                Intent intent = new Intent();
                intent.setClass(Save_success.this, Subject_list.class);
                startActivity(intent);
            }
            else {
                Intent intent;
                intent = new Intent();
                //intent.setClass(Save_success.this, MainActivity.class);
                intent.setClass(Save_success.this, home.class);
                startActivity(intent);
            }
            //finish();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false; //返回鍵失效
        }
        return super.onKeyDown(keyCode, event);
    }
}