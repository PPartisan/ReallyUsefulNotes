package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.support.v7.widget.PopupMenu;
import android.view.View;

public class PopUpMenuClickListener implements View.OnClickListener {

    private PopupMenu menu;

    PopUpMenuClickListener(PopupMenu menu) {
        this.menu = menu;
    }

    @Override
    public void onClick(View v) {
        menu.show();
    }
}
