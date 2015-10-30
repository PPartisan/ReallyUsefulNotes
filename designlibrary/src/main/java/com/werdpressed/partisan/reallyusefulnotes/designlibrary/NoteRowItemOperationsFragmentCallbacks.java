package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import java.util.ArrayList;

public interface NoteRowItemOperationsFragmentCallbacks {

    void notifyNoteFragmentAdapterDataReady(ArrayList<NoteRowItem> items);

    void notifyNoteFragmentAdapterNewItemAdded(int newItemPosition);
    void notifyNoteFragmentAdapterItemDelete(int position);
    void notifyNoteFragmentAdapterItemMoved(int fromPosition, int toPosition);
    void notifyNoteFragmentAdapterItemOrderChanged();

    void notifyMainActivityListOrdersUpdated();

}
