package com.example.contactlog1.models;

public class ContactLog {
    private final String name;
    private final String phoneNumber;
    private final String yesterdayHours;
    private final String yesterdayCount;
    private final String lastWeekHours;
    private final String lastWeekCount;
    private final String lastMonthHours;
    private final String lastMonthCount;

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
