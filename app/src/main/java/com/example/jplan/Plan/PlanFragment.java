package com.example.jplan.Plan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jplan.Model.Constants;
import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Plan;
import com.example.jplan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PlanFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PlanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Plan> mPlanData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    String str_title, str_memo, str_icon;
    int  total_Plan_int, count_Plan_int;
    boolean check_Plan_bool;

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

        initDataset();
    }

    // todo PlanAddActivity로 추가된 Plan item 2줄로 출력 해야 함 -> recyclerview
    // todo 근데 추가된 item이 실행 되었는지 안되었는지 확인할라면 - todo 에 띄워서 체크 된거 확인하기 + 원 더블클릭해서 1회로 치기
    // todo 오늘의 뭐시기 그건 그냥 일정 보여주기라서 뭔가 추가적으로 할 수는 없을 듯

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        getActivity().setTitle("Plan");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        mLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.scrollToPosition(0);
        System.out.println("fragment test oncreateview");
        mAdapter = new PlanAdapter(mPlanData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void initDataset() {
        mPlanData = new ArrayList<>();

        db.collection("User").document(auth.getCurrentUser().getUid()).collection("Plan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot item : queryDocumentSnapshots) {
                    str_title = item.get(Constants.TITLE_PLAN).toString();
                    str_memo = item.get(Constants.MEMO_PLAN).toString();
                    str_icon = item.get(Constants.ICON_PLAN).toString();
                    total_Plan_int = Integer.parseInt(item.get("total_Plan").toString());
                    count_Plan_int = Integer.parseInt(item.get("count_Plan").toString());
                    check_Plan_bool = Boolean.parseBoolean(item.get("check_Plan").toString());
                    mPlanData.add(new Plan(str_title, str_memo, str_icon, total_Plan_int, count_Plan_int, check_Plan_bool));
                }
                mAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).tb_title.setText("Plan");
        System.out.println("fragment test onresume");
        ((MainActivity) getActivity()).addBtn.setVisibility(View.VISIBLE);

        ((MainActivity) getActivity()).addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanAddActivity.class);
                startActivity(intent);
            }
        });

    }
}