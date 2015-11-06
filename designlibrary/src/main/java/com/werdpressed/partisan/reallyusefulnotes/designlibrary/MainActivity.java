package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.AddTask;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.DeleteTask;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.FilesDatabaseHelper;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.LoadCursorFragment;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.LoadCursorFragmentCallbacks;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.TaskCallbacks;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks.UpdateListOrderTask;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LoadCursorFragmentCallbacks, NoteRowItemOperationsFragmentCallbacks, AddNotesDialogCallbacks, AllNotesFragmentCallbacks, TaskCallbacks {

    private static final String TAG = "MainActivity";
    private static final int NOTE_CONTAINER_ID = (R.id.note_fragment_container);

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private ImageView mHeaderImageView;

    private AllNotesFragment mAllNotesFragment;
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
        //setHeaderImageDrawable();

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
            case R.id.action_sort_by_custom:
                mNoteRowItemOperationsFragment
                        .sortNoteRowItemsBy(NoteRowItemOperationsFragment.SORT_BY_CUSTOM);
                break;
            case R.id.action_sort_by_date_added:
                mNoteRowItemOperationsFragment
                        .sortNoteRowItemsBy(NoteRowItemOperationsFragment.SORT_BY_DATE_ADDED);
                break;
            case R.id.action_sort_by_title:
                mNoteRowItemOperationsFragment
                        .sortNoteRowItemsBy(NoteRowItemOperationsFragment.SORT_BY_TITLE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mHeaderImageView = (ImageView) findViewById(R.id.header_image);
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

    @SuppressWarnings("deprecated")
    private void setHeaderImageDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderImageView.setImageDrawable(getDrawable(R.drawable.header_image));
        } else {
            mHeaderImageView.setImageDrawable(getResources().getDrawable(R.drawable.header_image));
        }
    }

    private void addNoteFragment() {
        mAllNotesFragment = (AllNotesFragment) getSupportFragmentManager()
                .findFragmentByTag(AllNotesFragment.TAG);
        if (mAllNotesFragment == null) {
            mAllNotesFragment = AllNotesFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(NOTE_CONTAINER_ID, mAllNotesFragment, AllNotesFragment.TAG)
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
        determineWelcomeMessageVisibility(mAllNotesFragment.getAdapter().getItemCount());
    }

    private void determineWelcomeMessageVisibility(int dataSize) {
        if (dataSize < 1) {
            if (mAllNotesFragment.getWelcomeMessageVisibility() == View.GONE) {
                mAllNotesFragment.setWelcomeMessageVisibility(View.VISIBLE);
            }
        } else {
            if (mAllNotesFragment.getWelcomeMessageVisibility() == View.VISIBLE) {
                mAllNotesFragment.setWelcomeMessageVisibility(View.GONE);
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
        mAllNotesFragment.getAdapter().updateAllData(items);
        //removeLoadCursorFragment(); <-- Perhaps only call this if refreshing data?
    }

    @Override
    public void notifyNoteFragmentAdapterNewItemAdded(int newItemPosition) {
        mAllNotesFragment.getAdapter().addNewEntry(newItemPosition);
    }

    @Override
    public void notifyNoteFragmentAdapterItemDelete(int position) {
        mAllNotesFragment.getAdapter().deleteEntry(position);
    }

    @Override
    public void notifyNoteFragmentAdapterItemMoved(int fromPosition, int toPosition) {
        mAllNotesFragment.getAdapter().moveEntry(fromPosition, toPosition);
    }

    @Override
    public void notifyNoteFragmentAdapterItemOrderChanged() {
        mAllNotesFragment.getRecyclerView().updateListOrder();
    }

    @Override
    public void notifyMainActivityListOrdersUpdated() {
        //noinspection unchecked
        new UpdateListOrderTask(FilesDatabaseHelper.getInstance(this))
                .execute(mNoteRowItemOperationsFragment.getNoteRowItems());
    }

    @Override
    public void addNewNote(String title) {
        NoteRowItem newItem = new NoteRowItem();
        newItem.setTitle(title);

        new AddTask(this).execute(newItem);
    }

    @Override
    public ArrayList<NoteRowItem> requestNotes() {
        ArrayList<NoteRowItem> items = mNoteRowItemOperationsFragment.getNoteRowItems();
        determineWelcomeMessageVisibility(items.size());
        return items;
    }

    @Override
    public void deleteNote(int position) {
        long keyId = mNoteRowItemOperationsFragment.getNoteRowItems().get(position).getKeyId();
        new DeleteTask(this, keyId).execute();
        mNoteRowItemOperationsFragment.deleteEntry(position);
        determineWelcomeMessageVisibility();
    }

    @Override
    public void moveNote(int fromPosition, int toPosition) {
        mNoteRowItemOperationsFragment.moveEntry(fromPosition, toPosition);
    }

    @Override
    public void updateNoteListOrders() {
        mNoteRowItemOperationsFragment.moveEntryCompleteUpdateListOrders();
    }

    @Override
    public void addNewNoteToNoteRowItems(NoteRowItem item) {
        mNoteRowItemOperationsFragment.addNewEntry(item);
        determineWelcomeMessageVisibility();
    }
}