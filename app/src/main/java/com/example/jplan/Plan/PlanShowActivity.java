package com.example.jplan.Plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jplan.R;

import org.w3c.dom.Text;

public class PlanShowActivity extends AppCompatActivity {

    TextView title_plan_tv, day_plan_tv, memo_plan_tv;
    View circle_iv;
    CheckBox todo_check;

    String title_str, day_str, memo_str, icon_str, check_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_show);

        title_plan_tv = findViewById(R.id.title_plan_tv);
        day_plan_tv = findViewById(R.id.day_plan_tv);
        memo_plan_tv = findViewById(R.id.memo_plan_tv);
        circle_iv = findViewById(R.id.item_img_plan);
        todo_check = findViewById(R.id.todo_check);

        Intent intent = getIntent();
        title_str = intent.getStringExtra("title_Plan");
        day_str = intent.getStringExtra("total_Plan");
        memo_str = intent.getStringExtra("memo_Plan");
        icon_str = intent.getStringExtra("icon_Plan");
        check_bool = intent.getStringExtra("check_Plan");
        System.out.println("test check bool " + check_bool);
        title_plan_tv.setText(title_str);
        day_plan_tv.setText(day_str + "일");
        memo_plan_tv.setText(memo_str);
        if (check_bool == "false") {
            // 체크 아님
            System.out.println("test check false 1 ");
            todo_check.setChecked(true);
        } else {
            System.out.println("test check true 1 ");
            todo_check.setChecked(false);
        }

        if (icon_str.equals("walk")) {
            circle_iv.setBackgroundResource(R.drawable.walk_ic);
        } else if (icon_str.equals("bus")) {
            circle_iv.setBackgroundResource(R.drawable.bus_ic);

        } else if (icon_str.equals("airplane")) {
            circle_iv.setBackgroundResource(R.drawable.airplane_ic);

        } else if (icon_str.equals("android")) {
            circle_iv.setBackgroundResource(R.drawable.android_ic);

        } else if (icon_str.equals("health")) {
            circle_iv.setBackgroundResource(R.drawable.health_ic);

        } else if (icon_str.equals("code")) {
            circle_iv.setBackgroundResource(R.drawable.code_ic);

        } else if (icon_str.equals("money")) {
            circle_iv.setBackgroundResource(R.drawable.money_ic);

        } else if (icon_str.equals("pet")) {
            circle_iv.setBackgroundResource(R.drawable.pet_ic);

        } else if (icon_str.equals("score")) {
            circle_iv.setBackgroundResource(R.drawable.score_ic);

        } else if (icon_str.equals("smile")) {
            circle_iv.setBackgroundResource(R.drawable.smile_ic);

        } else if (icon_str.equals("star")) {
            circle_iv.setBackgroundResource(R.drawable.star_ic);

        } else if (icon_str.equals("tea")) {
            circle_iv.setBackgroundResource(R.drawable.tea_ic);

        } else if (icon_str.equals("thumbsup")) {
            circle_iv.setBackgroundResource(R.drawable.thumbsup_ic);

        } else if (icon_str.equals("water")) {
            circle_iv.setBackgroundResource(R.drawable.water_ic);

        } else if (icon_str.equals("heart")) {
            circle_iv.setBackgroundResource(R.drawable.heart_ic);

        }


    }
}