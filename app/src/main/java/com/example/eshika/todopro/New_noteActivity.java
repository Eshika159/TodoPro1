package com.example.eshika.todopro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class New_noteActivity extends AppCompatActivity {
    private Button createnote;
    private EditText title,desc;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private Menu mainMenu;
    private String noteid="no";
    private boolean isExist;
    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        try{
            noteid=getIntent().getStringExtra("noteId");
           if(!noteid.trim().equals(""))
               isExist=true;
           else
               isExist=false;

            if(noteid.equals("no")){
            mainMenu.getItem(0).setVisible(false);
            isExist=false;}
            else{
            isExist=true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        createnote=(Button)findViewById(R.id.createnote_btn);
        title=(EditText)findViewById(R.id.title);
        desc=(EditText)findViewById(R.id.description);

       //Log.i("----------------Copy",copy);
        //Toast.makeText(this, copy, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("notes").child(auth.getCurrentUser().getUid());

        createnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit=title.getText().toString().trim();
                String content=desc.getText().toString().trim();
                // Toast.makeText(New_noteActivity.this,"Eshika", Toast.LENGTH_SHORT).show();
             // desc.setText(p.getData());

                if(!TextUtils.isEmpty(tit)&&!TextUtils.isEmpty(content)){
                    addNote(tit,content);
                }
                else {
                 //   String copy= getDefaults("copy",getApplicationContext());
                   // if(copy.isEmpty()){
                        Toast.makeText(New_noteActivity.this, "Fill empty Fields", Toast.LENGTH_SHORT).show();
                    }
                   /* else {

                        desc.setText(copy);
                        Toast.makeText(New_noteActivity.this, "Enter title for copied text", Toast.LENGTH_SHORT).show();
                        String txt=title.getText().toString().trim();
                        if(txt.isEmpty()){
                            Toast.makeText(New_noteActivity.this, "Sorry you didnt enter title", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            addNote(txt,copy);
                            Toast.makeText(New_noteActivity.this, "Copied text=" + copy, Toast.LENGTH_SHORT).show();

                        }
                       }*/



                //}
                }

        });
        putData();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
    private void putData() {
        if (isExist) {

            databaseReference.child(noteid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("Title")&&dataSnapshot.hasChild("Description")) {
                        String tit = dataSnapshot.child("Title").getValue().toString();
                        String content = dataSnapshot.child("Description").getValue().toString();
                        title.setText(tit);
                        desc.setText(content);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
             x=data.getStringExtra("key");

        }
    }

    private void addNote(String tit, String content) {
        if(auth.getCurrentUser()!=null) {
            if (isExist) {
                Map updateMap=new HashMap();
                updateMap.put("Title",title.getText().toString().trim());
                updateMap.put("Description",desc.getText().toString().trim());
                updateMap.put("timestamp",ServerValue.TIMESTAMP);
                databaseReference.child(noteid).updateChildren(updateMap);
                startActivity(new Intent(New_noteActivity.this,Main2Activity.class));
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                //update a note
            } else {
                //create a note
                final DatabaseReference db = databaseReference.push();
                final Map notemap = new HashMap();
                notemap.put("Title", tit);
                notemap.put("Description", content);
                notemap.put("timestamp", ServerValue.TIMESTAMP);
                Thread mainthread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.setValue(notemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(New_noteActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(New_noteActivity.this,Main2Activity.class));
                                }else {
                                    Toast.makeText(New_noteActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                        });
                    }
                });
                mainthread.start();

            }
        }
        else{
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
   getMenuInflater().inflate(R.menu.new_note_menu,menu);
       // mainMenu=menu;

    return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         switch(item.getItemId()){
             case android.R.id.home:
                 finish();
                 break;

             case R.id.new_note_delbuttn:
                 if(isExist){
                     deleteNote();
                 }
                 else{
                     Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();}
                 break;
         }
    return true;
    }

    private void deleteNote(){
        databaseReference.child(noteid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(New_noteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                noteid="no";
                    finish();
                }
                else{
                    Log.e("New_noteActivity",task.getException().toString());
                    Toast.makeText(New_noteActivity.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
