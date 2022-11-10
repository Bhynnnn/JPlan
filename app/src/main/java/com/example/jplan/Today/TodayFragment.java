package com.example.jplan.Today;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jplan.Model.Constants;
import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Today;
import com.example.jplan.Model.mCalendar;
import com.example.jplan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TodayFragment extends Fragment {

    private static RecyclerView mRecyclerView;
    private static TodayAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<Today> mTodayData;
    private static ArrayList<Today> mNewTodayData;


    private RecyclerView mRecyclerView_calendar;
    private CalendarAdapter mAdapter_calendar;
    private RecyclerView.LayoutManager mLayoutManager_calendar;
    private ArrayList<mCalendar> mCalendarData;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    String timestamp_year = yearFormat.format(toTimeStamp);

    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    String timestamp_month = monthFormat.format(toTimeStamp);

    String str_year, str_month, str_day, str_date;
    static String str_title;
    static String str_start;
    static String str_finish;
    static String str_memo;
    String str_todayT = "";
    String str_time;
    TextView date;
    String str_startT, str_finishT;

    Button today_add_btn;
    TextView today_tv;
    Calendar calendar = new GregorianCalendar();
    Context context;
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    String chkDate = SDF.format(calendar.getTime());
    String todayDate = chkDate;

    //    String yesterday = SDF.format(relativeYesterday.getTime());
//    String tomorrow = SDF.format(relativeTomorrow.getTime());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        context = getContext();
        today_tv = view.findViewById(R.id.today_tv);
        today_add_btn = view.findViewById(R.id.today_add_btn);

        today_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
                mAdapter.notifyDataSetChanged();
                mAdapter_calendar.notifyDataSetChanged();
            }
        });

        today_tv.setText(todayDate);
        mNewTodayData = new ArrayList<>();
        getCalendarData();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new TodayAdapter(mNewTodayData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView_calendar = (RecyclerView) view.findViewById(R.id.recyclerView_calendar);
        mLayoutManager_calendar = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mRecyclerView_calendar.setLayoutManager(mLayoutManager_calendar);
        mAdapter_calendar = new CalendarAdapter(mCalendarData, TodayFragment.this);
        mRecyclerView_calendar.setAdapter(mAdapter_calendar);
        mRecyclerView_calendar.setItemAnimator(new DefaultItemAnimator());
        mAdapter_calendar.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();

        showToday(todayDate);

        return view;
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.activity_today_add);

        EditText memo_today_edt = bottomSheetDialog.findViewById(R.id.memo_today_edt);
        EditText title_today_edt = bottomSheetDialog.findViewById(R.id.title_today_edt);
        TimePicker time_finish = bottomSheetDialog.findViewById(R.id.time_finish);
        TimePicker time_start = bottomSheetDialog.findViewById(R.id.time_start);
        Button today_add_btn = bottomSheetDialog.findViewById(R.id.today_add_btn);
        time_start.setIs24HourView(true);
        time_finish.setIs24HourView(true);

        time_start.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                System.out.println("addToday start Time " + hourOfDay + ":" + minute);
                str_startT = hourOfDay + ":" + minute;
                System.out.println("addToday start Time " + str_startT);

            }
        });

        time_finish.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                str_finishT = hourOfDay + ":" + minute;
            }
        });

        time_start.setIs24HourView(true);
        time_finish.setIs24HourView(true);

        bottomSheetDialog.show();


//        TimePickerDialog dialog = new TimePickerDialog(TodayFragment.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener, 15, 24, false);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.show();


        today_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_title = title_today_edt.getText().toString();
                str_memo = memo_today_edt.getText().toString();
                if (str_title.equals("")) {
//                    Toast.makeText(TodayAddActivity.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 추후에 추가하고자 하는 시간에 데이터 값 있다면 밑에 또 추가 or 거절
                    System.out.println("value of Today title " + str_title);
                    System.out.println("value of Today start " + str_startT);
                    System.out.println("value of Today finish " + str_finishT);
                    System.out.println("value of Today memo " + str_memo);
                    Today today = new Today();
                    today.setTitle_Today(str_title);
                    today.setStart_Today(str_startT);
                    today.setFinish_Today(str_finishT);
                    today.setMemo_Today(str_memo);

                    System.out.println("timeStamp_date today " + timestamp_date);
                    db.collection("User").document(auth.getCurrentUser()
                            .getUid()).collection("Today")
                            .document(timestamp_year)
                            .collection(timestamp_month).document(timestamp_date).collection("PlanByTime").add(today).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "오늘의 일정 추가완료", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                                mAdapter.notifyDataSetChanged();
                                mAdapter_calendar.notifyDataSetChanged();
                                showToday(timestamp_date);
                            } else {
                                Toast.makeText(getContext(), "오늘의 일정 추가실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        mAdapter.notifyDataSetChanged();
        mAdapter_calendar.notifyDataSetChanged();



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public static void showToday(String date) {
        System.out.println("showToday date " + date);
        mTodayData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            mTodayData.add(i, new Today(i, "", "", "", ""));
        }
        String[] splitText;

        splitText = date.split("-");

        for (int j = 0; j < splitText.length; j++) {
            System.out.println(" text = " + splitText[j]);
        }
        String str_year = splitText[0];
        String str_month = splitText[1];
        String str_date = splitText[2];

        db.collection("User")
                .document(auth.getCurrentUser().getUid())
                .collection("Today")
                .document(str_year)
                .collection(str_month).document(date).collection("PlanByTime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                System.out.println("task is null");
                                updateReceiptsList(mTodayData);

                            } else {
                                System.out.println("task is not null");

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    str_title = documentSnapshot.get(Constants.TITLE_TODAY).toString();
                                    str_start = documentSnapshot.get(Constants.START_TODAY).toString();
                                    str_finish = documentSnapshot.get(Constants.FINISH_TODAY).toString();
                                    str_memo = documentSnapshot.get(Constants.MEMO_TODAY).toString();
                                    System.out.println("test str_start " + str_start);
                                    int idx = str_start.indexOf(":");
                                    // : 앞부분을 추출
                                    // substring은 첫번째 지정한 인덱스는 포함하지 않는다.
                                    String result = str_start.substring(0, idx);
                                    System.out.println("substring result" + result);
                                    int timeIdx = Integer.parseInt(result);

                                    mTodayData.get(timeIdx).setAll(timeIdx, str_title, str_start, str_finish, str_memo);

                                    System.out.println("test fragment " + mTodayData.toString());
                                }

                                updateReceiptsList(mTodayData);

                            }
                        }
                    }
                });


    }

    public static void updateReceiptsList(ArrayList<Today> mTodayData) {
        mNewTodayData.clear();
        mNewTodayData.addAll(mTodayData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();


    }
}
