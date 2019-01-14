package cm.trixobase.sharenda.system;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.system.database.MySQLiteOpenHelper;

/**
 * Created by noumianguebissie on 5/11/18.
 */

public abstract class DomainObject<T> extends MySQLiteOpenHelper {

    protected long id = -1;
    protected ContentValues data;

    public abstract static class Builder<T extends DomainObject> {
        protected final T instance;

        protected Builder() {
            instance = newInstance();
            instance.data = new ContentValues();
        }

        public Builder(T newInstance) {
            instance = newInstance;
        }

        protected abstract T newInstance();

        public Builder withData(ContentValues newData) {
            return this;
        }

        public T build() {
            return this.instance;
        }

    }

    protected DomainObject() {
        super(instanceContext);
    }

    public long getId() {
        return id;
    }

    protected ContentValues getData() {
        return data;
    }

    protected abstract String getTableName();

    protected long save() {
        try {
            if (getId() != -1) {
                instanceDatabase.update(getTableName(), data, ColumnName.ID + " = ?", new String[]{String.valueOf(getId())});
                Log.e(Sharenda.Log, "Update Succes in " + getTableName() + " id=" + getId());
            } else {
                id = instanceDatabase.insert(getTableName(), null, data);
                data.put(AttributeName.Id, id);
                Log.e(Sharenda.Log, "Insert Succes in " + getTableName() + " :  id=" + getId());
            }
        } catch (SQLiteException ex) {
            Log.e(Sharenda.Log, "Exception SQL lors de la sauvegarde - " + ex);
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Insert Error in "  + getTableName() + " : with id=" + getId() + "=> " + e);
        }
        return id;
    }

    protected long delete(long instanceId) {
        try {
            instanceDatabase.delete(getTableName(), ColumnName.ID + " = " + instanceId, null);
            Log.e(Sharenda.Log, "Delete Succes in " + getTableName() + " with id=" + instanceId);
            return 0;
        } catch (SQLiteException ex) {
            Log.e(Sharenda.Log, "Exception SQL lors de la suppression (" + getTableName() + "): " + ex);
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Delete Error (" + getTableName() + ") - " + getTableName() + " " + e);
        }
        return -1;
    }

}