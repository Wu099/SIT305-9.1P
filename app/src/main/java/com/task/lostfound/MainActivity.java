package com.task.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void createClick(View e){
        Intent intent = new Intent(MainActivity.this,CreateActivity.class);
        startActivity(intent);
    }
    public void showAllClick(View e){
        Intent intent = new Intent(MainActivity.this,ShowActivity.class);
        startActivity(intent);
    }

    public void showMapClick(View e){
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        startActivity(intent);
    }
}