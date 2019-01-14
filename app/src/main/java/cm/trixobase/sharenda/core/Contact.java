package cm.trixobase.sharenda.core;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.DomainObject;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.database.RequestHandler;
import cm.trixobase.sharenda.system.media.PhoneProcess;

/**
 * Created by noumianguebissie on 4/15/18.
 */

public class Contact extends RequestHandler {

    @Override
    protected String getTableName() {
        return TableName.CONTACT;
    }

    public static class Builder extends DomainObject.Builder<Contact> {

        private Builder() {
            super();
        }

        @Override
        protected Contact newInstance() {
            return new Contact();
        }

        @Override
        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            instance.setName(newData.getAsString(AttributeName.Contact_Name))
                    .setNumber(newData.getAsString(AttributeName.Contact_Number))
                    .setNote(newData.getAsLong(AttributeName.Contact_Note));
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Contact setName(String name) {
        this.name = name;
        return this;
    }

    private Contact setNumber(String number) {
        this.number = number;
        return this;
    }

    private Contact setNote(long note) {
        this.note = note;
        return this;
    }

    public static List<UiContact> getAll(Context context) {
        return PhoneProcess.getAllContacts(context.getContentResolver());
    }

    public static UiContact getByNumber(Context context, String number) {
        return PhoneProcess.getByNumber(context, number);
    }

    public static List<UiContact> getAllByGroup(Context context, long groupId) {
        List<UiContact> members = new ArrayList<>();
        startTransaction(context);
        List<String> numbers = RequestHandler.getAllNumbersMembersByGroup(groupId);
        stopTransaction();
        for (String number : numbers)
            members.add(PhoneProcess.getByNumber(context, number));
        return members;
    }

    public static List<String> getAllNumbersByGroup(Context context, long groupId) {
        startTransaction(context);
        List<String> numbers = RequestHandler.getAllNumbersMembersByGroup(groupId);
        stopTransaction();
        return numbers;
    }

    private String name;
    private String number;
    private long note;

    private long save(Context context) {
        getData().put(ColumnName.CONTACT_NAME, name);
        getData().put(ColumnName.CONTACT_NUMBER, number);
        getData().put(ColumnName.CONTACT_NOTE, note);
        startTransaction(context);
        long contactId = super.save();
        stopTransaction();
        return contactId;
    }

    public static void saveOwner(Context context) {
        ContentValues ownerContactData = new ContentValues();
        ownerContactData.put(AttributeName.Contact_Name, Sharenda.Contact_NAME);
        ownerContactData.put(AttributeName.Contact_Number, Sharenda.Contact_Number);
        ownerContactData.put(AttributeName.Contact_Note, Sharenda.Contact_Note);

        builder().withData(ownerContactData).build().save(context);
    }

}

