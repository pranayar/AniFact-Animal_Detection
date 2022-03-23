package com.example.imgp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imgp.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

public class cam extends AppCompatActivity {
    String common;
    ImageView imageView;
    Button predict;
    private Bitmap img;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        imageView = findViewById(R.id.imageView);
        predict =findViewById(R.id.predict);
        tv=findViewById(R.id.textView5);
        if (ContextCompat.checkSelfPermission(cam.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(cam.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);


    }


    @Override
    protected void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode,requestCode,data);
        if(requestCode==101)
        {
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            img=bitmap;
        }

    }
        public void onClick(View v) {
            img=Bitmap.createScaledBitmap(img,224, 224,true);

            try {
                Model model = Model.newInstance(getApplicationContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                TensorImage tensorImage=new TensorImage(DataType.UINT8);
                tensorImage.load(img);
                ByteBuffer byteBuffer=tensorImage.getBuffer();
                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                Model.Outputs outputs = model.process(inputFeature0);

                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    /*if(outputFeature0.getFloatArray()[0]>0)
                   tv.setText("wolf");
                    else
                        tv.setText("none");
                    // Releases model resources if no longer used.

                     */
                float f[]=new float[10];
                f[0]=outputFeature0.getFloatArray()[0];
                f[1]=outputFeature0.getFloatArray()[1];
                f[2]=outputFeature0.getFloatArray()[2];
                f[3]=outputFeature0.getFloatArray()[3];
                f[4]=outputFeature0.getFloatArray()[4];
                f[5]=outputFeature0.getFloatArray()[5];
                f[6]=outputFeature0.getFloatArray()[6];
                f[7]=outputFeature0.getFloatArray()[7];
                f[8]=outputFeature0.getFloatArray()[8];
                f[9]=outputFeature0.getFloatArray()[9];

                String[] s={"wolf","elephant","giraffe","deer","camel","cat","cow","kangaroo","dog","pig"};
                int ind=indexOf(f,largest(f));

                common=s[ind];
                tv.setText(common);
                model.close();

            } catch (IOException e) {
                // TODO Handle the exception
            }
        }

    static float largest(float arr[])
    {
        int i;

        // Initialize maximum element
        float max = arr[0];

        // Traverse array elements from second and
        // compare every element with current max
        for (i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];

        return max;
    }
    static int indexOf(float arr[],float max)
    {
        for (int i=0;i<arr.length;i++)
        {
            if(arr[i]==max)
                return i;
        }
        return -1;
    }
}

