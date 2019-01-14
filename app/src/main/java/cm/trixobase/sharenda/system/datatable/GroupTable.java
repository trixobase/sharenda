package cm.trixobase.sharenda.system.datatable;

import android.database.sqlite.SQLiteDatabase;

import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;

/**
 * Created by noumianguebissie on 5/10/18.
 */

public class GroupTable {

    private static final String TABLE_CREATE_GROUP = "create table " + TableName.GROUP + "("
            + ColumnName.ID + " integer primary key autoincrement, "
            + ColumnName.GROUP_NAME + " text not null, "
            + ColumnName.GROUP_CATEGORY + " text not null);";

    public static void createIn(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_GROUP);
    }

}
