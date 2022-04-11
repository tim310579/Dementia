package com.example.dementia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
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

public class Subject_list extends AppCompatActivity {
    /*********************************
     //此頁面為顫存頁面，沒什麼用
     //Fake 頁面才是會顯示紀錄筆數的頁面*****
     *********************************/

    private TextView tv_welcome_msg, tv_show_time;
    private TextView tv_remind_words;
    private String login_admin_number, login_admin_name;

    private Button bn_logout;

    private final int H = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int W = ViewGroup.LayoutParams.MATCH_PARENT;
    private TableLayout tab;
    private ArrayList<String> tabCol = new ArrayList<>();
    private ArrayList<String> tabH = new ArrayList<>();

    private TextView[] tv_subject_number = new TextView[20];
    private Button[] bn_subject_name = new Button[6]; //5 subjects(包含一個空格)
    private Button[][] bn_history_cnt = new Button[6][7]; //7 admins
    private int[][] int_history_cnt = new int[6][7]; //7 admins,紀錄本日記錄比數上的紀錄筆數
    private String[][] str_history_cnt = new String[6][7]; //7 admins,紀錄本日記錄比數上的紀錄筆數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

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
                "即可進入紀錄畫面\n" +"點選紀錄筆數的"+
                "<u><font color='#0000ff'>數字</font></u>"+
                "可以查看歷史紀錄"));
        //tv_remind_words_5.setText(Html.fromHtml("<u>"+"數字"+"</u>"));

        tab = (TableLayout) findViewById(R.id.table_subject_list);
        final View[] view = new View[20]; //長直的
        //final String[] subject_numbers = {"", "S001", "S002", "S003", "S004", "S005"};
        //final String[] subject_names = {"", "王大明", "林大名", "張大名", "謝大名", "陳大名", "吳大名"};

        final ArrayList<String> subject_numbers = gv.admin_manage_patient_number;
        final ArrayList<String> subject_names = gv.admin_manage_patient_name;

        final ArrayList<String> admin_numbers = gv.admin_manage_admin_number;
        final ArrayList<String> admin_names = gv.admin_manage_admin_name;

        //final String[] admin_numbers = {"usr000000", "A002", "A003", "A004", "A005", "A006", "A007"};
        //final String[] admin_names = {"lin", "Vincent", "林孟辰", "Benny", "陳計師", "黃宇", "陳昱銘"};

        final View[] view_k = new View[20]; //橫向
        final int admin_number_cnt = 7;

