package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.notehelper.NoteRowItemTouchHelper;

public class AllNotesFragment extends Fragment {

    public static final String TAG = "AllNotesFragment";

    private AllNotesFragmentCallbacks mCallbacks;

    private TextView welcomeMessage;

    private AllNotesFragmentRecyclerView mRecyclerView;
    private AllNotesFragmentAdapter mAdapter;

    public static AllNotesFragment newInstance() {
        return new AllNotesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (AllNotesFragmentCallbacks) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_notes_fragment, container, false);

        welcomeMessage = (TextView) rootView.findViewById(R.id.anf_welcome);

        mRecyclerView = (AllNotesFragmentRecyclerView) rootView.findViewById(R.id.anf_recycler_view);
        mAdapter = new AllNotesFragmentAdapter(mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
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

    public AllNotesFragmentAdapter getAdapter() {
        return mAdapter;
    }

    public AllNotesFragmentRecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
