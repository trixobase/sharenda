package cm.trixobase.sharenda.system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.domain.ui.UiUser;
import cm.trixobase.sharenda.system.DomainObject;
import cm.trixobase.sharenda.system.Sharenda;

/**
 * Created by noumianguebissie on 5/15/18.
 */

public abstract class RequestHandler extends DomainObject {

    private static Cursor cursor;

    /**
     * To add a Where clause:
     * String selection= COLUMN_NAME + " = ? ";
     * String[] selectionArg=new String[] {"blond"};
     */

    public static boolean UserHasRegistered(Context context) {
        boolean userHasRegistered = false;
        String[] projections = null;
        String selection = null;
        String[] selectionArg = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = "1";

        startTransaction(context);
        try {
            cursor = instanceDatabase.query(TableName.USER, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            if (cursor.moveToFirst()) {
                userHasRegistered = true;
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Echec lors de la verification de l'utilisateur: " + e);
        } finally {
            Log.e(Sharenda.Log, "Utilisateur present: " + userHasRegistered + " (" + cursor.getCount() + ")");
            stopTransaction();
        }
        return userHasRegistered;
    }

    protected static UiUser getAuthenticatedUser() {
        String[] projections = null;
        String selection = ColumnName.ID + " = ? ";
        String[] selectionArg = new String[]{"1"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = "1";

        try {
            cursor = instanceDatabase.query(TableName.USER, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            if (cursor.moveToFirst()) {
                return cursorToUser(cursor);
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Echec lors de la recuperation des infos de l'utilisateur: " + e);
        } finally {
            Log.e(Sharenda.Log, "utilisateur trouve: " + cursor.getCount());
        }
        return null;
    }

    protected static List<String> getAllNumbersMembersByGroup(long groupId) {
        List<String> allNumbers = new ArrayList<>();
        try {
            cursor = instanceDatabase.rawQuery(String.format("select %s from %s where %s  = ?",
                    ColumnName.GROUP_MEMBERS_CONTACT_NUMBER,
                    TableName.GROUP_MEMBERS,
                    ColumnName.GROUP_MEMBERS_GROUP_ID), new String[]{String.valueOf(groupId)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                allNumbers.add(cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.GROUP_MEMBERS_CONTACT_NUMBER)));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getAllNumbersMembersByGroup - " + e);
        } finally {
            Log.e(Sharenda.Log, "all numbers retrieved: " + cursor.getCount());
        }
        return allNumbers;
    }

    protected static List<UiGroup> getAllGroups() {
        List<UiGroup> allGroups = new ArrayList<>();
        String[] projections = null;
        String selection = null;
        String[] selectionArg = null;
        String groupBy = null;
        String having = null;
        String orderBy = ColumnName.GROUP_NAME;
        String maxResultsListSize = null;

        try {
            cursor = instanceDatabase.query(TableName.GROUP, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                allGroups.add(cursorToGroup(cursor));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getAllGroups - " + e);
        } finally {
            Log.e(Sharenda.Log, "all groups retrieved: " + cursor.getCount());
        }
        return allGroups;
    }

    protected static UiGroup getGroupById(long groupId) {
        String[] projections = null;
        String selection = ColumnName.ID + " = ? ";
        String[] selectionArg = new String[]{String.valueOf(groupId)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = "1";

        try {
            cursor = instanceDatabase.query(TableName.GROUP, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                return cursorToGroup(cursor);
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getGroupById - " + e);
        } finally {
            Log.e(Sharenda.Log, "group retrieved: " + cursor.getCount());
        }
        return null;
    }

    protected static List<UiGroup> getAllGroupsByCategory(String category) {
        List<UiGroup> groupList = new ArrayList<>();
        String[] projections = null;
        String selection = ColumnName.GROUP_CATEGORY + " = ? ";
        String[] selectionArg = new String[]{category};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = null;

        try {
            cursor = instanceDatabase.query(TableName.GROUP, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                groupList.add(cursorToGroup(cursor));
                cursor.moveToNext();
            }
            Log.e(Sharenda.Log, "getAllGroupsByCategory - " + cursor.getCount());
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getAllGroupsByCategory - " + cursor.getCount() + "--" + e);
        }
        return groupList;

    }

    protected static List<UiActivity> getAllActivities() {
        List<UiActivity> allActivities = new ArrayList<>();
        String[] projections = null;
        String selection = null;
        String[] selectionArg = null;
        String groupBy = null;
        String having = null;
        String orderBy = ColumnName.ACTIVITY_DATE;
        String maxResultsListSize = null;

        try {
            cursor = instanceDatabase.query(TableName.ACTIVITY, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                allActivities.add(cursorToActivity(cursor));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getAllActivities - " + e);
        } finally {
            Log.e(Sharenda.Log, "all activities retrieved: " + allActivities.size());
        }
        return allActivities;
    }

    protected static List<UiActivity> getAllActivitiesByDate(String date) {
        List<UiActivity> allActivities = new ArrayList<>();
        String[] projections = null;
        String selection = ColumnName.ACTIVITY_DATE + " = ? ";
        String[] selectionArg = new String[]{date};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = null;

        try {
            cursor = instanceDatabase.query(TableName.ACTIVITY, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                allActivities.add(cursorToActivity(cursor));
                cursor.moveToNext();
            }
            Log.e(Sharenda.Log, "getAllActivitiesByDate: " + cursor.getCount());
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getAllActivitiesByDate - " + cursor.getCount() + "--" + e);
        }
        return allActivities;
    }

    protected static List<UiActivity> getAllActivitiesByGroup(long groupId) {
        List<UiActivity> allActivities = new ArrayList<>();
        String[] projections = null;
        String selection = ColumnName.ACTIVITY_GROUP_INVITE + " = ? ";
        String[] selectionArg = new String[]{String.valueOf(groupId)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = null;

        try {
            cursor = instanceDatabase.query(TableName.ACTIVITY, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                allActivities.add(cursorToActivity(cursor));
                cursor.moveToNext();
            }
            Log.e(Sharenda.Log, "getAllActivitiesByGroup: " + cursor.getCount());
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getAllActivitiesByGroup - " + cursor.getCount() + "--" + e);
        }
        return allActivities;
    }

    protected static UiActivity getActivityByAlarmId(int alarmId) {
        String[] projections = null;
        String selection = ColumnName.ACTIVITY_ALERT_ID + " = ? ";
        String[] selectionArg = new String[]{String.valueOf(alarmId)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String maxResultsListSize = "1";

        try {
            cursor = instanceDatabase.query(TableName.ACTIVITY, projections, selection, selectionArg, groupBy, having, orderBy, maxResultsListSize);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                return cursorToActivity(cursor);
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "getActivityByAlarmId - " + e);
        } finally {
            Log.e(Sharenda.Log, "Activity retrieved: " + cursor.getCount());
        }
        return null;
    }

    private static UiGroup cursorToGroup(Cursor cursor) {
        ContentValues data = new ContentValues();
        try {
            data.put(AttributeName.Id, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ID)));
            data.put(AttributeName.Group_Name, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.GROUP_NAME)));
            data.put(AttributeName.Group_Category, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.GROUP_CATEGORY)));
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Fail to convert cursor to group: " + e);
        }
        return (UiGroup) UiGroup.builder().withData(data).build();
    }

    private static UiActivity cursorToActivity(Cursor cursor) {
        ContentValues data = new ContentValues();
        try {
            data.put(AttributeName.Id, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ID)));
            data.put(AttributeName.Activity_Owner, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_OWNER)));
            data.put(AttributeName.Activity_Title, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_TITLE)));
            data.put(AttributeName.Activity_Date, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_DATE)));
            data.put(AttributeName.Activity_Begin_Hour, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_HOUR_BEGIN)));
            data.put(AttributeName.Activity_Time, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_TIME)));
            data.put(AttributeName.Activity_Type, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_TYPE)));
            if (cursor.getInt(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_MODE)) == -1)
                data.put(AttributeName.Activity_Mode, true);
            else data.put(AttributeName.Activity_Mode, false);
            if (cursor.getInt(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_FIX_HOUR)) == -1)
                data.put(AttributeName.Activity_Mode_Hour, false);
            else data.put(AttributeName.Activity_Mode_Hour, true);
            data.put(AttributeName.Activity_Group_Invites_Id, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_GROUP_INVITE)));
            data.put(AttributeName.Activity_Alert_Id, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ACTIVITY_ALERT_ID)));
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Fail to convert cursor to activity: " + e);
        }
        return (UiActivity) UiActivity.builder().withData(data).build();
    }

    private static UiUser cursorToUser(Cursor cursor) {
        ContentValues data = new ContentValues();
        try {
            data.put(AttributeName.Id, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ID)));
            data.put(AttributeName.User_Civility, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.USER_CIVILITY)));
            data.put(AttributeName.User_Surname, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.USER_SURNAME)));
            data.put(AttributeName.User_Age, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.USER_AGE)));
            data.put(AttributeName.User_Civil_Status, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.USER_CIVIL_STATUS)));
            data.put(AttributeName.User_Habit, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.USER_HABIT)));
            data.put(AttributeName.User_Password, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.USER_PASSWORD)));
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Fail to convert cursor to user: " + e);
        }
        return (UiUser) UiUser.builder().withData(data).build();
    }

}
