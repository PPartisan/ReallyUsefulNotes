package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.AddTask;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.DeleteTask;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.LoadCursorFragment;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.LoadCursorFragmentCallbacks;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LoadCursorFragmentCallbacks, NoteRowItemOperationsFragmentCallbacks, AddNotesDialogCallbacks, NoteFragmentCallbacks {

    private static final String TAG = "MainActivity";
    private static final int NOTE_CONTAINER_ID = (R.id.note_fragment_container);

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;

    private NoteFragment mNoteFragment;
    private AddNotesDialog addNotesDialog;

    private NoteRowItemOperationsFragment mNoteRowItemOperationsFragment;
    private LoadCursorFragment mLoadCursorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindActivity();

        setSupportActionBar(mToolbar);
        mFloatingActionButton.setOnClickListener(this);

        addNoteFragment();

        addLoadCursorFragment();
        addNoteRowItemOperationsFragment();
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab:
                if (addNotesDialog == null) {
                    addNotesDialog = new AddNotesDialog(this);
                }
                addNotesDialog.create();
                break;
        }
    }

    private void addNoteFragment() {
        mNoteFragment = (NoteFragment) getSupportFragmentManager()
                .findFragmentByTag(NoteFragment.TAG);
        if (mNoteFragment == null) {
            mNoteFragment = NoteFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(NOTE_CONTAINER_ID, mNoteFragment, NoteFragment.TAG)
                    .commit();
        }
    }

    private void addLoadCursorFragment() {
        mLoadCursorFragment = (LoadCursorFragment)getSupportFragmentManager()
                        .findFragmentByTag(LoadCursorFragment.TAG);
        if (mLoadCursorFragment == null) {
            mLoadCursorFragment = LoadCursorFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(mLoadCursorFragment, LoadCursorFragment.TAG)
                    .commit();
        }

    }

    private void removeLoadCursorFragment() {
        if (mLoadCursorFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mLoadCursorFragment)
                    .commit();
        }
    }

    private void addNoteRowItemOperationsFragment() {
        mNoteRowItemOperationsFragment = (NoteRowItemOperationsFragment) getSupportFragmentManager()
                        .findFragmentByTag(NoteRowItemOperationsFragment.TAG);
        if (mNoteRowItemOperationsFragment == null) {
            mNoteRowItemOperationsFragment = NoteRowItemOperationsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(mNoteRowItemOperationsFragment, NoteRowItemOperationsFragment.TAG)
                    .commit();
        }
    }

    private void determineWelcomeMessageVisibility() {
        determineWelcomeMessageVisibility(mNoteFragment.getAdapter().getItemCount());
    }

    private void determineWelcomeMessageVisibility(int dataSize) {
        if (dataSize < 1) {
            if (mNoteFragment.getWelcomeMessageVisibility() == View.GONE) {
                mNoteFragment.setWelcomeMessageVisibility(View.VISIBLE);
            }
        } else {
            if (mNoteFragment.getWelcomeMessageVisibility() == View.VISIBLE) {
                mNoteFragment.setWelcomeMessageVisibility(View.GONE);
            }
        }
    }

    @Override
    public void sendDataToNoteRowItemsOperationsFragment(Cursor data) {
        mNoteRowItemOperationsFragment.refreshData(data);
        determineWelcomeMessageVisibility();
    }

    @Override
    public void notifyNoteFragmentAdapterDataReady(ArrayList<NoteRowItem> items) {
        mNoteFragment.getAdapter().updateAllData(items);
        //removeLoadCursorFragment(); <-- Perhaps only call this if refreshing data?
    }

    @Override
    public void notifyNoteFragmentAdapterNewItemAdded(int newItemPosition) {
        mNoteFragment.getAdapter().addNewEntry(newItemPosition);
    }

    @Override
    public void notifyNoteFragmentAdapterItemDelete(int position) {
        mNoteFragment.getAdapter().deleteEntry(position);
    }

    @Override
    public void notifyNoteFragmentAdapterItemMoved(int fromPosition, int toPosition) {
        mNoteFragment.getAdapter().moveEntry(fromPosition, toPosition);
    }

    @Override
    public void addNewNote(String title) {

        NoteRowItem newItem = new NoteRowItem();
        newItem.setTitle(title);

        new AddTask(this).execute(newItem);
        mNoteRowItemOperationsFragment.addNewEntry(newItem);

        determineWelcomeMessageVisibility();
    }

    @Override
    public ArrayList<NoteRowItem> requestNotes() {
        ArrayList<NoteRowItem> items = mNoteRowItemOperationsFragment.getNoteRowItems();
        determineWelcomeMessageVisibility(items.size());
        return items;
    }

    @Override
    public void deleteNote(int position) {

        new DeleteTask(this, mNoteRowItemOperationsFragment.getNoteRowItems().get(position).getKeyId()).execute();
        mNoteRowItemOperationsFragment.deleteEntry(position);
        determineWelcomeMessageVisibility();

    }

    @Override
    public void moveNote(int fromPosition, int toPosition) {

        mNoteRowItemOperationsFragment.moveEntry(fromPosition, toPosition);
        //ToDo Update DB list_order data to reflect move

    }
}