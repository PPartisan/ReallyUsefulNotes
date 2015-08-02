package com.werdpressed.partisan.reallyusefulnotes.localdatabase;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

public class NoteFragment extends Fragment {

    private static final String KEY_ID = "key_id";
    private static final String TITLE_ID = "title_id";
    private static final String CONTENT_ID = "content_id";

    private View rootView;
    private ScrollView mScrollView;
    private EditText mEditText;


    public static NoteFragment newInstance(long keyId, String title, String content) {
        NoteFragment frag = new NoteFragment();
        Bundle args = new Bundle();

        args.putLong(KEY_ID, keyId);
        args.putString(TITLE_ID, title);
        args.putString(CONTENT_ID, content);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (getTitle() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.note_fragment, container, false);

        mScrollView = (ScrollView) rootView.findViewById(R.id.note_fragment_parent);

        mEditText = (EditText) rootView.findViewById(R.id.note_fragment_edit_text);
        mEditText.setText(getContent());

        return rootView;
    }

    public long getKeyId(){
        return getArguments().getLong(KEY_ID);
    }

    public String getTitle() {
        return getArguments().getString(TITLE_ID);
    }

    private String getContent(){
        return getArguments().getString(CONTENT_ID);
    }

    public String getCurrentText() {
        return mEditText.getText().toString();
    }

    public void setContent(String newText) {
        mEditText.setText(newText);
    }

    public void setTitle(String newTitle) {
        if (getTitle() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        }
    }

}
