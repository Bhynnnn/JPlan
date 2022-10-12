package com.example.jplan.Diary;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Diary;
import com.example.jplan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    int cnt=0;
    String relative_date;

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


//        System.out.println("Today : " + chkDate);

//        calendar.add(Calendar.DATE, -1);
//        chkDate = SDF.format(calendar.getTime());
//        System.out.println("Yesterday : " + chkDate);

        showDiary(timestamp_date);
        System.out.println("date format " + day);

        yesterday_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                chkDate = SDF.format(calendar.getTime());
                System.out.println("The day before: " + 1 +"일 "+ chkDate);
                showDiary(chkDate);
            }
        });

        tomorrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                chkDate = SDF.format(calendar.getTime());
                System.out.println("The day after: " + 1 +"일 "+ chkDate);
                showDiary(chkDate);
            }
        });


        return view;
    }

    public void showDiary(String timestamp_date){

        firebaseFirestore.collection("User")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Diary").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getDocuments().contains(timestamp_date)){
                    // 해당 날짜 있다면

                }
                else{
//                    Glide.with(getContext())
//                            .load(diary.getImgUrl())
//                            .into(diary_pic);
                    diary_pic.setBackgroundResource(R.drawable.logo);
                    icon_tv.setText("작성된 일기가 없습니다😅");
                    diary_content_tv.setText("일기를 새로 작성해주세요");
                }
            }
        });

        firebaseFirestore.collection("User")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Diary")
                .document(timestamp_date)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Diary diary = value.toObject(Diary.class);
                        // FEED 프로필 이미지 로드
                        Glide.with(getContext())
                                .load(diary.getImgUrl())
                                .into(diary_pic);
                        icon_tv.setText(diary.getIcon());
                        diary_content_tv.setText(diary.getDiaryContent());
                    }
                });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).tb_title.setText("Diary");
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