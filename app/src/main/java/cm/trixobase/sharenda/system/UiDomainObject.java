package cm.trixobase.sharenda.system;

import android.content.ContentValues;

import cm.trixobase.sharenda.common.AttributeName;


/**
 * Created by noumianguebissie on 4/25/18.
 */

public abstract class UiDomainObject {

    protected ContentValues data;
    protected long id;

    public abstract static class Builder<T extends UiDomainObject> {
        protected final T instance;

        protected Builder() {
            instance = newInstance();
            instance.data = new ContentValues();
            }

        protected abstract T newInstance();

        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            instance.data = newData;
            return this;
        }

        public T build() {
            return instance;
        }

    }

    public void setId(long id) {
        this.data.put(AttributeName.Id, id);
        this.id = id;
    }

    public ContentValues getData() {
        return data;
    }

    public long getId() {
        return id;
    }

}
