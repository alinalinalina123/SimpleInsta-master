package com.michalenko.olga.simpleinsta.home.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.model.Post;

import java.util.ArrayList;
import java.util.Collections;

public class TimeLineAdapter  extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    private ArrayList<Post> mData = new ArrayList<Post>();
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    DatabaseReference myRef ;



    public TimeLineAdapter(Context context, ArrayList<Post> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        Collections.reverse(data);
        this.mData = data;

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("images");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mData.get(position);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorageRef.child(post.getImagePath()))
                .into(holder.image);
        holder.comment.setText(post.getComment());
        holder.owner.setText(post.getOwner());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView image;
        TextView owner;
        TextView comment;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.photoTimeline);
            owner = (TextView) itemView.findViewById(R.id.owner);
            comment = (TextView) itemView.findViewById(R.id.comment);

        }


    }



}