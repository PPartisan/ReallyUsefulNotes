package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.FilesDatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;

public class NoteRowItemOperationsFragment extends Fragment {

    public static final String TAG = "NoteRowOpsFrag";

    public static final int SORT_BY_CUSTOM = 844;
    public static final int SORT_BY_DATE_ADDED = SORT_BY_CUSTOM + 1;
    public static final int SORT_BY_TITLE = SORT_BY_DATE_ADDED + 1;

    private NoteRowItemOperationsFragmentCallbacks callback;
    private ArrayList<NoteRowItem> mNoteRowItems = new ArrayList<>();

    public static NoteRowItemOperationsFragment newInstance() {
        return new NoteRowItemOperationsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (NoteRowItemOperationsFragmentCallbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        /* Could write to database here.
        Currently delete, add & move happens automatically however.
         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public void sortNoteRowItemsBy(int sortCode) {
        switch (sortCode) {
            case SORT_BY_CUSTOM:
                Collections.sort(mNoteRowItems, NoteRowItem.listOrderComparator);
                break;
            case SORT_BY_DATE_ADDED:
                Collections.sort(mNoteRowItems, NoteRowItem.keyIdComparator);
                break;
            case SORT_BY_TITLE:
                Collections.sort(mNoteRowItems, NoteRowItem.titleComparator);
                break;
            default:
                throw new IllegalArgumentException();
        }
        callback.notifyNoteFragmentAdapterItemOrderChanged();
    }

    public void refreshData(Cursor data) {
        convertCursorToArrayList(data);
    }

    public ArrayList<NoteRowItem> getNoteRowItems(){
        return mNoteRowItems;
    }

    public void addNewEntry(NoteRowItem newItem) {
        mNoteRowItems.add(newItem);
        callback.notifyNoteFragmentAdapterNewItemAdded(mNoteRowItems.indexOf(newItem));
    }

    public void deleteEntry(int position) {
        mNoteRowItems.remove(position);
        callback.notifyNoteFragmentAdapterItemDelete(position);
    }

    public void moveEntry(int fromPosition, int toPosition) {
        Collections.swap(mNoteRowItems, fromPosition, toPosition);
        callback.notifyNoteFragmentAdapterItemMoved(fromPosition, toPosition);
    }

    public void moveEntryCompleteUpdateListOrders() {
        updateListOrder();
    }

    private void updateListOrder() {
        for (int i = 0; i < mNoteRowItems.size(); i++) {
            mNoteRowItems.get(i).setListOrder(i);
        }
        callback.notifyMainActivityListOrdersUpdated();
    }

    private void convertCursorToArrayList(Cursor data) {

        if (!isDataValid(data)) return;

        NoteRowItem item;
        mNoteRowItems = new ArrayList<>(data.getCount());

        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);

            item = new NoteRowItem();

            item.setTitle(data.getString(data.getColumnIndex(FilesDatabaseHelper.TITLE)));
            item.setContent(data.getString(data.getColumnIndex(FilesDatabaseHelper.CONTENT)));
            item.setListOrder(data.getInt(data.getColumnIndex(FilesDatabaseHelper.LIST_ORDER)));
            item.setKeyId(data.getInt(data.getColumnIndex(FilesDatabaseHelper.KEY_ID)));

            mNoteRowItems.add(item);
        }

        data.close();

        callback.notifyNoteFragmentAdapterDataReady(mNoteRowItems);
    }

    private static boolean isDataValid(Cursor data) {
        return ((data != null) && (data.getCount() > 0));
    }

}
