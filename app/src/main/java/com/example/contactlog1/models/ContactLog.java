package com.example.contactlog1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactLog implements Parcelable {
    private final String name;
    private final String phoneNumber;
    private final String yesterdayHours;
    private final String yesterdayCount;
    private final String lastWeekHours;
    private final String lastWeekCount;
    private final String lastMonthHours;
    private final String lastMonthCount;

    protected ContactLog(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        yesterdayHours = in.readString();
        yesterdayCount = in.readString();
        lastWeekHours = in.readString();
        lastWeekCount = in.readString();
        lastMonthHours = in.readString();
        lastMonthCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(yesterdayHours);
        dest.writeString(yesterdayCount);
        dest.writeString(lastWeekHours);
        dest.writeString(lastWeekCount);
        dest.writeString(lastMonthHours);
        dest.writeString(lastMonthCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactLog> CREATOR = new Creator<ContactLog>() {
        @Override
        public ContactLog createFromParcel(Parcel in) {
            return new ContactLog(in);
        }

        @Override
        public ContactLog[] newArray(int size) {
            return new ContactLog[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getYesterdayHours() {
        return yesterdayHours;
    }

    public String getYesterdayCount() {
        return yesterdayCount;
    }

    public String getLastWeekHours() {
        return lastWeekHours;
    }

    public String getLastWeekCount() {
        return lastWeekCount;
    }

    public String getLastMonthHours() {
        return lastMonthHours;
    }

    public String getLastMonthCount() {
        return lastMonthCount;
    }

    public ContactLog(String name, String phoneNumber, String yesterdayHours, String yesterdayCount, String lastWeekHours, String lastWeekCount, String lastMonthHours, String lastMonthCount) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.yesterdayHours = yesterdayHours;
        this.yesterdayCount = yesterdayCount;
        this.lastWeekHours = lastWeekHours;
        this.lastWeekCount = lastWeekCount;
        this.lastMonthHours = lastMonthHours;
        this.lastMonthCount = lastMonthCount;
    }
}
