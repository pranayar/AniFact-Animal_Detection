package com.example.imgp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imgp.ml.MobilenetAnimals;
import com.example.imgp.ml.Model;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Button select, predict,info;
    private ImageView imageView ;
    int SELECT_IMAGE_CODE = 1;
    private TextView tv;
    private Bitmap img;
    String common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.pickedImage);
        tv = (TextView) findViewById((R.id.textView));
        predict = (Button) findViewById(R.id.button2);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

        info = (Button)findViewById(R.id.info);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AnimalInformation.class);
                intent.putExtra("Common Name",common);
                startActivity(intent);
            }
        });



        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img=Bitmap.createScaledBitmap(img,72, 72,true);

//                try {
//                    Model model = Model.newInstance(getApplicationContext());
//
//                    // Creates inputs for reference.
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 144, 144, 3}, DataType.UINT8);
//                    TensorImage tensorImage=new TensorImage(DataType.UINT8);
//                    tensorImage.load(img);
//                    ByteBuffer byteBuffer=tensorImage.getBuffer();
//                    inputFeature0.loadBuffer(byteBuffer);
//
//                    // Runs model inference and gets result.
//                    Model.Outputs outputs = model.process(inputFeature0);
//
//                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    /*if(outputFeature0.getFloatArray()[0]>0)
//                   tv.setText("wolf");
//                    else
//                        tv.setText("none");
//                    // Releases model resources if no longer used.
//
//                     */
//                    float f[]=new float[9];
//                    f[0]=outputFeature0.getFloatArray()[0];
//                    f[1]=outputFeature0.getFloatArray()[1];
//                    f[2]=outputFeature0.getFloatArray()[2];
//                    f[3]=outputFeature0.getFloatArray()[3];
//                    f[4]=outputFeature0.getFloatArray()[4];
//                    f[5]=outputFeature0.getFloatArray()[5];
//                    f[6]=outputFeature0.getFloatArray()[6];
//                    f[7]=outputFeature0.getFloatArray()[7];
//                    f[8]=outputFeature0.getFloatArray()[8];
//                    //f[9]=outputFeature0.getFloatArray()[9];
//                    Log.i("F is ",Arrays.toString(f));
//                    String[] s={"Bull","Butterfly","Cats","Chicken","Dogs","Elephant","Horse","Sheep","Squirrel"};
//                    //animal_names = {'Bull': 0,'Butterfly': 1,'Cats': 2,'Chicken': 3,'Dogs': 4,'Elephant': 5,'Horse': 6,'Sheep': 7,'Squirrel': 8}
//                    // String[] s={"wolf","elephant","giraffe","deer","camel","cat","cow","kangaroo","dog","pig"};
//               int ind=indexOf(f,largest(f));
//
//               common=s[ind];
//               tv.setText(common);
//                    model.close();
//
//                } catch (IOException e) {
//                    // TODO Handle the exception
//                }
                try {
                    MobilenetAnimals model = MobilenetAnimals.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 72, 72, 3}, DataType.FLOAT32);
                    ByteBuffer byteBuffer= ByteBuffer.allocateDirect(4*72*72*3);
                    byteBuffer.order(ByteOrder.nativeOrder());
                    int[] intvalues=new int[72*72];
                    img.getPixels(intvalues,0,img.getWidth(),0,0,img.getWidth(),img.getHeight());
                    int pixel=0;
                    for(int i=0;i<72;i++)
                    {
                        for(int j=0;j<72;j++) {
                        int val=intvalues[pixel++];
                            byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f /255));
                            byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f/255));
                            byteBuffer.putFloat((val & 0xFF) * (1.f/255));
                        }
                    }
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    MobilenetAnimals.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    float[] confidence=outputFeature0.getFloatArray();
                    float temp=largest(confidence);
                    String[] s={"Bull","Butterfly","Cat","Chicken","Dog","Elephant","Horse","Sheep","Squirrel"};
                    common=s[indexOf(confidence,temp)];
                    Log.i("max is ", common);
                    //animal_names = {'Bull': 0,'Butterfly': 1,'Cats': 2,'Chicken': 3,'Dogs': 4,'Elephant': 5,'Horse': 6,'Sheep': 7,'Squirrel': 8}
                    // Releases model resources if no longer used.
                    tv.setText(common);
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            imageView.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
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




