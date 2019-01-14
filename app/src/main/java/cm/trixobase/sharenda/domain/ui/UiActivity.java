package cm.trixobase.sharenda.domain.ui;

import android.content.Context;

import java.util.Calendar;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.Group;
import cm.trixobase.sharenda.system.UiDomainObject;
import cm.trixobase.sharenda.system.manager.Manager;

/**
 * Created by noumianguebissie on 4/15/18.
 */

public class UiActivity extends UiDomainObject {

    public static class Builder extends UiDomainObject.Builder<UiActivity> {
        private Builder() {
            super();
        }

        @Override
        protected UiActivity newInstance() {
            return new UiActivity();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public UiActivity setOwner(String ownerNumber) {
        getData().put(AttributeName.Activity_Owner, ownerNumber);
        return this;
    }

    public UiActivity setTitle(String title) {
        getData().put(AttributeName.Activity_Title, title);
        return this;
    }

    public UiActivity setDate(Calendar calendar) {
        getData().put(AttributeName.Activity_Date, Manager.date.getDate(calendar));
        return this;
    }

    public UiActivity setDate(String date) {
        getData().put(AttributeName.Activity_Date, date);
        return this;
    }

    public UiActivity setBeginHour(String hour) {
        getData().put(AttributeName.Activity_Begin_Hour, hour);
        return this;
    }

    public UiActivity setTime(int time) {
        getData().put(AttributeName.Activity_Time, time);
        return this;
    }

    public UiActivity setType(String type) {
        getData().put(AttributeName.Activity_Type, type);
        return this;
    }

    public UiActivity setMode(boolean mode) {
        getData().put(AttributeName.Activity_Mode, mode);
        return this;
    }

    public UiActivity setModeHour(boolean modeHour) {
        getData().put(AttributeName.Activity_Mode_Hour, modeHour);
        return this;
    }

    public UiActivity setIdGroup(long idGroup) {
        getData().put(AttributeName.Activity_Group_Invites_Id, idGroup);
        return this;
    }

    public String getOwner() {
        return getData().getAsString(AttributeName.Activity_Owner);
    }

    public String getTitle() {
        String title = getData().getAsString(AttributeName.Activity_Title);
        if (title.length() >12)
            title = title.substring(0, 12);
        return title; }

    public long getGroupInvitedId() {
        return getData().getAsInteger(AttributeName.Activity_Group_Invites_Id);
    }

    public String getType() {
        return getData().getAsString(AttributeName.Activity_Type);
    }

    public String  getInvites(Context context) {
        if (getGroupInvitedId() != -1)
            return Group.getById(context, getGroupInvitedId()).getName();
        return context.getResources().getString(R.string.label_text_type_personal);
    }

    public String getDate() {
        return getData().getAsString(AttributeName.Activity_Date);
    }

    public String getBeginHour() {
        return getData().getAsString(AttributeName.Activity_Begin_Hour);
    }

    public String getDay() {
        return getData().getAsString(AttributeName.Activity_Date).substring(0, 2);
    }

    public String getMonth() {
        return getData().getAsString(AttributeName.Activity_Date).substring(3, 5);
    }

    public int getHour() {
        return Integer.valueOf(getData().getAsString(AttributeName.Activity_Begin_Hour).substring(0, 2));
    }

    public int getMinute() {
        return Integer.valueOf(getData().getAsString(AttributeName.Activity_Begin_Hour).substring(3, 5));
    }

    public boolean addMinute(int minuteToAdd) {
        String date = getDate();
        Calendar calendar = Manager.date.getDateAfterAddMinute(date, getBeginHour(), minuteToAdd);
        setDate(calendar);
        setBeginHour(Manager.time.calendarToTime(calendar));
        return date.equalsIgnoreCase(getDate());
    }

    public void removeMinute(int minuteToRemove) {
        Calendar calendar = Manager.date.getDateAfterRemoveMinute(getDate(), getBeginHour(), minuteToRemove);
        setDate(calendar);
        setBeginHour(Manager.time.calendarToTime(calendar));
    }

    public int getTime() {
        return getData().getAsInteger(AttributeName.Activity_Time);
    }

    public boolean hasAutoSaved() {
        return getData().getAsBoolean(AttributeName.Activity_Mode);
    }

    public boolean hasFixBeginHour() {
        if (getData().containsKey(AttributeName.Activity_Mode_Hour))
            return getData().getAsBoolean(AttributeName.Activity_Mode_Hour);
        else return false;
    }

    public boolean mustBeingDoToday() {
        return Manager.date.getDate(Calendar.getInstance()).equalsIgnoreCase(getDate());
    }

    public boolean isPassed() {
        Calendar calendar = Manager.date.getDateAfterAddMinute(getDate(), getBeginHour(), getTime());
        return Manager.date.dateIsPassed(
                Manager.date.getDate(calendar),
                Manager.time.getTime(calendar));
    }

    public boolean comeBefore(UiActivity newActivity) {
        return Manager.date.firstActivityComeAfter(this, newActivity);
    }

    public int getBeginMoment() {
        return getHour() * 60 + getMinute();
    }

    public int getAlarmId() {
        return getData().getAsInteger(AttributeName.Activity_Alert_Id);
    }

    public UiActivity setAlarmId() {
        if (!getData().containsKey(AttributeName.Activity_Alert_Id))
            getData().put(AttributeName.Activity_Alert_Id, Integer.valueOf(getDay().concat(getMonth()).concat(String.valueOf(getHour())).concat(String.valueOf(getMinute()))));
        return this;
    }

}
