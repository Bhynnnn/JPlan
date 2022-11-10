package com.example.jplan.Mypage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jplan.R;
import com.example.jplan.SaturdayDecorator;
import com.example.jplan.SundayDecorator;
import com.example.jplan.Todo.Todo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MypageFragment extends Fragment {
    MaterialCalendarView calendarView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    ArrayList<String> dateArr = new ArrayList();
    TextView diary_goal, todo_goal, plan_goal;
    double todoCount = 0;
    double todoAllCount = 0;
    double planCount = 0;
    double planAllCount = 0;

    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    String timestamp_year = yearFormat.format(toTimeStamp);

    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    String timestamp_month = monthFormat.format(toTimeStamp);
    String str_year, str_month;

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
        todo_goal = (TextView) view.findViewById(R.id.todo_goal);
        plan_goal = (TextView) view.findViewById(R.id.plan_goal);
        diary_goal = (TextView) view.findViewById(R.id.diary_goal);

        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        calendarView.setClickable(false);

        getCalendarDiary();
        System.out.println("diary date Arr size " + dateArr.size());

        return view;
    }


    public void getDiaryGoal(ArrayList<String> dateArr) {

        System.out.println("diary mypage arr " + dateArr.size());
        Calendar calendar = Calendar.getInstance();
        double maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String diaryGoal = String.format("%.2f", (double) dateArr.size()/maxDate * 100.0);

        System.out.println("diary mypage maxdate " + maxDate);
        System.out.println("diary mypage goal percent : " + String.format("%.2f", (double) dateArr.size()/maxDate * 100.0) + "%");

        diary_goal.setText(String.format("%.2f", (double) dateArr.size()/maxDate * 100.0) + "%");

        System.out.println("diary mypage Goal "+diaryGoal);

    }

    public void getPlanGoal() {
        firestore.collection("User")
                .document(auth.getCurrentUser().getUid())
                .collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            planCount = 0;
                            planAllCount = 0;
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                System.out.println("plan get title " +  snapshot.get("title_Plan").toString());
                                planAllCount++;
                                if(snapshot.get("count_Plan").equals(snapshot.get("total_Plan"))){
                                    planCount++;
                                }
                            }
                            if (planCount == 0){
                                plan_goal.setText(String.format("0.00%"));
                            }
                            System.out.println("plan count all "+ planAllCount);
                            System.out.println("plan count " + planCount);
                            plan_goal.setText(String.format("%.2f", (double) planCount/planAllCount * 100.0) + "%");
                        }
                    }
                });

    }

    public void getTodoGoal() {
        db.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    todoCount = 0;
                    todoAllCount = 0;
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        todoAllCount++;
                        System.out.println("+++Todo snapshot children key 0 " + dataSnapshot);
                        System.out.println("+++Todo snapshot children key 1 " + dataSnapshot.getKey());
                        System.out.println("+++Todo snapshot children title " + dataSnapshot.child("title").getValue().toString());

                        if(dataSnapshot.child("selected").getValue().toString().equals("true")){
                            todoCount++;
                        }
                    }
                    System.out.println("todo count " + todoCount);
                    System.out.println("todo All count " + todoAllCount);
                    System.out.println("get todo goal percent " + (double)todoCount/todoAllCount * 100.0 );
                    todo_goal.setText(String.format("%.2f", (double) todoCount/todoAllCount * 100.0) + "%");


                }
                else{
                    System.out.println("list 존재 x");
                    todoCount = 0;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        diary_goal.setText(String.format("%.2f", (double) dateArr.size()/maxDate * 100.0) + "%");


    }


    public void getCalendarDiary() {
        str_year = timestamp_year;
        str_month = timestamp_month;
        System.out.println("getcalendardiary "+str_month);
        System.out.println("getcalendardiary "+str_year);
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                System.out.println("month changed listener 3 " + date.getYear());
                str_year = String.valueOf(date.getYear());
                str_month = String.valueOf(date.getMonth() + 1);
                System.out.println("str year " + str_year);
                System.out.println("str_month " + str_month);
            }
        });
        dateArr.clear();
        firestore.collection("User").document(auth.getCurrentUser().getUid())
                .collection("Diary")
                .document(str_year)
                .collection(str_month)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("task success");

                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                System.out.println("diary test key " + snapshot.getId());
                                dateArr.add(snapshot.getId());
                            }

                            System.out.println("diary str " + dateArr.size());
                            getDiaryGoal(dateArr);

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

        System.out.println("mypage calendar year " + str_year);
        System.out.println("mypage calendar month " + str_month);
        System.out.println("mypage calendar dateArr " + dateArr.size());

        getPlanGoal();
        getTodoGoal();

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
    }
}