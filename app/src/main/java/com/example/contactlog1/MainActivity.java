package com.example.contactlog1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.contactlog1.adapters.RecyclerViewAdapter;
import com.example.contactlog1.interfaces.RecyclerViewInterface;
//import com.example.contactlog1.models.ContactLog;
import com.example.contactlog1.models.ContactLog;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlog1.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private final ArrayList<ContactLog> contactLogs = new ArrayList<>();
    private int cd_position;
    private String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        populateInitialData();
        adapter = new RecyclerViewAdapter(this, this, contactLogs);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpSpinnerListener();
    }
    private boolean isFirstSelection = true;

    private void setUpSpinnerListener() {
        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
        String[] options = {"Select option", "yesterday", "last week", "last month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerFilter.setAdapter(adapter);
        spinnerFilter.setSelection(0);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle item selection here
                if (isFirstSelection) {
                    isFirstSelection = false;
                    System.out.println("WWWWWWWW");
                    return;
                }
                System.out.println("position: " + position);
                selectedOption = options[position];
                System.out.println("Selected option: " + selectedOption);
                // Depending on the selected option, perform filtering or other actions
                // For example:
                if (!selectedOption.equals("Select option")) {
                    Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                    intent.putExtra("CONTACT_LOG", contactLogs.get(cd_position));
                    intent.putExtra("SELECTED_OPTION", selectedOption);
                    startActivity(intent);
                }
//                if (selectedOption.equals("yesterday")) {
//                    // Perform filtering for yesterday
//                    System.out.println("yesterday clicked");
//                } else if (selectedOption.equals("last week")) {
//                    // Perform filtering for last week
//                    System.out.println("last week clicked");
//                } else if (selectedOption.equals("last month")) {
//                    // Perform filtering for last month
//                    System.out.println("last month clicked");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void populateInitialData() {
        contactLogs.add(new ContactLog("John Doe", "1234567890", "8", "40", "160", "320", "640", "1280"));
        contactLogs.add(new ContactLog("Jane Doe", "0987654321", "7", "35", "140", "280", "560", "1120"));
        contactLogs.add(new ContactLog("Bob Smith", "1122334455", "6", "30", "120", "240", "480", "960"));
        contactLogs.add(new ContactLog("Alice Johnson", "2233445566", "5", "25", "100", "200", "400", "800"));;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

//    @Override
//    public void onItemClick(int position) {
//        System.out.println("Clicked " + position);
//    }



    @Override
    public void onItemClick(int position) {
        System.out.println("Clicked " + position);
        this.cd_position = position;

    }
}