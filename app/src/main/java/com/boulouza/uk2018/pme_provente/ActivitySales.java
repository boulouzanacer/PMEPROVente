package com.boulouza.uk2018.pme_provente;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.fragments.FragmentProductsInSales;
import com.boulouza.uk2018.pme_provente.fragments.FragmentReceipt;

public class ActivitySales extends AppCompatActivity {

    DATABASE controller;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        controller = new DATABASE(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Facture details");


        //launch fragments
        Fragment fragment1 = null;
        fragment1 = new FragmentProductsInSales();
        Launch_fragment(fragment1, R.id.frame_container1);

        Fragment fragment2 = null;
        fragment2 = new FragmentReceipt();
        Launch_fragment(fragment2, R.id.listview_container);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();

        }
        /*
        if(item.getItemId() == R.id.map){
            startActivity(new Intent(ActivityClients.this, ActivityMaps.class));
        }
        */
        return super.onOptionsItemSelected(item);
    }

    private void Launch_fragment(Fragment fragment, int ressource_id) {

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(ressource_id, fragment).commit();
            fragment = null;
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_to_exit, R.anim.exit_to_enter);
    }
}
