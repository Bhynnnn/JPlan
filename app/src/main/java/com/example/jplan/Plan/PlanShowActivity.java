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

        if (icon_str.equals("icon_1")) {
            circle_iv.setBackgroundResource(R.drawable.icon_1);
        } else if (icon_str.equals("icon_2")) {
            circle_iv.setBackgroundResource(R.drawable.icon_2);

        } else if (icon_str.equals("icon_3")) {
            circle_iv.setBackgroundResource(R.drawable.icon_3);

        } else if (icon_str.equals("icon_4")) {
            circle_iv.setBackgroundResource(R.drawable.icon_4);

        } else if (icon_str.equals("icon_5")) {
            circle_iv.setBackgroundResource(R.drawable.icon_5);

        } else if (icon_str.equals("icon_6")) {
            circle_iv.setBackgroundResource(R.drawable.icon_6);

        } else if (icon_str.equals("icon_7")) {
            circle_iv.setBackgroundResource(R.drawable.icon_7);

        } else if (icon_str.equals("icon_8")) {
            circle_iv.setBackgroundResource(R.drawable.icon_8);

        } else if (icon_str.equals("icon_9")) {
            circle_iv.setBackgroundResource(R.drawable.icon_9);

        } else if (icon_str.equals("icon_10")) {
            circle_iv.setBackgroundResource(R.drawable.icon_10);

        } else if (icon_str.equals("icon_11")) {
            circle_iv.setBackgroundResource(R.drawable.icon_11);

        } else if (icon_str.equals("icon_12")) {
            circle_iv.setBackgroundResource(R.drawable.icon_12);

        } else if (icon_str.equals("icon_13")) {
            circle_iv.setBackgroundResource(R.drawable.icon_13);

        } else if (icon_str.equals("icon_14")) {
            circle_iv.setBackgroundResource(R.drawable.icon_14);

        } else if (icon_str.equals("icon_15")) {
            circle_iv.setBackgroundResource(R.drawable.icon_15);
        }
        else if (icon_str.equals("icon_16")) {
            circle_iv.setBackgroundResource(R.drawable.icon_16);
        }
        else if (icon_str.equals("icon_17")) {
            circle_iv.setBackgroundResource(R.drawable.icon_17);
        }
        else if (icon_str.equals("icon_18")) {
            circle_iv.setBackgroundResource(R.drawable.icon_18);
        }
    }
}