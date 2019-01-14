package cm.trixobase.sharenda.core;

import android.content.ContentValues;
import android.content.Context;

import java.util.Calendar;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.domain.ui.UiUser;
import cm.trixobase.sharenda.system.DomainObject;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.manager.UserManager;
import cm.trixobase.sharenda.system.database.RequestHandler;

/**
 * Created by noumianguebissie on 4/27/18.
 */

public class User extends RequestHandler {

    @Override
    protected String getTableName() {
        return TableName.USER;
    }

    public static class Builder extends DomainObject.Builder<User> {

        private Builder() {
            super();
        }

        @Override
        protected User newInstance() {
            return new User();
        }

        @Override
        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            instance.setCivility(newData.getAsString(AttributeName.User_Civility))
                    .setSurname(newData.getAsString(AttributeName.User_Surname))
                    .setAge(newData.getAsLong(AttributeName.User_Age))
                    .setCivilStatus(newData.getAsString(AttributeName.User_Civil_Status))
                    .setPassword(newData.getAsLong(AttributeName.User_Password))
                    .setHabit(newData.getAsString(AttributeName.User_Habit));
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private User() {
        super();
    }

    private User setCivility(String civility) {
        this.civility = civility;
        return this;
    }

    private User setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    private User setAge(long age) {
        this.age = age;
        return this;
    }

    private User setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
        return this;
    }

    private User setPassword(long password) {
        this.password = password;
        return this;
    }

    private User setHabit(String habit) {
        this.habit = habit;
        return this;
    }

    public static UiUser getOwner(Context context) {
        startTransaction(context);
        UiUser userAuthenticated = RequestHandler.getAuthenticatedUser();
        stopTransaction();
        return userAuthenticated;
    }

    private String civility;
    private String surname;
    public long age;
    public String civilStatus;
    private long password;
    public String habit;

    public long save(Context context) {
        if (id == -1)
            Contact.saveOwner(instanceContext);
        getData().put(ColumnName.USER_CIVILITY, civility);
        getData().put(ColumnName.USER_SURNAME, surname);
        getData().put(ColumnName.USER_AGE, age);
        getData().put(ColumnName.USER_CIVIL_STATUS, civilStatus);
        getData().put(ColumnName.USER_HABIT, habit);
        getData().put(ColumnName.USER_PASSWORD, password);
        getData().put(ColumnName.USER_REGISTRATION_DATE, Manager.date.getDate(Calendar.getInstance()));
        startTransaction(context);
        long userId = super.save();
        stopTransaction();
        if (userId != -1) {
            UserManager.builder(instanceContext).withUser(this).setAttributes();
        }
        return userId;
    }

}
