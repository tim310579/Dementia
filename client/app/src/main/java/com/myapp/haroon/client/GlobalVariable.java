package com.myapp.haroon.client;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Application{
    private String ID, name, password;
    private String number;
    private String now_time;
    private int[] symptom_array;
    private String all_symptom;
    private int now_problem_page, prev_problem_page;
    private String[] symptom_records = new String[13];
    private int now_problem_page_in_overview;

    //public JSONObject[] all_day_records;
    public ArrayList<JSONObject> all_date_records = new ArrayList<JSONObject>();
    public ArrayList<JSONObject> un_upload_records = new ArrayList<JSONObject>();
    //每一筆資料有date, subject_number/name, 幫忙紀錄的admin_number/name(subject自己紀錄則為no_admin)
    //record裡面還包含一個JSONObject，記錄當天的13項symptom

    private String history_date; //查看歷史資料的日期
    private int history_cnt_record; //查看歷史資料時，紀錄所查看的那天有幾筆record
    private int now_in_history_cnt_record; //查看歷史資料時，紀錄已經看到第幾筆
    //private String[] history_date_record = new String[20]; //查看歷史資料時，紀錄所要查看的那天的所有record，一天最多10筆
    //private String history_record_subject_number, history_record_subject_name; //查看歷史資料時，病患名稱
    //private String[] history_record_admin_number = new String[20]; //查看歷史資料時，紀錄者名稱，不同admin可為同一subject紀錄
    //private String[] history_record_admin_name = new String[20];
    //private JSONObject[] history_record = new JSONObject[20]; //查看歷史資料時，紀錄所要查看的那天的所有record，一天最多20筆

    public ArrayList<JSONObject> history_record = new ArrayList<JSONObject>(); //查看歷史資料時，紀錄所要查看的那天的所有record


    private String admin_ID, admin_name;
    private String admin_number;

    private ArrayList<String> admin_manage_patient_number = new ArrayList<String>(); //


    public int is_admin = 0;

    private String login_admin_number, login_admin_name;
    //private String ip;
    //private String port;
    //private String predict_result;
    //private int get_result;


    //設定變數值
    public void set_ID(String ID){
        this.ID = ID;
    }
    public void set_name(String name){
        this.name = name;
    }
    public void set_password(String password){
        this.password = password;
    }

    public void set_number(String number){ this.number = number; }

    public void set_now_time(String now_time){
        this.now_time = now_time;
    }

    public void set_symptom_array(int[] symptom_array){ this.symptom_array = symptom_array; }
    public void set_all_symptom(String all_symptom){
        this.all_symptom = all_symptom;
    }
    public void set_now_problem_page(int now_problem_page){ this.now_problem_page = now_problem_page; }
    public void set_prev_problem_page(int prev_problem_page){ this.prev_problem_page = prev_problem_page; }

    public void set_symptom_records(String[] symptom_records){ this.symptom_records = symptom_records; }

    public void set_now_problem_page_in_overview(int now_problem_page_in_overview){ this.now_problem_page_in_overview = now_problem_page_in_overview; }
    //public void set_all_day_records(JSONObject[] all_day_records){this.all_day_records = all_day_records; }

    public void set_history_date(String history_date){
        this.history_date = history_date;
    }
    public void set_history_cnt_record(int history_cnt_record){ this.history_cnt_record = history_cnt_record; }
    public void set_now_in_history_cnt_record(int now_in_history_cnt_record){ this.now_in_history_cnt_record = now_in_history_cnt_record; }
    //public void set_history_date_record(String[] history_date_record){ this.history_date_record = history_date_record; }
    //public void set_history_record_subject_number(String history_record_subject_number){ this.history_record_subject_number = history_record_subject_number; }
    //public void set_history_record_subject_name(String history_record_subject_name){ this.history_record_subject_name = history_record_subject_name; }
    //public void set_history_record_admin_number(String[] history_record_admin_number){ this.history_record_admin_number = history_record_admin_number; }
    //public void set_history_record_admin_name(String[] history_record_admin_name){ this.history_record_admin_name = history_record_admin_name; }

    //public void set_history_record(JSONObject[] history_record){ this.history_record = history_record; }

    //記錄某天的history array不夠，double size

    /*
    public void double_history_record_admin_data(){ //查看某天history紀錄時，該subject有太多admin幫她記錄
        String[] tmp = this.history_record_admin_number;
        String[] tmp2 = this.history_record_admin_name;
        this.history_record_admin_number = new String[tmp.length*2];
        this.history_record_admin_name = new String[tmp2.length*2];
        for(int i = 0; i < tmp.length; i++){
            this.history_record_admin_number[i] = tmp[i];
        }
        for(int i = 0; i < tmp2.length; i++){
            this.history_record_admin_name[i] = tmp[i];
        }
    }*/
    public void clean_symptom_records(){
        this.symptom_records = new String[13];
    }

    public void set_admin_ID(String admin_ID){
        this.admin_ID = admin_ID;
    }
    public void set_admin_name(String admin_name){
        this.admin_name = admin_name;
    }
    public void set_admin_number(String admin_number){
        this.admin_number = admin_number;
    }

    public void set_login_admin_name(String login_admin_name){
        this.login_admin_name = login_admin_name;
    }
    public void set_login_admin_number(String login_admin_number){
        this.login_admin_number = login_admin_number;
    }

    //public void setip(String ip){ this.ip = ip;}
    //public void setport(String port){ this.port = port;}

    //取得 變數值
    public String get_ID() {
        return ID;
    }
    public String get_name() {
        return name;
    }
    public String get_password() {
        return password;
    }

    public String get_number(){ return number;}


    public String get_now_time() {
        return now_time;
    }

    public int[] get_symptom_array() {
        return symptom_array;
    }
    public String get_all_symptom() {
        return all_symptom;
    }
    public int get_now_problem_page() {
        return now_problem_page;
    }
    public int get_prev_problem_page() {
        return prev_problem_page;
    }

    public String[] get_symptom_records() {
        return symptom_records;
    }

    public int get_now_problem_page_in_overview() {
        return now_problem_page_in_overview;
    }

    public String get_history_date() {
        return history_date;
    }
    public int get_history_cnt_record() {
        return history_cnt_record;
    }
    public int get_now_in_history_cnt_record() {
        return now_in_history_cnt_record;
    }
    //public String[] get_history_date_record() {return history_date_record;}
    //public String get_history_record_subject_number() {return history_record_subject_number;}
    //public String get_history_record_subject_name() {return history_record_subject_name;}
    //public String[] get_history_record_admin_number() {return history_record_admin_number;}
    //public String[] get_history_record_admin_name() {return history_record_admin_name;}
    //public JSONObject[] get_history_record() {return history_record;}

    //public JSONObject[] get_all_day_records(){return all_day_records;}

    public String get_admin_ID() {
        return admin_ID;
    }
    public String get_admin_name() {
        return admin_name;
    }

    public String get_admin_number(){ return admin_number;}

    public String get_login_admin_name() {
        return login_admin_name;
    }
    public String get_login_admin_number(){ return login_admin_number;}


    public boolean haveInternet()
    {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            result = false;
        }
        else
        {
            if (!info.isAvailable())
            {
                result =false;
            }
            else
            {
                result = true;
            }
        }

        return result;
    }
}
