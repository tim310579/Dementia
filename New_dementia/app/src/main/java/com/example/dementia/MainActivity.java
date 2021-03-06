package com.example.dementia;

import static androidx.appcompat.app.AlertDialog.*;

//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private String ID="", name="", password;

    private TextView tv_ID, tv_name, tv_password;
    private EditText mEdit_ID, mEdit_name, mEdit_password;
    private Button bn_login, bn_change_pwd, bn_recover_pwd;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_ID = (TextView) findViewById(R.id.tv_ID);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_password = (TextView) findViewById(R.id.tv_password);

        mEdit_ID = (EditText) findViewById(R.id.mEdit_ID);
        mEdit_name = (EditText) findViewById(R.id.mEdit_name);
        mEdit_password = (EditText) findViewById(R.id.mEdit_password);
        mEdit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //????????????

        bn_login = (Button) findViewById(R.id.bn_login);
        bn_change_pwd = (Button) findViewById(R.id.bn_change_pwd);
        bn_recover_pwd = (Button) findViewById(R.id.bn_recover_pwd);

        bn_change_pwd.setText(Html.fromHtml("<u>"+"????????????"+"</u>"));
        bn_recover_pwd.setText(Html.fromHtml("<u>"+"??????????????????"+"</u>"));

        //bn_login.setOnClickListener(new ButtonClickListener());
        //bn_change_pwd.setOnClickListener(new ButtonClickListener());
        //bn_recover_pwd.setOnClickListener(new ButtonClickListener());

        bn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_symptom_choose();
            }
        });

        bn_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup_change_pwd);

                //dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                //TextView text = (TextView) dialog.findViewById(R.id.textView1);
                //text.setText("Android custom dialog example!");
                final EditText ID_or_name = (EditText) dialog.findViewById(R.id.mEdit_ID_or_name);
                final EditText origin_pwd = (EditText) dialog.findViewById(R.id.mEdit_origin_pwd);
                final EditText new_pwd = (EditText) dialog.findViewById(R.id.mEdit_new_pwd);
                final EditText new_pwd_again = (EditText) dialog.findViewById(R.id.mEdit_new_pwd_again);

                final TextView tv_err_msg = (TextView) dialog.findViewById(R.id.tv_err_msg);

                origin_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new_pwd_again.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                Button bn_check = (Button) dialog.findViewById(R.id.bn_check);
                bn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalVariable gv = (GlobalVariable) getApplicationContext();
                        if(!gv.haveInternet()){ //?????????
                        }
                        else {
                            if(!TextUtils.isEmpty(ID_or_name.getText()) && //??????????????????
                                    !TextUtils.isEmpty(origin_pwd.getText()) && //????????????
                                    !TextUtils.isEmpty(new_pwd.getText()) && //????????????
                                    !TextUtils.isEmpty(new_pwd_again.getText()) && //??????????????????
                            new_pwd.getText().toString().equals(new_pwd_again.getText().toString())) //??????????????????
                            {
                                String postUrl = "http://140.113.193.87:20059/appchangepwd";
                                OkHttpClient client = new OkHttpClient().newBuilder()
                                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                        .build();
                                /**??????????????????*/
                                JSONObject j_obj = new JSONObject();
                                try {
                                    j_obj.put("ID_or_name", ID_or_name.getText());
                                    j_obj.put("origin_pwd", origin_pwd.getText());
                                    j_obj.put("new_pwd", new_pwd.getText());
                                } catch (JSONException e) {
                                }
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, j_obj.toString());

                                Request request = new Request.Builder()
                                        .url(postUrl)
                                        .addHeader("Accept-Encoding", "gzip, deflate, br")
                                        .post(body)
                                        .build();
                                /**????????????*/
                                Call call = client.newCall(request);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        /**?????????????????????????????????*/
                                        //gv.set_name(e.getMessage());
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        /**????????????*/
                                        dialog.dismiss();
                                    }
                                });
                            }
                            else{ //has error
                                if(TextUtils.isEmpty(ID_or_name.getText()) || //?????????????????????
                                        TextUtils.isEmpty(origin_pwd.getText()) || //???????????????
                                        TextUtils.isEmpty(new_pwd.getText()) || //???????????????
                                        TextUtils.isEmpty(new_pwd_again.getText()))
                                {
                                    tv_err_msg.setText("??????????????????!");
                                }
                                else{ //??????????????????
                                    tv_err_msg.setText("????????????????????????!");
                                }

                            }

                        }
                        //dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });

        bn_recover_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup_recover_pwd);
                //dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                //TextView text = (TextView) dialog.findViewById(R.id.textView1);
                //text.setText("Android custom dialog example!");
                final EditText ID_or_name = (EditText) dialog.findViewById(R.id.mEdit_ID_or_name);
                final CheckBox chk_recover_pwd = (CheckBox) dialog.findViewById(R.id.chk_recover_pwd);

                final TextView tv_err_msg = (TextView) dialog.findViewById(R.id.tv_err_msg);

                Button bn_check = (Button) dialog.findViewById(R.id.bn_check);
                bn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalVariable gv = (GlobalVariable) getApplicationContext();
                        if(!gv.haveInternet()){ //?????????
                        }
                        else {
                            if(chk_recover_pwd.isChecked() && //?????????
                                    !TextUtils.isEmpty(ID_or_name.getText())) //?????????or name
                            {
                                String postUrl = "http://140.113.193.87:20059/resetpwd";
                                //String postUrl = "http://httpbin.org/post";
                                OkHttpClient client = new OkHttpClient().newBuilder()
                                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                        .build();
                                /**??????????????????*/
                                JSONObject j_obj = new JSONObject();
                                try {
                                    j_obj.put("ID_or_name", ID_or_name.getText());
                                } catch (JSONException e) {
                                }
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, j_obj.toString());

                                Request request = new Request.Builder()
                                        .url(postUrl)
                                        .addHeader("Accept-Encoding", "gzip, deflate, br")
                                        .post(body)
                                        .build();
                                /**????????????*/
                                Call call = client.newCall(request);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        /**?????????????????????????????????*/
                                        //gv.set_name(e.getMessage());
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        /**????????????*/
                                        dialog.dismiss();
                                    }
                                });
                            }
                            else {
                                if(TextUtils.isEmpty(ID_or_name.getText())){
                                    tv_err_msg.setText("????????????????????????!");
                                }
                                else {
                                    tv_err_msg.setText("???????????????????????????!");
                                }
                            }
                        }

                    }
                });

                dialog.show();
            }

        });

    }

    private void goto_symptom_choose(){new goto_symptom_choose().start();}
    //private void pop_change_pwd(){new pop_change_pwd().start();}
    //private void pop_recover_pwd(){new pop_recover_pwd().start();}

    class goto_symptom_choose extends Thread{

        public goto_symptom_choose(){
        }
        @Override
        public void run() {
            int login_fail = 0;
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            if (!TextUtils.isEmpty(mEdit_ID.getText())){ID = mEdit_ID.getText().toString().trim();}// fill in ID
            if (!TextUtils.isEmpty(mEdit_name.getText())){name = mEdit_name.getText().toString().trim();}

            if(ID.equals("") && name.equals("")) {login_fail=1;} //?????????

            if (TextUtils.isEmpty(mEdit_password.getText())) {login_fail=1; }// no password
            else {password = mEdit_password.getText().toString().trim();}

            //gv.set_ID(ID);
            //gv.set_number(ID);  //ID=number??????????????????????????????
            //gv.set_name(name);
            gv.set_password(password);
            //*************************************8
            //login_fail = 0; // ????????????
            //*************************************8
            if (login_fail == 0){   //can login
                //gv.reset_all_var(); // reset global vars
                gv.is_admin = 0;
                gv.set_admin_name("");
                gv.set_admin_ID("");
                gv.set_admin_number("");
                gv.set_name("");
                gv.set_number("");
                gv.set_ID("");
                gv.set_login_admin_number("");
                gv.set_login_admin_name("");
                gv.all_date_records.clear();
                gv.set_sensor_charge_status(""); //sensor is using(charging = no)
                gv.subject_sensor_charge_status.clear(); // for admin, (no sensor now)

                //if (ID.equals("")){ gv.set_ID("S001"); } // ????????????
                //if (ID.equals("")){ gv.set_number("S001"); } // ????????????
                //if (name.equals("")) { gv.set_name("?????????"); } // ????????????

                /*
                if(gv.get_ID().contains("A00")){ // ??????A00 ->// admin login
                    gv.is_admin = 1;
                    if (name.equals("")) { gv.set_login_admin_name("?????????"); };
                    //gv.set_admin_ID(ID);
                    gv.set_login_admin_number(ID);
                    //gv.set_number("A001");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Subject_list.class);
                    startActivity(intent);
                    //finish();
                }*/


                if(!gv.haveInternet()){ //?????????
                }
                else {
                    String postUrl = "http://140.113.193.87:20059/applogin";
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .build();
                    /**??????????????????*/
                    JSONObject j_obj = new JSONObject();
                    try {
                        j_obj.put("login_number", ID);
                        j_obj.put("login_name", name);
                        j_obj.put("login_password", gv.get_password());
                    }catch (JSONException e){
                    }
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, j_obj.toString());

                    Request request = new Request.Builder()
                            .url(postUrl)
                            .addHeader("Accept-Encoding", "gzip, deflate, br")
                            .post(body)
                            .build();
                    /**????????????*/
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            /**?????????????????????????????????*/
                            //gv.set_name(e.getMessage());
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            /**????????????*/
                            int has_error = 0;
                            GlobalVariable gv = (GlobalVariable) getApplicationContext();
                            try{
                                JSONObject j_obj = new JSONObject(response.body().string());
                                if(j_obj.getString("status").equals("manage")){
                                    gv.is_admin = 2; //????????????
                                }
                                else if(j_obj.getString("status").equals("hospital")){
                                    gv.is_admin = 1;
                                }
                                else{
                                    gv.is_admin = 0;
                                }
                                gv.set_login_admin_number(j_obj.getString("admin_number"));
                                gv.set_login_admin_name(j_obj.getString("admin_name"));
                                if(gv.is_admin == 0) {
                                    gv.set_number(j_obj.getString("subject_number"));
                                    gv.set_name(j_obj.getString("subject_name"));
                                    //gv.set_name(j_obj.getString("charging"));
                                    gv.set_sensor_charge_status(j_obj.getString("charging"));

                                    //gv.set_sensor_status();
                                }
                                else{
                                    //gv.set_login_admin_number(j_obj.getString("subjects"));
                                    //gv.set_login_admin_number(j_obj.getString("subjects"));

                                    JSONArray j_obj_p_list = j_obj.getJSONArray("subjects");
                                    //j_obj_p_list.get(0);
                                    gv.admin_manage_patient_number.clear();
                                    gv.admin_manage_patient_name.clear();
                                    gv.admin_manage_patient_number.clear();

                                    gv.admin_manage_patient_name.add(""); //??????????????????
                                    gv.admin_manage_patient_number.add("");
                                    gv.subject_sensor_charge_status.add(""); //??????????????????


                                    for(int i = 0; i < j_obj_p_list.length(); i++){
                                        gv.admin_manage_patient_name.add(j_obj_p_list.getJSONObject(i).getString("subject_name"));
                                        gv.admin_manage_patient_number.add(j_obj_p_list.getJSONObject(i).getString("subject_number"));
                                        gv.subject_sensor_charge_status.add(j_obj_p_list.getJSONObject(i).getString("charging"));
                                    }
                                    JSONArray j_obj_a_list = j_obj.getJSONArray("admin_list");
                                    gv.admin_manage_admin_number.clear();
                                    gv.admin_manage_admin_name.clear();
                                    for(int i = 0; i < j_obj_a_list.length(); i++) {
                                        gv.admin_manage_admin_number.add(j_obj_a_list.getJSONObject(i).getString("admin_id"));
                                        gv.admin_manage_admin_name.add(j_obj_a_list.getJSONObject(i).getString("admin_name"));
                                    }

                                    //gv.admin_manage_admin_number.add("usr000000");
                                    //gv.admin_manage_admin_name.add("lin");
                                    //gv.admin_manage_admin_number.add("usrtest");
                                    //gv.admin_manage_admin_name.add("linhah");
                                    //gv.set_login_admin_name(j_obj_a_list.toString());
                                }
                            }catch(JSONException e){
                                has_error = 1;
                                //gv.set_name("POST??????error???\n" + e.toString());
                            }

                            if(has_error == 1){
                                //????????????
                            }
                            else {
                                if (gv.is_admin == 0) { //????????????
                                    Intent intent = new Intent();
                                    //intent.setClass(MainActivity.this, Symptom_choose.class);
                                    intent.setClass(MainActivity.this, home.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, Subject_list.class);
                                    startActivity(intent);
                                }
                            }
                            //gv.set_name("POST?????????\n" + response +"_____"+ response.body().string());
                            //Intent intent = new Intent();
                            //intent.setClass(MainActivity.this, Symptom_choose.class);
                            //startActivity(intent);
                            //String decodeStr = response.body().string();
                            //gv.set_name("POST?????????\n" + response.body().string());
                            //gv.set_name("POST?????????\n" + decodeStr);
                        }
                    });
                    //Intent intent = new Intent();
                    //intent.setClass(MainActivity.this, Symptom_choose.class);
                    //startActivity(intent);
                    //finish();
                }


            }

        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//???????????????
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //}
}