package cm.trixobase.sharenda.system.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cm.trixobase.sharenda.core.Activity;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.system.DomainObject;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.media.PhoneProcess;

/**
 * Created by noumianguebissie on 5/27/18.
 */

public abstract class ActivityManager extends DomainObject {

    private static List<UiActivity> activitiesToSave = new ArrayList<>();
    private static List<UiActivity> activitiesDay = new ArrayList<>();
    private static boolean[] occupation;
    private static int[] position;
    private static long groupId = -1;

    private static final String Activity_Can_Save = "save";
    private static final String Activity_Edit_Before = "edit_before";
    private static final String Activity_Edit_After = "edit_after";
    private static final String Activity_Put_Before = "put_before";
    private static final String Activity_Put_After = "put_after";

    public static class Builder {

        private UiActivity activityToSave;

        private Builder(Context context) {
            super();
            instanceContext = context;
            activitiesToSave.clear();
        }

        public Builder withActivity(UiActivity activity) {
            if (activity.hasAutoSaved()) {
                activity.setDate(computeAndGetDate(activity.getType()));
                activity.setBeginHour(computeAndGetBeginHour(activity.getDate(), activity.getType()));
                activity.setTime(getMinTime(activity.getType()));
            }
            this.activityToSave = activity;
            groupId = activity.getGroupInvitedId();
            return this;
        }

        public long save() {
            computeActivitiesToSave(activityToSave);
            return saveActivities();
        }

    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    private static String computeAndGetDate(String typeToSave) {
        Calendar calendar = Manager.date.getDate(UserManager.getDateOfType(instanceContext, typeToSave));
        calendar.add(Calendar.MINUTE, 15);
        return Manager.date.getDate(calendar);
    }

    private static String computeAndGetBeginHour(String dateToSave, String typeToSave) {
        String beginHour = UserManager.getBeginHourMinOfType(instanceContext, typeToSave);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        return (Manager.date.dateIsPassed(dateToSave, beginHour)) ? Manager.time.getTime(calendar) : beginHour;
    }

    private static void computeActivitiesToSave(UiActivity activityToSave) {
        setupPlanningActivityFor(activityToSave.getDate());
        do {
            UiActivity activityToUpdate = getFirstActivityUsingPlace(activityToSave);
            switch (compareActivity(activityToSave, activityToUpdate)) {
                case Activity_Can_Save:
                    activityToSave = doSave(activityToSave);
                    break;
                case Activity_Edit_After:
                    activityToSave = doEditAfter(activityToSave, activityToUpdate);
                    break;
                case Activity_Put_After:
                    activityToSave = doPutAfter(activityToSave, activityToUpdate);
                    break;
                case Activity_Edit_Before:
                    doEditBefore(activityToSave, activityToUpdate);
                    activityToSave = null;
                    break;
                case Activity_Put_Before:
                    doPutBefore(activityToSave, activityToUpdate);
                    activityToSave = null;
                    break;
            }
        } while (activityToSave != null);
    }

    private static void setupPlanningActivityFor(String date) {
        occupation = new boolean[1440];
        position = new int[1440];
        activitiesDay = Activity.getAllByDate(instanceContext, date);

        for (UiActivity activity : activitiesDay) {
            int moment = activity.getBeginMoment();
            int index = activitiesDay.indexOf(activity);
            for (int i = 0; i < activity.getTime(); i++) {
                if (moment + i >= 1440)
                    break;
                occupation[moment + i] = true;
                position[moment + i] = index;
            }
        }
    }

    private static String compareActivity(UiActivity activityToSave, UiActivity activityToUpdate) {
        if (activityToUpdate == null)
            return Activity_Can_Save;
        if (!activityToUpdate.hasFixBeginHour()) {
            if (canEditBefore(activityToSave, activityToUpdate))
                return Activity_Edit_Before;
            if (canEditAfter(activityToSave, activityToUpdate))
                return Activity_Edit_After;
        }
        if (canPutBefore(activityToSave))
            return Activity_Put_Before;
        return Activity_Put_After;
    }

    private static boolean canEditBefore(UiActivity activityToSave, UiActivity activityToUpdate) {
        return isOkToOverride(activityToSave, activityToUpdate) && isOkToOverrideBefore(activityToUpdate, activityToSave.getBeginMoment() - 1);
    }

    private static boolean canEditAfter(UiActivity activityToSave, UiActivity activityToUpdate) {
        return isOkToOverride(activityToSave, activityToUpdate);
    }

