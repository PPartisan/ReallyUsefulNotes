package com.werdpressed.partisan.usefultexteditor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

public class NoteFragment extends Fragment {

    View rootView;
    ScrollView mScrollView;
    EditText mEditText;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.note_fragment, container, false);

        mScrollView = (ScrollView) rootView.findViewById(R.id.note_fragment_parent);

        mEditText = (EditText) rootView.findViewById(R.id.note_fragment_edit_text);

        return rootView;
    }
}
