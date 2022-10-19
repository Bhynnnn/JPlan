package com.example.jplan.Diary;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.jplan.Plan.PlanAdapter;
import com.example.jplan.R;
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

    Button yesterday_btn, tomorrow_btn;
    TextView today_tv, icon_tv, diary_content_tv;
    ImageView diary_pic;
    Date toTimeStamp = new Date();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);

    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    String day = dayFormat.format(toTimeStamp);

    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    String chkDate = SDF.format(calendar.getTime());


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
        yesterday_btn = view.findViewById(R.id.yesterday_btn);
        tomorrow_btn = view.findViewById(R.id.tomorrow_btn);
        diary_pic = view.findViewById(R.id.diary_pic);
        diary_content_tv = view.findViewById(R.id.diary_content_tv);
        today_tv = view.findViewById(R.id.today_tv);
        icon_tv = view.findViewById(R.id.icon_tv);

        today_tv.setText(timestamp_date);

        System.out.println("timestamp date " + timestamp_date);

        showDiary(timestamp_date);
        System.out.println("date format " + day);

        yesterday_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                chkDate = SDF.format(calendar.getTime());
                System.out.println("The day before: " + 1 + "일 " + chkDate);
                today_tv.setText(chkDate);
                showDiary(chkDate);

            }
        });

        tomorrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                chkDate = SDF.format(calendar.getTime());
                System.out.println("The day after: " + 1 + "일 " + chkDate);
                today_tv.setText(chkDate);
                showDiary(chkDate);
            }
        });


        return view;
    }

    public void showDiary(String timestamp_date) {


        firebaseFirestore.collection("User")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Diary")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(timestamp_date)) {
                                    System.out.println("document getdata " + document.getData());
                                    System.out.println("document getid " + document.getId());
                                    System.out.println("document getdata icon " + document.get("imgUrl").toString());

                                    Glide.with(getContext())
                                            .load(document.get("imgUrl").toString())
                                            .into(diary_pic);
                                    icon_tv.setText(document.get("icon").toString());
                                    diary_content_tv.setText(document.get("diaryContent").toString());

                                } else {
                                    System.out.println("document fail");
                                    Glide.with(getContext())
                                            .load(R.drawable.logo)
                                            .into(diary_pic);
                                    icon_tv.setText("작성된 일기가 없습니다😅");
                                    diary_content_tv.setText("일기를 새로 작성해주세요");
                                }

                            }
                        } else {
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

        ((MainActivity) getActivity()).tb_title.setText("Diary");
        ((MainActivity) getActivity()).addBtn.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DiaryAddActivity.class);
                startActivity(intent);
            }
        });
    }
}