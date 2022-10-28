package com.example.jplan.Mypage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.jplan.Main.MainActivity;
import com.example.jplan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MypageFragment extends Fragment {
    CalendarView calendarView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String date;
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
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);

//        mypage_email.setText(auth.getCurrentUser().getEmail());
//        db.collection("User").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    System.out.println("task test"+task.getResult().get("name"));
//                    mypage_name.setText(task.getResult().get("name").toString());
//                }
//            }
//        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year+"년 " +month+"월 " + dayOfMonth +"일";

            }
        });

        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity)getActivity()).tb_title.setText("Mypage");
//        ((MainActivity) getActivity()).addBtn.setVisibility(View.GONE);

    }
}