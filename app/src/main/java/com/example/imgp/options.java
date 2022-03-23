package com.example.imgp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class options extends AppCompatActivity {
    Button b1,b2,b3;
    //b1=gallery
    //b2=keyword search
    //b3=camera
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        b1 = (Button) findViewById(R.id.button6);
        b2=(Button)findViewById(R.id.button7);
        b3=(Button)findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }

            public void openActivity2() {
              Intent intent = new Intent(options.this,MainActivity.class);
              startActivity(intent);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }

            private void openActivity3()
            {
                Intent intent1 = new Intent(options.this,keyword_act.class);
                startActivity(intent1);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity4();
            }
            private void openActivity4() {
                Intent intent2 = new Intent(options.this,cam.class);
                startActivity(intent2);

            }

        });



    }
}
