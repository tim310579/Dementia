package com.myapp.haroon.client;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_ID = (TextView) findViewById(R.id.tv_ID);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_password = (TextView) findViewById(R.id.tv_password);

        mEdit_ID = (EditText) findViewById(R.id.mEdit_ID);
        mEdit_name = (EditText) findViewById(R.id.mEdit_name);
        mEdit_password = (EditText) findViewById(R.id.mEdit_password);
        mEdit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //隱藏密碼

        bn_login = (Button) findViewById(R.id.bn_login);
        bn_change_pwd = (Button) findViewById(R.id.bn_change_pwd);
        bn_recover_pwd = (Button) findViewById(R.id.bn_recover_pwd);

        bn_change_pwd.setText(Html.fromHtml("<u>"+"修改密碼"+"</u>"));
        bn_recover_pwd.setText(Html.fromHtml("<u>"+"恢復預設密碼"+"</u>"));

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

                origin_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new_pwd_again.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                Button bn_check = (Button) dialog.findViewById(R.id.bn_check);
                bn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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

                Button bn_check = (Button) dialog.findViewById(R.id.bn_check);
                bn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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

            if(ID.equals("") && name.equals("")) {login_fail=1;}

            if (TextUtils.isEmpty(mEdit_password.getText())) {login_fail=1; }// no password
            else {password = mEdit_password.getText().toString().trim();}

            gv.set_ID(ID);
            gv.set_number(ID);  //ID=number，帳號就是受試者編號
            gv.set_name(name);
            gv.set_password(password);
            //*************************************8
            login_fail = 0; // yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
            if (login_fail == 0){   //can login
                gv.is_admin = 0;
                gv.set_admin_name("no_admin");
                gv.set_admin_ID("no_admin");
                gv.set_admin_number("no_admin");

                if (ID.equals("")){ gv.set_ID("S001"); } // 之後刪掉
                if (ID.equals("")){ gv.set_number("S001"); } // 之後刪掉
                if (name.equals("")) { gv.set_name("王大明"); } // 之後刪掉

                if(gv.get_ID().contains("A00")){ // 包含A00 ->// admin login
                    gv.is_admin = 1;
                    if (name.equals("")) { gv.set_login_admin_name("高榮功"); };
                    //gv.set_admin_ID(ID);
                    gv.set_login_admin_number(ID);
                    //gv.set_number("A001");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Subject_list.class);
                    startActivity(intent);
                    //finish();
                }
                else {
                    if(!gv.haveInternet()){ //沒網路

                    }
                    else {
                        String postUrl = "http://140.113.86.106:50059/web2app";
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                .build();
                        /**設置傳送需求*/
                        JSONObject j_obj = new JSONObject();
                        try {
                            j_obj.put("admin_number", gv.get_admin_number());
                            j_obj.put("subject_number", gv.get_number());
                            String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                            j_obj.put("date", timeStamp);
                        }catch (JSONException e){

                        }
                        MediaType JSON = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(JSON, j_obj.toString());

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
                                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                                //gv.set_name("POST回傳：\n" + response +"_____"+ response.body().string());
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, Symptom_choose.class);
                                startActivity(intent);

                                //String decodeStr = response.body().string();
                                //gv.set_name("POST回傳：\n" + response.body().string());
                                //gv.set_name("POST回傳：\n" + decodeStr);
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
    }

    //}
}