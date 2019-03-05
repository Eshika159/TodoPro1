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

public class forgotpass_activity extends AppCompatActivity implements View.OnClickListener {
    private EditText input_email;
    private Button reset;
    private TextView back_btn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass_activity);
        input_email=(EditText)findViewById(R.id.forgot_email);
        reset=(Button)findViewById(R.id.forgot_reset_btn);
        back_btn=(TextView)findViewById(R.id.textBack_btn);

        reset.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textBack_btn){
            startActivity(new Intent(this,StartActivity.class));
            finish();
            }
            else if(view.getId()==R.id.forgot_reset_btn){
            resetPassword(input_email.getText().toString());
        }
    }

    private void resetPassword(final String s) {
        auth.sendPasswordResetEmail(s).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           
                if(task.isSuccessful())
                    Toast.makeText(forgotpass_activity.this, "We have sent password to email "+s, Toast.LENGTH_SHORT).show();
            else
                    Toast.makeText(forgotpass_activity.this, "Unable to sent email", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
