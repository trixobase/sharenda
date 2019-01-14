package cm.trixobase.sharenda.system.datatable;

import android.database.sqlite.SQLiteDatabase;

import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;

/**
 * Created by noumianguebissie on 5/15/18.
 */

public class ActivityTable {

    private static final String TABLE_CREATE_ACTIVITY = "create table " + TableName.ACTIVITY + "("
            + ColumnName.ID + " integer primary key autoincrement, "
            + ColumnName.ACTIVITY_OWNER + " text not null, "
            + ColumnName.ACTIVITY_TITLE + " text not null, "
            + ColumnName.ACTIVITY_DATE + " text not null, "
            + ColumnName.ACTIVITY_HOUR_BEGIN + " text not null, "
            + ColumnName.ACTIVITY_TIME + " integer not null, "
            + ColumnName.ACTIVITY_TYPE + " text not null, "
            + ColumnName.ACTIVITY_MODE + " integer not null, "
            + ColumnName.ACTIVITY_FIX_HOUR + " integer not null, "
            + ColumnName.ACTIVITY_ALERT_ID + " integer not null, "
            + ColumnName.ACTIVITY_GROUP_INVITE + " integer not null constraint fk_event_group references " + TableName.GROUP + "(" + ColumnName.ID + "));";

    public static void createIn(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_ACTIVITY);
    }

}
