package com.werdpressed.partisan.reallyusefulnotes.designlibrary.notehelper;

public interface NoteRowItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemMoveComplete();

}
