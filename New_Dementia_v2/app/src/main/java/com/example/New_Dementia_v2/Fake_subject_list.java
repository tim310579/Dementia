package com.example.New_Dementia_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Fake_subject_list extends AppCompatActivity {
    /*********************************
    //Fake 頁面才是會顯示紀錄筆數的頁面*****
    *********************************/
    private int MAX_SUBJECT=500;

    private TextView tv_welcome_msg, tv_show_time;
    private TextView tv_remind_words;
    private String login_admin_number, login_admin_name;

    private Button bn_logout;

    private final int H = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int W = ViewGroup.LayoutParams.MATCH_PARENT;
    private TableLayout tab;
    //private ArrayList<String> tabCol = new ArrayList<>();
    //private ArrayList<String> tabH = new ArrayList<>();

    //private TextView[] tv_subject_number = new TextView[20];
    //private Button[] bn_subject_name = new Button[6]; //5 subjects(包含一個空格)
    //private Button[][] bn_history_cnt = new Button[6][7]; //7 admins
    //private int[][] int_history_cnt = new int[6][7]; //7 admins,紀錄本日記錄比數上的紀錄筆數
    //private String[][] str_history_cnt = new String[6][7]; //7 admins,紀錄本日記錄比數上的紀錄筆數

    private ArrayList<String> subject_numbers = new ArrayList<String>();
    private ArrayList<String> subject_names = new ArrayList<String>();

    private ArrayList<String> admin_numbers = new ArrayList<String>();
    private ArrayList<String> admin_names = new ArrayList<String>();

    private ArrayList<TextView> tv_subject_numbers = new ArrayList<TextView>();
    private ArrayList<Button> bn_subject_names = new ArrayList<Button>();
    private ArrayList<Button> bn_subject_sensor_charge_status = new ArrayList<Button>();
    private ArrayList<String> subject_sensor_charge_status = new ArrayList<String>();

    private ArrayList<Button> bn_subject_watch_charge_status = new ArrayList<Button>();
    private ArrayList<String> subject_watch_charge_status = new ArrayList<String>();

    private ArrayList<ArrayList<Integer>> int_history_cnts = new ArrayList<>();
    private ArrayList<ArrayList<Button>> bn_history_cnts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_subject_list);

        final GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_welcome_msg = (TextView) findViewById(R.id.tv_welcome_msg);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);
        tv_remind_words = (TextView) findViewById(R.id.tv_remind_words);
        //tv_remind_words_5 = (TextView) findViewById(R.id.tv_remind_words_5);

        bn_logout = (Button) findViewById(R.id.bn_logout);

        login_admin_number = gv.get_login_admin_number();
        login_admin_name = gv.get_login_admin_name();

        tv_welcome_msg.setText("您現在登入" + login_admin_number + "-" + login_admin_name + "的帳號");
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
        tv_show_time.setText(timeStamp);
        tv_remind_words.setText(Html.fromHtml("點選"+"<u><font color='#8e00ad'>受試者姓名</font></u>"+
                "即可進入紀錄畫面<br>" +"點選紀錄筆數的"+
                "<u><font color='#0000ff'>數字</font></u>"+
                "可以查看歷史紀錄<br>" + "手環或手錶配戴時請確認狀態為" +
                "<u><font color='#878787'>配戴中</font></u>" +
                "，以記錄手環或手錶配戴時間"+
                "<br>手環或手錶取下後請確認狀態為"+
                "<u><font color='#ff0000'>已取下</font></u>"+
                "，以記錄手環或手錶取下時間"));
        //tv_remind_words_5.setText(Html.fromHtml("<u>"+"數字"+"</u>"));

        tab = (TableLayout) findViewById(R.id.table_subject_list);

        //final String[] subject_numbers = {"", "S001", "S002", "S003", "S004", "S005"};
        //final String[] subject_names = {"", "王大明", "林大名", "張大名", "謝大名", "陳大名", "吳大名"};

        subject_numbers = gv.admin_manage_patient_number;
        subject_names = gv.admin_manage_patient_name;
        subject_sensor_charge_status = gv.subject_sensor_charge_status;
        subject_watch_charge_status = gv.subject_watch_charge_status;

        admin_numbers = gv.admin_manage_admin_number;
        admin_names = gv.admin_manage_admin_name;

        //final String[] admin_numbers = {"usr000000", "A002", "A003", "A004", "A005", "A006", "A007"};
        //final String[] admin_names = {"lin", "Vincent", "林孟辰", "Benny", "陳計師", "黃宇", "陳昱銘"};
        if (subject_numbers.size() > MAX_SUBJECT){
            MAX_SUBJECT = MAX_SUBJECT*2+subject_numbers.size();
        }
        final View[] view = new View[MAX_SUBJECT]; //長直的
        final View[] view_k = new View[100]; //橫向
        final int admin_number_cnt = 7;

        //for(int d = 0; d < int_history_cnt.length; d++){
        //    Arrays.fill(int_history_cnt[d], 0);
        //}
        for(int i = 0; i < subject_numbers.size(); i++){
            int_history_cnts.add(new ArrayList<Integer>());
            bn_history_cnts.add(new ArrayList<Button>());
            for(int j = 0; j < admin_numbers.size(); j++){
                int_history_cnts.get(i).add(0);
                //bn_history_cnts.get(i).add(new Button());
            }
        }
        //Arrays.fill(int_history_cnt, 0);
        //String tmp = "";
        for (JSONObject obj : gv.all_date_records){
            String tmp_subject_number="none", tmp_admin_number="none", tmp_timestamp="none";
            try{
                tmp_subject_number = obj.getString("subject_number");
                tmp_admin_number = obj.getString("admin_number");
                tmp_timestamp = obj.getString("date");
            }catch(JSONException e){
            }
            int idx_subject = subject_numbers.indexOf(tmp_subject_number);
            int idx_admin = admin_numbers.indexOf(tmp_admin_number);
            //int idx_subject = Arrays.asList(subject_numbers).indexOf(tmp_subject_number);
            //int idx_admin = Arrays.asList(admin_numbers).indexOf(tmp_admin_number);
            if( idx_subject >= 0 && idx_admin >= 0) {
                //int_history_cnt[idx_subject][idx_admin] += 1;
                int tmp = int_history_cnts.get(idx_subject).get(idx_admin);
                String timestamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                if (tmp_timestamp.equals(timestamp)) {  //是當天的
                    int_history_cnts.get(idx_subject).set(idx_admin, tmp + 1);
                }
            }
            //str_history_cnt[0][0] = tmp_subject_number + tmp_admin_number;
            //tmp += Integer.toString(idx_subject) + Integer.toString(idx_admin) + " : ";
            //tmp += tmp_subject_number + tmp_admin_number + " : ";
        }

        gv.set_admin_number(gv.get_login_admin_number());
        gv.set_admin_name(gv.get_login_admin_name());


        for (int j = 0; j < subject_numbers.size(); j++) {
            final int i = j;
            //final int i = subject_numbers.size()-j-1;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view[i] = inflater.inflate(R.layout.every_subject_table_row , null, true); //讀取的page2.

            //tv_subject_number[i] = (TextView) view[i].findViewById(R.id.tv_subject_number);
            //bn_subject_name[i] = (Button) view[i].findViewById(R.id.bn_subject_name);
            //TextView tv_tmp = new TextView(this);
            tv_subject_numbers.add((TextView) view[i].findViewById(R.id.tv_subject_number));
            bn_subject_names.add((Button) view[i].findViewById(R.id.bn_subject_name));
            bn_subject_sensor_charge_status.add((Button) view[i].findViewById(R.id.bn_subject_sensor_charge_status));
            bn_subject_watch_charge_status.add((Button) view[i].findViewById(R.id.bn_subject_watch_charge_status));

            //tv_subject_number[i].setText(subject_numbers.get(i));
            //bn_subject_name[i].setText(subject_names.get(i));

            tv_subject_numbers.get(i).setText(subject_numbers.get(i));
            bn_subject_names.get(i).setText(subject_names.get(i));
            //bn_subject_sensor_charge_status.get(i).setText(subject_sensor_charge_status.get(i));//subject_names.get(i));

            if(subject_sensor_charge_status.get(i).equals("no")==true){  //使用中(未充電，已取下)
                bn_subject_sensor_charge_status.get(i).setText("配戴中");
                bn_subject_sensor_charge_status.get(i).setTextColor(Color.rgb(135,135,135));
            }
            else{
                bn_subject_sensor_charge_status.get(i).setText("已取下");
                bn_subject_sensor_charge_status.get(i).setTextColor(Color.rgb(255,0,0));
            }

            if(subject_watch_charge_status.get(i).equals("no")==true){  //使用中(未充電，已取下)
                bn_subject_watch_charge_status.get(i).setText("配戴中");
                bn_subject_watch_charge_status.get(i).setTextColor(Color.rgb(135,135,135));
            }
            else{
                bn_subject_watch_charge_status.get(i).setText("已取下");
                bn_subject_watch_charge_status.get(i).setTextColor(Color.rgb(255,0,0));
            }


            if(i == 0){
                bn_subject_sensor_charge_status.get(i).setText("");
                bn_subject_sensor_charge_status.get(i).setEnabled(false);
                bn_subject_watch_charge_status.get(i).setText("");
                bn_subject_watch_charge_status.get(i).setEnabled(false);
            }
            if(subject_names.get(i).equals("")){
                //bn_subject_name[i].setEnabled(false);
                bn_subject_names.get(i).setEnabled(false);
            }
            for (int h = 0; h < admin_numbers.size(); h++){
                final int k = h;
                LinearLayout second_layout = (LinearLayout) view[i].findViewById(R.id.history_cnt_buttons);
                view_k[k] = inflater.inflate(R.layout.every_subject_history_cnt_button , null, true); //

                //bn_history_cnt[i][k] = (Button) view_k[k].findViewById(R.id.bn_history_cnt_A00x);
                bn_history_cnts.get(i).add((Button) view_k[k].findViewById(R.id.bn_history_cnt_A00x));

                if(i == 0){
                    //bn_history_cnt[i][k].setTextColor(Color.rgb(0, 0, 0)); //black
                    //bn_history_cnt[i][k].setEnabled(false);
                    //bn_history_cnt[i][k].setText(admin_numbers.get(k));

                    bn_history_cnts.get(i).get(k).setTextColor(Color.rgb(0, 0, 0));
                    bn_history_cnts.get(i).get(k).setEnabled(false);
                    bn_history_cnts.get(i).get(k).setText(admin_numbers.get(k));
                }
                else {

                    //if (int_history_cnt[i][k] > 0) {
                    if (int_history_cnts.get(i).get(k) > 0) {
                        //bn_history_cnt[i][k].setText(Integer.toString(int_history_cnt[i][k])); //>0，有紀錄筆數
                        //bn_history_cnt[i][k].setEnabled(true);
                        bn_history_cnts.get(i).get(k).setText(Integer.toString(int_history_cnts.get(i).get(k))); //>0，有紀錄筆數
                        bn_history_cnts.get(i).get(k).setEnabled(true);
                    }
                    else {
                        //bn_history_cnt[i][k].setText(""); //沒有紀錄筆數
                        //bn_history_cnt[i][k].setEnabled(false);
                        bn_history_cnts.get(i).get(k).setText(""); //=0，沒有紀錄筆數
                        bn_history_cnts.get(i).get(k).setEnabled(false);
                    }
                }

                //bn_history_cnt[i][k].setOnClickListener(new View.OnClickListener() {
                bn_history_cnts.get(i).get(k).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view_) {
                        //點到該名字，跳出症狀選擇頁面
                        GlobalVariable gv = (GlobalVariable) getApplicationContext();
                        gv.set_number(subject_numbers.get(i));
                        gv.set_name(subject_names.get(i));
                        gv.set_admin_number(admin_numbers.get(k));
                        gv.set_admin_name(admin_names.get(k));

                        Intent intent = new Intent();
                        intent.setClass(Fake_subject_list.this, Check_history.class);
                        startActivity(intent);
                    }
                });

                //bn_history_cnt[i][k].setText(subject_record_cnt[i][k]);
                //if(subject_record_cnt[i][k] == ""){
                //   bn_history_cnt[i][k].setEnabled(false);
                //}
                second_layout.addView(view_k[k]);
            }
            //bn_subject_name[i].setOnClickListener(new View.OnClickListener() {
            bn_subject_names.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view_) {
                    //點到該名字，跳出症狀選擇頁面
                    GlobalVariable gv = (GlobalVariable) getApplicationContext();
                    gv.set_number(subject_numbers.get(i));
                    gv.set_name(subject_names.get(i));

                    Intent intent = new Intent();
                    intent.setClass(Fake_subject_list.this, Symptom_choose.class);
                    startActivity(intent);
                }
            });

            bn_subject_sensor_charge_status.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view_) {
                    String pass_url = "0";
                    GlobalVariable gv = (GlobalVariable) getApplicationContext();
                    if(bn_subject_sensor_charge_status.get(i).getText().toString().equals("配戴中") == true){ ////no->yes(配戴中->(已取下)充電中)
                        bn_subject_sensor_charge_status.get(i).setText("已取下");
                        bn_subject_sensor_charge_status.get(i).setTextColor(Color.rgb(255,0,0));
                        gv.subject_sensor_charge_status.set(i, "yes");
                        //pass_url = "http://140.113.86.106:50059/charging";
                        pass_url = "http://140.113.193.87:20059/charging";

                    }
                    else{
                        bn_subject_sensor_charge_status.get(i).setText("配戴中");
                        bn_subject_sensor_charge_status.get(i).setTextColor(Color.rgb(135,135,135));
                        gv.subject_sensor_charge_status.set(i, "no");
                        pass_url = "http://140.113.193.87:20059/uncharging";
                    }

                    /******************************
                     //send sensor status to server//
                     /******************************/
                    //String postUrl = "http://140.113.86.106:50059/web2app";
                    String postUrl = pass_url;
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .build();
                    //設置傳送需求
                    JSONObject j_obj = new JSONObject();
                    try {
                        //j_obj.put("admin_number", gv.get_login_admin_number());
                        //j_obj.put("subject_number", gv.get_number());
                        j_obj.put("subject_number", subject_numbers.get(i));
                        String tmp_timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                        j_obj.put("time", tmp_timeStamp);
                    } catch (JSONException e) {

                    }
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, j_obj.toString());

                    Request request = new Request.Builder()
                            .url(postUrl)
                            .addHeader("Accept-Encoding", "gzip, deflate, br")
                            .post(body)
                            .build();
                    //設置回傳
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            //如果傳送過程有發生錯誤
                            //gv.set_name(e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            //nothing
                            //tv_show_time.setText("passed");
                            //Intent intent = new Intent();
                            //intent.setClass(Fake_subject_list.this, Fake_subject_list.class);
                            //startActivity(intent);
                        }
                    });
                    /******************************
                     //send sensor status to server//
                     /******************************/


                    //gv.set_number(subject_numbers.get(i));
                    //gv.set_name(subject_names.get(i));

                    //Intent intent = new Intent();
                    //intent.setClass(Fake_subject_list.this, Symptom_choose.class);
                    //startActivity(intent);
                }
            });

            bn_subject_watch_charge_status.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view_) {
                    String pass_url = "0";
                    GlobalVariable gv = (GlobalVariable) getApplicationContext();
                    if(bn_subject_watch_charge_status.get(i).getText().toString().equals("配戴中") == true){ ////no->yes(配戴中->(已取下)充電中)
                        bn_subject_watch_charge_status.get(i).setText("已取下");
                        bn_subject_watch_charge_status.get(i).setTextColor(Color.rgb(255,0,0));
                        gv.subject_watch_charge_status.set(i, "yes");
                        //pass_url = "http://140.113.86.106:50059/charging";
                        pass_url = "http://140.113.193.87:20059/watch_charging";

                    }
                    else{
                        bn_subject_watch_charge_status.get(i).setText("配戴中");
                        bn_subject_watch_charge_status.get(i).setTextColor(Color.rgb(135,135,135));
                        gv.subject_watch_charge_status.set(i, "no");
                        pass_url = "http://140.113.193.87:20059/watch_uncharging";
                    }

                    /******************************
                     //send sensor status to server//
                     /******************************/
                    //String postUrl = "http://140.113.86.106:50059/web2app";

                    String postUrl = pass_url;
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .build();
                    //設置傳送需求
                    JSONObject j_obj = new JSONObject();
                    try {
                        //j_obj.put("admin_number", gv.get_login_admin_number());
                        //j_obj.put("subject_number", gv.get_number());
                        j_obj.put("subject_number", subject_numbers.get(i));
                        String tmp_timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                        j_obj.put("time", tmp_timeStamp);
                    } catch (JSONException e) {

                    }
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, j_obj.toString());

                    Request request = new Request.Builder()
                            .url(postUrl)
                            .addHeader("Accept-Encoding", "gzip, deflate, br")
                            .post(body)
                            .build();
                    //設置回傳
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            //如果傳送過程有發生錯誤
                            //gv.set_name(e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            //nothing
                            //tv_show_time.setText("passed");
                            //Intent intent = new Intent();
                            //intent.setClass(Fake_subject_list.this, Fake_subject_list.class);
                            //startActivity(intent);
                        }
                    });

                    /******************************
                     //send sensor status to server//
                     /******************************/


                    //gv.set_number(subject_numbers.get(i));
                    //gv.set_name(subject_names.get(i));

                    //Intent intent = new Intent();
                    //intent.setClass(Fake_subject_list.this, Symptom_choose.class);
                    //startActivity(intent);
                }
            });

            tab.addView(view[i]);
        }
        bn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Fake_subject_list.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //ConfirmExit();//按返回鍵，則執行退出確認
            //Intent intent = new Intent();
            //intent.setClass(Fake_subject_list.this, MainActivity.class);
            //startActivity(intent);
            Fake_subject_list.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}