    private static boolean canPutBefore(UiActivity activityToSave) {
        return isOkToOverrideBefore(activityToSave, activityToSave.getBeginMoment() - 1);
    }

    private static UiActivity doSave(UiActivity activityToSave) {
        int availableTime = getAvailableTime(activityToSave);
        int time = activityToSave.getTime();
        if (availableTime >= time - 1) {
            activitiesToSave.add(activityToSave);
            return null;
        }
        if (activityToSave.hasAutoSaved() && availableTime >= 15) {
            activityToSave.setTime(15);
            activitiesToSave.add(activityToSave);
            return null;
        }
        if (!activityToSave.hasAutoSaved() && availableTime >= getMinTime(activityToSave.getType())) {
            activityToSave.setTime(getMinTime(activityToSave.getType()));
            activitiesToSave.add(activityToSave);
            return null;
        }
        activityToSave.setDate(Manager.date.getNextDateOf(activityToSave.getDate()));
        return resetPlanning(activityToSave);
    }

    private static void doEditBefore(UiActivity activityToSave, UiActivity activityToUpdate) {
        UiActivity previousActivity = getPreviousActivityUsingPlace(activityToSave.getBeginMoment() - 1, activityToUpdate.getTime());
        int time = activityToUpdate.getBeginMoment() - activityToSave.getBeginMoment();
        if (previousActivity == null)
            activityToUpdate.removeMinute(time + activityToUpdate.getTime());
        else {
            int removeTime = activityToSave.getBeginMoment() - previousActivity.getBeginMoment();
            int timeToSet = 15;
            if (previousActivity.hasAutoSaved() && activityToUpdate.hasAutoSaved()) {
                previousActivity.setTime(timeToSet);
                activityToUpdate.removeMinute(time + removeTime - timeToSet);
                activityToUpdate.setTime(removeTime - timeToSet);
            } else if (previousActivity.hasAutoSaved() && !activityToUpdate.hasAutoSaved()) {
                timeToSet = getMinTime(activityToUpdate.getType());
                previousActivity.setTime(removeTime - timeToSet);
                activityToUpdate.removeMinute(time + timeToSet);
                activityToUpdate.setTime(timeToSet);
            } else if (!previousActivity.hasAutoSaved() && activityToUpdate.hasAutoSaved()) {
                timeToSet = getMinTime(previousActivity.getType());
                previousActivity.setTime(timeToSet);
                activityToUpdate.removeMinute(time + removeTime - timeToSet);
                activityToUpdate.setTime(removeTime - timeToSet);
            } else {
                previousActivity.setTime(removeTime / 2);
                activityToUpdate.removeMinute(time + (removeTime / 2));
                activityToUpdate.setTime(removeTime / 2);
            }
            activitiesToSave.add(previousActivity);
        }
        activitiesToSave.add(activityToUpdate);
        activitiesToSave.add(activityToSave);
    }

    private static void doPutBefore(UiActivity activityToSave, UiActivity activityToUpdate) {
        UiActivity previousActivity = getPreviousActivityUsingPlace(activityToUpdate.getBeginMoment() - 1, activityToSave.getTime());
        activityToSave.setBeginHour(activityToUpdate.getBeginHour());
        if (previousActivity == null)
            activityToSave.removeMinute(activityToSave.getTime());
        else {
            int time = activityToUpdate.getBeginMoment() - previousActivity.getBeginMoment();
            int timeToSet = 15;
            if (previousActivity.hasAutoSaved() && activityToSave.hasAutoSaved()) {
                previousActivity.setTime(timeToSet);
                activityToSave.removeMinute(time - timeToSet);
                activityToSave.setTime(time - timeToSet);
            } else if (previousActivity.hasAutoSaved() && !activityToSave.hasAutoSaved()) {
                timeToSet = getMinTime(activityToSave.getType());
                activityToSave.removeMinute(timeToSet);
                activityToSave.setTime(timeToSet);
                previousActivity.setTime(time - timeToSet);
            } else if (!previousActivity.hasAutoSaved() && activityToSave.hasAutoSaved()) {
                timeToSet = getMinTime(previousActivity.getType());
                activityToSave.removeMinute(time - timeToSet);
                activityToSave.setTime(time - timeToSet);
                previousActivity.setTime(timeToSet);
            } else {
                previousActivity.setTime(time / 2);
                activityToSave.removeMinute(time / 2);
                activityToSave.setTime(time / 2);
            }
            activitiesToSave.add(previousActivity);
        }
        activitiesToSave.add(activityToSave);
    }

