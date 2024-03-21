package com.example.contactlog1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlog1.adapters.RecyclerViewAdapter;
import com.example.contactlog1.databinding.ActivityMainBinding;
import com.example.contactlog1.interfaces.RecyclerViewInterface;
import com.example.contactlog1.models.ContactLog;

import java.util.ArrayList;

public class UnsavedContactLogActivity extends AppCompatActivity implements RecyclerViewInterface {
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
        binding.toolbar.setTitle("Unsaved ContactLogs");
        setSupportActionBar(binding.toolbar);

        EditTableHeaders();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        populateInitialData();
        adapter = new RecyclerViewAdapter(this, this, contactLogs);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpSpinnerListener();
    }

    private void EditTableHeaders() {
        TextView name = findViewById(R.id.textNameHeader);
        name.setVisibility(View.GONE);

        TextView yesterday = findViewById(R.id.yesterdayHeader);
        yesterday.setText("Yesterday/TR");
        TextView lastWeek = findViewById(R.id.lastWeekHeader);
        lastWeek.setText("LastWeek/TR");
        TextView lastMonth = findViewById(R.id.lastMonthHeader);
        lastMonth.setText("LastMonth/TR");
    }

    private boolean isFirstSelection = true;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem switchActivityMenuItem = menu.findItem(R.id.action_switch_activity);
        switchActivityMenuItem.setTitle("Switch to Saved ContactLogs");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_switch_activity) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setUpSpinnerListener() {
        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
        String[] options = {"Select option", "yesterday", "last week", "last month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerFilter.setAdapter(adapter);
        spinnerFilter.setSelection(0);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }
                System.out.println("position: " + position);
                selectedOption = options[position];
                System.out.println("Selected option: " + selectedOption);
                if (!selectedOption.equals("Select option")) {
                    Intent intent = new Intent(UnsavedContactLogActivity.this, ContactDetailsActivity.class);
                    intent.putExtra("CONTACT_LOG", contactLogs.get(cd_position));
                    intent.putExtra("SELECTED_OPTION", selectedOption);
                    intent.putExtra("SOURCE_ACTIVITY", "UnsavedContactLogActivity");
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void populateInitialData() {
        contactLogs.add(new ContactLog("", "1234567890", "8", "40", "160", "320", "640", "1280"));
        contactLogs.add(new ContactLog("", "0987654321", "7", "35", "140", "280", "560", "1120"));
        contactLogs.add(new ContactLog("", "1122334455", "6", "30", "120", "240", "480", "960"));
        contactLogs.add(new ContactLog("", "2233445566", "5", "25", "100", "200", "400", "800"));
        contactLogs.add(new ContactLog("", "3344556677", "4", "20", "80", "160", "320", "640"));
        contactLogs.add(new ContactLog("", "4455667788", "3", "15", "60", "120", "240", "480"));
        contactLogs.add(new ContactLog("", "5566778899", "2", "10", "40", "80", "160", "320"));
        contactLogs.add(new ContactLog("", "6677889900", "1", "5", "20", "40", "80", "160"));
    }
    @Override
    public void onItemClick(int position) {
        this.cd_position = position;
    }
}
