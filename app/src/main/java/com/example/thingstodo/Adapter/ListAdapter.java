package com.example.thingstodo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thingstodo.AddNewListItem;
import com.example.thingstodo.Database.DataBaseHelper;
import com.example.thingstodo.MainActivity;
import com.example.thingstodo.Model.ListModel;
import com.example.thingstodo.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ListModel> list;
    private MainActivity activity;
    private DataBaseHelper helper;

    public ListAdapter(DataBaseHelper helper, MainActivity activity) {
        this.helper = helper;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final ListModel listItem = list.get(position);
        holder.checkBox.setText(listItem.getListItem());
        holder.checkBox.setChecked(convertToBool(listItem.getCheckedStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    helper.updateCheckedStatus(listItem.getId(), 1);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    deleteListItem(position);
                    notifyDataSetChanged();
                } else {
                    helper.updateCheckedStatus(listItem.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

            CheckBox checkBox;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.list_checkbox);
            }
    }

    public boolean convertToBool(int val) {
        return val!=0;
    }

    public Context getContext() {
        return activity;
    }

    public void setList(List<ListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void deleteListItem(int position) {
        ListModel listItem = list.get(position);
        helper.deleteItem(listItem.getId());
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void editListItem(int position) {
        ListModel listItem = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", listItem.getId());
        bundle.putString("listItem", listItem.getListItem());

        AddNewListItem newListItem = new AddNewListItem();
        newListItem.setArguments(bundle);
        newListItem.show(activity.getSupportFragmentManager(), newListItem.getTag());
    }

}