//放假資料test###############**************-----------------
/*
        if(gv.all_date_records.size() < 0) {
            String[] fake_dates = {"2022/02/13", "2022/02/15", "2022/02/16", "2022/02/09", "2022/02/22", "2022/02/21"};
            String[] fake_subject_numbers = {"S002", "S001", "S003", "S004", "S005", "S005"};
            String[] fake_subject_names = {"林大名", "王大明", "張大名", "謝大名", "陳大名", "陳大名"};
            String[] fake_admin_numbers = {"A006", "A001", "A003", "A004", "A002", "A007"};
            String[] fake_admin_names = {"黃宇", "高榮功", "林孟辰", "Benny", "Vincent", "陳昱銘"};

            for (int h = 0; h < 6; h++) {
                JSONObject today_record = new JSONObject();
                try {
                    today_record.put("subject_number", fake_subject_numbers[h]);
                    today_record.put("subject_name", fake_subject_names[h]);
                    today_record.put("admin_number", fake_admin_numbers[h]);
                    today_record.put("admin_name", fake_admin_names[h]);
                    today_record.put("date", fake_dates[h]);
                    today_record.put("record", new JSONObject());
                } catch (JSONException e) {
                }
                gv.all_date_records.add(today_record); //放到存所有日期的Object array
            }
        }
 */
        final int[] record_cnt = {0};
        //放假資料test###############**************-----------------
        //先從server要資料
        if(!gv.haveInternet()){ //沒網路

        }
        else {
            gv.all_date_records.clear();
            if(gv.all_date_records.size() == 0) {//沒有record時跟server要
                String postUrl = "http://140.113.86.106:50059/web2app";
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build();
                //設置傳送需求
                final JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("admin_number", gv.get_login_admin_number()); //要該admin負責的所有病人所有record
                    //j_obj.put("subject_number", "all_subject");
                    timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                    j_obj.put("date", timeStamp);
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
                        //取得回傳

                        try {
                            JSONObject j_obj = new JSONObject(response.body().string());
                            //JSONObject j_arr = new JSONObject(j_obj.getJSONObject("records"));
                            //gv.set_name("POST回傳：\n" +j_obj.getJSONArray("records").getJSONObject(0).toString());
                            //gv.set_name("POST回傳：\n" + j_obj.getString("records") + "\n" + j_obj.getString("records").getClass().getSimpleName());
                            //gv.set_number(Integer.toString(j_obj.getJSONArray("records").length()));
                            //j_obj.getJSONArray("records").length();
                            //gv.set_name(j_obj.toString());
                            for (int i = 0; i < j_obj.getJSONArray("records").length(); i++) {
                                JSONObject j_origin = j_obj.getJSONArray("records").getJSONObject(i);
                                JSONObject j_tmp = new JSONObject();
                                j_tmp.put("date", j_origin.getString("date"));
                                j_tmp.put("record", j_origin.getJSONObject("content"));
                                j_tmp.put("admin_number", j_origin.getString("admin_id"));
                                j_tmp.put("subject_number", j_origin.getString("patient_id"));
                                //改***************************************
                                j_tmp.put("subject_name", j_origin.getString("subject_name"));
                                j_tmp.put("admin_name", j_origin.getString("admin_name"));
                                //if (j_origin.getString("admin_id").equals("no_admin")) {
                                //    j_tmp.put("admin_name", "no_admin");
                                //} else {
                                 //   j_tmp.put("admin_name", "lin");
                                //}
                                gv.all_date_records.add(j_tmp);
                                //record_cnt[0] += 1;
                            }
                            //tv_welcome_msg.setText(Integer.toString(record_cnt[0]));
                        } catch (JSONException e) {
                            //gv.set_name("POST回傳err：\n" + e.toString());
                        }
                        //tv_welcome_msg.setText(gv.all_date_records.toString());
                        //gv.set_name(gv.all_date_records.toString());
                        //gv.set_number(Integer.toString(record_cnt[0]));
                        //gv.set_login_admin_number(Integer.toString(gv.all_date_records.size()));
                        //gv.set_login_admin_name(gv.all_date_records.toString());
                        Intent intent = new Intent();
                        intent.setClass(Subject_list.this, Fake_subject_list.class);
                        startActivity(intent);
                        Subject_list.this.finish();

                        //gv.all_date_records.add(j_obj);


                        //String decodeStr = response.body().string();
                        //gv.set_name("POST回傳：\n" + response.body().string());
                        //gv.set_name("POST回傳：\n" + decodeStr);
                    }

                });
                    //gv.all_date_records.clear();
                    //for(String subject_i : subject_numbers) { //該病患對應該admin的record



                //gv.set_name(gv.all_date_records.toString());
                //gv.set_number(Integer.toString(record_cnt[0]));
                //Intent intent = new Intent();
                //intent.setClass(Subject_list.this, Save_success.class);
                //startActivity(intent);
            }
        }
        //Handler handler = new Handler();
        //handler.postDelayed(new Runnable() {
         //   public void run() {
        //        // yourMethod();
        //    }
       // }, 3000);   //5 seconds
        //tv_welcome_msg.setText(gv.all_date_records.toString());
        //tv_welcome_msg.setText(Integer.toString(record_cnt[0]));

        /*


        for(int d = 0; d < int_history_cnt.length; d++){
            Arrays.fill(int_history_cnt[d], 0);
        }
        //Arrays.fill(int_history_cnt, 0);
        //String tmp = "";
        for (JSONObject obj : gv.all_date_records){
            String tmp_subject_number="none", tmp_admin_number="none";
            try{
                tmp_subject_number = obj.getString("subject_number");
                tmp_admin_number = obj.getString("admin_number");
            }catch(JSONException e){
            }
            int idx_subject = subject_numbers.indexOf(tmp_subject_number);
            int idx_admin = admin_numbers.indexOf(tmp_admin_number);
            //int idx_subject = Arrays.asList(subject_numbers).indexOf(tmp_subject_number);
            //int idx_admin = Arrays.asList(admin_numbers).indexOf(tmp_admin_number);
            if( idx_subject >= 0 && idx_admin >= 0) {
                int_history_cnt[idx_subject][idx_admin] += 1;
            }
            //str_history_cnt[0][0] = tmp_subject_number + tmp_admin_number;
            //tmp += Integer.toString(idx_subject) + Integer.toString(idx_admin) + " : ";
            //tmp += tmp_subject_number + tmp_admin_number + " : ";
        }
        //tv_show_time.setText(tmp);

         */
        /*
        for(int m = 0; m < int_history_cnt.length; m++){
            for(int n = 0 ; n < int_history_cnt[m].length; n++){
                if(int_history_cnt[m][n] > 0){
                    bn_history_cnt[m][n].setText(Integer.toString(int_history_cnt[m][n])); //>0，有紀錄筆數
                    bn_history_cnt[m][n].setEnabled(true);
                }
                else{
                    bn_history_cnt[m][n].setText(""); //沒有紀錄筆數
                    bn_history_cnt[m][n].setEnabled(false);
                }
            }
        }*/
        /*
        gv.set_admin_number(gv.get_login_admin_number());
        gv.set_admin_name(gv.get_login_admin_name());
        for (int j = 0; j < subject_numbers.size(); j++) {
            final int i = j;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view[i] = inflater.inflate(R.layout.every_subject_table_row , null, true); //讀取的page2.

            tv_subject_number[i] = (TextView) view[i].findViewById(R.id.tv_subject_number);
            bn_subject_name[i] = (Button) view[i].findViewById(R.id.bn_subject_name);


            tv_subject_number[i].setText(subject_numbers.get(i));
            bn_subject_name[i].setText(subject_names.get(i));
            if(subject_names.get(i).equals("")){
                bn_subject_name[i].setEnabled(false);
            }
            for (int h = 0; h < admin_numbers.size(); h++){
                final int k = h;
                LinearLayout second_layout = (LinearLayout) view[i].findViewById(R.id.history_cnt_buttons);
                view_k[k] = inflater.inflate(R.layout.every_subject_history_cnt_button , null, true); //

                bn_history_cnt[i][k] = (Button) view_k[k].findViewById(R.id.bn_history_cnt_A00x);

                if(i == 0){
                    bn_history_cnt[i][k].setTextColor(Color.rgb(0, 0, 0)); //black
                    bn_history_cnt[i][k].setEnabled(false);
                    bn_history_cnt[i][k].setText(admin_numbers.get(k));
                }
                else {

                    if (int_history_cnt[i][k] > 0) {
                        bn_history_cnt[i][k].setText(Integer.toString(int_history_cnt[i][k])); //>0，有紀錄筆數
                        bn_history_cnt[i][k].setEnabled(true);
                    } else {
                        bn_history_cnt[i][k].setText(""); //沒有紀錄筆數
                        bn_history_cnt[i][k].setEnabled(false);
                    }
                }

                bn_history_cnt[i][k].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view_) {
                        //點到該名字，跳出症狀選擇頁面
                        GlobalVariable gv = (GlobalVariable) getApplicationContext();
                        gv.set_number(subject_numbers.get(i));
                        gv.set_name(subject_names.get(i));
                        gv.set_admin_number(admin_numbers.get(k));
                        gv.set_admin_name(admin_names.get(k));

                        Intent intent = new Intent();
                        intent.setClass(Subject_list.this, Check_history.class);
                        startActivity(intent);
                    }
                });

                //bn_history_cnt[i][k].setText(subject_record_cnt[i][k]);
                //if(subject_record_cnt[i][k] == ""){
                //   bn_history_cnt[i][k].setEnabled(false);
                //}
                second_layout.addView(view_k[k]);
            }
            //bn_history_cnt[1][0].setText(str_history_cnt[0][0]);


            bn_subject_name[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view_) {
                    //點到該名字，跳出症狀選擇頁面
                    GlobalVariable gv = (GlobalVariable) getApplicationContext();
                    gv.set_number(subject_numbers.get(i));
                    gv.set_name(subject_names.get(i));

                    Intent intent = new Intent();
                    intent.setClass(Subject_list.this, Symptom_choose.class);
                    startActivity(intent);
                }
            });

            tab.addView(view[i]);
        }

        //bn_history_cnt[1][0].setText(Integer.toString(gv.all_date_records.size()));
        //bn_history_cnt[1][0].setText(record_cnt[0]);


        //tv_today_record_cnt.setWidth(bn_history_cnt[0][0].getMeasuredWidth()*admin_number_cnt);
        //bn_history_cnt[0][0].setText(Integer.toString(bn_history_cnt[0][0].getMeasuredWidth()));

        bn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Subject_list.this, MainActivity.class);
                startActivity(intent);
            }
        });

         */
    }
    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
      //  if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        //    //ConfirmExit();//按返回鍵，則執行退出確認
          //  Subject_list.this.finish();
            //return true;
        //}
      //  return super.onKeyDown(keyCode, event);
    //}


}