package com.example.eshika.todopro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.widget.ImageView;
import android.widget.TextView;

import static android.support.v4.content.ContextCompat.startActivity;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    public FingerprintHandler(Context context){

        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal , 0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an authentication error" + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authentication failed",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error: " + helpString,false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("You can now access the app", true);
        context.startActivity(new Intent(context,StartActivity.class));





    }
    private void update(String s, boolean b){
        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView)((Activity)context).findViewById(R.id.fingerprintimage);
        paraLabel.setText(s);

        if(b==false){
            paraLabel.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else{
            paraLabel.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            imageView.setImageResource(R.mipmap.done);

        }
    }
}