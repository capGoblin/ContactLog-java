package com.example.contactlog1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlog1.adapters.RecyclerViewAdapter;
import com.example.contactlog1.databinding.ActivityMainBinding;
import com.example.contactlog1.interfaces.RecyclerViewInterface;
import com.example.contactlog1.models.ContactLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UnsavedContactLogActivity extends AppCompatActivity implements RecyclerViewInterface {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private final ArrayList<ContactLog> contactLogs = new ArrayList<>();
    private int cd_position = -1;
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
        getUnsavedContactLog();

        adapter = new RecyclerViewAdapter(this, this, contactLogs, calculateMaxHeaderWidth());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpSpinnerListener();
    }

    private void getUnsavedContactLog() {
        Uri uri = Uri.parse("content://call_log/calls");
        Map<String, ContactLog> contactMap = new HashMap<>();

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
                    if(phoneNumber == null || phoneNumber.isEmpty()) {
                        continue;
                    }
                    long currentTime = System.currentTimeMillis();
                    String timePeriod = getTimePeriod(callDate, currentTime);
                    String formattedDuration = formatCallDuration(callDuration);
                    ContactLog contactLog = contactMap.get(phoneNumber);
                    if (contactLog == null) {
                        contactLog = new ContactLog("", phoneNumber, "", "0", "", "0", "", "0");
                        contactMap.put(phoneNumber, contactLog);
                    }
                    switch (timePeriod) {
                        case "Yesterday":
                            contactLog.setYesterdayHours(formattedDuration);
                            int yesterdayCount = Integer.parseInt(contactLog.getYesterdayCount());
                            yesterdayCount++;
                            contactLog.setYesterdayCount(Integer.toString(yesterdayCount));
                            break;
                        case "Last Week":
                            contactLog.setLastWeekHours(formattedDuration);
                            int lastWeekCount = Integer.parseInt(contactLog.getYesterdayCount());
                            lastWeekCount++;
                            contactLog.setLastWeekCount(Integer.toString(lastWeekCount));
                            break;
                        case "Last Month":
                            contactLog.setLastMonthHours(formattedDuration);
                            int lastMonthCount = Integer.parseInt(contactLog.getYesterdayCount());
                            lastMonthCount++;
                            contactLog.setLastMonthCount(Integer.toString(lastMonthCount));
                            break;
                        default:
                            break;
                    }
                } while (cursor.moveToNext());
                contactLogs.addAll(contactMap.values());

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

        TextView phoneNoHeader = findViewById(R.id.phoneNoHeader);
        TextView yesterdayHeader = findViewById(R.id.yesterdayHeader);
        TextView lastWeekHeader = findViewById(R.id.lastWeekHeader);
        TextView lastMonthHeader = findViewById(R.id.lastMonthHeader);

        Paint paint = new Paint();
        paint.setTextSize(phoneNoHeader.getTextSize());

        int phoneNoWidth = (int) paint.measureText(phoneNoHeader.getText().toString());
        int yesterdayWidth = (int) paint.measureText(yesterdayHeader.getText().toString());
        int lastWeekWidth = (int) paint.measureText(lastWeekHeader.getText().toString());
        int lastMonthWidth = (int) paint.measureText(lastMonthHeader.getText().toString());

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
        ArrayAdapter<String> Aadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerFilter.setAdapter(Aadapter);
        spinnerFilter.setSelection(0);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }
                selectedOption = options[position];
                if (!selectedOption.equals("Select option")) {
                    adapter.setSelectedOption(selectedOption);
                    adapter.notifyDataSetChanged();
//                    if(cd_position != -1) {
//                        for(int i = 0; i < contactLogs.toArray().length; i++) {
//                            ContactLog cl = contactLogs.get(i)
                    TextView yesterdayTextView = findViewById(R.id.yesterdayHeader);
                    TextView lastWeekTextView = findViewById(R.id.lastWeekHeader);
                    TextView lastMonthTextView = findViewById(R.id.lastMonthHeader);
                    if(selectedOption.equals(options[1])) {
                        yesterdayTextView.setVisibility(TextView.VISIBLE);
                        lastWeekTextView.setVisibility(TextView.GONE);
                        lastMonthTextView.setVisibility(TextView.GONE);
                    } else if (selectedOption.equals(options[2])) {
                        yesterdayTextView.setVisibility(TextView.GONE);
                        lastWeekTextView.setVisibility(TextView.VISIBLE);
                        lastMonthTextView.setVisibility(TextView.GONE);
                    } else if (selectedOption.equals(options[3])) {
                        yesterdayTextView.setVisibility(TextView.GONE);
                        lastWeekTextView.setVisibility(TextView.GONE);
                        lastMonthTextView.setVisibility(TextView.VISIBLE);
//                    } else if (selectedOption.equals(options[2])) {
//
//                    } else if (selectedOption.equals(options[3])) {

                    }
//                    handleMainActivity(selectedOption);

//                        }
//                        Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
//                        intent.putExtra("CONTACT_LOG", contactLogs.get(cd_position));
//                        intent.putExtra("SELECTED_OPTION", selectedOption);
//                        intent.putExtra("SOURCE_ACTIVITY", "MainActivity");
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(MainActivity.this, "Select a contact log", Toast.LENGTH_SHORT).show();
//                        selectedOption = options[0];
//                        spinnerFilter.setSelection(0);
//                    }
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
        if(position != -1) {
            Intent intent = new Intent(UnsavedContactLogActivity.this, ContactDetailsActivity.class);
            intent.putExtra("CONTACT_LOG", contactLogs.get(position));
//            intent.putExtra("SELECTED_OPTION", selectedOption);
            intent.putExtra("SOURCE_ACTIVITY", "UnsavedContactLogActivity");
            startActivity(intent);
        }
    }
}
