package com.myapp.haroon.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class History_record_overview extends AppCompatActivity {
    private TextView tv_show_number, tv_show_name, tv_show_time;

    private TextView tv_number_2, tv_name_2, tv_time_2;
    private TextView tv_admin_name_2;
    private String number;
    private String name, time;

    private int[] symptom_array;
    private String[] all_symptom = {"妄想", "幻覺", "激動/攻擊性", "憂鬱/情緒不佳",
            "焦慮", "昂然自得/欣快感", "冷漠/毫不在意", "言行失控", "暴躁易怒/情緒易變",
            "怪異動作", "睡眠/夜間行為", "食慾/飲食行為改變", "跌倒"};
    private int severity=-1, distress=-1, begin_time=-1, end_time=-1;
    private String detailed_begin_time, detailed_end_time;

    private TextView[] tv_problem_item_in_overview = new TextView[13];

    private Button bn_check_other_history, bn_next_record, bn_logout;

    private TextView tv_severity_item, tv_distress_item, tv_begin_time, tv_end_time, tv_event_description;

    private JSONObject history_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_record_overview);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_show_number = (TextView) findViewById(R.id.tv_show_number);
        tv_show_name = (TextView) findViewById(R.id.tv_show_name);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);

        tv_show_number.setText(gv.get_number());
        tv_show_name.setText(gv.get_name());
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
        //System.out.println(timeStamp);
        tv_show_time.setText(timeStamp);

        tv_number_2 = (TextView) findViewById(R.id.tv_number_2);
        tv_name_2 = (TextView) findViewById(R.id.tv_name_2);
        tv_time_2 = (TextView) findViewById(R.id.tv_overview_date);

        tv_admin_name_2 = (TextView) findViewById(R.id.tv_admin_name_2);

        int record_id = gv.get_now_in_history_cnt_record();
        //history_record = gv.get_history_record()[record_id]; //get等等要顯示的record


        //String history_record_with_symptoms = "";
        JSONObject history_record_with_symptoms = new JSONObject();
        //String[] tokens = new String[13];
        String[] tokens = {"none", "none", "none", "none", "none", "none",
                "none", "none", "none", "none", "none", "none", "none"};
        int has_record = 0;
        String tmp = "";

        try {
            history_record = gv.history_record.get(record_id);
            gv.set_now_in_history_cnt_record(record_id+1);

            tv_number_2.setText("受試者編號 " + history_record.getString("subject_number"));
            tv_name_2.setText("受試者姓名 " + history_record.getString("subject_name"));
            tv_time_2.setText("記錄日期 " + history_record.getString("date"));
            String tmp_admin_number = history_record.getString("admin_number");
            String tmp_admin_name = history_record.getString("admin_name");
            //if(tmp_admin_number == "no_admin"){ //該record是由subject自行記錄
            //    tv_admin_name_2.setText("");
           // }
            //else{
            tv_admin_name_2.setText("紀錄者登入帳號 " + tmp_admin_number + " " + tmp_admin_name + '\n');
            //}
            history_record_with_symptoms = history_record.getJSONObject("record");
            for (int i = 0; i < 13; i++){
                tokens[i] = history_record_with_symptoms.getString(all_symptom[i]);
                //tmp+=tokens[i];
                has_record = 1;
            }
            //tv_time_2.setText(tmp);
        }catch (JSONException e){

        }

        //String[] tokens = history_record_with_symptoms.split("=====");

        bn_check_other_history = (Button) findViewById(R.id.bn_check_other_history);
        bn_next_record = (Button) findViewById(R.id.bn_next_record);
        bn_logout = (Button) findViewById(R.id.bn_logout);
        if(gv.is_admin > 0){ //admin是回到受試者清單
            bn_logout.setText("回到受試者清單");
        }
        else{
            bn_logout.setText("回主畫面");
        }

        bn_check_other_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                gv.set_history_cnt_record(0);
                gv.set_now_in_history_cnt_record(0);
                Intent intent = new Intent();
                intent.setClass(History_record_overview.this  , Check_history.class);
                startActivity(intent);
            }
        });

        bn_next_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                if(gv.get_now_in_history_cnt_record() >= gv.get_history_cnt_record()){
                    //已經是該日期最後一筆了
                    final Dialog dialog = new Dialog(History_record_overview.this);
                    dialog.setContentView(R.layout.popup_filling_error);
                    TextView tv_error_message = (TextView) dialog.findViewById(R.id.tv_error_message);
                    tv_error_message.setText("您已經瀏覽這個\n日期的所有紀錄");

                    dialog.show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(History_record_overview.this, History_record_overview.class);
                    startActivity(intent);
                }
            }
        });
        bn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                if (gv.is_admin > 0) {
                    Intent intent = new Intent();
                    intent.setClass(History_record_overview.this, Subject_list.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    //intent.setClass(History_record_overview.this, MainActivity.class);
                    intent.setClass(History_record_overview.this, home.class);
                    startActivity(intent);
                }
            }
        });


        //tv_time_2.setText(history_record + Integer.toString(tokens.length));

        LinearLayout mainLinerLayout = (LinearLayout) findViewById(R.id.layout_records_history);
        //LinearLayout every_record_layout = (LinearLayout) findViewById(R.id.layout_every_record);
        final View[] view = new View[13];

        //String tmpp = "ooo";
        //if(tokens.length==13) {
        //for (int j = 0; j < 12; j++){
         //   if(!tokens[j].equals("none")) { //該symptom有紀錄
          //      tv_time_2.setText(tv_time_2.getText() + tokens[j]);
          //  }
        //}
        //has_record = 0;
        if (has_record == 1){
            for (int j = 0; j < 12; j++) { //跌倒另外處理
                final int i = j;
                //if (tokens[i] != null) { //有紀錄，開始顯示
                //if(!tokens[i].isEmpty()){
                //if( tokens[i].contains("___")){ //有___表示該項symptom有記錄到
                if(!tokens[j].equals("none")){ //該symptom有紀錄
                    //tmpp += "k";
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view[i] = inflater.inflate(R.layout.every_history_record_overview , null, true); //讀取的page2.

                    tv_problem_item_in_overview[i] = (TextView) view[i].findViewById(R.id.tv_problem_item_in_history_overview);
                    tv_problem_item_in_overview[i].setText(all_symptom[i]);

                    tv_severity_item = (TextView) view[i].findViewById(R.id.tv_severity_item);
                    tv_distress_item = (TextView) view[i].findViewById(R.id.tv_distress_item);
                    tv_begin_time = (TextView) view[i].findViewById(R.id.tv_begin_time);
                    tv_end_time = (TextView) view[i].findViewById(R.id.tv_end_time);
                    tv_event_description = (TextView) view[i].findViewById(R.id.tv_event_description);

                    String tmp_symptom_record = tokens[i];
                    String[] each_symptom_tokens = tmp_symptom_record.split("___", 5);

                    tv_severity_item.setText("#嚴重度 " + each_symptom_tokens[0]);
                    tv_distress_item.setText("#困擾程度 " + each_symptom_tokens[1]);
                    tv_begin_time.setText("開始: " + each_symptom_tokens[2]); //.split("/", 2)[1]);
                    tv_end_time.setText("結束: " + each_symptom_tokens[3]);//.split("/", 2)[1]);

                    //if (each_symptom_tokens.length > 4){ //有填事件敘述
                    if(!each_symptom_tokens[4].isEmpty()){ //有填事件敘述
                        tv_event_description.setText("事件簡述\n                    " + each_symptom_tokens[4]);
                    }
                    else{
                        tv_event_description.setText("事件簡述\n                    (無)");
                    }

                    //tv_problem_item_in_overview.setText(tokens[0]+tokens[1]+tokens[2]+tokens[3]);
                    mainLinerLayout.addView(view[i]); //加入畫面上

                }
            }
            //單獨處理跌倒overview
            if (tokens[12].contains("___")){ //有填跌倒
                final int k = 12;
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view[k] = inflater.inflate(R.layout.every_history_record_overview_fall_down , null, true); //讀取的page2.

                tv_problem_item_in_overview[k] = (TextView) view[k].findViewById(R.id.tv_problem_item_in_history_overview);
                tv_problem_item_in_overview[k].setText(all_symptom[k]);

                //tv_severity_item = (TextView) view[i].findViewById(R.id.tv_severity_item);
                //tv_distress_item = (TextView) view[i].findViewById(R.id.tv_distress_item);
                tv_begin_time = (TextView) view[k].findViewById(R.id.tv_begin_time);
                tv_end_time = (TextView) view[k].findViewById(R.id.tv_end_time);
                tv_event_description = (TextView) view[k].findViewById(R.id.tv_event_description);

                String tmp_symptom_record = tokens[k];
                String[] each_symptom_tokens = tmp_symptom_record.split("___", 3);

                //tv_severity_item.setText("#嚴重度 " + each_symptom_tokens[0]);
                //tv_distress_item.setText("#困擾程度 " + each_symptom_tokens[1]);
                tv_begin_time.setText("開始: " + each_symptom_tokens[0]); //.split("/", 2)[1]);
                tv_end_time.setText("結束: " + each_symptom_tokens[1]); //.split("/", 2)[1]);

                //if (each_symptom_tokens.length > 2){ //有填事件敘述
                if(!each_symptom_tokens[2].isEmpty()){ //有填事件敘述
                    tv_event_description.setText("事件簡述\n                    " + each_symptom_tokens[2]);
                }
                else{
                    tv_event_description.setText("事件簡述\n                    (無)");
                }

                //tv_problem_item_in_overview.setText(tokens[0]+tokens[1]+tokens[2]+tokens[3]);
                mainLinerLayout.addView(view[k]); //加入畫面上
            }
        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            return false; //返回鍵失效
        }
        return super.onKeyDown(keyCode, event);
    }
}