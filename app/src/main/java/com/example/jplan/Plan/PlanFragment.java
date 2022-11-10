package com.example.jplan.Plan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jplan.Model.Constants;
import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Plan;
import com.example.jplan.R;
import com.example.jplan.Today.TodayAdapter;
import com.example.jplan.Today.TodayFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PlanFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PlanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Plan> mPlanData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    Dialog dialog;
    View item_img_plan;
    String str_title_edt, str_memo_edt, getStr_icon;
    String str_title, str_memo, str_icon;
    int  total_Plan_int, count_Plan_int;
    boolean check_Plan_bool;
    int adapterCount;
    Button plan_add_btn;
    TextView today_tv;
    Context context;

    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);

    Date toMonth = new Date();
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    String date_month = monthFormat.format(toMonth);


    public PlanFragment() {
        // Required empty public constructor
    }

    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("fragment test oncreate");
//        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        getActivity().setTitle("Plan");
        System.out.println("data size print first" + mPlanData.size()); //0
        initDataset();
        // 느려.
        System.out.println("data size print after init " + mPlanData.size());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        mLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.scrollToPosition(0);
        System.out.println("fragment test oncreateview");
        mAdapter = new PlanAdapter(mPlanData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPlanData.clear();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                System.out.println("data size print refresh 0 " + mPlanData.size());
//
////                initDataset();
//                System.out.println("data size print refresh after init " + mPlanData.size());
//
////                mAdapter.notifyDataSetChanged();
//
//                mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//                mRecyclerView.setLayoutManager(gridLayoutManager);
//                mRecyclerView.scrollToPosition(0);
//                System.out.println("fragment test oncreateview");
//                mAdapter = new PlanAdapter(mPlanData);
//                mRecyclerView.setAdapter(mAdapter);
//                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//
//                ft.detach(PlanFragment.this).attach(PlanFragment.this).commit();
//
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        context = getContext();
        today_tv = view.findViewById(R.id.today_tv);

        today_tv.setText(timestamp_date);

        plan_add_btn = view.findViewById(R.id.plan_add_btn);

        plan_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void showBottomSheetDialog(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.activity_plan_add);

        EditText title_plan_edt = bottomSheetDialog.findViewById(R.id.title_plan_edt);
        EditText memo_plan_edt = bottomSheetDialog.findViewById(R.id.memo_plan_edt);
        RadioGroup rdoG = bottomSheetDialog.findViewById(R.id.rdoG);
//        RadioButton rdo_week,rdo_month;
        CheckBox check_Plan = bottomSheetDialog.findViewById(R.id.todo_check);
        Button plan_add_btn = bottomSheetDialog.findViewById(R.id.plan_add_btn);
        item_img_plan = bottomSheetDialog.findViewById(R.id.item_img_plan);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.icon_dialog);
        bottomSheetDialog.show();

        Button icon_1 = dialog.findViewById(R.id.icon_1);
        Button icon_2 = dialog.findViewById(R.id.icon_2);
        Button icon_3 = dialog.findViewById(R.id.icon_3);
        Button icon_4 = dialog.findViewById(R.id.icon_4);
        Button icon_5 = dialog.findViewById(R.id.icon_5);
        Button icon_6 = dialog.findViewById(R.id.icon_6);
        Button icon_7 = dialog.findViewById(R.id.icon_7);
        Button icon_8 = dialog.findViewById(R.id.icon_8);
        Button icon_9 = dialog.findViewById(R.id.icon_9);
        Button icon_10 = dialog.findViewById(R.id.icon_10);
        Button icon_11 = dialog.findViewById(R.id.icon_11);
        Button icon_12 = dialog.findViewById(R.id.icon_12);
        Button icon_13 = dialog.findViewById(R.id.icon_13);
        Button icon_14 = dialog.findViewById(R.id.icon_14);
        Button icon_15 = dialog.findViewById(R.id.icon_15);
        Button icon_16 = dialog.findViewById(R.id.icon_16);
        Button icon_17 = dialog.findViewById(R.id.icon_17);
        Button icon_18 = dialog.findViewById(R.id.icon_18);

        BtnClick btnOnClick = new BtnClick();

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


        item_img_plan.setBackgroundResource(R.drawable.add_small);
        item_img_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "pick icon for thumbnail", Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });

        rdoG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdo_week) {
                    total_Plan_int = 7;
                    System.out.println("test week " + total_Plan_int);
                    Toast.makeText(getContext(), "week selected " + total_Plan_int, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "week selected " + total_Plan_int, Toast.LENGTH_SHORT).show();
                    if (date_month == "01" || date_month == "03" || date_month == "05" || date_month == "07" || date_month == "08" || date_month == "10" || date_month == "12") {
                        total_Plan_int = 31;
                    } else if (date_month == "02") {
                        total_Plan_int = 28;
                    } else {
                        total_Plan_int = 30;
                    }
                    System.out.println("test month " + total_Plan_int);

                }

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
                    Toast.makeText(getContext(), "빈칸이 존재합니다.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "PLAN 추가완료", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                                mAdapter.notifyDataSetChanged();
//                                adapterCount = mAdapter.getItemCount();
//                                mAdapter.notifyItemInserted(adapterCount);
//                                System.out.println("real last adapter count " + mAdapter.getItemCount());
                            }
                            else{
                                Toast.makeText(getContext(), "PLAN 추가실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        mAdapter.notifyItemInserted(adapterCount);
    }




    private void initDataset() {
        System.out.println("data size print init 0 " + mPlanData.size());   // 0

        db.collection("User").document(auth.getCurrentUser().getUid()).collection("Plan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot item : queryDocumentSnapshots) {
                    str_title = item.get(Constants.TITLE_PLAN).toString();
                    str_memo = item.get(Constants.MEMO_PLAN).toString();
                    str_icon = item.get(Constants.ICON_PLAN).toString();
                    total_Plan_int = Integer.parseInt(item.get(Constants.TOTAL_PLAN).toString());
                    count_Plan_int = Integer.parseInt(item.get(Constants.COUNT_PLAN).toString());
                    check_Plan_bool = Boolean.parseBoolean(item.get(Constants.CHECK_PLAN).toString());
                    mPlanData.add(new Plan(str_title, str_memo, str_icon, total_Plan_int, count_Plan_int, check_Plan_bool));
                }
                System.out.println("data size print " + mPlanData.size());
            }

        });
        System.out.println("data size print init last " + mPlanData.size());


    }

    private class BtnClick implements View.OnClickListener {
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

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();

    }

}