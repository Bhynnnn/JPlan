package com.example.jplan.Mypage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jplan.R;
import com.example.jplan.SaturdayDecorator;
import com.example.jplan.SundayDecorator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MypageFragment extends Fragment {
    MaterialCalendarView calendarView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> dateArr = new ArrayList();
    String date;
    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);


    public MypageFragment() {
        // Required empty public constructor
    }

    public static MypageFragment newInstance() {
        MypageFragment fragment = new MypageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarview);

        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        calendarView.setClickable(false);

        getCalendarDiary();
        System.out.println("diary date Arr size " + dateArr.size());

        getMyGoal();


        return view;
    }


    public void getMyGoal() {
        db.collection("User").document(auth.getCurrentUser().getUid()).collection("Diary").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        System.out.println("diary test key " + snapshot.getId());
                        String snapshotId = snapshot.getId();
                        String[] snapshotIdArr = snapshotId.split("-");

                        for (int i = 0; i < snapshotIdArr.length; i++) {
                            // snapshot에서의 month
                            System.out.println(snapshotIdArr[1]);
                            // 지금 보고있는 달력 창이 몇월인지 알아야함

                            calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
                                @Override
                                public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                                    System.out.println("month changed listener 3 " + date);
                                    String str = date.toString();
                                    String[] strArr = str.split("-");

                                    for (int i = 0; i < strArr.length; i++) {
                                        // 월
                                        System.out.println("month split " + strArr[1]);
                                        int month = Integer.valueOf(strArr[1]) + 1;
                                        System.out.println("month split real " +month);
                                        // month - 이번달 몇월인지 알려줌
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });
    }

    public void getCalendarDiary() {
        db.collection("User").document(auth.getCurrentUser().getUid()).collection("Diary").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        System.out.println("diary test key " + snapshot.getId());
                        dateArr.add(snapshot.getId());
                    }
                    for (int i = 0; i < dateArr.size(); i++) {
                        System.out.println("dateArr " + dateArr.get(i));
                        String dateStr = dateArr.get(i);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date = formatter.parse(dateStr);
                            calendarView.setDateSelected(date, true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity)getActivity()).tb_title.setText("Mypage");
//        ((MainActivity) getActivity()).addBtn.setVisibility(View.GONE);

    }
}