package com.example.jplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private ArrayList<Todo> mDataset;

    public TodoAdapter(ArrayList data) {
        mDataset = data;
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
        holder.setItem(todo);
        holder.title_Todo.setText(todo.getTitle());
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
