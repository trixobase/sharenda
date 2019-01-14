package cm.trixobase.sharenda.system;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Locale;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.system.manager.Manager;

/**
 * Created by noumianguebissie on 7/10/18.
 */

public class Sharenda extends Application {

    public static final String OwnerNumber = BaseName.Trixobase_Number_To_Display;
    public static final String Log = "SHARENDA WARNING: ";

    public static final int Max_Contact_For_Invitation = 20;
    public static final int Min_Contact_For_Group = 3;
    public static final int Max_Contact_For_Group = 10;
    public static final int Max_History_Activity_To_Show = 10;

    public static final String SMS_Invitation_Message = "Sharenda.. disponible sur GooglePlayStore. Conseil, télécharge !!";
    public static final String Prefix_Sms = "Sharenda_ID";
    public static final String Suffix_Sms = "Info envoyée de mon smartphone par " + SMS_Invitation_Message;

    public static final String Language = "language";
    public static final String Language_Default = "en_US";
    public static final String Language_French = "fr";
    public static final String Language_English = Language_Default;

    public static final String Contact_NAME = "trixobase lunike";
    public static final String Contact_Number = BaseName.Trixobase_Number_To_Display;
    public static final long Contact_Note = 3;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setLanguage (Context context, String language){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Language, language);
        edit.apply();

        setLanguage(context);
    }

    public static void setLanguage (Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(Language, Language_Default);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public static String sharedActivitySms(UiActivity activity) {
        return Prefix_Sms
                .concat(getDate(activity.getDate()))
                .concat(getHour(activity.getHour(), activity.getMinute()))
                .concat(getTime(activity.getTime()))
                .concat(getType(activity.getType()))
                .concat(activity.getTitle()) + ": " + activity.getDate() + "|" + activity.getBeginHour() + ". "
                + Suffix_Sms;
    }

    private static String getDate(String date) {
        String dateToShow = String.valueOf(Manager.date.getDate(date).get(Calendar.DAY_OF_YEAR));
        while (dateToShow.length() < 3) {
            dateToShow = 0 + dateToShow;
        }
        return dateToShow;
    }

    private static String getHour(int hour, int minute) {
        String hourToShow = String.valueOf(hour * 60 + minute);
        while (hourToShow.length() < 4) {
            hourToShow = 0 + hourToShow;
        }
        return hourToShow;
    }

    private static String getTime(int time) {
         time = time / 30;
        if (time > 9)
            time = 9;
        else if (time == 0)
            time = 1;
        return String.valueOf(time);
    }

    private static String getType(String type) {
        switch (type) {
            case AttributeName.Category_Work:
                return "T ";
            case AttributeName.Category_Family:
                return "F ";
            case AttributeName.Category_Studies:
                return "E ";
            case AttributeName.Category_Enjoy:
                return "L ";
            case AttributeName.Category_Sport:
                return "S ";
            default:
                return "B ";
        }
    }

}
