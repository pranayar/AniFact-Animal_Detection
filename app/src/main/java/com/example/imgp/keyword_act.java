package com.example.imgp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class keyword_act extends AppCompatActivity {
    Button searchButton;
    EditText searchText;
   String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        searchButton = (Button)findViewById(R.id.search);
        MainActivity s = new MainActivity();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = (EditText)findViewById(R.id.searchAnimal);
                String mySearch = String.valueOf(searchText.getText());

                DocumentReference docref = FirebaseFirestore.getInstance().collection("animals").document(mySearch);
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

                                Intent intent = new Intent(getApplicationContext(),SearchedAnimal.class);
                                intent.putExtra("Common Name",common);
                                intent.putExtra("Scientific Name",scientific);
                                intent.putExtra("Food",foodEaten);
                                intent.putExtra("Location",locationFound);

                                startActivity(intent);
                            }
                            else{
                                String t = "No data";

                                Log.d("Error:","No data");
                            }

                        }



                    }
                });
            }

        });
    }

}