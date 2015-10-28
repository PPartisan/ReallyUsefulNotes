package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;


public class PopUpMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "PopUpMenuItemClickList";

    private int position;
    private NoteFragmentCallbacks mCallback;

    PopUpMenuItemClickListener(int position, NoteFragmentCallbacks callbacks) {
        this.position = position;
        mCallback = callbacks;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.row_action_edit:
                Log.e(getClass().getSimpleName(), "edit: position " + position);
                break;
            case R.id.row_action_delete:
                Log.e(TAG, "position is " + position);
                mCallback.deleteNote(position);
                break;
        }

        return false;
    }
}
