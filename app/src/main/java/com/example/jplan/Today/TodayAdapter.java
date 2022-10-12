package com.example.jplan.Today;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jplan.Model.Today;
import com.example.jplan.R;

import java.util.ArrayList;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {
    private ArrayList<Today> mDataset;
    String str_po;
    public TodayAdapter(ArrayList data) {
        mDataset = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( TodayAdapter.ViewHolder holder, int position) {
        System.out.println("test item ");
        Today today = mDataset.get(position);

        System.out.println("today adapter " + position + " item " + today.getStart_Today());
        if(today.getTitle_Today().equals("")){
            holder.dot.setImageResource(R.drawable.dot_uncolored);
        }
        else{
            holder.dot.setImageResource(R.drawable.dot_colored);
        }
        if(today.getMemo_Today().equals("")){
            holder.memo_item.setVisibility(View.GONE);
        }
        else{
            holder.memo_item.setVisibility(View.VISIBLE);
        }

        if(today.getStart_Today().equals("")){
            // 값이 없을 때
            holder.start_item.setText(today.getStart_Today());
        }
        else{
            holder.start_item.setText(today.getStart_Today() + "~");
        }
        holder.title_item.setText(today.getTitle_Today());
        holder.finish_item.setText(today.getFinish_Today());
        holder.memo_item.setText(today.getMemo_Today());
        if(position<10){
            str_po = "0"+position;
        }
        else{
            str_po = ""+position;
        }
        holder.item_time.setText(str_po + ":00");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_time, title_item, start_item, finish_item, memo_item;
        public ImageView dot;
        public ViewHolder(View view) {
            super(view);
            title_item = (TextView) view.findViewById(R.id.title_item);
            start_item = (TextView) view.findViewById(R.id.start_item);
            finish_item = (TextView) view.findViewById(R.id.finish_item);
            memo_item = (TextView) view.findViewById(R.id.memo_item);
            item_time = (TextView) view.findViewById(R.id.item_time);
            dot = (ImageView) view.findViewById(R.id.dot);



        }
    }


}
