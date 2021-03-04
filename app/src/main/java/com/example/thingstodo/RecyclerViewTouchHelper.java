package com.example.thingstodo;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.thingstodo.Adapter.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final ListAdapter adapter;

    public RecyclerViewTouchHelper(ListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext(), R.style.AlertDialog);
            builder.setTitle("Deleting from list");
            builder.setMessage("Confirm deletion of selected item?");
            builder.setPositiveButton("Confirm", (dialog, which) -> adapter.deleteListItem(position));
            builder.setNegativeButton("Cancel", (dialog, which) -> adapter.notifyItemChanged(position));
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            adapter.editListItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.green))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit)
                .addSwipeLeftLabel("Edit")
                .setSwipeLeftLabelColor(Color.WHITE)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete)
                .addSwipeRightLabel("Delete")
                .setSwipeRightLabelColor(Color.WHITE)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}
