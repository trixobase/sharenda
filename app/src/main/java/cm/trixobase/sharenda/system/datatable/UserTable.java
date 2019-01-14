package cm.trixobase.sharenda.system.datatable;

import android.database.sqlite.SQLiteDatabase;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;

/**
 * Created by noumianguebissie on 4/25/18.
 */

public class UserTable {

    private static final String TABLE_CREATE_USER = "create table " + TableName.USER + "("
            + ColumnName.ID + " integer primary key autoincrement, "
            + ColumnName.USER_CIVILITY + " text not null, "
            + ColumnName.USER_SURNAME + " text not null, "
            + ColumnName.USER_AGE + " integer not null check (" + ColumnName.USER_AGE + " > 12), "
            + ColumnName.USER_PASSWORD + " integer not null, "
            + ColumnName.USER_CIVIL_STATUS + " text not null, "
            + ColumnName.USER_HABIT + " text not null, "
            + ColumnName.USER_REGISTRATION_DATE + " text not null);";

    public static void createIn(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_USER);
    }

}
