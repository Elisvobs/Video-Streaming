package com.byskopo.byskopoyako;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("video");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Video, RecyclerViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Video, RecyclerViewHolder>(
                        Video.class,
                        R.layout.row,
                        RecyclerViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(RecyclerViewHolder viewHolder, Video video, int i) {
                        viewHolder.setVideo(
                                getApplication(),
                                video.getTitle(),
//                                video.getYear(),
//                                video.getTime(),
                                video.getUrl()
                        );
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}