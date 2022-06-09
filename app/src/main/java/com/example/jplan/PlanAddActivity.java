package com.example.jplan;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlanAddActivity extends AppCompatActivity {
    View item_img_plan;
    EditText title_plan_edt, detail_plan_edt, memo_plan_edt;
    CheckBox check_Plan;
    DatePickerDialog datePickerDialog;
    Button plan_add_btn;
    Button btn_tea, btn_android, btn_walk, btn_bus, btn_money, btn_star, btn_heart, btn_health, btn_smile, btn_airplane, btn_pet, btn_thumbsUp, btn_water, btn_code, btn_score;
    String str_title_edt, str_memo_edt, str_icon;
    int  total_Plan_int, count_Plan_int;
    boolean check_Plan_bool;
    Dialog dialog;
    RadioGroup rdoG;
    RadioButton rdo_week, rdo_month;
    Context context = this;
    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM", Locale.getDefault());
    String date_month = dateFormat.format(toTimeStamp);
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);
        setTitle("계획 추가하기");
        dialog = new Dialog(PlanAddActivity.this);
        dialog.setContentView(R.layout.icon_dialog);

        init();
        listner();

//        Glide.with(PlanAddActivity.this).load(R.drawable.ic_floating_add).into((ImageView) item_img_plan);

        item_img_plan.setBackgroundResource(R.drawable.add_small);

        item_img_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 대표 icon 다이얼로그 띄우던지 icon 여러개 중 고를 수 있도록
                Toast.makeText(PlanAddActivity.this, "pick icon for thumbnail", Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });

        rdoG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdo_week) {
                    total_Plan_int = 7;
                    System.out.println("test week " + total_Plan_int);
                    Toast.makeText(PlanAddActivity.this, "week selected " + total_Plan_int, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlanAddActivity.this, "week selected " + total_Plan_int, Toast.LENGTH_SHORT).show();
                    if (date_month == "01" || date_month == "03" || date_month == "05" || date_month == "07" || date_month == "08" || date_month == "10" || date_month == "12") {
                        total_Plan_int = 31;
                    } else if (date_month == "02") {
                        total_Plan_int = 28;
                    } else {
                        total_Plan_int = 30;
                    }
                    System.out.println("test month " + total_Plan_int);

                }
