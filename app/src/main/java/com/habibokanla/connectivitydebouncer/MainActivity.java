package com.habibokanla.connectivitydebouncer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.habibokanla.connectivitydebouncer.app.ConnectivityDebouncerApp;
import com.habibokanla.connectivitydebouncer.tracker.ConnectivityChangeEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityDebouncerApp.getComponent().inject(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Subscribe
    public void onConnectivityUpdate(ConnectivityChangeEvent event) {
        String message = "%s with last know network type %s";
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                String.format(message, event.info.isConnected ? "Connected" : "Disconnected", event.info.networkType),
                Snackbar.LENGTH_INDEFINITE);

        snackbar.getView().setBackgroundColor(
                ContextCompat.getColor(this, event.info.isConnected ? android.R.color.holo_green_dark : android.R.color.holo_red_light));
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
