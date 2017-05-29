package com.example.c309660.cn_tema6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    public EditText pValue;
    public EditText nValue;
    public String pString;
    public String nString;
    Intent k = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pValue = (EditText) findViewById(R.id.pValue);
        nValue = (EditText) findViewById(R.id.nValue);
    }

    public void startProgram(View view) {
        try {
            pString = pValue.getText().toString();
            nString = nValue.getText().toString();
            Variabile.p = Integer.parseInt(pString);
            Variabile.n = Integer.parseInt(nString);

            if(Variabile.p == Variabile.n) {
               k = new Intent(this, Problema1.class);
            }
            else if(Variabile.p > Variabile.n){
                k = new Intent(this, Problema2.class);
            }
            startActivity(k);
        }
        catch(Exception e){
            Toast errorMessage = Toast.makeText(this, "A intervenit o eroare.", Toast.LENGTH_LONG);
        }
    }

}
