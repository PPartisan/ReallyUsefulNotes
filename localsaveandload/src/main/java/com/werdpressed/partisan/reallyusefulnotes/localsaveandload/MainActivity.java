package com.werdpressed.partisan.reallyusefulnotes.localsaveandload;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks.AddTask;
import com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks.FilesDatabaseHelper;
import com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks.LoadTask;
import com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks.SaveTask;


public class MainActivity extends AppCompatActivity implements LoadTask.LoadComplete{

    private static final String NOTE_TAG = "note_fragment_tag";

    private AddTask at = null;
    private LoadTask lt = null;
    private SaveTask st = null;

    private Cursor data = null;

    private AlertDialog loadingDialog;

    private NoteFragment mNoteFragment;

    private int keyId;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (data == null) {
            lt = new LoadTask(this);
            lt.execute();
        }

        /*if (getFragmentManager().findFragmentById(R.id.note_fragment_container) == null) {
            mNoteFragment = NoteFragment.newInstance(0, null, null);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.note_fragment_container, mNoteFragment, NOTE_TAG)
                    .commit();
        }*/
        loadingDialog = loadingDialog();
        loadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!data.isClosed()) {
            data.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_note:
                buildAddDialog().show();
                break;
            case R.id.action_save_note:
                mNoteFragment = (NoteFragment) getFragmentManager()
                        .findFragmentById(R.id.note_fragment_container);
                st = new SaveTask(this, mNoteFragment.getKeyId());
                st.execute(mNoteFragment.getCurrentText());
                break;
            case R.id.action_view_notes:
                buildViewNoteDialog().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public Cursor getData(){
        return data;
    }

    @Override
    public void cursorReady(Cursor cursor) {
        data = cursor;
        if (loadingDialog.isShowing()) loadingDialog.dismiss();

        if (getFragmentManager().findFragmentById(R.id.note_fragment_container) == null) {
            data.moveToFirst();
            keyId = data.getInt(data.getColumnIndex(FilesDatabaseHelper.KEY_ID));
            title = data.getString(data.getColumnIndex(FilesDatabaseHelper.TITLE));
            content = data.getString(data.getColumnIndex(FilesDatabaseHelper.CONTENT));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.note_fragment_container, NoteFragment.newInstance(keyId, title, content))
                    .addToBackStack(null)
                    .commit();
        }
    }


    private AlertDialog buildAddDialog() {
        return new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(getString(R.string.and_title))
                .setView(R.layout.add_note_dialog)
                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText newTitle = (EditText) ((AlertDialog) dialog).findViewById(R.id.and_title_entry);
                        at = new AddTask(MainActivity.this);
                        at.execute(newTitle.getText().toString());
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
    }

    private AlertDialog buildViewNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        final ListAdapter listAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                data,
                new String[] {FilesDatabaseHelper.TITLE},
                new int[] {android.R.id.text1},
                0);

        builder.setTitle(getString(R.string.app_name));
        builder.setView(R.layout.view_notes_df);
        builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.moveToPosition(which);
                keyId = data.getInt(data.getColumnIndex(FilesDatabaseHelper.KEY_ID));
                title = data.getString(data.getColumnIndex(FilesDatabaseHelper.TITLE));
                content = data.getString(data.getColumnIndex(FilesDatabaseHelper.CONTENT));
                loadNoteFragment(keyId, title, content);
            }
        });
        builder.setPositiveButton(getString(R.string.cancel), null);

        return builder.create();
    }

    private AlertDialog loadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(getString(R.string.loading));
        return builder.create();
    }

    private void loadNoteFragment(int keyId, String title, String content) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.note_fragment_container, NoteFragment.newInstance(keyId, title, content))
                .addToBackStack(null)
                .commit();
    }
}