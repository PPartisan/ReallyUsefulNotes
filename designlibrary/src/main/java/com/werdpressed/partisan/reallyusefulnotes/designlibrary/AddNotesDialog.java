package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.view.CircularRevealLinearLayout;

import java.lang.ref.WeakReference;

public class AddNotesDialog implements DialogInterface.OnClickListener{

    private WeakReference<Context> mContext;

    public AddNotesDialog(Context ctx) {
        mContext = new WeakReference<>(ctx);
    }

    public AlertDialog create(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get(), R.style.AlertDialogStyle);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.and_title);
        builder.setView(R.layout.add_note_dialog);
        builder.setPositiveButton(R.string.accept, this);
        builder.setNegativeButton(R.string.cancel, null);
        return builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText titleField = (EditText)((AlertDialog)dialog).findViewById(R.id.and_title_entry);
        String title = titleField.getText().toString();
        ((AddNotesDialogCallbacks)mContext.get()).addNewNote(title);
    }
}
