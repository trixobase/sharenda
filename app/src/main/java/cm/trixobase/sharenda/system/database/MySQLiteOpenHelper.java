package cm.trixobase.sharenda.system.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.datatable.ContactTable;
import cm.trixobase.sharenda.system.datatable.ActivityTable;
import cm.trixobase.sharenda.system.datatable.GroupMembersTable;
import cm.trixobase.sharenda.system.datatable.GroupTable;
import cm.trixobase.sharenda.system.datatable.UserTable;

/**
 * Created by noumianguebissie on 4/28/18.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sharenda.db";
    private static final int DATABASE_VERSION = 1;
    private static MySQLiteOpenHelper instance;
    protected static SQLiteDatabase instanceDatabase;
    protected static Context instanceContext;

    protected MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected static void startTransaction(Context context) {
        if (instance == null) {
            instance = new MySQLiteOpenHelper(context);
            instanceContext = context;
        }
        try {
            if (instanceDatabase == null || !instanceDatabase.isOpen()) {
                instanceDatabase = instance.getReadableDatabase();
                instanceDatabase.beginTransaction();
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, " Erreur lors de l'ouverture de la BD: " + e);
        }
    }

    protected static void stopTransaction() {
        try {
            if (instanceDatabase.inTransaction()) {
                instanceDatabase.setTransactionSuccessful();
                instanceDatabase.endTransaction();
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Erreur en fin de transaction de la BD: " + e);
        }
        try {
            if (instanceDatabase != null && instanceDatabase.isOpen())
                instanceDatabase.close();
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Erreur lors de la fermeture de la BD: " + e);
        }
    }

    protected static void forceStopTransaction() {
        try {
            if (instanceDatabase != null && instanceDatabase.isOpen()) {
                instanceDatabase.close();
                Log.e(Sharenda.Log, "Fermeture forcee de la BD avec succes");
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Erreur lors de la fermeture forcee de la BD: " + e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        UserTable.createIn(database);
        ContactTable.createIn(database);
        GroupTable.createIn(database);
        ActivityTable.createIn(database);
        GroupMembersTable.createIn(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MySQLiteOpenHelper.class.getName(),
                "Upgrading instanceDatabase from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        instanceDatabase.execSQL("DROP TABLE IF EXISTS " + TableName.GROUP_MEMBERS);
        instanceDatabase.execSQL("DROP TABLE IF EXISTS " + TableName.ACTIVITY);
        instanceDatabase.execSQL("DROP TABLE IF EXISTS " + TableName.USER);
        instanceDatabase.execSQL("DROP TABLE IF EXISTS " + TableName.GROUP);
        instanceDatabase.execSQL("DROP TABLE IF EXISTS " + TableName.CONTACT);
        onCreate(instanceDatabase);
    }

}
