package com.yy.material;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
    }

    public void trans(View v) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, tv, "textview").toBundle());
    }

}
