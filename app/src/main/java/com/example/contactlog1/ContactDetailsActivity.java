package com.example.contactlog1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
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
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Enable the back button

        ContactLog cl = getIntent().getParcelableExtra("CONTACT_LOG");
        assert cl != null;
        String name = cl.getName();
        String phoneNumber = cl.getPhoneNumber();
        String yesterdayHours = cl.getYesterdayHours();
        String lastWeekHours = cl.getLastWeekHours();
        String lastMonthHours = cl.getLastMonthHours();
        String selectedOption = getIntent().getStringExtra("SELECTED_OPTION");
        TextView nameTextView = findViewById(R.id.cd_textNameHeader);

        TextView phoneTextView = findViewById(R.id.cd_phoneNoHeader);

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
        switch (Objects.requireNonNull(selectedOption)) {
            case "yesterday":
                cdLastWeekTextView.setVisibility(TextView.GONE);
                cdLastMonthTextView.setVisibility(TextView.GONE);

                lastWeekTextView.setVisibility(TextView.GONE);
                lastMonthTextView.setVisibility(TextView.GONE);
                System.out.println("yesterday clicked");
                break;
            case "last week":
                cdYesterdayTextView.setVisibility(TextView.GONE);
                cdLastMonthTextView.setVisibility(TextView.GONE);

                yesterdayTextView.setVisibility(TextView.GONE);
                lastMonthTextView.setVisibility(TextView.GONE);
                // Perform filtering for last week
                System.out.println("last week clicked");
                break;
            case "last month":
                cdYesterdayTextView.setVisibility(TextView.GONE);
                cdLastWeekTextView.setVisibility(TextView.GONE);

                yesterdayTextView.setVisibility(TextView.GONE);
                lastWeekTextView.setVisibility(TextView.GONE);
                // Perform filtering for last month
                System.out.println("last month clicked");
                break;
        }
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle toolbar item clicks
//        if (item.getItemId() == android.R.id.home) {
//            // Respond to the back button click
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//@Override
//public boolean onSupportNavigateUp() {
//    onBackPressed();
//    return true;
//}

}
