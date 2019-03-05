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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard_activity extends AppCompatActivity implements View.OnClickListener {
   private EditText newpass;
    private Button changepassbtn,logoutbtn;
    private FirebaseAuth auth;
    private TextView txtwel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_activity);
        newpass=(EditText)findViewById(R.id.dashboard_pass);
        changepassbtn=(Button)findViewById(R.id.dashboard_newpass);
        logoutbtn=(Button)findViewById(R.id.dashboard_logout);
        txtwel=(TextView)findViewById(R.id.textWelcome);
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
            txtwel.setText("Welcome"+auth.getCurrentUser().getEmail());

        changepassbtn.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.dashboard_newpass){
            changePasswd(newpass.getText().toString());
        }
        else if(view.getId()==R.id.dashboard_logout){
            logout();
        }
    }

    private void logout() {
        auth.signOut();
        //means not logged
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(Dashboard_activity.this,StartActivity.class));
            finish();
        }
    }

    private void changePasswd(String newpass) {
        FirebaseUser user=auth.getCurrentUser();
        user.updatePassword(newpass).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
