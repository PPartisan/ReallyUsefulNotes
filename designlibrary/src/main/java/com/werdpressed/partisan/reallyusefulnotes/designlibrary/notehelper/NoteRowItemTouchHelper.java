package com.werdpressed.partisan.reallyusefulnotes.designlibrary.notehelper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;


public class NoteRowItemTouchHelper extends ItemTouchHelper.Callback {

    private NoteRowItemTouchHelperAdapter mAdapter;

    public NoteRowItemTouchHelper(NoteRowItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //ToDo Invoke callback to update list_order entries in Array and DataBase
        Log.e(getClass().getSimpleName(), "clearView. onItemMoveComplete");
        mAdapter.onItemMoveComplete();
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        boolean conditions = (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
                && (viewHolder instanceof NoteRowItemTouchHelperViewHolder);

        if (conditions) {
            ((NoteRowItemTouchHelperViewHolder) viewHolder).onItemSelected();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /* Unused from here on out. No swipes for now! */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) { }

}
