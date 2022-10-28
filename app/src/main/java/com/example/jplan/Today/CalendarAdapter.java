package com.example.jplan.Today;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplan.Model.Today;
import com.example.jplan.Model.mCalendar;
import com.example.jplan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    public static ArrayList<mCalendar> mDataset;
    private int selectedItemPosition = 3;
    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    String chkDate = SDF.format(calendar.getTime());
    TodayFragment todayFragment;
    String str_today;


    public CalendarAdapter(ArrayList<mCalendar> mCalendarData, TodayFragment todayFragment) {
        this.mDataset = mCalendarData;
        this.todayFragment = todayFragment;
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        mCalendar calendar = mDataset.get(position);
        holder.item_date.setText(calendar.getDate());
        holder.item_day.setText(calendar.getDay());
        holder.item_year.setText(calendar.getYear());
        holder.item_month.setText(calendar.getMonth());

        System.out.println("selected Item " + selectedItemPosition);
        System.out.println("selected position " + position);
        str_today = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDate();

        System.out.println("calendar adapter year 3" + calendar.getYear());
        System.out.println("calendar adapter month 3" + calendar.getMonth());
        System.out.println("calendar adapter date 3" + calendar.getDate());
        System.out.println("calendar adapter day 3 " + calendar.getDay());

        // 시작할때 오늘 날짜 값 버튼 클릭된 상태여야함
        if (str_today.equals(chkDate)) {
            // 오늘
            holder.itemView.setBackgroundColor(Color.rgb(144, 169, 207));
        }

        if (selectedItemPosition == position){
            System.out.println("selected same ");
            holder.itemView.setBackgroundColor(Color.rgb(144, 169, 207));
        }
        else{
            System.out.println("selected not same ");
            holder.itemView.setBackgroundColor(Color.WHITE);

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_day, item_date, item_year, item_month;
        public CardView week_cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_day = (TextView) itemView.findViewById(R.id.item_day);
            item_date = (TextView) itemView.findViewById(R.id.item_date);
            item_year = (TextView) itemView.findViewById(R.id.item_year);
            item_month = (TextView) itemView.findViewById(R.id.item_month);
            week_cardView = (CardView) itemView.findViewById(R.id.week_cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("selected Item click" + selectedItemPosition);

                    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                    // Updating old as well as new positions
                    notifyItemChanged(selectedItemPosition);
                    selectedItemPosition = getAdapterPosition();
                    System.out.println("selected Item click" + selectedItemPosition);
                    notifyItemChanged(selectedItemPosition);
                    // 날짜 item click
                    // 색 넣기, 해당 날짜 data 가져와 다른 recyclerview로 값 넘겨야함
                    // todayFragment에 showToday(date)로 date 값만 넘겨 (2022-10-24 형식)
                    System.out.println("calendar adapter year " + item_year.getText());
                    System.out.println("calendar adapter month " + item_month.getText());
                    System.out.println("calendar adapter date " + item_date.getText());
                    System.out.println("calendar adapter day " + item_day.getText());

                    String today = item_year.getText() + "-" + item_month.getText() + "-" + item_date.getText();
                    String today_year = mDataset.get(getAdapterPosition()).getYear();
                    String today_month = mDataset.get(getAdapterPosition()).getMonth();
                    String today_date = mDataset.get(getAdapterPosition()).getDate();

                    today = today_year + "-" + today_month + "-" + today_date;
                    System.out.println("calendar adapter today string " + today);
                    // 클릭된 item 값
                    TodayFragment.showToday(today);

                    //오늘 값 나옴

                }
            });
        }
    }
}
