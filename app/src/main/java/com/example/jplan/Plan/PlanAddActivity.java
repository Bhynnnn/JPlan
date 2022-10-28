package com.example.jplan.Plan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Plan;
import com.example.jplan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlanAddActivity extends AppCompatActivity {
    View item_img_plan;
    EditText title_plan_edt, detail_plan_edt, memo_plan_edt;
    CheckBox check_Plan;
    DatePickerDialog datePickerDialog;
    Button plan_add_btn;
    Button icon_1, icon_2, icon_3, icon_4, icon_5, icon_6, icon_7, icon_8, icon_9, icon_10;
    Button  icon_11, icon_12, icon_13, icon_14, icon_15, icon_16, icon_17, icon_18;
    String str_title_edt, str_memo_edt, str_icon;
    int total_Plan_int, count_Plan_int;
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
                    if (check_Plan.isChecked()) {
                        check_Plan_bool = true;
                    } else {
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
                    onRestart();
                }

            }
        });
    }

    class BtnOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon_1:
                    item_img_plan.setBackgroundResource(R.drawable.icon_1);
                    str_icon = "icon_1";
                    break;
                case R.id.icon_2:
                    item_img_plan.setBackgroundResource(R.drawable.icon_2);
                    str_icon = "icon_2";
                    break;
                case R.id.icon_3:
                    item_img_plan.setBackgroundResource(R.drawable.icon_3);
                    str_icon = "icon_3";
                    break;
                case R.id.icon_4:
                    item_img_plan.setBackgroundResource(R.drawable.icon_4);
                    str_icon = "icon_4";
                    break;
                case R.id.icon_5:
                    item_img_plan.setBackgroundResource(R.drawable.icon_5);
                    str_icon = "icon_5";
                    break;
                case R.id.icon_6:
                    item_img_plan.setBackgroundResource(R.drawable.icon_6);
                    str_icon = "icon_6";
                    break;
                case R.id.icon_7:
                    item_img_plan.setBackgroundResource(R.drawable.icon_7);
                    str_icon = "icon_7";
                    break;
                case R.id.icon_8:
                    item_img_plan.setBackgroundResource(R.drawable.icon_8);
                    str_icon = "icon_8";
                    break;
                case R.id.icon_9:
                    item_img_plan.setBackgroundResource(R.drawable.icon_9);
                    str_icon = "icon_9";
                    break;
                case R.id.icon_10:
                    item_img_plan.setBackgroundResource(R.drawable.icon_10);
                    str_icon = "icon_10";
                    break;
                case R.id.icon_11:
                    item_img_plan.setBackgroundResource(R.drawable.icon_11);
                    str_icon = "icon_11";
                    break;
                case R.id.icon_12:
                    item_img_plan.setBackgroundResource(R.drawable.icon_12);
                    str_icon = "icon_12";
                    break;
                case R.id.icon_13:
                    item_img_plan.setBackgroundResource(R.drawable.icon_13);
                    str_icon = "icon_13";
                    break;
                case R.id.icon_14:
                    item_img_plan.setBackgroundResource(R.drawable.icon_14);
                    str_icon = "icon_14";
                    break;
                case R.id.icon_15:
                    item_img_plan.setBackgroundResource(R.drawable.icon_15);
                    str_icon = "icon_15";
                    break;
                case R.id.icon_16:
                    item_img_plan.setBackgroundResource(R.drawable.icon_16);
                    str_icon = "icon_16";
                    break;
                case R.id.icon_17:
                    item_img_plan.setBackgroundResource(R.drawable.icon_17);
                    str_icon = "icon17";
                    break;
                case R.id.icon_18:
                    item_img_plan.setBackgroundResource(R.drawable.icon_18);
                    str_icon = "icon18";
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

        icon_1 = dialog.findViewById(R.id.icon_1);
        icon_2 = dialog.findViewById(R.id.icon_2);
        icon_3 = dialog.findViewById(R.id.icon_3);
        icon_4 = dialog.findViewById(R.id.icon_4);
        icon_5 = dialog.findViewById(R.id.icon_5);
        icon_6 = dialog.findViewById(R.id.icon_6);
        icon_7 = dialog.findViewById(R.id.icon_7);
        icon_8 = dialog.findViewById(R.id.icon_8);
        icon_9 = dialog.findViewById(R.id.icon_9);
        icon_10 = dialog.findViewById(R.id.icon_10);
        icon_11 = dialog.findViewById(R.id.icon_11);
        icon_12 = dialog.findViewById(R.id.icon_12);
        icon_13 = dialog.findViewById(R.id.icon_13);
        icon_14 = dialog.findViewById(R.id.icon_14);
        icon_15 = dialog.findViewById(R.id.icon_15);
        icon_16 = dialog.findViewById(R.id.icon_16);
        icon_17 = dialog.findViewById(R.id.icon_17);
        icon_18 = dialog.findViewById(R.id.icon_18);


    }

    public void listner() {
        BtnOnClick btnOnClick = new BtnOnClick();

        icon_1.setOnClickListener(btnOnClick);
        icon_2.setOnClickListener(btnOnClick);
        icon_3.setOnClickListener(btnOnClick);
        icon_4.setOnClickListener(btnOnClick);
        icon_5.setOnClickListener(btnOnClick);
        icon_6.setOnClickListener(btnOnClick);
        icon_7.setOnClickListener(btnOnClick);
        icon_8.setOnClickListener(btnOnClick);
        icon_9.setOnClickListener(btnOnClick);
        icon_10.setOnClickListener(btnOnClick);
        icon_11.setOnClickListener(btnOnClick);
        icon_12.setOnClickListener(btnOnClick);
        icon_13.setOnClickListener(btnOnClick);
        icon_14.setOnClickListener(btnOnClick);
        icon_15.setOnClickListener(btnOnClick);
        icon_16.setOnClickListener(btnOnClick);
        icon_17.setOnClickListener(btnOnClick);
        icon_18.setOnClickListener(btnOnClick);
    }


}