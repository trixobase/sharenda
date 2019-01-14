package cm.trixobase.sharenda.system.datatable;

import android.database.sqlite.SQLiteDatabase;

import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;

/**
 * Created by noumianguebissie on 5/11/18.
 */

public class  ContactTable {

    private static final String TABLE_CREATE_CONTACT = "create table " + TableName.CONTACT + "("
            + ColumnName.ID + " integer primary key autoincrement, "
            + ColumnName.CONTACT_NAME + " text not null, "
            + ColumnName.CONTACT_NUMBER + " text not null, "
            + ColumnName.CONTACT_NOTE + " integer not null);";

    public static void createIn(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_CONTACT);
    }

}
