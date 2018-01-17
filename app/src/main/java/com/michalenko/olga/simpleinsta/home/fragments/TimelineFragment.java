package com.michalenko.olga.simpleinsta.home.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.home.adapters.TimeLineAdapter;
import com.michalenko.olga.simpleinsta.model.Post;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    ArrayList<Post> listURI;
    private TimeLineAdapter adapter;

    public TimelineFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listURI = new ArrayList<Post>();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) getView().findViewById(R.id.rvTimeLine);
    }

    @Override
    public void onResume() {
        super.onResume();
        listURI.clear();
        getPost();
    }

    public void getPost() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child("postList");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null)
                        listURI.add(post);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new TimeLineAdapter(getActivity(), listURI);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Проверьте подключение к интернет", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
