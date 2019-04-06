package com.boulouza.uk2018.pme_provente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent nnn = new Intent(MainActivity.this, ItemListActivity.class);
        startActivity(nnn);
    }
}
