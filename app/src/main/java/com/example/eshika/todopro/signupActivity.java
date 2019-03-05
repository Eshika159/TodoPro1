package com.example.eshika.todopro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {
Button signup;
TextView login,forgetpass;
EditText inputemail,inputpass;
private FirebaseAuth auth;
private DatabaseReference fuserdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=(Button)findViewById(R.id.register_button);
         login=(TextView)findViewById(R.id.login_user);
        forgetpass=(TextView)findViewById(R.id.forgot);
        inputemail=(EditText)findViewById(R.id.email_signup);
        inputpass=(EditText)findViewById(R.id.pass_signup);
signup.setOnClickListener(this);
login.setOnClickListener(this);
forgetpass.setOnClickListener(this);
auth=FirebaseAuth.getInstance();
fuserdb=FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_user)
        {
         startActivity(new Intent(signupActivity.this,StartActivity.class));
         finish();
        }
        else if(view.getId()==R.id.forgot)
        {
            startActivity(new Intent(signupActivity.this,forgotpass_activity.class));
            finish();
        }
        else if(view.getId()==R.id.register_button)
        {
            signupUser(inputemail.getText().toString(),inputpass.getText().toString());
            //startactivity(new Intent(signupActivity.this,forgotpass_activity.class));
            //finish();
        }
    }

    private void signupUser(final String email, String pass) {
auth.createUserWithEmailAndPassword(email,pass)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    fuserdb.child(auth.getCurrentUser().getUid()).child("basic").child("name").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){

                       }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Some error occured .Sorry Try Again Later" + task.getException(), Toast.LENGTH_SHORT).show();
                }
                    else
                    Toast.makeText(getApplicationContext(),"You are succesfully registered",Toast.LENGTH_SHORT).show();


            }
        });
    }

}
