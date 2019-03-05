package com.example.eshika.todopro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ProcessTextActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
      //  String s=getData();
        CharSequence text=getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
       Toast.makeText(this, text, Toast.LENGTH_LONG).show();
setDefaults("copy",text.toString(),getApplicationContext());

        boolean readonly=getIntent()
                .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY,false);

       /* Intent ret=new Intent();
        ret.putExtra("Key",s);
        setResult(100,ret);
        finish();*/


       }
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


}
