package cm.trixobase.sharenda.domain.ui;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.system.UiDomainObject;

/**
 * Created by noumianguebissie on 5/13/18.
 */

public class UiUser extends UiDomainObject {

    public static class Builder extends UiDomainObject.Builder<UiUser> {
        @Override
        protected UiUser newInstance() {
            return new UiUser();
        }
    }

    public static UiUser.Builder builder() {
        return new UiUser.Builder();
    }

    public UiUser setCivility(String civility) {
        getData().put(AttributeName.User_Civility, civility);
        return this;
    }

    public String getCivility() {
        return getData().getAsString(AttributeName.User_Civility);
    }

    public UiUser setSurname(String surname) {
        getData().put(AttributeName.User_Surname, surname);
        return this;
    }

    public String getSurname() {
        return getData().getAsString(AttributeName.User_Surname);
    }

    public UiUser setAge(long age) {
        getData().put(AttributeName.User_Age, age);
        return this;
    }

    public long getAge() {
        return getData().getAsLong(AttributeName.User_Age);
    }

    public UiUser setCivilStatus(String civilStatus) {
        getData().put(AttributeName.User_Civil_Status, civilStatus);
        return this;
    }

    public String getCivilStatus() {
        return getData().getAsString(AttributeName.User_Civil_Status);
    }

    public UiUser setPassword(long password) {
        getData().put(AttributeName.User_Password, password);
        return this;
    }

    public UiUser setHabit(String habit) {
        getData().put(AttributeName.User_Habit, habit);
        return this;
    }

    public String getHabit() {
        return getData().getAsString(AttributeName.User_Habit);
    }

}
