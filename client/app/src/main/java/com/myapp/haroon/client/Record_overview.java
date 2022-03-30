package com.myapp.haroon.client;


import static android.provider.Telephony.Mms.Part.CHARSET;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class Record_overview extends AppCompatActivity {
    //private String postUrl = "http://140.113.86.106:50059/app2web";
    //private String msg = null;  //存放要Post的訊息
    //private String result;  //存放Post回傳值


    static Handler handler; //宣告成static讓service可以直接使用


    private TextView tv_all_records;
    private String[] all_symptom_records;
    private TextView tv_show_number, tv_show_name, tv_show_time;

    private TextView tv_number_2, tv_name_2, tv_time_2;
    private TextView tv_overview_tip;
    private String number;
    private String name, time;

    private int[] symptom_array;
    private String[] all_symptom = {"妄想", "幻覺", "激動/攻擊性", "憂鬱/情緒不佳",
            "焦慮", "昂然自得/欣快感", "冷漠/毫不在意", "言行失控", "暴躁易怒/情緒易變",
            "怪異動作", "睡眠/夜間行為", "食慾/飲食行為改變", "跌倒"};
    private int severity=-1, distress=-1, begin_time=-1, end_time=-1;
    private String detailed_begin_time, detailed_end_time;

    private TextView[] tv_problem_item_in_overview = new TextView[13];

    //private Button[] bn_modify = new Button[13]; //13個修改按鍵，各自負責每個symptom
    //private Button[] bn_check = new Button[13]; //同上
    private Button bn_modify,  bn_check;

    private CheckBox[] checkBoxes_severity = new CheckBox[3]; //12個symptom, 每個symptom有3個嚴重性
    private CheckBox[] checkBoxes_distress = new CheckBox[6];
    private CheckBox[] checkBoxes_begin_time = new CheckBox[2];
    private CheckBox[] checkBoxes_end_time = new CheckBox[2];

    private EditText mEdit_begin_hr;
    private EditText mEdit_begin_min;
    private EditText mEdit_end_hr;
    private EditText mEdit_end_min;

    private TextView tv_begin_hr, tv_begin_min, tv_end_hr, tv_end_min;

    private Button bn_begin_date, bn_end_date;
    private TextView tv_begin_date, tv_end_date;

    private EditText mEdit_event_description;

    private String final_record="";

    private Button bn_check_history, bn_save_and_upload;

    private int[] can_modify = new int[13];
    // 0-> others has modified, 1->you can modify, 一次只能改一個症狀的record
    // 控制13個修改/確定鍵
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_overview);


        Arrays.fill(can_modify, 1); // 所有修改鍵可以使用

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
        tv_time_2 = (TextView) findViewById(R.id.tv_time_2);

        number = gv.get_number();
        name = gv.get_name();
        time = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());

        tv_number_2.setText("受試者編號 " + number);
        tv_name_2.setText("受試者姓名 " + name);
        tv_time_2.setText("記錄日期 " + time);

        symptom_array = gv.get_symptom_array();

        all_symptom_records =  gv.get_symptom_records();

        bn_check_history = (Button) findViewById(R.id.bn_check_history);
        bn_save_and_upload = (Button) findViewById(R.id.bn_save_and_upload);

        bn_check_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Record_overview.this);
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
                        intent.setClass(Record_overview.this  , Check_history.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        bn_save_and_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //can_modify全為1時才可按下存檔上傳
                int can_save_and_upload = 1;
                for (int i = 0; i < 13; i++){
                    if(can_modify[i] != 1){
                        can_save_and_upload = 0;
                        break;
                    }
                }
                if (can_save_and_upload == 1) {
                    //TODO
                    final GlobalVariable gv = (GlobalVariable) getApplicationContext();

                    String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                    final JSONObject today_record = new JSONObject();
                    //String tmp_symptom_records = ""; //把String[]轉成單一String儲存
                    JSONObject tmp_symptom_records = new JSONObject();; //把String[]轉成單一JSONObject儲存
                    for(int i = 0; i < 13; i++){
                        try {
                            if(gv.get_symptom_records()[i]!=null) { //有紀錄
                                tmp_symptom_records.put(all_symptom[i], gv.get_symptom_records()[i]);
                            }//放中文不行
                            else{
                                tmp_symptom_records.put(all_symptom[i], "none");
                            }
                            //tmp_symptom_records.put("symptom"+Integer.toString(i), gv.get_symptom_records()[i]);
                        }catch(JSONException e){
                        }
                            //tmp_symptom_records += gv.get_symptom_records()[i];
                        //tmp_symptom_records += "====="; //用=====分隔不同symptom
                    }
                    try {
                        //放入受試者number, name
                        today_record.put("subject_number", gv.get_number());
                        today_record.put("subject_name", gv.get_name());
                        //紀錄admin number, name
                        if(gv.is_admin > 0){
                            today_record.put("admin_number", gv.get_login_admin_number());
                            today_record.put("admin_name", gv.get_login_admin_name());
                        }
                        else{
                            today_record.put("admin_number", gv.get_login_admin_number());
                            today_record.put("admin_name", gv.get_login_admin_name());
                        }
                        today_record.put("date", timeStamp);
                        today_record.put("record", tmp_symptom_records);
                    }catch(JSONException e){
                    }

                    //gv.all_date_records.add(today_record); //放到存所有日期的Object array
                    //gv.clean_symptom_records();

                    if(!gv.haveInternet()){ //沒網路
                        final Dialog dialog = new Dialog(Record_overview.this);
                        dialog.setContentView(R.layout.popup_unsaved_notice);

                        TextView tv_hint_no_net = (TextView) dialog.findViewById((R.id.tv_message));
                        tv_hint_no_net.setText("目前無網路連線，按下確定會保留紀錄但不會上傳資料，請連接網路後至歷史紀錄頁面更新");
                        Button bn_back= (Button) dialog.findViewById(R.id.bn_back);
                        Button bn_check_to_leave = (Button) dialog.findViewById(R.id.bn_check_to_leave);
                        bn_check_to_leave.setText("確定存檔");
                        bn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        bn_check_to_leave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String time_now = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                                try {
                                    today_record.put("save_time", time_now);
                                }catch(JSONException e){
                                }
                                //gv.all_date_records.add(today_record); //放到存所有日期的Object array
                                gv.clean_symptom_records();
                                gv.un_upload_records.add(today_record); //未上傳的record
                                Intent intent = new Intent();
                                intent.setClass(Record_overview.this  , Save_success.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                    else{ //有網路，要上傳
                        String time_now = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                        try {
                            today_record.put("save_time", time_now);   //存檔時間
                            today_record.put("upload_time", time_now); //上傳時間
                        }catch(JSONException e){
                        }
                        gv.all_date_records.add(today_record); //放到存所有日期的Object array
                        //gv.set_name(today_record.toString());
                        gv.clean_symptom_records();


                        //
                        //RequestParams params = new RequestParams();

                        String postUrl = "http://140.113.86.106:50059/app2web";
                        //String postUrl = "http://httpbin.org/post";
                        //String postUrl = "https://jsonplaceholder.typicode.com/posts";

                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                .build();
                        /**設置傳送需求*/
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, today_record.toString());

                        Request request = new Request.Builder()
                                .url(postUrl)
                                .addHeader("Accept-Encoding", "gzip, deflate, br")
                                .post(body)
                                .build();
                        /**設置回傳*/
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                /**如果傳送過程有發生錯誤*/
                                //gv.set_name(e.getMessage());

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                /**取得回傳*/
                                /*
                                try{
                                    JSONObject j_obj = new JSONObject(response.body().string());
                                    gv.set_name("POST回傳：\n" + j_obj.toString());
                                }catch(JSONException e){
                                    gv.set_name("POST回傳：\n" + e.toString());
                                }
                                 */
                                //gv.set_name("POST回傳：\n" + response +"_____"+ response.body().string());
                                Intent intent = new Intent();
                                intent.setClass(Record_overview.this, Save_success.class);
                                startActivity(intent);

                                //String decodeStr = response.body().string();
                                //gv.set_name("POST回傳：\n" + response.body().string());
                                //gv.set_name("POST回傳：\n" + decodeStr);
                            }
                        });

                        //Intent intent = new Intent();
                        //intent.setClass(Record_overview.this, Save_success.class);
                        //startActivity(intent);
                    }


                }
                else{
                    final Dialog dialog = new Dialog(Record_overview.this);
                    dialog.setContentView(R.layout.popup_filling_error);
                    TextView tv_error_message = (TextView) dialog.findViewById(R.id.tv_error_message);
                    tv_error_message.setText("請您修改後，按下確定");

                    dialog.show();
                }
            }
        });


        LinearLayout mainLinerLayout = (LinearLayout) findViewById(R.id.layout_records);
        //LinearLayout every_record_layout = (LinearLayout) findViewById(R.id.layout_every_record);
        final View[] view = new View[13];

        //顯示有記錄過的畫面, 跌倒另外處理
        for (int j = 0; j < 12; j++){ //跌倒另外處理
            final int i = j;
            if (symptom_array[i] != 0){ //有紀錄，開始顯示
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view[i] = inflater.inflate(R.layout.activity_every_record_overview , null, true); //讀取的page2.
                //view[i] //wrfesfsfes

                tv_problem_item_in_overview[i] = (TextView) view[i].findViewById(R.id.tv_problem_item_in_overview);

                tv_problem_item_in_overview[i].setText(all_symptom[i]);

                Test_function func_0 = new Test_function();
                func_0.set_checkbox_init(view[i]); // find ids of checkbox and edittext
                func_0.set_checkbox_listener();


                bn_modify = (Button) view[i].findViewById(R.id.bn_modify);

                bn_check = (Button) view[i].findViewById(R.id.bn_check);

                String tmp_symptom_record = all_symptom_records[i];
                String[] tokens = tmp_symptom_record.split("___",5);

                checkBoxes_severity[Integer.parseInt(tokens[0])-1].setChecked(true); //嚴重性是123
                checkBoxes_distress[Integer.parseInt(tokens[1])].setChecked(true);
                if(tokens[2].indexOf("上午") >= 0){ //if -1, no keyword in string
                    checkBoxes_begin_time[0].setChecked(true);
                }
                else{
                    checkBoxes_begin_time[1].setChecked(true);
                }
                if(tokens[3].indexOf("上午") >= 0){
                    checkBoxes_end_time[0].setChecked(true);
                }
                else{
                    checkBoxes_end_time[1].setChecked(true);
                }
                //找到開始日期
                String begin_date = tokens[2].split("午")[0].split(" ")[0];
                String end_date = tokens[3].split("午")[0].split(" ")[0];

                tv_begin_date.setText(begin_date);
                tv_end_date.setText(end_date);

                //抓出分和秒
                String[] begin_time_hr_and_min = tokens[2].split("午")[1].split(":");
                mEdit_begin_hr.setText(begin_time_hr_and_min[0]);
                mEdit_begin_min.setText(begin_time_hr_and_min[1]);

                String[] end_time_hr_and_min = tokens[3].split("午")[1].split(":");
                mEdit_end_hr.setText(end_time_hr_and_min[0]);
                mEdit_end_min.setText(end_time_hr_and_min[1]);

                //if(tokens.length > 4){ //有寫事件敘述
                if(!tokens[4].isEmpty()){ //有寫事件敘述
                    mEdit_event_description.setText(tokens[4]);
                }
                else{
                    mEdit_event_description.setText("(無)");
                }

                Test_function func = new Test_function();
                func.set_enable(0, false);

                bn_begin_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view_) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(Record_overview.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                //String format = "您設定的日期為:"+ setDateFormat(year,month,day);
                                //tv_begin_date.setText(String.valueOf(year) + "/" +String.valueOf(month+1) + "/" + String.valueOf(day));
                                tv_begin_date.setText(String.valueOf(year) + "/" +String.format("%02d", month+1) + "/" + String.format("%02d", day));

                            }

                        }, mYear,mMonth, mDay).show();
                    }
                });
                bn_end_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view_) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(Record_overview.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                //String format = "您設定的日期為:"+ setDateFormat(year,month,day);
                                //tv_end_date.setText(String.valueOf(year) + "/" +String.valueOf(month+1) + "/" + String.valueOf(day));
                                tv_end_date.setText(String.valueOf(year) + "/" +String.format("%02d", month+1) + "/" + String.format("%02d", day));

                            }

                        }, mYear,mMonth, mDay).show();
                    }
                });

                bn_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view_) {
                        if (can_modify[i] != 0) { // can modify
                            Arrays.fill(can_modify, 0); // if a button is modifying. others cannot
                            can_modify[i] = 1;

                            Test_function func = new Test_function();
                            func.set_checkbox_init(view[i]);
                            func.set_enable(0, true);

                        }
                    }
                });

                bn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view_) {
                        if(can_modify[i] == 1) { //只有modifying對應的確定鍵可以使用
                            //Arrays.fill(can_modify, 1);

                            Test_function func = new Test_function();
                            func.set_checkbox_init(view[i]);

                            String error_msg = func.return_error_msg(mEdit_begin_hr, mEdit_begin_min, mEdit_end_hr, mEdit_end_min,
                                    checkBoxes_severity, checkBoxes_distress, checkBoxes_begin_time, checkBoxes_end_time);

                            int has_error = 0;
                            if (!error_msg.equals("")) { //has error, popup error msg
                                final Dialog dialog = new Dialog(Record_overview.this);
                                dialog.setContentView(R.layout.popup_filling_error);
                                TextView tv_error_message = (TextView) dialog.findViewById(R.id.tv_error_message);
                                tv_error_message.setText(error_msg);

                                dialog.show();
                                has_error = 1;
                            }
                            if (has_error == 0) { // no error, can check
                                Arrays.fill(can_modify, 1);
                                func.set_enable(0, false);

                                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                                final_record = "";
                                final_record = final_record + severity + "___" + distress + "___";

                                final_record += detailed_begin_time;
                                final_record += "___";
                                final_record += detailed_end_time;
                                final_record += "___" + mEdit_event_description.getText().toString().trim();

                                String[] tmp_symptom_records = gv.get_symptom_records();
                                tmp_symptom_records[i] = final_record;
                                gv.set_symptom_records(tmp_symptom_records);


                            }
                        }
                    }
                });
                //tv_problem_item_in_overview.setText(tokens[0]+tokens[1]+tokens[2]+tokens[3]);
                mainLinerLayout.addView(view[i]); //加入畫面上
            }
        }
        // 另外處理跌倒的overview畫面
        if (symptom_array[12] != 0){ //有填跌倒
            final int k = 12;
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view[k] = inflater.inflate(R.layout.activity_every_record_overview_fall_down , null, true); //讀取的page2.

            tv_problem_item_in_overview[k] = (TextView) view[k].findViewById(R.id.tv_problem_item_in_overview_fall_down);
            tv_problem_item_in_overview[k].setText(all_symptom[k]);

            Test_function func_0 = new Test_function();
            func_0.set_fall_down_checkbox_init(view[k]); // find ids of checkbox and edittext
            func_0.set_fall_down_checkbox_listener();

            bn_modify = (Button) view[k].findViewById(R.id.bn_modify);
            bn_check = (Button) view[k].findViewById(R.id.bn_check);

            String tmp_symptom_record = all_symptom_records[k];
            String[] tokens = tmp_symptom_record.split("___", 3);

            if(tokens[0].indexOf("上午") >= 0){ //if -1, no keyword in string
                checkBoxes_begin_time[0].setChecked(true);
            }
            else{
                checkBoxes_begin_time[1].setChecked(true);
            }
            if(tokens[1].indexOf("上午") >= 0){
                checkBoxes_end_time[0].setChecked(true);
            }
            else{
                checkBoxes_end_time[1].setChecked(true);
            }
            String begin_date = tokens[0].split("午")[0].split(" ")[0];
            String end_date = tokens[1].split("午")[0].split(" ")[0];

            tv_begin_date.setText(begin_date);
            tv_end_date.setText(end_date);

            //抓出分和秒
            String[] begin_time_hr_and_min = tokens[0].split("午")[1].split(":");
            mEdit_begin_hr.setText(begin_time_hr_and_min[0]);
            mEdit_begin_min.setText(begin_time_hr_and_min[1]);

            String[] end_time_hr_and_min = tokens[1].split("午")[1].split(":");
            mEdit_end_hr.setText(end_time_hr_and_min[0]);
            mEdit_end_min.setText(end_time_hr_and_min[1]);

            //if(tokens.length > 2){ //有寫事件敘述
            if(!tokens[2].isEmpty()){ //有寫事件敘述
                mEdit_event_description.setText(tokens[2]);
            }
            else{
                mEdit_event_description.setText("(無)");
            }
            Test_function func = new Test_function();
            func.set_enable(1, false);

            bn_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view_) {
                    if (can_modify[k] != 0) { // can modify
                        Arrays.fill(can_modify, 0); // if a button is modifying. others cannot
                        can_modify[k] = 1;

                        Test_function func = new Test_function();
                        func.set_fall_down_checkbox_init(view[k]);
                        func.set_enable(1, true);
                    }
                }
            });

            bn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view_) {
                    if(can_modify[k] == 1) { //只有modifying對應的確定鍵可以使用

                        Test_function func = new Test_function();
                        func.set_fall_down_checkbox_init(view[k]);
                            /*
                            checkBoxes_severity[0] = (CheckBox) view[i].findViewById(R.id.chk_severity_1);
                            checkBoxes_severity[1] = (CheckBox) view[i].findViewById(R.id.chk_severity_2);
                            checkBoxes_severity[2] = (CheckBox) view[i].findViewById(R.id.chk_severity_3);
                            checkBoxes_distress[0] = (CheckBox) view[i].findViewById(R.id.chk_distress_0);
                            checkBoxes_distress[1] = (CheckBox) view[i].findViewById(R.id.chk_distress_1);
                            checkBoxes_distress[2] = (CheckBox) view[i].findViewById(R.id.chk_distress_2);
                            checkBoxes_distress[3] = (CheckBox) view[i].findViewById(R.id.chk_distress_3);
                            checkBoxes_distress[4] = (CheckBox) view[i].findViewById(R.id.chk_distress_4);
                            checkBoxes_distress[5] = (CheckBox) view[i].findViewById(R.id.chk_distress_5);

                            checkBoxes_begin_time[0] = (CheckBox) view[i].findViewById(R.id.chk_begin_morning);
                            checkBoxes_begin_time[1] = (CheckBox) view[i].findViewById(R.id.chk_begin_afternoon);

                            checkBoxes_end_time[0] = (CheckBox) view[i].findViewById(R.id.chk_end_morning);
                            checkBoxes_end_time[1] = (CheckBox) view[i].findViewById(R.id.chk_end_afternoon);

                            mEdit_begin_hr = (EditText) view[i].findViewById(R.id.mEdit_begin_hr);
                            mEdit_begin_min = (EditText) view[i].findViewById(R.id.mEdit_begin_min);
                            mEdit_end_hr = (EditText) view[i].findViewById(R.id.mEdit_end_hr);
                            mEdit_end_min = (EditText) view[i].findViewById(R.id.mEdit_end_min);

                            mEdit_event_description = (EditText) view[i].findViewById(R.id.mEdit_event_description);
                            */

                        String error_msg = func.return_fall_down_error_msg(mEdit_begin_hr, mEdit_begin_min,
                                mEdit_end_hr, mEdit_end_min,
                                checkBoxes_begin_time, checkBoxes_end_time);

                        int has_error = 0;
                        if (!error_msg.equals("")) { //has error, popup error msg
                            final Dialog dialog = new Dialog(Record_overview.this);
                            dialog.setContentView(R.layout.popup_filling_error);
                            TextView tv_error_message = (TextView) dialog.findViewById(R.id.tv_error_message);
                            tv_error_message.setText(error_msg);

                            dialog.show();
                            has_error = 1;
                        }
                        if (has_error == 0) { // no error, can check
                            Arrays.fill(can_modify, 1);
                            func.set_enable(1, false);

                            GlobalVariable gv = (GlobalVariable) getApplicationContext();
                            final_record = "";

                            final_record += detailed_begin_time;
                            final_record += "___";
                            final_record += detailed_end_time;
                            final_record += "___" + mEdit_event_description.getText().toString().trim();

                            String[] tmp_symptom_records = gv.get_symptom_records();
                            tmp_symptom_records[k] = final_record;
                            gv.set_symptom_records(tmp_symptom_records);

                        }
                    }
                }
            });
            mainLinerLayout.addView(view[k]); //加入畫面上
        }

    }


    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態

                    if (isChecked) {//等於 buttonView.isChecked()
                        for (int i = 0; i < 3; i++) {    //嚴重性3選1
                            if (checkBoxes_severity[i].getText().toString().equals(buttonView.getText().toString())) {
                                checkBoxes_severity[i].setChecked(true);
                                severity = i + 1;
                            } else {
                                checkBoxes_severity[i].setChecked(false);
                            }
                        }
                    }
                    else{
                            //buttonView.setChecked(false);
                            //severity = -1;
                    }
                }
            };
    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange_2 =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if (isChecked){//等於 buttonView.isChecked()
                        for (int i = 0; i < 6; i++) {    //困擾度6選1
                            if (checkBoxes_distress[i].getText().toString().equals(buttonView.getText().toString())) {
                                checkBoxes_distress[i].setChecked(true);
                                distress = i;
                            } else {
                                checkBoxes_distress[i].setChecked(false);
                            }
                        }
                    }
                    else {
                            //buttonView.setChecked(false);
                            //distress =-1;
                    }

                }
            };
    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange_3 =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if (isChecked) {//等於 buttonView.isChecked()
                        for (int i = 0; i < 2; i++) {    //開始時間3選1
                            if (checkBoxes_begin_time[i].getText().toString().equals(buttonView.getText().toString())) {
                                checkBoxes_begin_time[i].setChecked(true);
                                begin_time = i;
                            } else {
                                checkBoxes_begin_time[i].setChecked(false);

                            }
                        }
                    }

                    else{
                        //buttonView.setChecked(false); //***
                        //begin_time = -1;

                    }

                }
            };

    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange_4 =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if (isChecked){//等於 buttonView.isChecked()
                        for (int i = 0; i < 2; i++) {    //結束時間3選1
                            if (checkBoxes_end_time[i].getText().toString().equals(buttonView.getText().toString())) {
                                checkBoxes_end_time[i].setChecked(true);
                                end_time = i;
                            } else {
                                checkBoxes_end_time[i].setChecked(false);

                            }
                        }
                    }
                    else {
                            //buttonView.setChecked(false);
                            //end_time = -1;
                    }
                }
            };
    public class Test_function {
        void set_each_enable(CheckBox[] ch, boolean t_f){
            for(int i = 0; i < ch.length; i++){
                ch[i].setEnabled(t_f);
            }
        }
        void set_enable(int option, boolean t_f){   //option 0->一般症狀, 1->跌倒
            Test_function func1 = new Test_function();
            if (option == 0) {
                func1.set_each_enable(checkBoxes_severity, t_f);
                func1.set_each_enable(checkBoxes_distress, t_f);
            }
            func1.set_each_enable(checkBoxes_begin_time, t_f);
            func1.set_each_enable(checkBoxes_end_time, t_f);

            mEdit_begin_hr.setEnabled(t_f);
            mEdit_begin_min.setEnabled(t_f);
            mEdit_end_hr.setEnabled(t_f);
            mEdit_end_min.setEnabled(t_f);
            mEdit_event_description.setEnabled(t_f);

            bn_begin_date.setEnabled(t_f);
            bn_end_date.setEnabled(t_f);
            tv_begin_date.setEnabled(t_f);
            tv_end_date.setEnabled(t_f);

            if(t_f == false){
                int gray_c = 170;
                mEdit_begin_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_begin_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_end_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_end_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_event_description.setTextColor(Color.rgb(gray_c, gray_c, gray_c));

                tv_begin_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_begin_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_end_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_end_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));

                tv_begin_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_end_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));

                bn_begin_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                bn_end_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
            }
            else{
                int gray_c = 0;
                mEdit_begin_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_begin_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_end_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_end_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                mEdit_event_description.setTextColor(Color.rgb(gray_c, gray_c, gray_c));

                tv_begin_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_begin_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_end_hr.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_end_min.setTextColor(Color.rgb(gray_c, gray_c, gray_c));

                tv_begin_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                tv_end_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));

                bn_begin_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
                bn_end_date.setTextColor(Color.rgb(gray_c, gray_c, gray_c));
            }
        }
        void set_checkbox_init(View view)
        {
            checkBoxes_severity[0] = (CheckBox)view.findViewById(R.id.chk_severity_1);
            checkBoxes_severity[1] = (CheckBox)view.findViewById(R.id.chk_severity_2);
            checkBoxes_severity[2] = (CheckBox)view.findViewById(R.id.chk_severity_3);
            checkBoxes_distress[0] = (CheckBox)view.findViewById(R.id.chk_distress_0);
            checkBoxes_distress[1] = (CheckBox)view.findViewById(R.id.chk_distress_1);
            checkBoxes_distress[2] = (CheckBox)view.findViewById(R.id.chk_distress_2);
            checkBoxes_distress[3] = (CheckBox)view.findViewById(R.id.chk_distress_3);
            checkBoxes_distress[4] = (CheckBox)view.findViewById(R.id.chk_distress_4);
            checkBoxes_distress[5] = (CheckBox)view.findViewById(R.id.chk_distress_5);

            checkBoxes_begin_time[0] = (CheckBox)view.findViewById(R.id.chk_begin_morning);
            checkBoxes_begin_time[1] = (CheckBox)view.findViewById(R.id.chk_begin_afternoon);

            checkBoxes_end_time[0] = (CheckBox)view.findViewById(R.id.chk_end_morning);
            checkBoxes_end_time[1] = (CheckBox)view.findViewById(R.id.chk_end_afternoon);

            mEdit_begin_hr = (EditText) view.findViewById(R.id.mEdit_begin_hr);
            mEdit_begin_min = (EditText) view.findViewById(R.id.mEdit_begin_min);
            mEdit_end_hr = (EditText) view.findViewById(R.id.mEdit_end_hr);
            mEdit_end_min = (EditText) view.findViewById(R.id.mEdit_end_min);

            tv_begin_hr = (TextView) view.findViewById(R.id.tv_begin_hr);
            tv_begin_min = (TextView) view.findViewById(R.id.tv_begin_min);
            tv_end_hr = (TextView) view.findViewById(R.id.tv_end_hr);
            tv_end_min = (TextView) view.findViewById(R.id.tv_end_min);

            bn_begin_date = (Button) view.findViewById(R.id.bn_begin_date);
            bn_end_date = (Button) view.findViewById(R.id.bn_end_date);

            tv_begin_date = (TextView) view.findViewById(R.id.tv_begin_date);
            tv_end_date = (TextView) view.findViewById(R.id.tv_end_date);

            mEdit_event_description = (EditText) view.findViewById(R.id.mEdit_event_description);

        }
        void set_checkbox_listener(){
            checkBoxes_severity[0].setOnCheckedChangeListener(checkBoxOnCheckedChange);
            checkBoxes_severity[1].setOnCheckedChangeListener(checkBoxOnCheckedChange);
            checkBoxes_severity[2].setOnCheckedChangeListener(checkBoxOnCheckedChange);
            checkBoxes_distress[0].setOnCheckedChangeListener(checkBoxOnCheckedChange_2);
            checkBoxes_distress[1].setOnCheckedChangeListener(checkBoxOnCheckedChange_2);
            checkBoxes_distress[2].setOnCheckedChangeListener(checkBoxOnCheckedChange_2);
            checkBoxes_distress[3].setOnCheckedChangeListener(checkBoxOnCheckedChange_2);
            checkBoxes_distress[4].setOnCheckedChangeListener(checkBoxOnCheckedChange_2);
            checkBoxes_distress[5].setOnCheckedChangeListener(checkBoxOnCheckedChange_2);

            checkBoxes_begin_time[0].setOnCheckedChangeListener(checkBoxOnCheckedChange_3);
            checkBoxes_begin_time[1].setOnCheckedChangeListener(checkBoxOnCheckedChange_3);

            checkBoxes_end_time[0].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);
            checkBoxes_end_time[1].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);
        }

        void set_fall_down_checkbox_init(View view) {
            checkBoxes_begin_time[0] = (CheckBox)view.findViewById(R.id.chk_begin_morning);
            checkBoxes_begin_time[1] = (CheckBox)view.findViewById(R.id.chk_begin_afternoon);

            checkBoxes_end_time[0] = (CheckBox)view.findViewById(R.id.chk_end_morning);
            checkBoxes_end_time[1] = (CheckBox)view.findViewById(R.id.chk_end_afternoon);

            mEdit_begin_hr = (EditText) view.findViewById(R.id.mEdit_begin_hr);
            mEdit_begin_min = (EditText) view.findViewById(R.id.mEdit_begin_min);
            mEdit_end_hr = (EditText) view.findViewById(R.id.mEdit_end_hr);
            mEdit_end_min = (EditText) view.findViewById(R.id.mEdit_end_min);

            mEdit_event_description = (EditText) view.findViewById(R.id.mEdit_event_description);

            tv_begin_hr = (TextView) view.findViewById(R.id.tv_begin_hr);
            tv_begin_min = (TextView) view.findViewById(R.id.tv_begin_min);
            tv_end_hr = (TextView) view.findViewById(R.id.tv_end_hr);
            tv_end_min = (TextView) view.findViewById(R.id.tv_end_min);

            bn_begin_date = (Button) view.findViewById(R.id.bn_begin_date);
            bn_end_date = (Button) view.findViewById(R.id.bn_end_date);

            tv_begin_date = (TextView) view.findViewById(R.id.tv_begin_date);
            tv_end_date = (TextView) view.findViewById(R.id.tv_end_date);

        }
        void set_fall_down_checkbox_listener(){
            checkBoxes_begin_time[0].setOnCheckedChangeListener(checkBoxOnCheckedChange_3);
            checkBoxes_begin_time[1].setOnCheckedChangeListener(checkBoxOnCheckedChange_3);

            checkBoxes_end_time[0].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);
            checkBoxes_end_time[1].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);
        }

        String return_error_msg(EditText mEdit_begin_hr, EditText mEdit_begin_min,
                                EditText mEdit_end_hr, EditText mEdit_end_min,
                                CheckBox[] checkBoxes_severity, CheckBox[] checkBoxes_distress,
                                CheckBox[] checkBoxes_begin_time, CheckBox[] checkBoxes_end_time)
        {
            detailed_begin_time = "";
            detailed_end_time = "";
            if (begin_time==2){ // now
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm").format(Calendar.getInstance().getTime());
                detailed_begin_time = timeStamp;
                //final_record += timeStamp;
            }
            else if (begin_time==0){ //morning
                //String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                String timeStamp = tv_begin_date.getText().toString();
                detailed_begin_time = timeStamp + " 上午"+
                        mEdit_begin_hr.getText().toString().trim() + ":" +
                        mEdit_begin_min.getText().toString().trim();
                //final_record += detailed_begin_time;
            }
            else if (begin_time==1){ //afternoon
                //String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                String timeStamp = tv_begin_date.getText().toString();
                detailed_begin_time = timeStamp + " 下午"+
                        mEdit_begin_hr.getText().toString().trim() + ":" +
                        mEdit_begin_min.getText().toString().trim();
                //final_record += detailed_begin_time;
            }
            if (end_time==2){ // now
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm").format(Calendar.getInstance().getTime());
                detailed_end_time = timeStamp;
                //final_record += timeStamp;
            }
            else if (end_time==0){ //morning
                //String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                String timeStamp = tv_end_date.getText().toString();
                detailed_end_time = timeStamp + " 上午"+
                        mEdit_end_hr.getText().toString().trim() + ":" +
                        mEdit_end_min.getText().toString().trim();
                //final_record += detailed_end_time;
            }
            else if (end_time==1){ //afternoon
                //String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                String timeStamp = tv_end_date.getText().toString();
                detailed_end_time += timeStamp + " 下午"+
                        mEdit_end_hr.getText().toString().trim() + ":" +
                        mEdit_end_min.getText().toString().trim();
                //final_record += detailed_end_time;
            }

            //處理漏填或其他錯誤
            String error_msg = "";
            severity = -1;
            distress = -1;
            begin_time = -1;
            end_time = -1;
            for (int i = 0; i<3; i++){
                if (checkBoxes_severity[i].isChecked()){ //檢查看看是不是有被選到
                    severity = i+1;
                    break;
                }
            }
            for (int i = 0; i<6; i++){
                if (checkBoxes_distress[i].isChecked()){ //檢查看看是不是有被選到
                    distress = i;
                    break;
                }
            }
            for (int i = 0; i<3; i++){
                if (checkBoxes_begin_time[i].isChecked()){ //檢查看看是不是有被選到
                    begin_time = i;
                    break;
                }
            }
            for (int i = 0; i<3; i++){
                if (checkBoxes_end_time[i].isChecked()){ //檢查看看是不是有被選到
                    end_time = i;
                    break;
                }
            }
            if (severity==-1){
                error_msg = "請您填答嚴重度，\n再按下確定";
            }
            else if (distress==-1){
                error_msg = "請您填答困擾程度，\n再按下確定";
            }
            else if(begin_time==-1 || end_time == -1){
                error_msg = "請您填答事件發生時間，\n再按下確定";
            }
            else if((begin_time==0 || begin_time==1)&&
                    (TextUtils.isEmpty(mEdit_begin_hr.getText()) || TextUtils.isEmpty(mEdit_begin_min.getText()))){
                //if(TextUtils.isEmpty(mEdit_begin_hr.getText()) || TextUtils.isEmpty(mEdit_begin_min.getText())){
                //開始時間填上/下午，但漏填時或分
                error_msg = "請您填答事件發生時間，\n再按下確定";

            }
            else if((end_time==0 || end_time==1)&&
                    (TextUtils.isEmpty(mEdit_end_hr.getText()) || TextUtils.isEmpty(mEdit_end_min.getText()))){
                //if(TextUtils.isEmpty(mEdit_end_hr.getText()) || TextUtils.isEmpty(mEdit_end_min.getText())){
                //結束時間填上/下午，但漏填時或分
                error_msg = "請您填答事件發生時間，\n再按下確定";

            }
            else {
                int begin_hr = 0, end_hr = 0, begin_min = 0, end_min = 0;

                try {
                    begin_hr = Integer.parseInt(mEdit_begin_hr.getText().toString().trim());
                } catch (NumberFormatException e) {
                    begin_hr = 100;
                } // if parse error->set to error hr

                try {
                    end_hr = Integer.parseInt(mEdit_end_hr.getText().toString().trim());
                } catch (NumberFormatException e) {
                    end_hr = 100;
                }

                try {
                    begin_min = Integer.parseInt(mEdit_begin_min.getText().toString().trim());
                } catch (NumberFormatException e) {
                    begin_min = 100;
                }

                try {
                    end_min = Integer.parseInt(mEdit_end_min.getText().toString().trim());
                } catch (NumberFormatException e) {
                    end_min = 100;
                }

                //
                if (begin_time == 0 || begin_time == 1) {    //填上下午，但是時間填錯
                    if (begin_hr > 12 || begin_hr < 0) {
                        error_msg = "您輸入小時數字需在0-12之間";
                    } else if (begin_min > 59 || begin_min < 0) {   //同上
                        error_msg = "您輸入分鐘數字需在0-59之間";
                    }
                }
                if (end_time == 0 || end_time == 1) {    //填上下午，但是時間填錯
                    if (end_hr > 12 || end_hr < 0) {
                        error_msg = "您輸入小時數字需在0-12之間";
                    } else if (end_min > 59 || end_min < 0) {   //同上
                        error_msg = "您輸入分鐘數字需在0-59之間";
                    }
                }
                if (error_msg == "") { //前面還沒有error，最後檢查開始結束時間先後的問題
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ahh:mm");
                    Date formatDate_begin, formatDate_end;
                    try {
                        formatDate_begin = sdf.parse(detailed_begin_time);
                        formatDate_end = sdf.parse(detailed_end_time);
                        if (formatDate_begin.getTime() > formatDate_end.getTime()) {
                            //<=這裡改*************************************
                            //<=這裡改*************************************
                            //開始時間晚於結束時間87
                            error_msg = "輸入的結束時間應晚於\n開始時間，完成後再按下確定";
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            return error_msg;
        }

        String return_fall_down_error_msg(EditText mEdit_begin_hr, EditText mEdit_begin_min,
                                EditText mEdit_end_hr, EditText mEdit_end_min,
                                CheckBox[] checkBoxes_begin_time, CheckBox[] checkBoxes_end_time)
        {
            detailed_begin_time = "";
            detailed_end_time = "";
            if (begin_time==2){ // now
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm").format(Calendar.getInstance().getTime());
                detailed_begin_time = timeStamp;
                //final_record += timeStamp;
            }
            else if (begin_time==0){ //morning
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                detailed_begin_time = timeStamp + "上午"+ mEdit_begin_hr.getText().toString().trim()
                        + ":" + mEdit_begin_min.getText().toString().trim();
                //final_record += detailed_begin_time;
            }
            else if (begin_time==1){ //afternoon
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                detailed_begin_time = timeStamp + "下午"+ mEdit_begin_hr.getText().toString().trim()
                        + ":" + mEdit_begin_min.getText().toString().trim();
                //final_record += detailed_begin_time;
            }
            if (end_time==2){ // now
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm").format(Calendar.getInstance().getTime());
                detailed_end_time = timeStamp;
                //final_record += timeStamp;
            }
            else if (end_time==0){ //morning
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                detailed_end_time = timeStamp + "上午"+ mEdit_end_hr.getText().toString().trim()
                        + ":" + mEdit_end_min.getText().toString().trim();
                //final_record += detailed_end_time;
            }
            else if (end_time==1){ //afternoon
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                detailed_end_time += timeStamp + "下午"+ mEdit_end_hr.getText().toString().trim()
                        + ":" + mEdit_end_min.getText().toString().trim();
                //final_record += detailed_end_time;
            }

            //處理漏填或其他錯誤
            String error_msg = "";
            //severity = -1;
            //distress = -1;
            begin_time = -1;
            end_time = -1;

            for (int i = 0; i<3; i++){
                if (checkBoxes_begin_time[i].isChecked()){ //檢查看看是不是有被選到
                    begin_time = i;
                    break;
                }
            }
            for (int i = 0; i<3; i++){
                if (checkBoxes_end_time[i].isChecked()){ //檢查看看是不是有被選到
                    end_time = i;
                    break;
                }
            }

            if(begin_time==-1 || end_time == -1){
                error_msg = "請您填答事件發生時間，\n再按下確定";
            }
            else if((begin_time==0 || begin_time==1)&&
                    (TextUtils.isEmpty(mEdit_begin_hr.getText()) || TextUtils.isEmpty(mEdit_begin_min.getText()))){
                //if(TextUtils.isEmpty(mEdit_begin_hr.getText()) || TextUtils.isEmpty(mEdit_begin_min.getText())){
                //開始時間填上/下午，但漏填時或分
                error_msg = "請您填答事件發生時間，\n再按下確定";

            }
            else if((end_time==0 || end_time==1)&&
                    (TextUtils.isEmpty(mEdit_end_hr.getText()) || TextUtils.isEmpty(mEdit_end_min.getText()))){
                //if(TextUtils.isEmpty(mEdit_end_hr.getText()) || TextUtils.isEmpty(mEdit_end_min.getText())){
                //結束時間填上/下午，但漏填時或分
                error_msg = "請您填答事件發生時間，\n再按下確定";

            }
            else {
                int begin_hr = 0, end_hr = 0, begin_min = 0, end_min = 0;

                try {
                    begin_hr = Integer.parseInt(mEdit_begin_hr.getText().toString().trim());
                } catch (NumberFormatException e) {
                    begin_hr = 100;
                } // if parse error->set to error hr

                try {
                    end_hr = Integer.parseInt(mEdit_end_hr.getText().toString().trim());
                } catch (NumberFormatException e) {
                    end_hr = 100;
                }

                try {
                    begin_min = Integer.parseInt(mEdit_begin_min.getText().toString().trim());
                } catch (NumberFormatException e) {
                    begin_min = 100;
                }

                try {
                    end_min = Integer.parseInt(mEdit_end_min.getText().toString().trim());
                } catch (NumberFormatException e) {
                    end_min = 100;
                }

                //
                if (begin_time == 0 || begin_time == 1) {    //填上下午，但是時間填錯
                    if (begin_hr > 12 || begin_hr < 0) {
                        error_msg = "您輸入小時數字需在0-12之間";
                    } else if (begin_min > 59 || begin_min < 0) {   //同上
                        error_msg = "您輸入分鐘數字需在0-59之間";
                    }
                }
                if (end_time == 0 || end_time == 1) {    //填上下午，但是時間填錯
                    if (end_hr > 12 || end_hr < 0) {
                        error_msg = "您輸入小時數字需在0-12之間";
                    } else if (end_min > 59 || end_min < 0) {   //同上
                        error_msg = "您輸入分鐘數字需在0-59之間";
                    }
                }
                if (error_msg == "") { //前面還沒有error，最後檢查開始結束時間先後的問題
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ahh:mm");
                    Date formatDate_begin, formatDate_end;
                    try {
                        formatDate_begin = sdf.parse(detailed_begin_time);
                        formatDate_end = sdf.parse(detailed_end_time);
                        if (formatDate_begin.getTime() > formatDate_end.getTime()) {
                            //<=這裡改*************************************
                            //<=這裡改*************************************
                            //開始時間晚於結束時間87
                            error_msg = "輸入的結束時間應晚於\n開始時間，完成後再按下確定";
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            return error_msg;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false; //返回鍵失效
        }
        return super.onKeyDown(keyCode, event);
    }
}