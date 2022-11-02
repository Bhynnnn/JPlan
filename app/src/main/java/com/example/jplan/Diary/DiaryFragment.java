package com.example.jplan.Diary;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Diary;
import com.example.jplan.Model.mCalendar;
import com.example.jplan.Plan.PlanAdapter;
import com.example.jplan.R;
import com.example.jplan.Today.CalendarAdapter;
import com.example.jplan.Today.TodayFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment {

    Button diary_add_btn;
    static TextView today_tv;
    static TextView icon_tv;
    static TextView diary_content_tv;
    static ImageView diary_pic;
    String str_year, str_month, str_day, str_date;

    Date toTimeStamp = new Date();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);

    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    String day = dayFormat.format(toTimeStamp);

    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    String chkDate = SDF.format(calendar.getTime());

    private RecyclerView mRecyclerView_calendar;
    private DiaryAdapter mAdapter_calendar;
    private RecyclerView.LayoutManager mLayoutManager_calendar;
    private ArrayList<mCalendar> mCalendarData;

    static Context context;
    static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public DiaryFragment() {
        // Required empty public constructor
    }

    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDiary(timestamp_date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
//        yesterday_btn = view.findViewById(R.id.yesterday_btn);
//        tomorrow_btn = view.findViewById(R.id.tomorrow_btn);
        diary_pic = view.findViewById(R.id.diary_pic);
        diary_content_tv = view.findViewById(R.id.diary_content_tv);
        today_tv = view.findViewById(R.id.today_tv);
        icon_tv = view.findViewById(R.id.icon_tv);
        diary_add_btn = view.findViewById(R.id.diary_add_btn);
        getCalendarData();
        mRecyclerView_calendar = (RecyclerView) view.findViewById(R.id.recyclerView_calendar);
        mLayoutManager_calendar = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mRecyclerView_calendar.setLayoutManager(mLayoutManager_calendar);
        mAdapter_calendar = new DiaryAdapter(mCalendarData, DiaryFragment.this);
        mRecyclerView_calendar.setAdapter(mAdapter_calendar);
        mRecyclerView_calendar.setItemAnimator(new DefaultItemAnimator());
        showDiary(timestamp_date);

        context = getContext();

        today_tv.setText(timestamp_date);

        System.out.println("timestamp date " + timestamp_date);

        System.out.println("date format " + day);

        diary_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DiaryAddActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public String getDay(int date) {
        String day = "";
        switch (date) {
            case 1:
                day = "SUN";
                break;
            case 2:
                day = "MON";
                break;
            case 3:
                day = "TUE";
                break;
            case 4:
                day = "WED";
                break;
            case 5:
                day = "THU";
                break;
            case 6:
                day = "FRI";
                break;
            case 7:
                day = "SAT";
                break;

        }
        return day;
    }

    private void getCalendarData() {
        mCalendarData = new ArrayList<>();

        //오늘 날짜
        System.out.println("today date " + chkDate);

        // 3일전
        calendar.add(Calendar.DATE, -3);
        chkDate = SDF.format(calendar.getTime());

        System.out.println("The day before " + 1 + "일 " + chkDate);

        String[] splitText;

        splitText = chkDate.split("-");

        for (int j = 0; j < splitText.length; j++) {
            System.out.println(" text = " + splitText[j]);
        }
        str_year = splitText[0];
        str_month = splitText[1];
        str_date = splitText[2];
        System.out.println("day of week " + calendar.get(Calendar.DAY_OF_WEEK));
        str_day = getDay(calendar.get(Calendar.DAY_OF_WEEK));
        mCalendarData.add(new mCalendar(str_year, str_month, str_date, str_day));

        for (int i = 0; i < 6; i++) {
            // 3일전
            calendar.add(Calendar.DATE, 1);
            chkDate = SDF.format(calendar.getTime());

            System.out.println("The day before " + i + "일 " + chkDate);

            splitText = chkDate.split("-");

            for (int j = 0; j < splitText.length; j++) {
                System.out.println(" text = " + splitText[j]);
            }
            str_year = splitText[0];
            str_month = splitText[1];
            str_date = splitText[2];
            System.out.println("the day of week " + calendar.get(Calendar.DAY_OF_WEEK));
            str_day = getDay(calendar.get(Calendar.DAY_OF_WEEK));
            mCalendarData.add(new mCalendar(str_year, str_month, str_date, str_day));
        }
    }

    public static void showDiary(String date) {

        firebaseFirestore.collection("User")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Diary")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Glide.with(context)
                                    .load(R.drawable.jplan_logo)
                                    .into(diary_pic);
                            today_tv.setText(date);
                            icon_tv.setText("작성된 일기가 없습니다😅");
                            diary_content_tv.setText("일기를 새로 작성해주세요");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(date)) {

                                    System.out.println("document getdata " + document.getData());
                                    System.out.println("document getid " + document.getId());
                                    System.out.println("document getdata icon " + document.get("imgUrl").toString());
                                    today_tv.setText(date);
                                    Glide.with(context)
                                            .load(document.get("imgUrl").toString())
                                            .into(diary_pic);
                                    icon_tv.setText(document.get("icon").toString());
                                    diary_content_tv.setText(document.get("diaryContent").toString());

                                }

                            }
                        }
                    {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
    });
}

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
        today_tv.setText(timestamp_date);
        showDiary(timestamp_date);

    }
}