//                            switch (checkedId) {
//                                case R.id.rdo_week:
//                                    total_Plan_int = 7;
//                                    Toast.makeText(PlanAddActivity.this, "week selected "+ total_Plan_int, Toast.LENGTH_SHORT).show();
//                                    break;
//                                case R.id.rdo_month:
//                                    //1, 3, 5, 7, 8, 10, 12 -> 31
//                                    //2, 4, 6, 9, 11
//                                    if (date_month == "01" || date_month == "03" || date_month == "05" || date_month == "07" || date_month == "08" || date_month == "10" || date_month == "12") {
//                                        total_Plan_int = 31;
//                                    } else if (date_month == "02") {
//                                        total_Plan_int = 28;
//                                    } else {
//                                        total_Plan_int = 30;
//                                    }
//                                    break;
//                            }
                System.out.println("test total " + total_Plan_int);

            }
        });
        plan_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // plan 추가 버튼
                str_memo_edt = memo_plan_edt.getText().toString();
                str_title_edt = title_plan_edt.getText().toString();

                // detail 을 일주일, 한달 기준으로 cut
                // 현재 날짜 기준 ~ 월 마지막 날 까지 / 7일

                if (str_title_edt == "") {
                    // 빈칸 있으면 저장 안되게
                    Toast.makeText(PlanAddActivity.this, "빈칸이 존재합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if(check_Plan.isChecked()){
                        check_Plan_bool = true;
                    }
                    else{
                        check_Plan_bool = false;
                    }
                    Plan plan = new Plan();
                    plan.setTitle_Plan(str_title_edt);
                    plan.setMemo_Plan(str_memo_edt);
                    plan.setTotal_Plan(total_Plan_int);
                    plan.setCount_Plan(0);
                    plan.setIcon_Plan(str_icon);
                    plan.setCheck_Plan(check_Plan_bool);
                    System.out.println("test total " + total_Plan_int);

                    db.collection("User").document(auth.getCurrentUser().getUid()).collection("Plan").add(plan).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(PlanAddActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
//                    }
                    onRestart();
                }

            }
        });
    }

    class BtnOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_walk:
                    item_img_plan.setBackgroundResource(R.drawable.walk_ic);
                    str_icon = "walk";
                    break;
                case R.id.btn_tea:
                    item_img_plan.setBackgroundResource(R.drawable.tea_ic);
                    str_icon = "tea";
                    break;
                case R.id.btn_android:
                    item_img_plan.setBackgroundResource(R.drawable.android_ic);
                    str_icon = "android";
                    break;
                case R.id.btn_thumbsUp:
                    str_icon = "thumbsup";
                    item_img_plan.setBackgroundResource(R.drawable.thumbsup_ic);
                    break;
                case R.id.btn_bus:
                    item_img_plan.setBackgroundResource(R.drawable.bus_ic);

                    str_icon = "bus";
                    break;
                case R.id.btn_health:
                    item_img_plan.setBackgroundResource(R.drawable.health_ic);
                    str_icon = "health";
                    break;
                case R.id.btn_heart:
                    item_img_plan.setBackgroundResource(R.drawable.heart_ic);
                    str_icon = "heart";
                    break;
                case R.id.btn_smile:
                    item_img_plan.setBackgroundResource(R.drawable.smile_ic);
                    str_icon = "smile";
                    break;
                case R.id.btn_money:
                    item_img_plan.setBackgroundResource(R.drawable.money_ic);
                    str_icon = "money";
                    break;
                case R.id.btn_pet:
                    item_img_plan.setBackgroundResource(R.drawable.pet_ic);
                    str_icon = "pet";
                    break;
                case R.id.btn_code:
                    item_img_plan.setBackgroundResource(R.drawable.code_ic);
                    str_icon = "code";
                    break;
                case R.id.btn_star:
                    item_img_plan.setBackgroundResource(R.drawable.star_ic);
                    str_icon = "star";
                    break;
                case R.id.btn_score:
                    item_img_plan.setBackgroundResource(R.drawable.score_ic);
                    str_icon = "score";
                    break;
                case R.id.btn_water:
                    item_img_plan.setBackgroundResource(R.drawable.water_ic);
                    str_icon = "water";
                    break;
                case R.id.btn_airplane:
                    item_img_plan.setBackgroundResource(R.drawable.airplane_ic);
                    str_icon = "airplane";
                    break;
                default:
                    break;
            }

            dialog.dismiss();

        }
    }

    public void init() {
        title_plan_edt = findViewById(R.id.title_plan_edt);
        memo_plan_edt = findViewById(R.id.memo_plan_edt);
        plan_add_btn = findViewById(R.id.plan_add_btn);
        item_img_plan = findViewById(R.id.item_img_plan);

        rdoG = findViewById(R.id.rdoG);
        rdo_week = findViewById(R.id.rdo_week);
        rdo_month = findViewById(R.id.rdo_month);

        check_Plan = findViewById(R.id.todo_check);

        btn_walk = dialog.findViewById(R.id.btn_walk);
        btn_tea = dialog.findViewById(R.id.btn_tea);
        btn_android = dialog.findViewById(R.id.btn_android);
        btn_thumbsUp = dialog.findViewById(R.id.btn_thumbsUp);
        btn_bus = dialog.findViewById(R.id.btn_bus);
        btn_health = dialog.findViewById(R.id.btn_health);
        btn_heart = dialog.findViewById(R.id.btn_heart);
        btn_smile = dialog.findViewById(R.id.btn_smile);
        btn_money = dialog.findViewById(R.id.btn_money);
        btn_pet = dialog.findViewById(R.id.btn_pet);
        btn_code = dialog.findViewById(R.id.btn_code);
        btn_star = dialog.findViewById(R.id.btn_star);
        btn_score = dialog.findViewById(R.id.btn_score);
        btn_water = dialog.findViewById(R.id.btn_water);
        btn_airplane = dialog.findViewById(R.id.btn_airplane);
    }

    public void listner() {
        BtnOnClick btnOnClick = new BtnOnClick();

        btn_walk.setOnClickListener(btnOnClick);
        btn_tea.setOnClickListener(btnOnClick);
        btn_android.setOnClickListener(btnOnClick);
        btn_thumbsUp.setOnClickListener(btnOnClick);
        btn_bus.setOnClickListener(btnOnClick);
        btn_heart.setOnClickListener(btnOnClick);
        btn_health.setOnClickListener(btnOnClick);
        btn_smile.setOnClickListener(btnOnClick);
        btn_money.setOnClickListener(btnOnClick);
        btn_pet.setOnClickListener(btnOnClick);
        btn_code.setOnClickListener(btnOnClick);
        btn_star.setOnClickListener(btnOnClick);
        btn_score.setOnClickListener(btnOnClick);
        btn_water.setOnClickListener(btnOnClick);
        btn_airplane.setOnClickListener(btnOnClick);
    }
}