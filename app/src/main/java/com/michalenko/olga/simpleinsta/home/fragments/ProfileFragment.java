package com.michalenko.olga.simpleinsta.home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.home.FullscreenActivity;
import com.michalenko.olga.simpleinsta.home.adapters.PhotoAdapter;
import com.michalenko.olga.simpleinsta.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment implements PhotoAdapter.ItemClickListener {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private TextView emailEdit;
    ArrayList<String> listURI;
    private FirebaseAuth mAuth;
    private PhotoAdapter adapter;
    private PhotoAdapter.ItemClickListener listener;
    public ProfileFragment() {
        listener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        listURI.clear();
        getURIS();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listURI = new ArrayList<String>();
        emailEdit = (TextView) getView().findViewById(R.id.textEmail);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) getView().findViewById(R.id.rvProfile);
        emailEdit.setText(mAuth.getCurrentUser().getEmail().toString());
    }

    public void getURIS(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(mAuth.getCurrentUser().getUid().toString());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
            Post post = postSnapshot.getValue(Post.class);
                    if( post != null)
                    listURI.add(post.getImagePath());
                }

                int numberOfColumns = 3;
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
                adapter = new PhotoAdapter(getActivity(), listURI);
                adapter.setClickListener(listener);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Проверьте подключение к интернет", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(getActivity(), FullscreenActivity.class);
        intent.putExtra("path", listURI.get(position));
                startActivity(intent);
    }
}
