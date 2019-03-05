package com.example.eshika.todopro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    Button login;
    EditText email,pass;
    TextView forgot,signup;
    private FirebaseAuth auth;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
        login=(Button)findViewById(R.id.dashboard_newpass);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.dashboard_pass);
        forgot=(TextView)findViewById(R.id.forgot);
        signup=(TextView)findViewById(R.id.signup);
        constraintLayout=(ConstraintLayout)findViewById(R.id.activity_main);
        forgot.setOnClickListener((View.OnClickListener) this);
        signup.setOnClickListener((View.OnClickListener) this);
        login.setOnClickListener((View.OnClickListener) this);

        //init firebase
        auth=FirebaseAuth.getInstance();
        //means already a user is logged in directly open mainactivity
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(StartActivity.this,Main2Activity.class));

        }

        }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.forgot){
            startActivity(new Intent(StartActivity.this,forgotpass_activity.class));
            finish();
        }
        else if(view.getId()==R.id.signup){
            startActivity(new Intent(StartActivity.this,signupActivity.class));
            finish();
        }
        else if(view.getId()==R.id.dashboard_newpass){
            loginUser(email.getText().toString(),pass.getText().toString());
        }

    }

    @Override
    public void onBackPressed() {

            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
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

    private void loginUser(String email, final String passwd) {
        auth.signInWithEmailAndPassword(email,passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful())
                        {
                            if(passwd.length()<0)
                            Toast.makeText(StartActivity.this, "Password length must be 6", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            startActivity(new Intent(StartActivity.this,Main2Activity.class));
                        }
                            
                    }
                });

    }
}
