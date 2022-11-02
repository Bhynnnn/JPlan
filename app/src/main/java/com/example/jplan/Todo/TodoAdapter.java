package com.example.jplan.Todo;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplan.ItemTouchHelperListener;
import com.example.jplan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> implements ItemTouchHelperListener {
    private ArrayList<Todo> mDataset;
    private ArrayList<String> mKeyDataset;
    Map<String, Integer> map = new HashMap<>();
    List<String> keyList;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public TodoAdapter(ArrayList mDataset, ArrayList mKeyDataset) {
        this.mDataset = mDataset;
        this.mKeyDataset = mKeyDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(TodoAdapter.ViewHolder holder, int position) {
        Todo todo = mDataset.get(position);
        holder.title_Todo.setText(todo.getTitle());
        if (todo.getSelected().equals("false")) {
            holder.check_todo.setChecked(false);
        } else if (todo.getSelected().equals("true")) {
            holder.check_todo.setChecked(true);
        }
        holder.check_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("adapter position " + holder.getAdapterPosition());
                System.out.println("check todo is checked? " + holder.check_todo.isChecked());

                if (holder.check_todo.isChecked()) {
                    db.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).child(mKeyDataset.get(holder.getAdapterPosition()))
                            .child("selected").setValue("true");
                }
                if (!holder.check_todo.isChecked()) {
                    db.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).child(mKeyDataset.get(holder.getAdapterPosition()))
                            .child("selected").setValue("false");
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // itemView 클릭시 수정
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();

    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        Todo change_item = mDataset.get(from_position);

        mDataset.remove(from_position);
        mDataset.add(to_position, change_item);

        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        mDataset.remove(position);

        System.out.println("remove key " + mKeyDataset.get(position));

        db.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).child(mKeyDataset.get(position)).removeValue();

        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_Todo;
        public CheckBox check_todo;

        public ViewHolder(View view) {
            super(view);
            title_Todo = (TextView) view.findViewById(R.id.title_Todo);
            check_todo = (CheckBox) view.findViewById(R.id.check_todo);
        }

    }
}
