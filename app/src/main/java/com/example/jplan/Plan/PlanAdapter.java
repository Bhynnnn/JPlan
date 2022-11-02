package com.example.jplan.Plan;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jplan.Model.Constants;
import com.example.jplan.DoubleClickListener;
import com.example.jplan.Model.Plan;
import com.example.jplan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private ArrayList<Plan> mDataset;
    private Context context;
    Plan newPlan = new Plan();

    GestureDetector gd;
    GestureDetector.OnDoubleTapListener listener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    int count;
    public PlanAdapter(ArrayList data) {
        System.out.println("todo adapter data 0 " + data);

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
        System.out.println("todo adapter data 1 " + mDataset);

        holder.setItem(plan);
        System.out.println("plan adapter icon " + plan.getIcon_Plan());
        System.out.println("plan adapter title " + plan.getTitle_Plan());

        // 횟수 첫 세팅
        holder.total_tv.setText(String.valueOf(plan.getTotal_Plan()));
        holder.count_tv.setText(String.valueOf(plan.getCount_Plan()));
        if (plan.getIcon_Plan().equals("icon_1")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_1);
        } else if (plan.getIcon_Plan().equals("icon_2")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_2);

        } else if (plan.getIcon_Plan().equals("icon_3")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_3);

        } else if (plan.getIcon_Plan().equals("icon_4")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_4);

        } else if (plan.getIcon_Plan().equals("icon_5")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_5);

        } else if (plan.getIcon_Plan().equals("icon_6")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_6);

        } else if (plan.getIcon_Plan().equals("icon_7")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_7);

        } else if (plan.getIcon_Plan().equals("icon_8")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_8);

        } else if (plan.getIcon_Plan().equals("icon_9")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_9);

        } else if (plan.getIcon_Plan().equals("icon_10")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_10);

        } else if (plan.getIcon_Plan().equals("icon_11")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_11);

        } else if (plan.getIcon_Plan().equals("icon_12")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_12);

        } else if (plan.getIcon_Plan().equals("icon_13")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_13);

        } else if (plan.getIcon_Plan().equals("icon_14")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_14);

        } else if (plan.getIcon_Plan().equals("icon_15")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_15);
        }
        else if (plan.getIcon_Plan().equals("icon_16")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_16);
        }
        else if (plan.getIcon_Plan().equals("icon_17")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_17);
        }
        else if (plan.getIcon_Plan().equals("icon_18")) {
            holder.circle_iv.setBackgroundResource(R.drawable.icon_18);
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
                // 상세보기
                Toast.makeText(context, "single" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlanShowActivity.class);
                intent.putExtra("check_Plan", String.valueOf(plan.isCheck_Plan()));
                intent.putExtra("title_Plan", plan.getTitle_Plan());
                intent.putExtra("total_Plan", String.valueOf(plan.getTotal_Plan()));
                intent.putExtra("memo_Plan", plan.getMemo_Plan());
                intent.putExtra("icon_Plan", plan.getIcon_Plan());
                context.startActivity(intent);


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
