package cm.trixobase.sharenda.domain.ui;

import java.util.ArrayList;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.system.UiDomainObject;

/**
 * Created by noumianguebissie on 4/15/18.
 */

public class UiGroup extends UiDomainObject {

    public static class Builder extends UiDomainObject.Builder<UiGroup> {
        @Override
        protected UiGroup newInstance() {
            return new UiGroup();
        }

        public Builder identifiedById(long id) {
            instance.data.put(AttributeName.Id, id);
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public UiGroup setName(String name) {
        getData().put(AttributeName.Group_Name, name);
        return this;
    }

    public UiGroup setCategory(String category) {
        getData().put(AttributeName.Group_Category, category);
        return this;
    }

    public UiGroup setMembersCount(long membersCount) {
        getData().put(AttributeName.Group_Members, membersCount);
        return this;
    }

    public UiGroup setMembersNumber(ArrayList<String> membersNumber) {
        for (int t=0 ; t<membersNumber.size() ; t++)
            getData().put(AttributeName.Group_Members_Numbers + String.valueOf(t), membersNumber.get(t));
        getData().put(AttributeName.Group_Members_Size, membersNumber.size());
        return this;
    }

    public String getName() {
        return getData().getAsString(AttributeName.Group_Name);
    }

    public String getCategory() {
        return getData().getAsString(AttributeName.Group_Category);
    }

    public String getMembersCount() {
        String count = String.valueOf(getData().getAsLong(AttributeName.Group_Members));
        if (count.length() == 1)
            count = 0 + count;
        return count;
    }

}
