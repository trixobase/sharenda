package cm.trixobase.sharenda.system.datatable;

import android.database.sqlite.SQLiteDatabase;

import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;

/**
 * Created by noumianguebissie on 5/11/18.
 */

public class GroupMembersTable {

    private static final String TABLE_CREATE_GROUP_MEMBERS = "create table " + TableName.GROUP_MEMBERS + "("
            + ColumnName.ID + " integer primary key autoincrement, "
            + ColumnName.GROUP_MEMBERS_GROUP_ID + " integer not null constraint fk_group_members_group references " + TableName.GROUP + "(" + ColumnName.ID + "), "
            + ColumnName.GROUP_MEMBERS_CONTACT_NUMBER + " integer not null constraint fk_group_members_contact references " + TableName.CONTACT + "(" + ColumnName.ID + "));";

    public static void createIn(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_GROUP_MEMBERS);
    }

}
