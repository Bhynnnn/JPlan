package com.example.jplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment {

    Button yesterday_btn, tomorrow_btn, diary_pic_btn;
    TextView today_tv, icon_tv, diary_content_tv;

    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp) ;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
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
        diary_pic_btn = view.findViewById(R.id.diary_pic_btn);
        diary_content_tv = view.findViewById(R.id.diary_content_tv);
        today_tv = view.findViewById(R.id.today_tv);
        icon_tv = view.findViewById(R.id.icon_tv);

        today_tv.setText(timestamp_date);

        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).tb_title.setText("Diary");

        ((MainActivity) getActivity()).addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DiaryAddActivity.class);
                startActivity(intent);
            }
        });
    }
}