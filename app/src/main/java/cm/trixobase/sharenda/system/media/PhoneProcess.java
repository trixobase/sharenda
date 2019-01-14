package cm.trixobase.sharenda.system.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.TranslateElement;
import cm.trixobase.sharenda.core.Activity;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;

/**
 * Created by noumianguebissie on 5/13/18.
 */

public class PhoneProcess {

    private static Cursor phones;

    public static class SmsReceiver extends BroadcastReceiver {

        public SmsReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    final SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }

                    if (msgs.length > -1) {
                        String messageBody = msgs[0].getMessageBody();
                        String phoneNumber = msgs[0].getDisplayOriginatingAddress();
                        if (messageBody.startsWith(Sharenda.Prefix_Sms))
                            createForeignActivity(context, formatNumber(phoneNumber), messageBody);
                    }
                }
            }
        }
    }

    public static class TimeAlert extends BroadcastReceiver {

        public TimeAlert() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            UiActivity activity = Activity.getAllByAlarmId(context, intent.getIntExtra(AttributeName.Activity_Alert_Id, 0));
            showNotification(context,
                    context.getResources().getString(R.string.success_owner_begin),
                    context.getResources().getString(R.string.label_text_form_time) + " " + activity.getTime(),
                    context.getResources().getString(R.string.label_text_form_type).concat(" ").concat(Manager.getTranslate(context.getResources(), TranslateElement.Category, activity.getType())));
        }

    }

    private static void createForeignActivity(Context context, String number, String messageBody) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(messageBody.substring(11, 14)));
            int begin = Integer.valueOf(messageBody.substring(14, 18));
            int hour = begin / 60;
            int minute = begin - (hour * 60);

            UiActivity activity = UiActivity.builder().build()
                    .setOwner(number)
                    .setTitle(messageBody.substring(messageBody.indexOf(" ") + 1, messageBody.indexOf(":")))
                    .setDate(Manager.date.getDate(calendar))
                    .setBeginHour(Manager.time.getTime(hour, minute))
                    .setTime(Integer.valueOf(messageBody.substring(18, 19)))
                    .setType(getType(messageBody.substring(19, 20)))
                    .setMode(false)
                    .setModeHour(true)
                    .setIdGroup(-1);

            activity.setAlarmId();
            Activity.builder().withData(activity.getData()).build().save();
            showNotification(context,
                    context.getResources().getString(R.string.success_foreign_receive),
                    context.getResources().getString(R.string.label_text_of) + " " + Contact.getByNumber(context, number).getName(),
                    activity.getType().concat(": ").concat(activity.getDate()).concat(" ").concat(context.getString(R.string.label_text_separator_date)).concat(" ").concat(activity.getBeginHour()));
        } catch (Exception e) {
            Log.w(Sharenda.Log, e);
            Manager.showToastLongMessage(context, context.getResources().getString(R.string.error_foreign_receive));
        }
    }

    private static void showNotification(Context context, String ticker, String description, String notification) {
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        Intent notificationIntent = new Intent(context, EventActivity.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
//                0);
//
//        Notification.Builder builder = new Notification.Builder(context)
//                .setTicker(ticker)
//                .setContentTitle(context.getString(R.string.app_name))
//                .setContentText("(" + description + ")")
//                .setSubText(notification)
//                .setSmallIcon(R.drawable.sharenda_icon_notification)
//                .setContentIntent(pendingIntent);
//        notificationManager.notify(new Date().hashCode(), builder.build());
    }

    private static String getType(String type) {
        switch (type.toUpperCase()) {
            case "T":
                return AttributeName.Category_Work;
            case "B":
                return AttributeName.Category_Business;
            case "F":
                return AttributeName.Category_Family;
            case "E":
                return AttributeName.Category_Studies;
            case "L":
                return AttributeName.Category_Enjoy;
            case "S":
                return AttributeName.Category_Sport;
            default:
                return "Error";
        }
    }

    public static boolean VerifyContactsAreSufficient(Context context) {
        boolean response = false;
        int count = 0;
        try {
            phones = context.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Echec lors de la verification du nombre de contact: " + e);
        } finally {
            while (phones.moveToNext()) {
                count++;
                if (count > 2) {
                    break;
                }
            }
            if (count > 2)
                response = true;
            Log.e(Sharenda.Log, "Contacts suffisants: " + response + " (" + count + ")");
        }
        return response;
    }

    public static List<UiContact> getAllContacts(ContentResolver contentResolver) {
        List<UiContact> allContacts = new ArrayList<>();
        List<String> allNumbers = new ArrayList<>();
        phones = contentResolver.query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = formatName(phones.getString(phones.getColumnIndex(Phone.DISPLAY_NAME)));
            String number = formatNumber(phones.getString(phones.getColumnIndex(Phone.NUMBER)));

            if (canAdd(allNumbers, name, number)) {
                allContacts.add(UiContact.builder().build()
                        .setName(name)
                        .setNumber(number)
                        .setOperator(getOperator(number))
                        .setNote(0));
                allNumbers.add(number);
            }
        }
        phones.close();
        return allContacts;
    }

    public static UiContact getByNumber(Context context, String number) {
        phones = context.getContentResolver().query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            if (number.equalsIgnoreCase(formatNumber(phones.getString(phones.getColumnIndex(Phone.NUMBER)))))
                return UiContact.builder().build()
                        .setName(formatName(phones.getString(phones.getColumnIndex(Phone.DISPLAY_NAME))))
                        .setNumber(number)
                        .setOperator(getOperator(number))
                        .setNote(0);
        }
        Log.w(Sharenda.Log, "Any contact retrieve with number=" + number);
        return UiContact.builder().build()
                .setName("Error")
                .setNumber("number not exist")
                .setOperator("")
                .setNote(0);
    }

    private static boolean canAdd(List<String> allNumbers, String name, String number) {
        if (name.equalsIgnoreCase(""))
            return false;
        if (number.equalsIgnoreCase(" ")) {
            return false;
        }
        if (number.contains("#") || number.contains("*")) {
            return false;
        }
        if (allNumbers.contains(number)) {
            return false;
        }
        return true;
    }

    public static long sendSmsMessage(List<String> numbers, String message) {
        long response = -1;
        try {
            for (String number : numbers) {
                SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
                Log.e(Sharenda.Log, "SMS envoyé au: " + number);
            }
            response = 0;
        } catch (Exception e) {
            Log.e(Sharenda.Log, "SMS non envoyé: " + e);
            response = -1;
        } finally {
            return response;
        }
    }

    private static String formatName(String name) {
        return Manager.phoneNumber.formatName(name);
    }

    private static String formatNumber(String number) {
        return Manager.phoneNumber.formatNumber(number);
    }

    private static String getOperator(String number) {
        return Manager.phoneNumber.getOperatorOf(number);
    }

}
