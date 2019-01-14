package cm.trixobase.sharenda.system.media;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.Activity;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;

/**
 * Created by noumianguebissie on 9/15/18.
 */

public class AlarmProcess implements Serializable {

    public static class Builder {

        private AlarmProcess instance;
        private Context instanceContext;

        public Builder(Context context) {
            instance = new AlarmProcess();
            instanceContext = context;
        }

        public Builder withAlarmId(int alarmId) {
            instance.setAlarmId(alarmId);
            return this;
        }

        public Builder withDate(String date) {
            instance.setAlarmTime(Manager.date.getDate(date));
            return this;
        }


        public Builder withBeginHour(String begin) {
            instance.setAlarmBeginHour(begin);
            return this;
        }

        public void save() {
            createAndSave(instanceContext, instance);
        }
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    private static final long serialVersionUID = 164867498741L;
    private static Calendar alarmTime;
    private static int alarmId;

    public static void setAlarmId(int alertId) {
        alarmId = alertId;
    }

    public static void setAlarmTime(Calendar calendar) {
        alarmTime = calendar;
    }

    public static void setAlarmBeginHour(String beginHour) {
        alarmTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(beginHour.substring(0, 2)));
        alarmTime.set(Calendar.MINUTE, Integer.valueOf(beginHour.substring(3, 5)));
        alarmTime.add(Calendar.MINUTE, -5);
    }

    private static void createAndSave(Context context, AlarmProcess alarm) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PhoneProcess.TimeAlert.class);
        intent.putExtra(AttributeName.Activity_Alert_Id, alarm.alarmId);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, alarm.alarmId, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, alarm.alarmTime.getTimeInMillis(), pendingintent);

        try {
            ObjectOutputStream alarmOOS = new ObjectOutputStream(context.openFileOutput(String.valueOf(alarm.alarmId), context.MODE_PRIVATE));
            alarmOOS.writeObject(alarm);
            alarmOOS.flush();
            alarmOOS.close();
        } catch (Exception e) {
            Log.w(Sharenda.Log, "AlarmProcess - createAndSave: " + e);
        }
    }

    public static AlarmProcess upload(Context context, Activity activity) {
        AlarmProcess alarm = new AlarmProcess();
        try {
            ObjectInputStream alarmOIS = new ObjectInputStream(context.openFileInput(String.valueOf(activity.alertId)));
            alarm = (AlarmProcess) alarmOIS.readObject();
            alarmOIS.close();
        } catch (Exception e) {
            alarm.alarmId = activity.alertId;
            alarm.alarmTime = Manager.date.getDate(activity.date);
            Log.w(Sharenda.Log, "AlarmProcess - upload: " + e);
        } finally {
            return alarm;
        }
    }

}