    private static UiActivity doEditAfter(UiActivity activityToSave, UiActivity activityToUpdate) {
        activitiesToSave.add(activityToSave);
        return addMinute(activityToUpdate, activityToSave.getTime());
    }

    private static UiActivity doPutAfter(UiActivity activityToSave, UiActivity activityToUpdate) {
        return addMinute(activityToSave, activityToUpdate.getTime());
    }

    private static UiActivity addMinute(UiActivity activity, int minuteToAdd) {
        return activity.addMinute(minuteToAdd) ? resetPlanning(activity) : activity;
    }

    private static UiActivity resetPlanning(UiActivity activity) {
        setupPlanningActivityFor(activity.getDate());
        if (activity.hasAutoSaved()) {
            activity.setBeginHour("05:30");
            activity.setTime(getMinTime(activity.getType()));
        } else activity.setBeginHour("00:00");
        return activity;
    }

    private static UiActivity getFirstActivityUsingPlace(UiActivity activity) {
        int beginMoment = activity.getBeginMoment();
        int time = activity.getTime();

        for (int i = 0; i <= time; i++) {
            if (beginMoment + i >= 1399)
                break;
            if (occupation[beginMoment + i])
                return activitiesDay.get(position[beginMoment + i]);
        }
        return null;
    }

    private static UiActivity getPreviousActivityUsingPlace(int beginMoment, int time) {
        Calendar calendar = Calendar.getInstance();
        int instanceMoment = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

        for (int i = 0; i <= time; i++) {
            if (beginMoment - i <= 0)
                break;
            if (beginMoment - i <= instanceMoment)
                break;
            if (occupation[beginMoment - i])
                return activitiesDay.get(position[beginMoment - i]);
        }
        return null;
    }

    private static int getAvailableTime(UiActivity activity) {
        int beginMoment = activity.getBeginMoment();
        int availableTime = activity.getTime();
        int time;
        for (time = 0; time <= availableTime; time++) {
            if (beginMoment + time >= 1439)
                break;
            if (occupation[beginMoment + time])
                break;
        }
        return time;
    }

    private static boolean isOkToOverride(UiActivity activityToSave, UiActivity activityToUpdate) {
        int updatePriority = getPriority(activityToUpdate.getType());
        int savePriority = getPriority(activityToSave.getType());

        return savePriority > updatePriority || savePriority == updatePriority && activityToUpdate.hasAutoSaved();
    }

    private static boolean isOkToOverrideBefore(UiActivity activity, int beginMoment) {
        UiActivity previousActivity = getPreviousActivityUsingPlace(beginMoment, activity.getTime());
        if (previousActivity == null) {
            if (beginMoment < 240)
                return false;
            else return true;
        }
        if (!isOkToOverride(activity, previousActivity))
            return false;
        int time = beginMoment + 1 - previousActivity.getBeginMoment();
        int activityTime = getMinTime(activity.getType());
        int previousTime = getMinTime(previousActivity.getType());
        if (previousActivity.hasAutoSaved() && activity.hasAutoSaved() && time >= 30)
            return true;
        else if (previousActivity.hasAutoSaved() && !activity.hasAutoSaved() && time >= 15 + activityTime)
            return true;
        else if (!previousActivity.hasAutoSaved() && activity.hasAutoSaved() && time >= 15 + previousTime)
            return true;
        else if (!previousActivity.hasAutoSaved() && !activity.hasAutoSaved() && time >= previousTime + activityTime)
            return true;
        else return false;
    }

    private static int getPriority(String type) {
        return UserManager.getPriorityOfType(instanceContext, type);
    }

    private static int getMinTime(String type) {
        return UserManager.getTimeMinOfType(instanceContext, type);
    }

    private static long saveActivities() {
        UiActivity activityToSave = null;
        long result = -1;

        for (UiActivity activity : activitiesToSave) {
            activity.setAlarmId();
            if (activity.getId() == -1)
                activityToSave = activity;
            result = Activity.builder().withData(activity.getData()).build().save();
            if (result == -1)
                forceStopTransaction();
        }
        stopTransaction();

        if (groupId != -1 && result != -1)
            PhoneProcess.sendSmsMessage(Contact.getAllNumbersByGroup(instanceContext, groupId), Sharenda.sharedActivitySms(activityToSave));
        return result;
    }

}
