package com.example.jplan;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private ArrayList<Plan> mDataset;
    private Context context;
    Plan newPlan = new Plan();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    int count;
    public PlanAdapter(ArrayList data) {
        mDataset = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlanAdapter.ViewHolder holder, int position) {

        Plan plan = mDataset.get(position);
        holder.setItem(plan);
        System.out.println("plan adapter icon " + plan.getIcon_Plan());
        System.out.println("plan adapter title " + plan.getTitle_Plan());

        // TODO: 2022/06/08 두번째 하얀 원 -> 물 차오르는 느낌으로 해아함 1~30 || 1~7 ---
        // TODO: 2022/06/09 아니 그 뭐여 그 그그그 처음 라디오로 일 수 체크하잖아 그게 days_Plan 이 변수에 들어가는거고
        // TODO: 2022/06/09 더블클릭 했을 때 까이는걸로? 하면 기억 못하니 하나 더 만들어야하네

        // 횟수 첫 세팅
        holder.total_tv.setText(String.valueOf(plan.getTotal_Plan()));
        holder.count_tv.setText(String.valueOf(plan.getCount_Plan()));
        if (plan.getIcon_Plan().equals("walk")) {
            holder.circle_iv.setBackgroundResource(R.drawable.walk_ic);
        } else if (plan.getIcon_Plan().equals("bus")) {
            holder.circle_iv.setBackgroundResource(R.drawable.bus_ic);

        } else if (plan.getIcon_Plan().equals("airplane")) {
            holder.circle_iv.setBackgroundResource(R.drawable.airplane_ic);

        } else if (plan.getIcon_Plan().equals("android")) {
            holder.circle_iv.setBackgroundResource(R.drawable.android_ic);

        } else if (plan.getIcon_Plan().equals("health")) {
            holder.circle_iv.setBackgroundResource(R.drawable.health_ic);

        } else if (plan.getIcon_Plan().equals("code")) {
            holder.circle_iv.setBackgroundResource(R.drawable.code_ic);

        } else if (plan.getIcon_Plan().equals("money")) {
            holder.circle_iv.setBackgroundResource(R.drawable.money_ic);

        } else if (plan.getIcon_Plan().equals("pet")) {
            holder.circle_iv.setBackgroundResource(R.drawable.pet_ic);

        } else if (plan.getIcon_Plan().equals("score")) {
            holder.circle_iv.setBackgroundResource(R.drawable.score_ic);

        } else if (plan.getIcon_Plan().equals("smile")) {
            holder.circle_iv.setBackgroundResource(R.drawable.smile_ic);

        } else if (plan.getIcon_Plan().equals("star")) {
            holder.circle_iv.setBackgroundResource(R.drawable.star_ic);

        } else if (plan.getIcon_Plan().equals("tea")) {
            holder.circle_iv.setBackgroundResource(R.drawable.tea_ic);

        } else if (plan.getIcon_Plan().equals("thumbsup")) {
            holder.circle_iv.setBackgroundResource(R.drawable.thumbsup_ic);

        } else if (plan.getIcon_Plan().equals("water")) {
            holder.circle_iv.setBackgroundResource(R.drawable.water_ic);

        } else if (plan.getIcon_Plan().equals("heart")) {
            holder.circle_iv.setBackgroundResource(R.drawable.heart_ic);

        }

        holder.itemView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                db.collection("User").document(auth.getCurrentUser().getUid()).collection("Plan")
                        .whereEqualTo(Constants.TITLE_PLAN, plan.getTitle_Plan())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot item : queryDocumentSnapshots) {
                            System.out.println("test adapter " + item.getId());
                            db.collection("User").document(auth.getCurrentUser().getUid())
                                    .collection("Plan").document(item.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.get("count_Plan").toString().equals(documentSnapshot.get("total_Plan").toString())) {
                                        Toast.makeText(context, "이미 완료된 계획 2", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.collection("User").document(auth.getCurrentUser().getUid())
                                                .collection("Plan").document(item.getId()).update("count_Plan", Integer.parseInt(documentSnapshot.get("count_Plan").toString())+1);

                                        count = Integer.parseInt(documentSnapshot.get("count_Plan").toString())+1;
                                        holder.count_tv.setText(String.valueOf(count));
                                    }
                                }
                            });

                        }
                    }

                });

                //  물 차오르도록 + 텍스트 값 바뀌도록



            }

            @Override
            public void onSingleClick() {
                Toast.makeText(context, "single" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                // TODO: 2022/06/08 싱글탭 -> 상세보기
            }

        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View circle_iv;
        private TextView count_tv, total_tv;

        public ViewHolder(View view) {
            super(view);
            circle_iv = view.findViewById(R.id.circle_iv);
            count_tv = view.findViewById(R.id.count_tv);
            total_tv = view.findViewById(R.id.total_tv);
        }

        public void setItem(Plan item) {
            count_tv.setText(String.valueOf(item.getCount_Plan()));
            total_tv.setText(String.valueOf(item.getTotal_Plan()));

        }

    }


}
