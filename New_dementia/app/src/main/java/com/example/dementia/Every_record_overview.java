package com.example.dementia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Every_record_overview extends AppCompatActivity {
    private TextView tv_problem_item_in_overview;
    private Button bn_modify, bn_check;
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

    private EditText mEdit_begin_hr, mEdit_begin_min, mEdit_end_hr, mEdit_end_min;
    private EditText mEdit_event_description;

    private String final_record="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_record_overview);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        symptom_array = gv.get_symptom_array();
        String tmp_symptom = "";

        tv_problem_item_in_overview = (TextView) findViewById(R.id.tv_problem_item_in_overview);
        //tv_problem_item_in_overview.setText(tmp_symptom);

    }
}