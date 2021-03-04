package com.example.thingstodo;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thingstodo.Adapter.ListAdapter;
import com.example.thingstodo.Database.DataBaseHelper;
import com.example.thingstodo.Model.ListModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    private DataBaseHelper helper;
    private List<ListModel> list;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.btn_add_item);
        helper = new DataBaseHelper(MainActivity.this);
        list = new ArrayList<>();
        adapter = new ListAdapter(helper, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        list = helper.getListItems();
        Collections.reverse(list);
        adapter.setList(list);

        fab.setOnClickListener(v -> AddNewListItem.newInstance().show(getSupportFragmentManager(), AddNewListItem.TAG));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        list = helper.getListItems();
        Collections.reverse(list);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}