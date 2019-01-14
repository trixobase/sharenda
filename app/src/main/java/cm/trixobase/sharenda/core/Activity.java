package cm.trixobase.sharenda.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.DomainObject;
import cm.trixobase.sharenda.system.database.RequestHandler;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.AlarmProcess;
import cm.trixobase.sharenda.system.media.PhoneProcess;

/**
 * Created by noumianguebissie on 4/15/18.
 */

public class Activity extends RequestHandler {

    @Override
    protected String getTableName() {
        return TableName.ACTIVITY;
    }

    public static class Builder extends DomainObject.Builder<Activity> {

        private Builder() {
            super();
        }

        @Override
        protected Activity newInstance() {
            return new Activity();
        }

        @Override
        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            instance.setOwner(newData.getAsString(AttributeName.Activity_Owner))
                    .setTitle(newData.getAsString(AttributeName.Activity_Title))
                    .setDate(newData.getAsString(AttributeName.Activity_Date))
                    .setBeginHour(newData.getAsString(AttributeName.Activity_Begin_Hour))
                    .setTime(newData.getAsLong(AttributeName.Activity_Time))
                    .setType(newData.getAsString(AttributeName.Activity_Type))
                    .setMode(newData.getAsBoolean(AttributeName.Activity_Mode))
                    .setModeHour(newData.getAsBoolean(AttributeName.Activity_Mode_Hour))
                    .setGroupId(newData.getAsLong(AttributeName.Activity_Group_Invites_Id))
                    .setAlertId(newData.getAsInteger(AttributeName.Activity_Alert_Id));
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Activity setOwner(String ownerNumber) {
        this.ownerNumber = ownerNumber;
        return this;
    }

    private Activity setTitle(String title) {
        this.title = title;
        return this;
    }

    private Activity setDate(String date) {
        this.date = date;
        return this;
    }

    private Activity setBeginHour(String beginHour) {
        this.begin = beginHour;
        return this;
    }

    private Activity setTime(long time) {
        this.time = time;
        return this;
    }

    private Activity setType(String type) {
        this.type = type;
        return this;
    }

    private Activity setMode(boolean mode) {
        if (mode)
            this.mode = -1;
        else this.mode = 0;
        return this;
    }

    private Activity setModeHour(boolean modeHour) {
        if (modeHour)
            this.fixHour = 0;
        else this.fixHour = -1;
        return this;
    }

    private Activity setGroupId(long groupId) {
        this.groupId = groupId;
        return this;
    }

    private Activity setAlertId(int alertId) {
        this.alertId = alertId;
        return this;
    }

    public static List<UiActivity> getAll(Context context) {
        startTransaction(context);
        List<UiActivity> activities = RequestHandler.getAllActivities();
        stopTransaction();
        return filter(activities);
    }

    public static List<UiActivity> getAllByDate(Context context, String date) {
        startTransaction(context);
        List<UiActivity> activityList = RequestHandler.getAllActivitiesByDate(date);
        stopTransaction();
        return filter(activityList);
    }

    public static UiActivity getAllByAlarmId(Context context, int alarmId) {
        startTransaction(context);
        UiActivity activity = RequestHandler.getActivityByAlarmId(alarmId);
        stopTransaction();
        return activity;
    }

    public static void deleteGroupInActivities(long groupId) {
        List<UiActivity> activities = RequestHandler.getAllActivitiesByGroup(groupId);
        for (UiActivity activity : activities) {
            activity.setIdGroup(-1);
            Activity.builder().withData(activity.getData()).build().save();
        }
    }

    private String ownerNumber;
    private String title;
    public String date;
    public String begin;
    private long time;
    private String type;
    private int mode;
    private int fixHour;
    private long groupId;
    public int alertId;

    public long save() {
        getData().put(ColumnName.ACTIVITY_OWNER, ownerNumber);
        getData().put(ColumnName.ACTIVITY_TITLE, title);
        getData().put(ColumnName.ACTIVITY_DATE, date);
        getData().put(ColumnName.ACTIVITY_HOUR_BEGIN, begin);
        getData().put(ColumnName.ACTIVITY_TIME, time);
        getData().put(ColumnName.ACTIVITY_TYPE, type);
        getData().put(ColumnName.ACTIVITY_MODE, mode);
        getData().put(ColumnName.ACTIVITY_FIX_HOUR, fixHour);
        getData().put(ColumnName.ACTIVITY_GROUP_INVITE, groupId);
        getData().put(ColumnName.ACTIVITY_ALERT_ID, alertId);

        startTransaction(instanceContext);
        long result = super.save();
        if (result != -1) {
            AlarmProcess.builder(instanceContext)
                    .withAlarmId(alertId)
                    .withDate(date)
                    .withBeginHour(begin)
                    .save();
        }
        return result;
    }

    public static List<UiActivity> filter(List<UiActivity> activityList) {
        List<UiActivity> activities = new ArrayList<>();
        if (!activityList.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2018, 5, 14, 12, 10);
            UiActivity activityTest = UiActivity.builder().build().setDate(calendar)
                    .setBeginHour(Manager.time.getTime(calendar));
            do {
                int item = 0;
                UiActivity recentActivity = activityTest;
                for (UiActivity activity : activityList) {
                    if (activity.comeBefore(recentActivity)) {
                        recentActivity = activity;
                        item = activityList.indexOf(activity);
                    }
                }
                activityList.remove(item);
                activities.add(recentActivity);

            } while (!activityList.isEmpty());
        }

        return reverse(activities);
    }

    public static List<UiActivity> reverse(List<UiActivity> list) {
        List<UiActivity> result = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--)
            result.add(list.get(i));
        return result;
    }

}
