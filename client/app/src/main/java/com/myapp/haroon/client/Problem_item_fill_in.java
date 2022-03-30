package com.myapp.haroon.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Problem_item_fill_in extends AppCompatActivity {
    private TextView tv_show_number, tv_show_name, tv_show_time;
    private TextView tv_problem_item;
    private String name, time;
    private Button bn_back, bn_next, bn_check_history;
    private int[] symptom_array;
    private String[] all_symptom = {"妄想", "幻覺", "激動/攻擊性", "憂鬱/情緒不佳",
            "焦慮", "昂然自得/欣快感", "冷漠/毫不在意", "言行失控", "暴躁易怒/情緒易變",
            "怪異動作", "睡眠/夜間行為", "食慾/飲食行為改變", "跌倒"};
    private int severity=-1, distress=-1, begin_time=-1, end_time=-1;
    private String detailed_begin_time, detailed_end_time;
    private CheckBox[] checkBoxes_severity = new CheckBox[3];
    private CheckBox[] checkBoxes_distress = new CheckBox[6];
    private CheckBox[] checkBoxes_begin_time = new CheckBox[3];
    private CheckBox[] checkBoxes_end_time = new CheckBox[3];
    //private CheckBox chk_severity_1, chk_severity_2, chk_severity_3;
    //private CheckBox chk_distress_0, chk_distress_1, chk_distress_2, chk_distress_3, chk_distress_4, chk_distress_5;
    //private CheckBox chk_begin_morning, chk_begin_afternoon, chk_begin_now_time;
    //private CheckBox chk_end_morning, chk_end_afternoon, chk_end_now_time;

    private EditText mEdit_begin_hr, mEdit_begin_min, mEdit_end_hr, mEdit_end_min;
    private EditText mEdit_event_description;
    private Button bn_begin_date, bn_end_date;
    private TextView tv_begin_date, tv_end_date;

    private String final_record="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_item_fill_in);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        //number = gv.get_number();
        //name = gv.get_name();

        tv_show_number = (TextView) findViewById(R.id.tv_show_number);
        tv_show_name = (TextView) findViewById(R.id.tv_show_name);
        tv_show_time = (TextView) findViewById(R.id.tv_show_time);

        tv_show_number.setText(gv.get_number());
        tv_show_name.setText(gv.get_name());
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
        //System.out.println(timeStamp);
        tv_show_time.setText(timeStamp);

        symptom_array = gv.get_symptom_array();
        String tmp_symptom = "";
        for (int i = gv.get_now_problem_page(); i < 12; i++){
            if (symptom_array[i] != 0){
                tmp_symptom = all_symptom[i];
                //gv.set_now_problem_page(i+1);
                break;
            }
        }
        tv_problem_item = (TextView) findViewById(R.id.tv_problem_item);
        tv_problem_item.setText(tmp_symptom);

        checkBoxes_severity[0] = (CheckBox)findViewById(R.id.chk_severity_1);
        checkBoxes_severity[1] = (CheckBox)findViewById(R.id.chk_severity_2);
        checkBoxes_severity[2] = (CheckBox)findViewById(R.id.chk_severity_3);
        checkBoxes_distress[0] = (CheckBox)findViewById(R.id.chk_distress_0);
        checkBoxes_distress[1] = (CheckBox)findViewById(R.id.chk_distress_1);
        checkBoxes_distress[2] = (CheckBox)findViewById(R.id.chk_distress_2);
        checkBoxes_distress[3] = (CheckBox)findViewById(R.id.chk_distress_3);
        checkBoxes_distress[4] = (CheckBox)findViewById(R.id.chk_distress_4);
        checkBoxes_distress[5] = (CheckBox)findViewById(R.id.chk_distress_5);

        checkBoxes_begin_time[0] = (CheckBox)findViewById(R.id.chk_begin_morning);
        checkBoxes_begin_time[1] = (CheckBox)findViewById(R.id.chk_begin_afternoon);
        checkBoxes_begin_time[2] = (CheckBox)findViewById(R.id.chk_begin_now_time);

        checkBoxes_end_time[0] = (CheckBox)findViewById(R.id.chk_end_morning);
        checkBoxes_end_time[1] = (CheckBox)findViewById(R.id.chk_end_afternoon);
        checkBoxes_end_time[2] = (CheckBox)findViewById(R.id.chk_end_now_time);

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
        checkBoxes_begin_time[2].setOnCheckedChangeListener(checkBoxOnCheckedChange_3);

        checkBoxes_end_time[0].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);
        checkBoxes_end_time[1].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);
        checkBoxes_end_time[2].setOnCheckedChangeListener(checkBoxOnCheckedChange_4);

        mEdit_begin_hr = (EditText) findViewById(R.id.mEdit_begin_hr);
        mEdit_begin_min = (EditText) findViewById(R.id.mEdit_begin_min);
        mEdit_end_hr = (EditText) findViewById(R.id.mEdit_end_hr);
        mEdit_end_min = (EditText) findViewById(R.id.mEdit_end_min);

        bn_begin_date = (Button) findViewById(R.id.bn_begin_date);
        bn_end_date = (Button) findViewById(R.id.bn_end_date);

        tv_begin_date = (TextView) findViewById(R.id.tv_begin_date);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);

        String timeStamp0 = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
        tv_begin_date.setText(timeStamp0);
        tv_end_date.setText(timeStamp0);

        mEdit_event_description = (EditText) findViewById(R.id.mEdit_event_description);

        bn_begin_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(Problem_item_fill_in.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //String format = "您設定的日期為:"+ setDateFormat(year,month,day);
                        tv_begin_date.setText(String.valueOf(year) + "/" +String.valueOf(month+1) + "/" + String.valueOf(day));
                    }

                }, mYear,mMonth, mDay).show();            }

        });

        bn_end_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(Problem_item_fill_in.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //String format = "您設定的日期為:"+ setDateFormat(year,month,day);
                        tv_end_date.setText(String.valueOf(year) + "/" +String.valueOf(month+1) + "/" + String.valueOf(day));
                    }

                }, mYear,mMonth, mDay).show();            }

        });


        bn_back = (Button) findViewById(R.id.bn_back);
        bn_back.setOnClickListener(new ButtonClickListener());
        bn_next = (Button) findViewById(R.id.bn_next);
        bn_next.setOnClickListener(new ButtonClickListener());

        bn_check_history = (Button) findViewById(R.id.bn_check_history);
        bn_check_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Problem_item_fill_in.this);
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
                        intent.setClass(Problem_item_fill_in.this  , Check_history.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        // button next page
        bn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GlobalVariable gv = (GlobalVariable) getApplicationContext();
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
                    detailed_begin_time = timeStamp + " 上午"+ mEdit_begin_hr.getText().toString().trim()
                            + ":" + mEdit_begin_min.getText().toString().trim();
                    //final_record += detailed_begin_time;
                }
                else if (begin_time==1){ //afternoon
                    //String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                    String timeStamp = tv_begin_date.getText().toString();
                    detailed_begin_time = timeStamp + " 下午"+ mEdit_begin_hr.getText().toString().trim()
                            + ":" + mEdit_begin_min.getText().toString().trim();
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
                    detailed_end_time = timeStamp + " 上午"+ mEdit_end_hr.getText().toString().trim()
                            + ":" + mEdit_end_min.getText().toString().trim();
                    //final_record += detailed_end_time;
                }
                else if (end_time==1){ //afternoon
                    //String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                    String timeStamp = tv_end_date.getText().toString();
                    detailed_end_time += timeStamp + " 下午"+ mEdit_end_hr.getText().toString().trim()
                            + ":" + mEdit_end_min.getText().toString().trim();
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
                    error_msg = "請您填答嚴重度，\n再按下一頁";
                }
                else if (distress==-1){
                    error_msg = "請您填答困擾程度，\n再按下一頁";
                }
                else if(begin_time==-1 || end_time == -1){
                    error_msg = "請您填答事件發生時間，\n再按下一頁";
                }
                else if((begin_time==0 || begin_time==1)&&
                        (TextUtils.isEmpty(mEdit_begin_hr.getText()) || TextUtils.isEmpty(mEdit_begin_min.getText()))){
                        //if(TextUtils.isEmpty(mEdit_begin_hr.getText()) || TextUtils.isEmpty(mEdit_begin_min.getText())){
                        //開始時間填上/下午，但漏填時或分
                    error_msg = "請您填答事件發生時間，\n再按下一頁";

                }
                else if((end_time==0 || end_time==1)&&
                        (TextUtils.isEmpty(mEdit_end_hr.getText()) || TextUtils.isEmpty(mEdit_end_min.getText()))){
                        //if(TextUtils.isEmpty(mEdit_end_hr.getText()) || TextUtils.isEmpty(mEdit_end_min.getText())){
                        //結束時間填上/下午，但漏填時或分
                    error_msg = "請您填答事件發生時間，\n再按下一頁";

                }
                else {
                    int begin_hr = 0, end_hr = 0, begin_min = 0, end_min = 0;

                    try { begin_hr = Integer.parseInt(mEdit_begin_hr.getText().toString().trim()); }
                    catch (NumberFormatException e) { begin_hr=100; } // if parse error->set to error hr

                    try { end_hr = Integer.parseInt(mEdit_end_hr.getText().toString().trim()); }
                    catch (NumberFormatException e) { end_hr=100; }

                    try { begin_min = Integer.parseInt(mEdit_begin_min.getText().toString().trim()); }
                    catch (NumberFormatException e) { begin_min=100; }

                    try { end_min = Integer.parseInt(mEdit_end_min.getText().toString().trim()); }
                    catch (NumberFormatException e) { end_min=100; }

                    //
                    if (begin_time == 0 || begin_time == 1){    //填上下午，但是時間填錯
                        if (begin_hr > 12 || begin_hr < 0) {
                            error_msg = "您輸入小時數字需在0-12之間";
                        }
                        else if(begin_min > 59 || begin_min < 0){   //同上
                            error_msg = "您輸入分鐘數字需在0-59之間";
                        }
                    }
                    if (end_time == 0 || end_time == 1){    //填上下午，但是時間填錯
                        if (end_hr > 12 || end_hr < 0) {
                            error_msg = "您輸入小時數字需在0-12之間";
                        }
                        else if(end_min > 59 || end_min < 0){   //同上
                            error_msg = "您輸入分鐘數字需在0-59之間";
                        }
                    }
                    if (error_msg == ""){ //前面還沒有error，最後檢查開始結束時間先後的問題
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ahh:mm");
                        Date formatDate_begin, formatDate_end;
                        try {
                            formatDate_begin = sdf.parse(detailed_begin_time);
                            formatDate_end = sdf.parse(detailed_end_time);
                            if (formatDate_begin.getTime() > formatDate_end.getTime()) {
 //<=這裡改*************************************
 //<=這裡改*************************************
                                //開始時間晚於結束時間87
                                error_msg = "輸入的結束時間應晚於\n開始時間，完成後再按下一頁";
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                int has_error = 0;
                if (!error_msg.equals("")){ //has error, popup error msg
                    final Dialog dialog = new Dialog(Problem_item_fill_in.this);
                    dialog.setContentView(R.layout.popup_filling_error);
                    TextView tv_error_message = (TextView) dialog.findViewById(R.id.tv_error_message);
                    tv_error_message.setText(error_msg);

                    dialog.show();
                    has_error = 1;

                }
                if (has_error == 0) { // no error, can goto next page

                    GlobalVariable gv = (GlobalVariable) getApplicationContext();
                    final_record = "";
                    final_record = final_record + severity + "___" + distress + "___";

                    final_record += detailed_begin_time;
                    final_record += "___";
                    final_record += detailed_end_time;
                    final_record += "___" + mEdit_event_description.getText().toString().trim();

                    String[] tmp_symptom_records = gv.get_symptom_records();
                    tmp_symptom_records[gv.get_now_problem_page()] = final_record;
                    gv.set_symptom_records(tmp_symptom_records);


                    //有跌倒
                    if (symptom_array[12] == 1) {
                        int next_is_final = 1;
                        for (int i = gv.get_now_problem_page() + 1; i < 12; i++) {
                            if (symptom_array[i] == 1) {   //還有其他symptom頁面
                                next_is_final = 0;
                                //gv.set_prev_problem_page(gv.get_now_problem_page());    //prevent back on phone
                                gv.set_now_problem_page(i);
                                Intent intent = new Intent();
                                intent.setClass(Problem_item_fill_in.this, Problem_item_fill_in.class);
                                startActivity(intent);
                                break;
                            }
                        }
                        //下一頁就是跌倒了
                        if (next_is_final == 1) {
                            //gv.set_prev_problem_page(gv.get_now_problem_page());
                            gv.set_now_problem_page(12);
                            Intent intent = new Intent();
                            intent.setClass(Problem_item_fill_in.this, Problem_item_fall_down.class);
                            startActivity(intent);
                        }
                    } else {
                        int next_is_final = 1;
                        //這不是最後一頁
                        for (int i = gv.get_now_problem_page() + 1; i < 12; i++) {
                            if (symptom_array[i] == 1) {
                                next_is_final = 0;
                                //gv.set_prev_problem_page(gv.get_now_problem_page());
                                gv.set_now_problem_page(i);
                                Intent intent = new Intent();
                                intent.setClass(Problem_item_fill_in.this, Problem_item_fill_in.class);
                                startActivity(intent);
                                break;
                            }
                        }
                        //跳去記錄總攬
                        if (next_is_final == 1) {
                            //gv.set_now_problem_page(12);
                            //gv.set_prev_problem_page(gv.get_now_problem_page());
                            Intent intent = new Intent();
                            intent.setClass(Problem_item_fill_in.this, Record_overview.class);
                            startActivity(intent);
                        }

                    }
                }

                /*
                final Dialog dialog = new Dialog(Problem_item_fill_in.this);
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
                        intent.setClass(Problem_item_fill_in.this  , Check_history.class);
                        startActivity(intent);
                    }
                });
                dialog.show();*/
            }
        });

    }


    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if(isChecked)//等於 buttonView.isChecked()
                    {
                        for (int i = 0; i < 3; i++){    //嚴重性3選1
                            if(checkBoxes_severity[i].getText().toString().equals(buttonView.getText().toString())){
                                checkBoxes_severity[i].setChecked(true);
                                severity = i+1;
                            }
                            else{
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
                    if(isChecked)//等於 buttonView.isChecked()
                    {
                        for (int i = 0; i < 6; i++){    //困擾度6選1
                            if(checkBoxes_distress[i].getText().toString().equals(buttonView.getText().toString())){
                                checkBoxes_distress[i].setChecked(true);
                                distress = i;
                            }
                            else{
                                checkBoxes_distress[i].setChecked(false);
                            }
                        }
                    }
                    else{
                        //buttonView.setChecked(false);
                        //distress =-1;
                    }
                }
            };
    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange_3 =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if(isChecked)//等於 buttonView.isChecked()
                    {

                        for (int i = 0; i < 3; i++){    //開始時間3選1
                            if(checkBoxes_begin_time[i].getText().toString().equals(buttonView.getText().toString())){
                                checkBoxes_begin_time[i].setChecked(true);
                                begin_time = i;
                            }
                            else{
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
                    if(isChecked)//等於 buttonView.isChecked()
                    {

                        for (int i = 0; i < 3; i++){    //結束時間3選1
                            if(checkBoxes_end_time[i].getText().toString().equals(buttonView.getText().toString())){
                                checkBoxes_end_time[i].setChecked(true);
                                end_time = i;
                            }
                            else{
                                checkBoxes_end_time[i].setChecked(false);

                            }
                        }
                    }
                    else{
                        //buttonView.setChecked(false);
                        //end_time = -1;
                    }
                }
            };


    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bn_back:
                    back_page();
                    break;

                //case R.id.bn_next:
                //    next_page();
                //    break;
                default:
                    break;
            }
        }
    }
    private void back_page(){new back_page().start();}
    //private void next_page(){new next_page().start();}
    class back_page extends Thread{
        public back_page(){
        }
        @Override
        public void run(){
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            int back_is_none=1;
            //往前看有沒有頁數
            for (int i = gv.get_now_problem_page()-1; i >=0; i--){
                if (symptom_array[i]==1){
                    back_is_none = 0;
                    gv.set_prev_problem_page(gv.get_now_problem_page());
                    gv.set_now_problem_page(i);
                    Intent intent;
                    intent = new Intent();
                    intent.setClass(Problem_item_fill_in.this  , Problem_item_fill_in.class);
                    startActivity(intent);
                    break;
                }
            }
            //跳回選symptom階段
            if (back_is_none==1){
                gv.set_prev_problem_page(gv.get_now_problem_page());
                Intent intent;
                intent = new Intent();
                intent.setClass(Problem_item_fill_in.this  , Symptom_choose.class);
                startActivity(intent);
            }



            //finish();
        }
    }
    /*
    class next_page extends Thread{
        public next_page(){
        }
        @Override
        public void run(){
            GlobalVariable gv = (GlobalVariable) getApplicationContext();
            final_record = "";
            final_record = final_record + severity + "___" + distress + "___";
            if (begin_time==2){ // now
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm:ss").format(Calendar.getInstance().getTime());
                final_record += timeStamp;
            }
            else if (begin_time==0){ //morning
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                final_record += timeStamp + "上午"+ mEdit_begin_hr.getText().toString().trim()
                        + ":" + mEdit_begin_min.getText().toString().trim();
            }
            else if (begin_time==1){ //afternoon
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                final_record += timeStamp + "下午"+ mEdit_begin_hr.getText().toString().trim()
                        + ":" + mEdit_begin_min.getText().toString().trim();
            }
            final_record += "___";
            if (end_time==2){ // now
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ahh:mm").format(Calendar.getInstance().getTime());
                final_record += timeStamp;
            }
            else if (end_time==0){ //morning
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                final_record += timeStamp + "上午"+ mEdit_end_hr.getText().toString().trim()
                        + ":" + mEdit_end_min.getText().toString().trim();
            }
            else if (end_time==1){ //afternoon
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                final_record += timeStamp + "下午"+ mEdit_end_hr.getText().toString().trim()
                        + ":" + mEdit_end_min.getText().toString().trim();
            }
            final_record += "___" + mEdit_event_description.getText().toString().trim();
            String[] tmp_symptom_records = gv.get_symptom_records();
            tmp_symptom_records[gv.get_now_problem_page()] = final_record;
            gv.set_symptom_records(tmp_symptom_records);
            //處理漏填或其他錯誤
            if (severity==-1){

            }

            //有跌倒
            if(symptom_array[12]==1){
                int next_is_final=1;
                for(int i = gv.get_now_problem_page()+1; i<12; i++){
                    if (symptom_array[i]==1){   //還有其他symptom頁面
                        next_is_final = 0;
                        //gv.set_prev_problem_page(gv.get_now_problem_page());    //prevent back on phone
                        gv.set_now_problem_page(i);
                        Intent intent = new Intent();
                        intent.setClass(Problem_item_fill_in.this  , Problem_item_fill_in.class);
                        startActivity(intent);
                        break;
                    }
                }
                //下一頁就是跌倒了
                if (next_is_final==1){
                    //gv.set_prev_problem_page(gv.get_now_problem_page());
                    gv.set_now_problem_page(12);
                    Intent intent = new Intent();
                    intent.setClass(Problem_item_fill_in.this  , Problem_item_fall_down.class);
                    startActivity(intent);
                }
            }
            else{
                int next_is_final=1;
                //這不是最後一頁
                for(int i = gv.get_now_problem_page()+1; i<12; i++){
                    if (symptom_array[i]==1){
                        next_is_final = 0;
                        //gv.set_prev_problem_page(gv.get_now_problem_page());
                        gv.set_now_problem_page(i);
                        Intent intent = new Intent();
                        intent.setClass(Problem_item_fill_in.this  , Problem_item_fill_in.class);
                        startActivity(intent);
                        break;
                    }
                }
                //跳去記錄總攬
                if (next_is_final==1) {
                    //gv.set_now_problem_page(12);
                    //gv.set_prev_problem_page(gv.get_now_problem_page());
                    Intent intent = new Intent();
                    intent.setClass(Problem_item_fill_in.this, Record_overview.class);
                    startActivity(intent);
                }

            }
            //others
            //finish();
        }
    }*/
    //@Override
    //public void onBackPressed() {
    // 這裡處理邏輯程式碼，大家注意：該方法僅適用於2.0或更新版的sdk
    //    return;
    //}
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false; //返回鍵失效
        }
        return super.onKeyDown(keyCode, event);
    }
}