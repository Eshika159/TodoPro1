package com.example.eshika.todopro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    private RecyclerView mNoteList;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference fNotesDatabase;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mNoteList=findViewById(R.id.main_notes_list);
        gridLayoutManager= new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        mNoteList.setHasFixedSize(true);
        mNoteList.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        //mNoteList.addItemDecoration(new GridSpacingItemDecoration(2,dpintoPixel(10),true));

        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            fNotesDatabase=FirebaseDatabase.getInstance().getReference().child("notes").child(auth.getCurrentUser().getUid());
        }

            updateUI();
        fetch();
      //  ProcessTextActivity p=new ProcessTextActivity();
        //Toast.makeText(this, p.getData(), Toast.LENGTH_SHORT).show();
    }

    private void fetch() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("notes").child(auth.getCurrentUser().getUid()).orderByChild("timestamp");
        FirebaseRecyclerOptions<NoteModel> options =
                new FirebaseRecyclerOptions.Builder<NoteModel>()
                        .setQuery(query, new SnapshotParser<NoteModel>() {
                            @NonNull
                            @Override
                            public NoteModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new NoteModel(
                                        snapshot.child("Title").getValue().toString(),
                                        snapshot.child("timestamp").getValue().toString()
                                );
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(options) {
            @Override
            public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.singlenote_layout, parent, false);

                return new NoteViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final NoteViewHolder holder, int position, @NonNull final NoteModel model) {
                final String noteid=getRef(position).getKey();
                fNotesDatabase.child(noteid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("Title")&&dataSnapshot.hasChild("Description")) {
                            String title = dataSnapshot.child("Title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();
                            holder.setNoteTitle(model.getNotetitle());
                            // holder.setNotetime(model.getNotetime());
                            GetTimeAgo gAgo = new GetTimeAgo();
                            holder.setNotetime(gAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));
                            holder.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(Main2Activity.this, New_noteActivity.class);
                                    intent.putExtra("noteId", noteid);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                    });



            }

            };
        mNoteList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setNegativeButton("Cancel",null);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateUI() {
        if(auth.getCurrentUser()!=null){
            Log.i("MainActivity2","fauth!=null");
        }
        else{
            //startactivity means login page
            startActivity(new Intent(Main2Activity.this,StartActivity.class));
            finish();
            Log.i("MainActivity2","fauth==null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.new_note_plus:
                Intent intent=new Intent(Main2Activity.this,New_noteActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                auth.signOut();
                //means not logged
                if(auth.getCurrentUser()==null){
                    startActivity(new Intent(Main2Activity.this,StartActivity.class));
                    finish();
                }
                break;
        }
        return true;
    }


    private int dpintoPixel(int dp){
        Resources r=getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));
    }

}
