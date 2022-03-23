package com.example.imgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnimalInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_information);
        Intent intent = getIntent();
        String commonName = intent.getStringExtra("Common Name");
        commonName = commonName.toLowerCase();
        TextView cn = (TextView)findViewById(R.id.aName);
        TextView sn = (TextView)findViewById(R.id.scientificName);
        TextView fd = (TextView)findViewById(R.id.food);
        TextView ltion = (TextView)findViewById(R.id.location);

        DocumentReference docref = FirebaseFirestore.getInstance().collection("animals").document(commonName);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override

            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){


                        String common = (String) doc.get("Common_Name");
                        String scientific = (String)doc.get("Scientific_Name");
                        String foodEaten = (String) doc.get("Food");
                        String locationFound = (String)doc.get("Location");
 
                        cn.setText(common);
                        sn.setText(scientific);
                        fd.setText(foodEaten);
                        ltion.setText(locationFound);

                    }
                    else{
                        String t = "No data";

                        Log.d("Error:","No data");
                    }

                }



            }
        });
    }
}