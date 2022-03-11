package com.myapp.haroon.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Check_history extends AppCompatActivity {
    private TextView tv_show_number, tv_show_name, tv_show_time;
    private Button bn_back_to_login, bn_record_another, bn_logout, bn_re_upload_record;
    private Button bn_test;
    //private CalendarView calendar_view;
    private TextView tv_please_click_date;

    private TextView tv_month;
    private String[] Months = {"鬼月", "一月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "十一月", "十二月"};

    private Button[] bn_days = new Button[36];
    private String[] dates = new String[36];
    private int start_date_index = 21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_history);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        tv_show_number = (TextView) findViewById(R.id.tv_show_number);
        tv_show_name = (TextView) findViewById(R.id.tv_show_name);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);

        tv_show_number.setText(gv.get_number());
        tv_show_name.setText(gv.get_name());
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
        //System.out.println(timeStamp);
        tv_show_time.setText(timeStamp);


        bn_back_to_login = (Button) findViewById(R.id.bn_back_to_login);
        bn_back_to_login.setOnClickListener(new ButtonClickListener());
        if(gv.is_admin > 0){
            bn_back_to_login.setText("回受試者清單");
        }
        else{
            bn_back_to_login.setText("回主畫面");
        }

        bn_record_another = (Button) findViewById(R.id.bn_record_another);
        bn_record_another.setOnClickListener(new ButtonClickListener());
        bn_logout = (Button) findViewById(R.id.bn_logout);
        bn_logout.setOnClickListener(new ButtonClickListener());
        bn_re_upload_record = (Button) findViewById(R.id.bn_re_upload_record);
        bn_re_upload_record.setOnClickListener(new ButtonClickListener());
        bn_test = (Button) findViewById(R.id.bn_test);
        bn_test.setOnClickListener(new ButtonClickListener());

        bn_days[1] = (Button) findViewById(R.id.bn_day1);
        bn_days[2] = (Button) findViewById(R.id.bn_day2);
        bn_days[3] = (Button) findViewById(R.id.bn_day3);
        bn_days[4] = (Button) findViewById(R.id.bn_day4);
        bn_days[5] = (Button) findViewById(R.id.bn_day5);
        bn_days[6] = (Button) findViewById(R.id.bn_day6);
        bn_days[7] = (Button) findViewById(R.id.bn_day7);
        bn_days[8] = (Button) findViewById(R.id.bn_day8);
        bn_days[9] = (Button) findViewById(R.id.bn_day9);
        bn_days[10] = (Button) findViewById(R.id.bn_day10);
        bn_days[11] = (Button) findViewById(R.id.bn_day11);
        bn_days[12] = (Button) findViewById(R.id.bn_day12);
        bn_days[13] = (Button) findViewById(R.id.bn_day13);
        bn_days[14] = (Button) findViewById(R.id.bn_day14);
        bn_days[15] = (Button) findViewById(R.id.bn_day15);
        bn_days[16] = (Button) findViewById(R.id.bn_day16);
        bn_days[17] = (Button) findViewById(R.id.bn_day17);
        bn_days[18] = (Button) findViewById(R.id.bn_day18);
        bn_days[19] = (Button) findViewById(R.id.bn_day19);
        bn_days[20] = (Button) findViewById(R.id.bn_day20);
        bn_days[21] = (Button) findViewById(R.id.bn_day21);
        bn_days[22] = (Button) findViewById(R.id.bn_day22);
        bn_days[23] = (Button) findViewById(R.id.bn_day23);
        bn_days[24] = (Button) findViewById(R.id.bn_day24);
        bn_days[25] = (Button) findViewById(R.id.bn_day25);
        bn_days[26] = (Button) findViewById(R.id.bn_day26);
        bn_days[27] = (Button) findViewById(R.id.bn_day27);
        bn_days[28] = (Button) findViewById(R.id.bn_day28);
        bn_days[29] = (Button) findViewById(R.id.bn_day29);
        bn_days[30] = (Button) findViewById(R.id.bn_day30);
        bn_days[31] = (Button) findViewById(R.id.bn_day31);
        bn_days[32] = (Button) findViewById(R.id.bn_day32);
        bn_days[33] = (Button) findViewById(R.id.bn_day33);
        bn_days[34] = (Button) findViewById(R.id.bn_day34);
        bn_days[35] = (Button) findViewById(R.id.bn_day35);

        for(int i = 1; i < 36; i++){
            bn_days[i].setEnabled(false);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String timeStamp_today = sdf.format(Calendar.getInstance().getTime());

        Date date = sdf.parse(timeStamp_today, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1;
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(Months[month]);

        //把今天設在第四行
        int today_day = Integer.parseInt(timeStamp_today.split("/")[2]);
        dates[start_date_index + day_of_week] = timeStamp_today;
        bn_days[start_date_index + day_of_week].setText(Integer.toString(today_day)); //把第四行某格設為今天日期
        bn_days[start_date_index + day_of_week].setBackground(this.getResources().getDrawable(R.drawable.button_today, getTheme()));
        dates[0] = "0000/00/00";

        calendar.add(Calendar.DATE, -(start_date_index + day_of_week)); // 往前推回n天,回到第一格

        for (int i = 1; i <= 35; i ++) { //更新35格的日期
            calendar.add(Calendar.DATE, 1); //後n天
            Date date_next_n = calendar.getTime();
            String str_day_next_n = sdf.format(date_next_n);
            dates[i] = str_day_next_n;
            int int_day_next_n = Integer.parseInt(str_day_next_n.split("/")[2]);
            bn_days[i].setText(Integer.toString(int_day_next_n)); //把前i設為當天日期
        }

        tv_please_click_date = (TextView) findViewById(R.id.tv_please_click_date);

        //放假資料test###############**************-----------------
        /*
        if(gv.all_date_records.size() < 5) {
            String[] fake_dates = {"2022/02/13", "2022/02/15", "2022/02/16", "2022/02/09", "2022/02/22"};
            String[] fake_subject_numbers = {"S002", "S001", "S003", "S004", "S005"};
            String[] fake_subject_names = {"林大名", "王大明", "張大名", "謝大名", "陳大名"};
            String[] fake_admin_numbers = {"A006", "no_admin", "no_admin", "A004", "A002"};
            String[] fake_admin_names = {"黃宇", "no_admin", "no_admin", "Benny", "Vincent"};

            for (int h = 0; h < 5; h++) {
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
        //放假資料test###############**************-----------------

        String that_record_date = "";
        for(JSONObject obj : gv.all_date_records){
            try {
                that_record_date = obj.getString("date");
                String detail_record = obj.getString("record");
                String that_record_subject_number = obj.getString("subject_number");
                String that_record_admin_number = obj.getString("admin_number");
                int date_index = Arrays.asList(dates).indexOf(that_record_date);

                if(gv.is_admin == 0){
                    if(date_index > 0 && that_record_subject_number.equals(gv.get_number())){
                        //目前calendar35天中有已記錄的日期 && 是該subject的記錄
                        bn_days[date_index].setTextColor(Color.parseColor("#0000ff")); //設為藍色
                        bn_days[date_index].setText(Html.fromHtml("<u>" + bn_days[date_index].getText() + "</u>"));
                        bn_days[date_index].setEnabled(true); //啟用該日期按鍵
                    }
                }
                else {
                    if (date_index > 0 && (that_record_subject_number.equals(gv.get_number()))
                            && that_record_admin_number.equals(gv.get_admin_number())) {
                        //目前calendar35天中有已記錄的日期 && 是該subject的記錄 && 是該admin幫她紀錄的 才亮
                        bn_days[date_index].setTextColor(Color.parseColor("#0000ff")); //設為藍色
                        bn_days[date_index].setText(Html.fromHtml("<u>" + bn_days[date_index].getText() + "</u>"));
                        bn_days[date_index].setEnabled(true); //啟用該日期按鍵
                    }
                }
                /*
                if(date_index > 0)
                {
                    if (gv.is_admin == 1){
                        if(gv.get_admin_number().equals("A001")) {
                            bn_days[date_index].setTextColor(Color.parseColor("#0000ff")); //設為藍色
                            bn_days[date_index].setText(Html.fromHtml("<u>" + bn_days[date_index].getText() + "</u>"));
                            bn_days[date_index].setEnabled(true); //啟用該日期按鍵
                        }
                    }
                }*/
            }catch(JSONException e){
                that_record_date = "erorrrrrrrrr";
            }
        }
        //initialize要看的日期資訊
        JSONObject[] is_null = new JSONObject[20];
        //gv.set_history_record(is_null);
        gv.history_record.clear(); //先預設要查看那天的record為空
        gv.set_history_cnt_record(0);
        gv.set_now_in_history_cnt_record(0);
        gv.set_history_date("0000/00/00");
        //跳去該日期的overview
        for(int i = 1; i < 36; i++) {
            final int j = i;
            bn_days[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalVariable gv = (GlobalVariable) getApplicationContext();

                    gv.set_history_date(dates[j]);
                    String tmp_subject_number, tmp_subject_name, tmp_admin_number, tmp_admin_name;
                    String tmp_date;
                    JSONObject tmp_record =  new JSONObject();
                    int cnt_records = 0;
                    for(JSONObject obj : gv.all_date_records){
                        try {
                            tmp_subject_number = obj.getString("subject_number");
                            tmp_subject_name = obj.getString("subject_name");
                            tmp_admin_number = obj.getString("admin_number");
                            tmp_admin_name = obj.getString("admin_name");
                            tmp_date = obj.getString("date");
                            tmp_record = obj.getJSONObject("record");
                            if(gv.is_admin == 0){
                                if (tmp_date.equals(gv.get_history_date()) &&  //日期是要查看的那天
                                        tmp_subject_number.equals(gv.get_number())) //只看該subject的資料
                                        //||  //只看該admin記錄的資料
                                //gv.get_login_admin_number().equals("A001")))  //之後可刪，A001可看所有subject
                                {
                                    gv.history_record.add(obj);
                                    cnt_records += 1;
                                }
                            }
                            else {
                                if (tmp_date.equals(gv.get_history_date()) &&  //日期是要查看的那天
                                        tmp_subject_number.equals(gv.get_number()) && //只看該subject的資料
                                        tmp_admin_number.equals(gv.get_admin_number()))//||  //只看該admin記錄的資料
                                //gv.get_login_admin_number().equals("A001")))  //之後可刪，A001可看所有subject
                                {
                                    gv.history_record.add(obj);
                                    cnt_records += 1;
                                }
                            }
                        }catch(JSONException e){
                            //tmp_record = "erorrrrrrrrr";
                        }
                    }
                    gv.set_history_cnt_record(cnt_records);
                    gv.set_now_in_history_cnt_record(0); //從第0筆開始看

                    //gv.set_name(gv.history_record.get(gv.history_record.size()-1).toString());

                    Intent intent = new Intent();
                    intent.setClass(Check_history.this, History_record_overview.class);
                    startActivity(intent);
                }
            });
        }
        bn_re_upload_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GlobalVariable gv = (GlobalVariable) getApplicationContext();
                if (!gv.haveInternet()) { //沒網路
                    final Dialog dialog = new Dialog(Check_history.this);
                    dialog.setContentView(R.layout.popup_unsaved_notice);

                    TextView tv_hint_no_net = (TextView) dialog.findViewById((R.id.tv_message));
                    tv_hint_no_net.setText("目前無網路連線，請先連接網路後再補上傳資料");
                    Button bn_back = (Button) dialog.findViewById(R.id.bn_back);
                    Button bn_check_to_leave = (Button) dialog.findViewById(R.id.bn_check_to_leave);
                    bn_check_to_leave.setText("我知道了");
                    bn_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    bn_check_to_leave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
                else {  //有網路，補上傳record
                    if (gv.un_upload_records.size() > 0) { //有record未上傳
                        //TODO 上傳資料
                        for (JSONObject json_obj : gv.un_upload_records) {
                            String time_now = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                            try {
                                json_obj.put("upload_time", time_now); //上傳時間
                            } catch (JSONException e) {
                            }
                            gv.all_date_records.add(json_obj);

                            //***********************************上傳到server
                            String postUrl = "http://140.113.86.106:50059/app2web";
                            //String postUrl = "http://httpbin.org/post";
                            //String postUrl = "https://jsonplaceholder.typicode.com/posts";

                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                    .build();
                            /**設置傳送需求*/
                            MediaType JSON = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(JSON, json_obj.toString());

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
                                    //gv.set_name(gv.get_name() + "POST回傳：\n" + response + "_____" + response.body().string());
                                }
                            });
                        }
                        gv.un_upload_records.clear();
                        Intent intent;
                        intent = new Intent();
                        intent.setClass(Check_history.this, Check_history.class);
                        startActivity(intent);
                    }
                    else { //沒有未上傳資料
                        final Dialog dialog = new Dialog(Check_history.this);
                        dialog.setContentView(R.layout.popup_unsaved_notice);

                        TextView tv_hint_no_net = (TextView) dialog.findViewById((R.id.tv_message));
                        tv_hint_no_net.setText("所有紀錄都已上傳，不用再按了");
                        Button bn_back = (Button) dialog.findViewById(R.id.bn_back);
                        Button bn_check_to_leave = (Button) dialog.findViewById(R.id.bn_check_to_leave);
                        bn_check_to_leave.setText("我知道了");
                        bn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        bn_check_to_leave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                }
            }
        });
        bn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final GlobalVariable gv = (GlobalVariable) getApplicationContext();
                //gv.web_to_app();

                final GlobalVariable gv = (GlobalVariable) getApplicationContext();
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
                        String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
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

                                try{
                                    JSONObject j_obj = new JSONObject(response.body().string());
                                    //JSONObject j_arr = new JSONObject(j_obj.getJSONObject("records"));
                                    gv.set_name("POST回傳：\n" +j_obj.getJSONArray("records").getJSONObject(0).toString());
                                    //gv.set_name("POST回傳：\n" + j_obj.getString("records") + "\n" + j_obj.getString("records").getClass().getSimpleName());
                                    gv.set_number(Integer.toString(j_obj.getJSONArray("records").length()));
                                    //j_obj.getJSONArray("records").length();
                                }catch(JSONException e){
                                    gv.set_name("POST回傳err：\n" + e.toString());
                                }

                            GlobalVariable gv = (GlobalVariable) getApplicationContext();
                            //gv.set_name("POST回傳：\n" + response +"_____"+ response.body().string());
                            Intent intent = new Intent();
                            intent.setClass(Check_history.this, Check_history.class);
                            startActivity(intent);

                            //String decodeStr = response.body().string();
                            //gv.set_name("POST回傳：\n" + response.body().string());
                            //gv.set_name("POST回傳：\n" + decodeStr);
                        }
                    });
                }
            }
        });
        //finish();



        /*
        LinearLayout mainLinerLayout = (LinearLayout) findViewById(R.id.calendar_layout);
        Button bn_daya1 = new Button(this);
        bn_daya1.setText("八");
        Resources res = this.getResources();
        Drawable drawable;
        drawable = res.getDrawable(R.drawable.button_day, getTheme());
        bn_daya1.setBackground(drawable);

        bn_daya1.setTextColor(Color.parseColor("#000000"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 100);
        bn_daya1.setLayoutParams(params);
        //bn_daya1.setWidth(40);
        //bn_daya1.setHeight(40);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //此处相当于布局文件中的Android:layout_gravity属性
        lp.gravity = Gravity.CENTER;
        bn_daya1.setLayoutParams(lp);
        bn_daya1.setGravity(Gravity.CENTER);
        bn_daya1.setTextSize(30);

        mainLinerLayout.addView(bn_daya1);

        */
        /*RadCalendarView calendarView = new RadCalendarView(this);

        calendarView.setCustomizationRule(new Procedure<CalendarCell>() {
            @Override
            public void apply(CalendarCell argument) {
                if (argument.getCellType() == CalendarCellType.Date && myShifts.contains(argument.getDate()) {
                    argument.setBackgroundColor(Color.BLUE, Color.BLUE);
                }
            }
        });

        setContentView(calendarView);


        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String tmp_str = "";
                tmp_str += Integer.toString(year) + '/' + Integer.toString(month+1) + '/' + Integer.toString(dayOfMonth);

                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                JSONObject tmp_record = new JSONObject();

                for(JSONObject obj : gv.all_date_records){
                    try {
                        tmp_str += obj.getString("date");
                        tmp_str += obj.getString("record");

                    }catch(JSONException e){
                        tmp_str = "erorrrrrrrrr";
                    }
                }
                    //  tmp_str += obj.toString();
                    //System.out.println(obj.toString());
                    //}
                tv_please_click_date.setText(tmp_str);
            }
        });
        //calendar_view.setDateTextAppearance();
        */

    }
    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bn_back_to_login:
                    goto_login();
                    break;

                case R.id.bn_record_another:
                    goto_symptom_choose();
                    break;
                case R.id.bn_logout:
                    logout();
                    break;
                //case R.id.bn_re_upload_record:
                  //  re_upload_record();
                    //break;
                default:
                    break;
            }
        }
    }
    private void goto_login(){new goto_login().start();}
    private void goto_symptom_choose(){new goto_symptom_choose().start();}
    private void logout(){new logout().start();}
    //private void re_upload_record(){new re_upload_record().start();}

    class goto_login extends Thread{
        public goto_login(){
        }
        @Override
        public void run(){
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            if (gv.is_admin > 0) {
                Intent intent = new Intent();
                intent.setClass(Check_history.this, Subject_list.class);
                startActivity(intent);
            }
            else {
                Intent intent;
                intent = new Intent();
                //intent.setClass(Check_history.this, MainActivity.class);
                intent.setClass(Check_history.this, home.class);
                startActivity(intent);
            }
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
            intent.setClass(Check_history.this  , Symptom_choose.class);
            startActivity(intent);
            //finish();
        }
    }
    class logout extends Thread{
        public logout(){
        }
        @Override
        public void run(){
            Intent intent;
            intent = new Intent();
            intent.setClass(Check_history.this  , MainActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    class re_upload_record extends Thread{
        public re_upload_record(){
        }
        @Override
        public void run(){
            final GlobalVariable gv = (GlobalVariable) getApplicationContext();
            if(!gv.haveInternet()) { //沒網路
                final Dialog dialog = new Dialog(Check_history.this);
                dialog.setContentView(R.layout.popup_unsaved_notice);

                TextView tv_hint_no_net = (TextView) dialog.findViewById((R.id.tv_message));
                tv_hint_no_net.setText("目前無網路連線，請先連接網路後再補上傳資料");
                Button bn_back= (Button) dialog.findViewById(R.id.bn_back);
                Button bn_check_to_leave = (Button) dialog.findViewById(R.id.bn_check_to_leave);
                bn_check_to_leave.setText("我知道了");
                bn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bn_check_to_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            else {  //有網路，補上傳record
                if (gv.un_upload_records.size() > 0) { //有record未上傳
                    //TODO 上傳資料
                    for (JSONObject json_obj : gv.un_upload_records) {
                        String time_now = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                        try {
                            json_obj.put("upload_time", time_now); //上傳時間
                        } catch (JSONException e) {
                        }
                        gv.all_date_records.add(json_obj);

                        //***********************************上傳到server
                        //String postUrl = "http://140.113.86.106:50059/app2web";
                        String postUrl = "http://httpbin.org/post";
                        //String postUrl = "https://jsonplaceholder.typicode.com/posts";

                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                .build();
                        /**設置傳送需求*/
                        MediaType JSON = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(JSON, json_obj.toString());

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
                                gv.set_name(gv.get_name() + "POST回傳：\n" + response + "_____" + response.body().string());
                            }
                        });
                    }
                    gv.un_upload_records.clear();
                    Intent intent;
                    intent = new Intent();
                    intent.setClass(Check_history.this, Check_history.class);
                    startActivity(intent);
                }
                else {

                }
            }

            //finish();
        }
    }
}