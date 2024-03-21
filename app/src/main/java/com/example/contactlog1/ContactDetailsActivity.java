package com.example.contactlog1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlog1.databinding.ActivityMainBinding;
import com.example.contactlog1.models.ContactLog;

import java.util.Objects;

public class ContactDetailsActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        setupToolbar();
        Intent intent = getIntent();
        ContactLog cl = intent.getParcelableExtra("CONTACT_LOG");
        String selectedOption = intent.getStringExtra("SELECTED_OPTION");
        String sourceActivity = intent.getStringExtra("SOURCE_ACTIVITY");
        if(sourceActivity != null && sourceActivity.equals("MainActivity")) {
            handleMainActivity(cl, selectedOption);
        } else if (sourceActivity != null && sourceActivity.equals("UnsavedContactLogActivity")) {
            handleUnsavedContactLogActivity(cl , selectedOption);
        }
    }

    private void handleUnsavedContactLogActivity(ContactLog cl, String selectedOption) {
        if (cl == null)
            return;
        String phoneNumber = cl.getPhoneNumber();
        String yesterdayHours = cl.getYesterdayHours();
        String lastWeekHours = cl.getLastWeekHours();
        String lastMonthHours = cl.getLastMonthHours();
        TextView nameTextView = findViewById(R.id.cd_textNameHeader);
        nameTextView.setVisibility(TextView.GONE);
        TextView phoneTextView = findViewById(R.id.cd_phoneNoHeader);
        TextView yesterdayTextView = findViewById(R.id.cd_yesterdayHeader);
        yesterdayTextView.setText("Yesterday/TR");
        TextView lastWeekTextView = findViewById(R.id.cd_lastWeekHeader);
        lastWeekTextView.setText("LastWeek/TR");
        TextView lastMonthTextView = findViewById(R.id.cd_lastMonthHeader);
        lastMonthTextView.setText("LastMonth/TR");
        TextView cdNameTextView = findViewById(R.id.cd_textName);
        cdNameTextView.setVisibility(TextView.GONE);
        TextView cdPhoneTextView = findViewById(R.id.cd_phoneNo);
        TextView cdYesterdayTextView = findViewById(R.id.cd_yesterday);
        TextView cdLastWeekTextView = findViewById(R.id.cd_lastWeek);
        TextView cdLastMonthTextView = findViewById(R.id.cd_lastMonth);

        String yesterdayInfo = cl.getYesterdayHours() + "/" + cl.getYesterdayCount();
        String lastWeekInfo = cl.getLastWeekHours()+ "/" + cl.getLastWeekCount();
        String lastMonthInfo = cl.getLastMonthHours() + "/" + cl.getLastMonthCount();
        cdPhoneTextView.setText(phoneNumber);
        cdYesterdayTextView.setText(yesterdayInfo);
        cdLastWeekTextView.setText(lastWeekInfo);
        cdLastMonthTextView.setText(lastMonthInfo);
        setVisibilityBasedOnOption(selectedOption, cdYesterdayTextView, cdLastWeekTextView, cdLastMonthTextView, yesterdayTextView, lastWeekTextView, lastMonthTextView);
    }

    private void handleMainActivity(ContactLog cl, String selectedOption) {
        if (cl == null)
            return;
        String name = cl.getName();
        String phoneNumber = cl.getPhoneNumber();
        String yesterdayHours = cl.getYesterdayHours();
        String lastWeekHours = cl.getLastWeekHours();
        String lastMonthHours = cl.getLastMonthHours();

        TextView yesterdayTextView = findViewById(R.id.cd_yesterdayHeader);
        TextView lastWeekTextView = findViewById(R.id.cd_lastWeekHeader);
        TextView lastMonthTextView = findViewById(R.id.cd_lastMonthHeader);

        TextView cdNameTextView = findViewById(R.id.cd_textName);

        TextView cdPhoneTextView = findViewById(R.id.cd_phoneNo);

        TextView cdYesterdayTextView = findViewById(R.id.cd_yesterday);
        TextView cdLastWeekTextView = findViewById(R.id.cd_lastWeek);
        TextView cdLastMonthTextView = findViewById(R.id.cd_lastMonth);
        cdNameTextView.setText(name);
        cdPhoneTextView.setText(phoneNumber);
        cdYesterdayTextView.setText(yesterdayHours);
        cdLastWeekTextView.setText(lastWeekHours);
        cdLastMonthTextView.setText(lastMonthHours);
        setVisibilityBasedOnOption(selectedOption, cdYesterdayTextView, cdLastWeekTextView, cdLastMonthTextView, yesterdayTextView, lastWeekTextView, lastMonthTextView);
    }
    private void setVisibilityBasedOnOption(String selectedOption, TextView cdYesterdayTextView, TextView cdLastWeekTextView, TextView cdLastMonthTextView, TextView yesterdayTextView, TextView lastWeekTextView, TextView lastMonthTextView) {
        switch (Objects.requireNonNull(selectedOption)) {
            case "yesterday":
                setVisibility(cdLastWeekTextView, cdLastMonthTextView, lastWeekTextView, lastMonthTextView);
                break;
            case "last week":
                setVisibility(cdYesterdayTextView, cdLastMonthTextView, yesterdayTextView, lastMonthTextView);
                break;
            case "last month":
                setVisibility(cdYesterdayTextView, cdLastWeekTextView, yesterdayTextView, lastWeekTextView);
                break;
        }
    }

    private void setVisibility(TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setVisibility(TextView.GONE);
        }
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceActivity = getIntent().getStringExtra("SOURCE_ACTIVITY");
                if (sourceActivity != null) {
                    switch (sourceActivity) {
                        case "MainActivity":
                            Intent mainIntent = new Intent(ContactDetailsActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            break;
                        case "UnsavedContactLogActivity":
                            Intent otherIntent = new Intent(ContactDetailsActivity.this, UnsavedContactLogActivity.class);
                            startActivity(otherIntent);
                            break;
                        default:
                            onBackPressed();
                            break;
                    }
                } else {
                    onBackPressed();
                }
            }
        });
    }
}

