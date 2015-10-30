package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.R;

import java.lang.ref.WeakReference;

public class LoadCursorFragment extends Fragment {

    public static final String TAG = "LoadCursorFragment";

    private LoadCursorFragmentCallbacks callbacks;

    public static LoadCursorFragment newInstance() {
        return new LoadCursorFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (LoadCursorFragmentCallbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new LoadTask(getContext()).execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    private class LoadTask extends AsyncTask<Void, Void, Cursor> {

        //private static final String TAG = "LoadTask";

        private WeakReference<Context> weakReference;
        private FilesDatabaseHelper db;
        private AlertDialog dialog;

        public LoadTask(Context context) {
            weakReference = new WeakReference<>(context);
            db = FilesDatabaseHelper.getInstance(weakReference.get());
            buildDialog();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor result = db.getReadableDatabase().query(
                    FilesDatabaseHelper.TABLE,
                    null, null, null, null, null, FilesDatabaseHelper.TITLE);
            result.getCount();
            return result;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            dialog.dismiss();
            callbacks.sendDataToNoteRowItemsOperationsFragment(cursor);
        }

        private void buildDialog() {
            dialog = new AlertDialog.Builder(weakReference.get(), R.style.AlertDialogStyle)
                    .setTitle(weakReference.get().getString(R.string.app_name))
                    .setMessage(weakReference.get().getString(R.string.loading))
                    .setPositiveButton(weakReference.get().getString(R.string.cancel), null)
                    .create();
        }
    }
}
