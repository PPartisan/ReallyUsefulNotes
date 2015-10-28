package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.notehelper.NoteRowItemTouchHelper;

public class NoteFragment extends Fragment {

    public static final String TAG = "AllNotesFragment";

    private NoteFragmentCallbacks mCallbacks;

    private TextView welcomeMessage;

    private RecyclerView mRecyclerView;
    private NoteFragmentAdapter mAdapter;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (NoteFragmentCallbacks) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_notes_fragment, container, false);

        welcomeMessage = (TextView) rootView.findViewById(R.id.anf_welcome);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.anf_recycler_view);
        mAdapter = new NoteFragmentAdapter(mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback = new NoteRowItemTouchHelper(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void setWelcomeMessageVisibility(int visibility) {
        welcomeMessage.setVisibility(visibility);
    }

    public int getWelcomeMessageVisibility() {
        return welcomeMessage.getVisibility();
    }

    public NoteFragmentAdapter getAdapter() {
        return mAdapter;
    }

}
