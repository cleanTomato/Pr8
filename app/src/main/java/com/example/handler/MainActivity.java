package com.example.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Arrays;

import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private ImageView dog_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OneTimeWorkRequest work1 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        OneTimeWorkRequest work2 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(Arrays.asList(work1,work2));
        dog_image = findViewById(R.id.image2);
    }

    public void Load_image(View v){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://random.dog/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<DogResponse> call = apiService.getRandomDog();
        call.enqueue(new Callback<DogResponse>() {
            @Override
            public void onResponse(Call<DogResponse> call, Response<DogResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = response.body().getUrl();
                    Glide.with(MainActivity.this).load(imageUrl).into(dog_image);
                }
            }
            @Override
            public void onFailure(Call<DogResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Ошибка!", Toast.LENGTH_SHORT);
            }
        });

    }

    public void StartThread(View v){
        handler.sendEmptyMessage(1);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            ImageView imageView = findViewById(R.id.image);
            switch (message.what) {
                case 1:
                    imageView.setImageResource(R.drawable.cat1);
                    handler.sendEmptyMessageDelayed(2,3000);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.cat2);
                    handler.sendEmptyMessageDelayed(3,3000);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.parrot3);
                    Toast toast = Toast.makeText(getApplicationContext(), "Конец", Toast.LENGTH_LONG);
                    toast.show();
                    break;
            }
            return true;
        }
    });

}