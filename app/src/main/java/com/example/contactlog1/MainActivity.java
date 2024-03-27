package com.example.contactlog1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlog1.adapters.RecyclerViewAdapter;
import com.example.contactlog1.interfaces.RecyclerViewInterface;
//import com.example.contactlog1.models.ContactLog;
import com.example.contactlog1.models.ContactLog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlog1.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private final ArrayList<ContactLog> contactLogs = new ArrayList<>();
    private int cd_position = -1;
    private String selectedOption;
    private boolean hasPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("Saved ContactLogs");
        setSupportActionBar(binding.toolbar);
        if(!hasPermission){
            permission.launch(android.Manifest.permission.READ_CALL_LOG);
        } else {
            getSavedContactLog();
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this, this, contactLogs, calculateMaxHeaderWidth());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpSpinnerListener();
    }
    @SuppressLint("NotifyDataSetChanged")
    private final ActivityResultLauncher<String> permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result){
            hasPermission = true;
            getSavedContactLog();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Permission required to log", Toast.LENGTH_SHORT).show();
        }
    });

    public  void  getSavedContactLog() {
        Map<String, ContactLog> contactLogMap = new HashMap<>();

        Uri uri = Uri.parse("content://call_log/calls");
        try (Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    @SuppressLint("Range") long callDuration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    @SuppressLint("Range") long callDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    if(name == null || name.isEmpty()) {
                        continue;
                    }
                    long currentTime = System.currentTimeMillis();
                    String timePeriod = getTimePeriod(callDate, currentTime);
                    String formattedDuration = formatCallDuration(callDuration);
                    ContactLog contactLog = contactLogMap.get(phoneNumber);
                    if (contactLog == null) {
                        contactLog = new ContactLog(name, phoneNumber, "", "", "", "", "", "");
                        contactLogMap.put(phoneNumber, contactLog);
                    }
                    switch (timePeriod) {
                        case "Yesterday":
                            contactLog.setYesterdayHours(addDuration(contactLog.getYesterdayHours(), formattedDuration));
                            break;
                        case "Last Week":
                            contactLog.setLastWeekHours(addDuration(contactLog.getLastWeekHours(), formattedDuration));
                            break;
                        case "Last Month":
                            contactLog.setLastMonthHours(addDuration(contactLog.getLastMonthHours(), formattedDuration));
                            break;
                        default:
                            break;
                    }
                } while (cursor.moveToNext());
                contactLogs.addAll(contactLogMap.values());

            }
        }
        contactLogs.sort(new Comparator<ContactLog>() {
            @Override
            public int compare(ContactLog o1, ContactLog o2) {
                boolean o1HasHours = !o1.getYesterdayHours().isEmpty() || !o1.getLastWeekHours().isEmpty() || !o1.getLastMonthHours().isEmpty();

                boolean o2HasHours = !o2.getYesterdayHours().isEmpty() || !o2.getLastWeekHours().isEmpty() || !o2.getLastMonthHours().isEmpty();

                if (o1HasHours && !o2HasHours) {
                    return -1;
                } else if (!o1HasHours && o2HasHours) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
    private int calculateMaxHeaderWidth() {
        int maxWidth = 0;

        TextView textNameHeader = findViewById(R.id.textNameHeader);
        TextView phoneNoHeader = findViewById(R.id.phoneNoHeader);
        TextView yesterdayHeader = findViewById(R.id.yesterdayHeader);
        TextView lastWeekHeader = findViewById(R.id.lastWeekHeader);
        TextView lastMonthHeader = findViewById(R.id.lastMonthHeader);

        Paint paint = new Paint();
        paint.setTextSize(textNameHeader.getTextSize());

        int textNameWidth = (int) paint.measureText(textNameHeader.getText().toString());
        int phoneNoWidth = (int) paint.measureText(phoneNoHeader.getText().toString());
        int yesterdayWidth = (int) paint.measureText(yesterdayHeader.getText().toString());
        int lastWeekWidth = (int) paint.measureText(lastWeekHeader.getText().toString());
        int lastMonthWidth = (int) paint.measureText(lastMonthHeader.getText().toString());

        maxWidth = Math.max(maxWidth, textNameWidth);
        maxWidth = Math.max(maxWidth, phoneNoWidth);
        maxWidth = Math.max(maxWidth, yesterdayWidth);
        maxWidth = Math.max(maxWidth, lastWeekWidth);
        maxWidth = Math.max(maxWidth, lastMonthWidth);

        return maxWidth;
    }
    private String formatCallDuration(long duration) {
        long hours = TimeUnit.SECONDS.toHours(duration);
        long minutes = TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = duration - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

        if (duration < 60) {
            return String.format(Locale.getDefault(), "%ds", duration);
        } else if (hours == 0) {
            return String.format(Locale.getDefault(), "%dm", minutes);
        } else {
            return String.format(Locale.getDefault(), "%dh", hours);
        }
    }
public String addDuration(String duration1, String duration2) {
        if (duration1.isEmpty()) {
            return duration2;
        }
        char unit1 = duration1.charAt(duration1.length() - 1);
        int value1 = Integer.parseInt(duration1.substring(0, duration1.length() - 1));

        char unit2 = duration2.charAt(duration2.length() - 1);
        int value2 = Integer.parseInt(duration2.substring(0, duration2.length() - 1));

        int totalSeconds = convertToSeconds(duration1) + convertToSeconds(duration2);

        return formatDuration(totalSeconds, unit1);
    }

    private int convertToSeconds(String duration) {
        char unit = duration.charAt(duration.length() - 1);
        int value = Integer.parseInt(duration.substring(0, duration.length() - 1));
        switch (unit) {
            case 's':
                return value;
            case 'm':
                return value * 60;
            case 'h':
                return value * 3600;
            default:
                return 0;
        }
    }

    private String formatDuration(int totalSeconds, char unit) {
        switch (unit) {
            case 's':
                return totalSeconds + "s";
            case 'm':
                return (totalSeconds / 60) + "m";
            case 'h':
                return (totalSeconds / 3600) + "h";
            default:
                return "";
        }
    }
    private String getTimePeriod(long callDate, long currentTime) {
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long oneWeekInMillis = 7 * oneDayInMillis;
        long oneMonthInMillis = 30 * oneDayInMillis;

        long timeDiff = currentTime - callDate;
        if (timeDiff <= oneDayInMillis) {
            return "Yesterday";
        } else if (timeDiff <= oneWeekInMillis) {
            return "Last Week";
        } else if (timeDiff <= oneMonthInMillis) {
            return "Last Month";
        } else {
            return "Other";
        }
    }
    private boolean isFirstSelection = true;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem switchActivityMenuItem = menu.findItem(R.id.action_switch_activity);
        switchActivityMenuItem.setTitle("Switch to Unsaved ContactLogs");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_switch_activity) {
            Intent intent = new Intent(this, UnsavedContactLogActivity.class);
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
                selectedOption = options[position];
                if (!selectedOption.equals("Select option")) {
                    if(cd_position != -1) {
                        Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                        intent.putExtra("CONTACT_LOG", contactLogs.get(cd_position));
                        intent.putExtra("SELECTED_OPTION", selectedOption);
                        intent.putExtra("SOURCE_ACTIVITY", "MainActivity");
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Select a contact log", Toast.LENGTH_SHORT).show();
                        selectedOption = options[0];
                        spinnerFilter.setSelection(0);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        this.cd_position = position;

    }
}