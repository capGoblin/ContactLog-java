package com.example.contactlog1.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlog1.MainActivity;
import com.example.contactlog1.R;
import com.example.contactlog1.UnsavedContactLogActivity;
import com.example.contactlog1.interfaces.RecyclerViewInterface;
import com.example.contactlog1.models.ContactLog;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ContactLog> contactLogs;
    private int selectedItem = RecyclerView.NO_POSITION;
    private final int maxHeaderWidth;
    private String selectedOption = "";

    public RecyclerViewAdapter(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList<ContactLog> contactLogs, int maxHeaderWidth) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.contactLogs = contactLogs;
        this.maxHeaderWidth = maxHeaderWidth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setWidth(maxHeaderWidth);
        holder.phoneNo.setWidth(maxHeaderWidth);
        holder.yesterday.setWidth(maxHeaderWidth);
        holder.lastWeek.setWidth(maxHeaderWidth);
        holder.lastMonth.setWidth(maxHeaderWidth);
        ContactLog contactLog = contactLogs.get(position);
        setBackgroundColor(holder, position);
        setContactLogDetails(holder, contactLog);
        if(!selectedOption.isEmpty()) {
            setOptionsToRows(selectedOption, holder);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selectedItem);
                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(selectedItem);
                recyclerViewInterface.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    private void setOptionsToRows(String selectedOption, ViewHolder holder) {
        switch (selectedOption) {
            case "yesterday":
                holder.lastWeek.setVisibility(View.GONE);
                holder.lastMonth.setVisibility(View.GONE);
                holder.yesterday.setVisibility(View.VISIBLE);
                System.out.println("GONE ANH");
//                cdYesterdayTextView.setText(yesterdayHours);
//                    setVisibility(cl, lastWeekHours, lastMonthHours);
//                adapter.notifyDataSetChanged();
                break;
            case "last week":
                holder.lastMonth.setVisibility(View.GONE);
                holder.yesterday.setVisibility(View.GONE);
                holder.lastWeek.setVisibility(View.VISIBLE);

//                cdLastWeekTextView.setText(lastWeekHours);
//                    setVisibility(cl, yesterdayHours, lastMonthHours);
//                adapter.notifyDataSetChanged();
                break;
            case "last month":
                holder.yesterday.setVisibility(View.GONE);
                holder.lastWeek.setVisibility(View.GONE);
                holder.lastMonth.setVisibility(View.VISIBLE);

//                cdLastMonthTextView.setText(lastMonthHours);
//                    setVisibility(cl, yesterdayHours, lastWeekHours);
//                adapter.notifyDataSetChanged();
                break;
        }
    }


    private void setBackgroundColor(@NonNull ViewHolder holder, int position) {
        if (position == selectedItem) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void setContactLogDetails(@NonNull ViewHolder holder, ContactLog contactLog) {
        if(context instanceof MainActivity) {
            holder.name.setText(contactLog.getName());
            holder.phoneNo.setText(contactLog.getPhoneNumber());
            holder.yesterday.setText(contactLog.getYesterdayHours());
            holder.lastWeek.setText(contactLog.getLastWeekHours());
            holder.lastMonth.setText(contactLog.getLastMonthHours());
        } else if(context instanceof UnsavedContactLogActivity) {
            holder.name.setVisibility(View.GONE);
            holder.phoneNo.setText(contactLog.getPhoneNumber());
            holder.lastWeek.setText(contactLog.getLastWeekHours());
            String yesterdayInfo = "";
            String lastWeekInfo = "";
            String lastMonthInfo = "";
            if(!Objects.equals(contactLog.getYesterdayHours(), "")) {
                yesterdayInfo = contactLog.getYesterdayHours() + "/" + contactLog.getYesterdayCount();
            }
            if(!Objects.equals(contactLog.getLastWeekHours(), "")) {
                lastWeekInfo = contactLog.getLastWeekHours() + "/" + contactLog.getLastWeekCount();
            }
            if(!Objects.equals(contactLog.getLastMonthHours(), "")) {
                lastMonthInfo = contactLog.getLastMonthHours() + "/" + contactLog.getLastMonthCount();
            }
            holder.yesterday.setText(yesterdayInfo);
            holder.lastWeek.setText(lastWeekInfo);
            holder.lastMonth.setText(lastMonthInfo);
        }
    }

    @Override
    public int getItemCount() {
        return contactLogs.size();
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        System.out.println(selectedOption + "HERER");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView phoneNo;
        TextView yesterday;
        TextView lastWeek;
        TextView lastMonth;
        public ViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            name = view.findViewById(R.id.rv_textName);
            phoneNo = view.findViewById(R.id.rv_phoneNo);
            yesterday = view.findViewById(R.id.rv_yesterday);
            lastWeek = view.findViewById(R.id.rv_lastWeek);
            lastMonth = view.findViewById(R.id.rv_lastMonth);
        }
    }
}
