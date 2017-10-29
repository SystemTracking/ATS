package com.asset.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TestRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_run);
    }
    public void Test_Run(View view){
        Intent intent=new Intent(this,TestRun2Activity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    }

}
