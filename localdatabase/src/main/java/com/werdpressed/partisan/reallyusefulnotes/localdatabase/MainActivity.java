package com.werdpressed.partisan.reallyusefulnotes.localdatabase;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks.AddTask;
import com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks.DeleteTask;
import com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks.FilesDatabaseHelper;
import com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks.LoadTask;
import com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks.SaveTask;


public class MainActivity extends AppCompatActivity implements
        LoadTask.LoadComplete, AddTask.AddComplete
{

    private static final String NOTE_TAG = "note_fragment_tag";

    private AddTask at = null;
    private LoadTask lt = null;
    private SaveTask st = null;
    private DeleteTask dt = null;

    private TextView welcomeMessage;

    private Cursor data = null;

    private AlertDialog loadingDialog;

    private NoteFragment mNoteFragment;

    private long keyId;
    private String title, content;

    private CursorStatus cursorStatus = CursorStatus.DEFAULT;

    private enum CursorStatus {
        ADD, DELETE, NO_DATA, DEFAULT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        welcomeMessage = (TextView) findViewById(R.id.ma_welcome);
        welcomeMessage.setVisibility(View.GONE);

        if (data == null) {
            loadingDialog = loadingDialog();
            loadingDialog.show();
            lt = new LoadTask(this);
            lt.execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!data.isClosed()) {
            data.close();
        }
        FilesDatabaseHelper.getInstance(this).close();
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
                cursorStatus = CursorStatus.ADD;
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
            case R.id.action_delete_notes:
                if (!data.moveToFirst()) break;
                cursorStatus = CursorStatus.DELETE;
                deleteDialog().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public Cursor getData(){
        return data;
    }

    @Override
    public void loadComplete(Cursor cursor) {
        data = cursor;

        if(loadingDialog.isShowing()) loadingDialog.dismiss();

        mNoteFragment = (NoteFragment) getFragmentManager().findFragmentById(R.id.note_fragment_container);

        if (!data.moveToFirst()) {
            cursorStatus = CursorStatus.NO_DATA;
            welcomeMessage.setVisibility(View.VISIBLE);
            if (mNoteFragment != null) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(mNoteFragment)
                        .commit();
            }
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        switch (cursorStatus) {
            case ADD:
                if(welcomeMessage.getVisibility() == View.VISIBLE) welcomeMessage.setVisibility(View.GONE);
                if (mNoteFragment != null) {
                    loadNoteFragment(keyId, title, null);
                } else {
                    getFragmentManager()
                            .beginTransaction()
                            .add(R.id.note_fragment_container, NoteFragment.newInstance(keyId, title, content))
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case DEFAULT:
                if (mNoteFragment != null) {
                    getSupportActionBar().setTitle(mNoteFragment.getTitle());
                    break;
                } else {
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
            case DELETE:
                data.moveToFirst();
                keyId = data.getInt(data.getColumnIndex(FilesDatabaseHelper.KEY_ID));
                title = data.getString(data.getColumnIndex(FilesDatabaseHelper.TITLE));
                content = data.getString(data.getColumnIndex(FilesDatabaseHelper.CONTENT));
                loadNoteFragment(keyId, title, content);
                break;
        }
        cursorStatus = CursorStatus.DEFAULT;
    }

    @Override
    public void addComplete(Long newKeyId) {
        keyId = newKeyId;
    }


    private AlertDialog buildAddDialog() {
        return new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(getString(R.string.and_title))
                .setIcon(R.mipmap.ic_launcher)
                .setView(R.layout.add_note_dialog)
                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText newTitle = (EditText) ((AlertDialog) dialog).findViewById(R.id.and_title_entry);
                        title = newTitle.getText().toString();
                        at = new AddTask(MainActivity.this);
                        at.execute(title);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
    }

    private AlertDialog buildViewNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        final ListAdapter listAdapter = new SimpleCursorAdapter(this,
                R.layout.view_notes_row,
                data,
                new String[] {FilesDatabaseHelper.TITLE},
                new int[] {R.id.vnr_text_view},
                0);

        builder.setTitle(getString(R.string.app_name));
        builder.setView(R.layout.view_notes_df);
        builder.setIcon(R.mipmap.ic_launcher);
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

    private AlertDialog deleteDialog(){
        mNoteFragment = (NoteFragment) getFragmentManager().findFragmentById(R.id.note_fragment_container);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        builder.setTitle(R.string.dnd_title);
        builder.setMessage(getString(R.string.dnd_content, mNoteFragment.getTitle()));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dt = new DeleteTask(MainActivity.this, mNoteFragment.getKeyId());
                dt.execute();
            }
        });
        return builder.create();
    }

    private void loadNoteFragment(long keyId, String title, String content) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.note_fragment_container, NoteFragment.newInstance(keyId, title, content))
                .addToBackStack(null)
                .commit();
    }
}