package cm.trixobase.sharenda.system.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.SettingName;
import cm.trixobase.sharenda.core.User;

/**
 * Created by noumianguebissie on 6/27/18.
 */

public abstract class UserManager {

    public static class Builder {

        private Builder(Context context) {
            super();
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        public Builder withUser(User user) {
            userToSet = user;
            return this;
        }

        public void setAttributes() {
            setWeight();
            computePriority();
            computeDate();
            computeBeginHourMin();
            computeBeginHourMax();
            computeTimeMin();
        }

        public void resetAttributes() {
            SharedPreferences.Editor edit = preferences.edit();

            edit.remove(SettingName.Date_Enjoy_Setted);
            edit.remove(SettingName.Date_Sport_Setted);

            edit.remove(SettingName.BeginHour_Enjoy_Setted);
            edit.remove(SettingName.BeginHour_Work_Setted);
            edit.remove(SettingName.BeginHour_Studies_Setted);
            edit.remove(SettingName.BeginHour_Sport_Setted);
            edit.remove(SettingName.BeginHour_Family_Setted);
            edit.remove(SettingName.BeginHour_Business_Setted);

            edit.remove(SettingName.EndHour_Enjoy_Setted);
            edit.remove(SettingName.EndHour_Work_Setted);
            edit.remove(SettingName.EndHour_Studies_Setted);
            edit.remove(SettingName.EndHour_Sport_Setted);
            edit.remove(SettingName.EndHour_Family_Setted);
            edit.remove(SettingName.EndHour_Business_Setted);

            edit.remove(SettingName.Time_Enjoy_Setted);
            edit.remove(SettingName.Time_Work_Setted);
            edit.remove(SettingName.Time_Studies_Setted);
            edit.remove(SettingName.Time_Sport_Setted);
            edit.remove(SettingName.Time_Family_Setted);
            edit.remove(SettingName.Time_Business_Setted);

            edit.apply();
        }
    }

    private static SharedPreferences preferences;
    private static User userToSet;

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    private static int weight;
    private static final int WEIGHT_AGE = 3;
    private static final int WEIGHT_STATUS = 4;
    private static final int WEIGHT_HABIT = 3;

    private static void setWeight() {
        int ageToTest;
        int statusToTest;
        int habitToTest;

        if (userToSet.age < 19)
            ageToTest = 1;
        else if (userToSet.age > 18 && userToSet.age < 30)
            ageToTest = 2;
        else ageToTest = 3;

        switch (userToSet.civilStatus) {
            case "Eleve":
            case "Pupil":
                statusToTest = 1;
                break;
            case "Etudiant":
            case "Student":
                statusToTest = 2;
                break;
            default:
                statusToTest = 3;
                break;
        }

        switch (userToSet.habit) {
            case "Fétard":
            case "Roisterer":
                habitToTest = 1;
                break;
            case "Sérieux":
            case "Serious":
                habitToTest = 3;
                break;
            default:
                habitToTest = 2;
                break;
        }

        weight = (ageToTest * WEIGHT_AGE) + (statusToTest * WEIGHT_STATUS) + (habitToTest * WEIGHT_HABIT);
    }

    /*
        Setters
     */

    private static void computePriority() {
        int priority_enjoy;
        int priority_work;
        int priority_studies;
        int priority_sport;
        int priority_family;
        int priority_business;

        if (weight < 15) {
            priority_enjoy = 5;
            priority_work = 4;
            priority_studies = 2;
            priority_sport = 1;
            priority_family = 3;
            priority_business = 6;
        } else if (weight < 21) {
            priority_enjoy = 3;
            priority_work = 6;
            priority_studies = 4;
            priority_sport = 1;
            priority_family = 5;
            priority_business = 2;
        } else {
            priority_enjoy = 1;
            priority_work = 6;
            priority_studies = 4;
            priority_sport = 2;
            priority_family = 3;
            priority_business = 5;
        }

        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(SettingName.Priority_Enjoy, priority_enjoy);
        edit.putInt(SettingName.Priority_Work, priority_work);
        edit.putInt(SettingName.Priority_Studies, priority_studies);
        edit.putInt(SettingName.Priority_Sport, priority_sport);
        edit.putInt(SettingName.Priority_Family, priority_family);
        edit.putInt(SettingName.Priority_Business, priority_business);
        edit.apply();

    }

