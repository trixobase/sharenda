package cm.trixobase.sharenda.domain.ui;

import android.support.annotation.NonNull;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.system.UiDomainObject;
import cm.trixobase.sharenda.system.manager.Manager;

/**
 * Created by noumianguebissie on 5/13/18.
 */

public class UiContact extends UiDomainObject implements Comparable<UiContact> {

    @Override
    public int compareTo(@NonNull UiContact contact) {
        return this.getName().compareTo(contact.getName());
    }

    private boolean isChecked = false;

    public static class Builder extends UiDomainObject.Builder<UiContact> {
        @Override
        protected UiContact newInstance() {
            return new UiContact();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return getData().getAsString(AttributeName.Contact_Name);
    }

    public String getDisplayName() {
        return Manager.compute.contactName(getName());
    }

    public String getNumber() {
        return getData().getAsString(AttributeName.Contact_Number);
    }

    public String getOperator() {
        return getData().getAsString(AttributeName.Contact_Operator);
    }

    public long getNote() {
        return getData().getAsLong(AttributeName.Contact_Note);
    }

    public UiContact setName(String name) {
        getData().put(AttributeName.Contact_Name, name);
        return this;
    }

    public UiContact setNumber(String number) {
        getData().put(AttributeName.Contact_Number, number);
        return this;
    }

    public UiContact setOperator(String operator) {
        getData().put(AttributeName.Contact_Operator, operator);
        return this;
    }


    public UiContact setNote(long note) {
        getData().put(AttributeName.Contact_Note, note);
        return this;
    }

    public void setChecked(boolean state) {
        isChecked = state;
    }

    public boolean isChecked() {
        return isChecked;
    }

}
