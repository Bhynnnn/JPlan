package com.example.jplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TodayFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TodayAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Today> mTodayData;
    FloatingActionButton fabAdd;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp) ;
    String str_title, str_start, str_finish, str_memo, str_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new TodayAdapter(mTodayData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fabAdd = view.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TodayAddActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    private void initDataset() {
        // array 24개
        // 시간도 00 ~ 23
        // 먼저 24칸 만들고
        // db조회했을 때 start_time 앞자리의 값들은 array에 그대로 담고
        // 없는 값들은 빈칸으로 넣어
        mTodayData = new ArrayList<>();

        for(int i=0; i<24; i++){
            mTodayData.add(i, new Today(i, "", "", "", ""));
        }
        db.collection("User").document(auth.getCurrentUser()
                .getUid()).collection("Today").document(timestamp_date).collection("PlanByTime")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot item : queryDocumentSnapshots){
                    str_title = item.get(Constants.TITLE_TODAY).toString();
                    str_start = item.get(Constants.START_TODAY).toString();
                    str_finish = item.get(Constants.FINISH_TODAY).toString();
                    str_memo = item.get(Constants.MEMO_TODAY).toString();

                    // db에 start_today 앞 시간이랑 array 에 있는 시간이 동일한게 있다? 그럼 뒤에 string 값 다 저장 ㄲ
                    // 근데 같은게 item에 없다 -> 그럼 그냥 "" 채워 넣기?
                    // arraylist를 위치 지정 가능?

                    int idx = str_start.indexOf(":");
                    // : 앞부분을 추출
                    // substring은 첫번째 지정한 인덱스는 포함하지 않는다.
                    String result = str_start.substring(0, idx);
                    System.out.println("substring result" + result);
                    int timeIdx = Integer.parseInt(result);

                    mTodayData.get(timeIdx).setAll(timeIdx, str_title, str_start, str_finish, str_memo);
                }
                System.out.println("test fragment " + mTodayData.toString());

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).tb_title.setText("Today");
    }
}
