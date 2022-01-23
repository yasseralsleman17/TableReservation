package com.example.tablereservation.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tablereservation.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RestaurantDetails extends AppCompatActivity {


    String restaurant_id,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restaurant_id = extras.getString("restaurant_id");
            id = extras.getString("id");

        }


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TableChart(restaurant_id,id)).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.table_chart:
                    selectedFragment = new TableChart(restaurant_id,id);
                    break;
                case R.id.chat:
                    selectedFragment = new Chat(restaurant_id,id);
                    break;

            }
            // It will help to replace the
            // one fragment to other.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };
}