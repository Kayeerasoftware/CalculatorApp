package com.GroupD.calculatorapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.GroupD.calculatorapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