    private static void computeDate() {
        String date_enjoy;
        String date_sport;

        if (weight < 25) {
            date_enjoy = AttributeName.Day_Saturday;
            date_sport = AttributeName.Day_Saturday;
        } else {
            date_enjoy = AttributeName.Day_Sunday;
            date_sport = AttributeName.Day_Sunday;
        }

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SettingName.Date_Enjoy, date_enjoy);
        edit.putString(SettingName.Date_Sport, date_sport);
        edit.apply();

    }

    private static void computeBeginHourMin() {
        String beginHour_enjoy;
        String beginHour_work;
        String beginHour_studies;
        String beginHour_sport;
        String beginHour_family;
        String beginHour_business;

        if (weight < 16) {
            beginHour_enjoy = "19:00";
            beginHour_work = "08:30";
            beginHour_studies = "16:30";
            beginHour_sport = "06:30";
            beginHour_family = "07:00";
            beginHour_business = "15:30";
        } else if (weight < 25) {
            beginHour_enjoy = "19:00";
            beginHour_work = "07:30";
            beginHour_studies = "14:00";
            beginHour_sport = "05:30";
            beginHour_family = "07:00";
            beginHour_business = "08:00";
        } else {
            beginHour_enjoy = "14:00";
            beginHour_work = "07:30";
            beginHour_studies = "07:00";
            beginHour_sport = "06:30";
            beginHour_family = "07:00";
            beginHour_business = "08:30";
        }

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SettingName.BeginHour_Enjoy, beginHour_enjoy);
        edit.putString(SettingName.BeginHour_Work, beginHour_work);
        edit.putString(SettingName.BeginHour_Studies, beginHour_studies);
        edit.putString(SettingName.BeginHour_Sport, beginHour_sport);
        edit.putString(SettingName.BeginHour_Family, beginHour_family);
        edit.putString(SettingName.BeginHour_Business, beginHour_business);
        edit.apply();

    }

    private static void computeBeginHourMax() {
        String endHour_enjoy;
        String endHour_work;
        String endHour_studies;
        String endHour_sport;
        String endHour_family;
        String endHour_business;

        if (weight < 16) {
            endHour_enjoy = "23:30";
            endHour_work = "21:00";
            endHour_studies = "20:15";
            endHour_sport = "10:00";
            endHour_family = "20:45";
            endHour_business = "19:45";
        } else if (weight < 25) {
            endHour_enjoy = "23:45";
            endHour_work = "21:45";
            endHour_studies = "23:45";
            endHour_sport = "10:45";
            endHour_family = "23:45";
            endHour_business = "23:45";
        } else {
            endHour_enjoy = "21:45";
            endHour_work = "23:45";
            endHour_studies = "23:45";
            endHour_sport = "09:45";
            endHour_family = "23:45";
            endHour_business = "23:45";
        }

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SettingName.EndHour_Enjoy, endHour_enjoy);
        edit.putString(SettingName.EndHour_Work, endHour_work);
        edit.putString(SettingName.EndHour_Studies, endHour_studies);
        edit.putString(SettingName.EndHour_Sport, endHour_sport);
        edit.putString(SettingName.EndHour_Family, endHour_family);
        edit.putString(SettingName.EndHour_Business, endHour_business);
        edit.apply();

    }

    private static void computeTimeMin() {
        int time_enjoy;
        int time_work;
        int time_studies;
        int time_sport;
        int time_family;
        int time_business;

        if (weight < 16) {
            time_enjoy = 15;
            time_work = 30;
            time_studies = 30;
            time_sport = 20;
            time_family = 10;
            time_business = 5;
        } else if (weight < 25) {
            time_enjoy = 10;
            time_work = 45;
            time_studies = 40;
            time_sport = 60;
            time_family = 20;
            time_business = 10;
        } else {
            time_enjoy = 30;
            time_work = 30;
            time_studies = 30;
            time_sport = 40;
            time_family = 40;
            time_business = 20;
        }

        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(SettingName.Time_Enjoy, time_enjoy);
        edit.putInt(SettingName.Time_Work, time_work);
        edit.putInt(SettingName.Time_Studies, time_studies);
        edit.putInt(SettingName.Time_Sport, time_sport);
        edit.putInt(SettingName.Time_Family, time_family);
        edit.putInt(SettingName.Time_Business, time_business);
        edit.apply();

    }

    /*
        Getters
     */

    public static int getPriorityOfType(Context context, String type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int defaultValue = 1;
        switch (type) {
            case AttributeName.Category_Enjoy:
                return preferences.getInt(SettingName.Priority_Enjoy, defaultValue);
            case AttributeName.Category_Work:
                return preferences.getInt(SettingName.Priority_Work, defaultValue);
            case AttributeName.Category_Studies:
                return preferences.getInt(SettingName.Priority_Studies, defaultValue);
            case AttributeName.Category_Sport:
                return preferences.getInt(SettingName.Priority_Sport, defaultValue);
            case AttributeName.Category_Family:
                return preferences.getInt(SettingName.Priority_Family, defaultValue);
            case AttributeName.Category_Business:
                return preferences.getInt(SettingName.Priority_Business, defaultValue);
            default:
                return defaultValue;
        }
    }

    public static String getDateOfType(Context context, String type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = "Au";
        switch (type) {
            case AttributeName.Category_Enjoy:
                return getDate(preferences.getString(SettingName.Date_Enjoy_Setted, preferences.getString(SettingName.Date_Enjoy, getDate(defaultValue))));
            case AttributeName.Category_Sport:
                return getDate(preferences.getString(SettingName.Date_Sport_Setted, preferences.getString(SettingName.Date_Sport, getDate(defaultValue))));
            default:
                return getDate(defaultValue);
        }
    }

    public static String getDateDayOfType(Context context, String type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = AttributeName.Day_Monday;
        switch (type) {
            case AttributeName.Category_Enjoy:
                return preferences.getString(SettingName.Date_Enjoy_Setted, preferences.getString(SettingName.Date_Enjoy, defaultValue));
            case AttributeName.Category_Sport:
                return preferences.getString(SettingName.Date_Sport_Setted, preferences.getString(SettingName.Date_Sport, defaultValue));
            default:
                return defaultValue;
        }
    }

    private static String getDate(String day) {
        switch (day) {
//            case AttributeName.Day_Monday:
//                return getSundayDate();
//            case AttributeName.Day_Tuesday:
//                return getSundayDate();
//            case AttributeName.Day_Wednesday:
//                return getSundayDate();
//            case AttributeName.Day_Thursday:
//                return getSundayDate();
//            case AttributeName.Day_Friday:
//                return getSundayDate();
            case AttributeName.Day_Saturday:
                return getSaturdayDate();
            case AttributeName.Day_Sunday:
                return getSundayDate();
            default:
                return Manager.date.getDate(Calendar.getInstance());
        }
    }

    public static int getTimeMinOfType(Context context, String type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int defaultValue = 15;
        switch (type) {
            case AttributeName.Category_Enjoy:
                return preferences.getInt(SettingName.Time_Enjoy_Setted, preferences.getInt(SettingName.Time_Enjoy, defaultValue));
            case AttributeName.Category_Work:
                return preferences.getInt(SettingName.Time_Work_Setted, preferences.getInt(SettingName.Time_Work, defaultValue));
            case AttributeName.Category_Studies:
                return preferences.getInt(SettingName.Time_Studies_Setted, preferences.getInt(SettingName.Time_Studies, defaultValue));
            case AttributeName.Category_Sport:
                return preferences.getInt(SettingName.Time_Sport_Setted, preferences.getInt(SettingName.Time_Sport, defaultValue));
            case AttributeName.Category_Family:
                return preferences.getInt(SettingName.Time_Family_Setted, preferences.getInt(SettingName.Time_Family, defaultValue));
            case AttributeName.Category_Business:
                return preferences.getInt(SettingName.Time_Business_Setted, preferences.getInt(SettingName.Time_Business, defaultValue));
            default:
                return defaultValue;
        }
    }

    public static String getBeginHourMinOfType(Context context, String type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) + 15);
        if (hour.length() == 1)
            hour = 0 + hour;
        if (minute.length() == 1)
            minute = 0 + minute;
        String defaultValue = hour + ":" + minute;

        switch (type) {
            case AttributeName.Category_Enjoy:
                return preferences.getString(SettingName.BeginHour_Enjoy_Setted, preferences.getString(SettingName.BeginHour_Enjoy, defaultValue));
            case AttributeName.Category_Work:
                return preferences.getString(SettingName.BeginHour_Work_Setted, preferences.getString(SettingName.BeginHour_Work, defaultValue));
            case AttributeName.Category_Studies:
                return preferences.getString(SettingName.BeginHour_Studies_Setted, preferences.getString(SettingName.BeginHour_Studies, defaultValue));
            case AttributeName.Category_Sport:
                return preferences.getString(SettingName.BeginHour_Sport_Setted, preferences.getString(SettingName.BeginHour_Sport, defaultValue));
            case AttributeName.Category_Family:
                return preferences.getString(SettingName.BeginHour_Family_Setted, preferences.getString(SettingName.BeginHour_Family, defaultValue));
            case AttributeName.Category_Business:
                return preferences.getString(SettingName.BeginHour_Business_Setted, preferences.getString(SettingName.BeginHour_Business, defaultValue));
            default:
                return defaultValue;
        }
    }

    public static String getBeginHourMaxOfType(Context context, String type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = "23:30";
        switch (type) {
            case AttributeName.Category_Enjoy:
                return preferences.getString(SettingName.EndHour_Enjoy_Setted, preferences.getString(SettingName.EndHour_Enjoy, defaultValue));
            case AttributeName.Category_Work:
                return preferences.getString(SettingName.EndHour_Work_Setted, preferences.getString(SettingName.EndHour_Work, defaultValue));
            case AttributeName.Category_Studies:
                return preferences.getString(SettingName.EndHour_Studies_Setted, preferences.getString(SettingName.EndHour_Studies, defaultValue));
            case AttributeName.Category_Sport:
                return preferences.getString(SettingName.EndHour_Sport_Setted, preferences.getString(SettingName.EndHour_Sport, defaultValue));
            case AttributeName.Category_Family:
                return preferences.getString(SettingName.EndHour_Family_Setted, preferences.getString(SettingName.EndHour_Family, defaultValue));
            case AttributeName.Category_Business:
                return preferences.getString(SettingName.EndHour_Business_Setted, preferences.getString(SettingName.EndHour_Business, defaultValue));
            default:
                return defaultValue;
        }
    }

    private static String getSaturdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 7 - calendar.get(Calendar.DAY_OF_WEEK));
        return Manager.date.getDate(calendar);
    }

    private static String getSundayDate() {
        Calendar calendar = Calendar.getInstance();
        int dayCount = calendar.get(Calendar.DAY_OF_WEEK);
        if(dayCount > 1) {
            dayCount = 7 - dayCount;
        }
        calendar.add(Calendar.DAY_OF_WEEK, dayCount);
        return Manager.date.getDate(calendar);
    }

    public static void resetPreferences() {
    }

}
