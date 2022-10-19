package com.example.jplan.Today;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplan.Model.Calendar;
import com.example.jplan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<Calendar> mData;

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        ViewHolder  vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        Calendar calendar = mData.get(position);

        Date toTimeStamp = new Date();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String today_day = dayFormat.format(toTimeStamp);
        String clicked_day = holder.day.toString();

        if (today_day.equals(clicked_day)) {
            holder.week_cardView.setBackgroundColor(Color.parseColor(String.valueOf(R.color.main_dark)));
        }
        holder.day.setText(calendar.getCl_day());
        holder.date.setText(calendar.getCl_date());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, date;
        CardView week_cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = (TextView) itemView.findViewById(R.id.day);
            date = (TextView) itemView.findViewById(R.id.date);
            week_cardView = (CardView) itemView.findViewById(R.id.week_cardView);
        }
    }
}
