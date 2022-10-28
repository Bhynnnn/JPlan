package com.example.jplan.Todo;

import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private ArrayList<Todo> mDataset;
    private ArrayList<String> mKeyDataset;

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

        mKeyDataset.get(0);

        System.out.println("todoAdapter key data " + mKeyDataset.get(0));
        System.out.println("todoAdapter key data " + mKeyDataset.get(1));
        System.out.println("todoAdapter key data " + mKeyDataset.get(2));
        System.out.println("todoAdapter key data " + mKeyDataset.get(3));
        System.out.println("todoAdapter key data " + mKeyDataset.get(4));


        holder.setItem(todo);
        holder.title_Todo.setText(todo.getTitle());
//        holder.check_todo.setChecked(Boolean.parseBoolean(todo.getSelected()));

        System.out.println("todo adapter title" + todo.getTitle());
        System.out.println("todo adapter check " + todo.getSelected());

        if (todo.getSelected().equals("false") ){
            holder.check_todo.setChecked(false);
        }
        else if(todo.getSelected().equals("true")){
            holder.check_todo.setChecked(true);
        }

        holder.check_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("adapter position " + holder.getAdapterPosition());
                System.out.println("check todo is checked? " + holder.check_todo.isChecked());

                if(holder.check_todo.isChecked()){
                    // 원래 체크되어잌ㅅ다 ( true )
                    System.out.println("in if " + holder.check_todo.isChecked());

                    holder.check_todo.setChecked(false);
                    String str_todo_uid = mKeyDataset.get(holder.getAdapterPosition());
                    db.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).child(str_todo_uid)
                            .child("selected").setValue("false");
                }
                else if (!holder.check_todo.isChecked()){
                    System.out.println("in if " + holder.check_todo.isChecked());

                    holder.check_todo.setChecked(true);
                    String str_todo_uid = mKeyDataset.get(holder.getAdapterPosition());
                    db.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).child(str_todo_uid)
                            .child("selected").setValue("true");
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_Todo;
        public CheckBox check_todo;

        public ViewHolder(View view) {
            super(view);
            title_Todo = (TextView) view.findViewById(R.id.title_Todo);
            check_todo = (CheckBox) view.findViewById(R.id.check_todo);
        }

        public void setItem(Todo item){
            title_Todo.setText(item.getTitle());
        }

    }


}
