package com.myapp.haroon.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class home extends AppCompatActivity {

    private TextView tv_welcome_msg, tv_show_time;
    private TextView tv_remind_words, tv_subject_number;
    private String login_admin_number, login_admin_name;

    private Button bn_logout;

    private Button bn_subject_name, bn_cnt_today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_welcome_msg = (TextView) findViewById(R.id.tv_welcome_msg);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);
        tv_remind_words = (TextView) findViewById(R.id.tv_remind_words);

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

        tv_subject_number = (TextView) findViewById(R.id.tv_subject_number);
        bn_subject_name = (Button) findViewById(R.id.bn_subject_name);
        bn_cnt_today = (Button) findViewById(R.id.bn_cnt_today);

        tv_subject_number.setText(gv.get_number());
        bn_subject_name.setText(gv.get_name());
        bn_cnt_today.setText("0");
        final int[] today_rec_cnt = {0};
        // final GlobalVariable gv = (GlobalVariable) getApplicationContext();
        if(!gv.haveInternet()){ //沒網路

        }
        else {
            String postUrl = "http://140.113.86.106:50059/web2app";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build();
            //設置傳送需求
            JSONObject j_obj = new JSONObject();
            try {
                j_obj.put("admin_number", gv.get_login_admin_number());
                j_obj.put("subject_number", gv.get_number());
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
                    gv.all_date_records.clear();

                    try{
                        JSONObject j_obj = new JSONObject(response.body().string());
                        //JSONObject j_arr = new JSONObject(j_obj.getJSONObject("records"));
                        //gv.set_name("POST回傳：\n" +j_obj.toString());
                        //gv.set_name("POST回傳：\n" + j_obj.getString("records") + "\n" + j_obj.getString("records").getClass().getSimpleName());
                        //gv.set_number(Integer.toString(j_obj.getJSONArray("records").length()));
                        bn_cnt_today.setText(Integer.toString(j_obj.getJSONArray("records").length()));

                        for(int i = 0; i < j_obj.getJSONArray("records").length(); i++){
                            JSONObject j_origin = j_obj.getJSONArray("records").getJSONObject(i);
                            JSONObject j_tmp = new JSONObject();
                            j_tmp.put("date", j_origin.getString("date"));
                            j_tmp.put("record", j_origin.getJSONObject("content"));
                            j_tmp.put("admin_number", j_origin.getString("admin_id"));
                            j_tmp.put("subject_number", j_origin.getString("patient_id"));
                            j_tmp.put("subject_name", "林依二");
                            if(j_origin.getString("admin_id").equals("no_admin")){
                                j_tmp.put("admin_name","no_admin");
                            }
                            else {
                                j_tmp.put("admin_name", "chen");
                            }
                            gv.all_date_records.add(j_tmp);
                        }
                        //for(JSONObject j_ob : gv.all_date_records){
                         //   try{
                        //        tv_welcome_msg.setText(j_ob.getString("_id").toString());
                         //   }catch (JSONException e){
                        //    }
                            //tv_welcome_msg.setText(j_ob.toString());
                       // }
                        //j_obj.getJSONArray("records").length();
                    }catch(JSONException e){
                        //gv.set_name("POST回傳err：\n" + e.toString());
                    }

                    //String decodeStr = response.body().string();
                    //gv.set_name("POST回傳：\n" + response.body().string());
                    //gv.set_name("POST回傳：\n" + decodeStr);
                }
            });
        }
        //bn_cnt_today = (Button) findViewById(R.id.bn_cnt_today);
        //bn_cnt_today.setText(Integer.toString(today_rec_cnt[0]));
        //bn_cnt_today.setText("12");

        bn_subject_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(home.this, Symptom_choose.class);
                startActivity(intent);
            }
        });

        bn_cnt_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(home.this, Check_history.class);
                startActivity(intent);
            }
        });

        bn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(home.this, MainActivity.class);
                startActivity(intent);
            }
        });





    }
}