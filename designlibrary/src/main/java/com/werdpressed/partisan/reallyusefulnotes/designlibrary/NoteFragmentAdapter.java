package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.notehelper.NoteRowItemTouchHelperAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

public class NoteFragmentAdapter extends RecyclerView.Adapter<NoteFragmentAdapter.ViewHolder>
        implements NoteRowItemTouchHelperAdapter{

    private WeakReference<NoteFragmentCallbacks> mWeakCallback;
    private ArrayList<NoteRowItem> mItems;

    public NoteFragmentAdapter(NoteFragmentCallbacks callback) {
        mWeakCallback = new WeakReference<>(callback);
        mItems = mWeakCallback.get().requestNotes();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nf_recycler_view_row, parent, false);
        return new ViewHolder(v, mWeakCallback.get());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.title.setText(mItems.get(position).getTitle());
        holder.menu.setOnClickListener(new PopUpMenuClickListener(holder.popup));

    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    public void updateAllData(ArrayList<NoteRowItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addNewEntry(int newItemPosition) {
        notifyItemInserted(newItemPosition);
    }

    public void deleteEntry(int itemPosition) {
        notifyItemRemoved(itemPosition);
    }

    public void moveEntry(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++ ) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        mWeakCallback.get().moveNote(fromPosition, toPosition);
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{

        private static final String TAG = "ViewHolder";

        private NoteFragmentCallbacks mCallback;

        private ImageButton menu;
        private TextView title;
        private PopupMenu popup;

        public ViewHolder(View itemView, NoteFragmentCallbacks callback) {
            super(itemView);

            mCallback = callback;

            title = (TextView) itemView.findViewById(R.id.nf_rv_title);
            menu = (ImageButton) itemView.findViewById(R.id.nf_rv_overflow_menu);

            popup = new PopupMenu(itemView.getContext(), menu, Gravity.END);
            popup.getMenuInflater().inflate(R.menu.nf_rvr_overflow_popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            switch (id) {
                case R.id.row_action_edit:
                    Log.e(getClass().getSimpleName(), "edit: position " + getAdapterPosition());
                    break;
                case R.id.row_action_delete:
                    mCallback.deleteNote(getAdapterPosition()); //May want to set keyId here too?
                    break;
            }
            return false;
        }
    }

}
