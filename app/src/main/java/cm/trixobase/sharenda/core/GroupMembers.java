package cm.trixobase.sharenda.core;

import android.content.ContentValues;

import java.util.List;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.system.DomainObject;

/**
 * Created by noumianguebissie on 5/12/18.
 */

public class GroupMembers extends DomainObject {

    @Override
    protected String getTableName() {
        return TableName.GROUP_MEMBERS;
    }

    public static class Builder extends DomainObject.Builder<GroupMembers> {

        @Override
        protected GroupMembers newInstance() {
            return new GroupMembers();
        }

        @Override
        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            return this;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public long save(long groupId, List<String> numbers) {
        long groupMembersId = -1;
        for (int i=0 ; i<numbers.size() ; i++) {
            id = -1;
            getData().clear();
            getData().put(ColumnName.GROUP_MEMBERS_GROUP_ID, groupId);
            getData().put(ColumnName.GROUP_MEMBERS_CONTACT_NUMBER, numbers.get(i));
            groupMembersId = super.save();
            if(groupMembersId == -1) {
                return groupMembersId;
            }
        }
        return groupMembersId;
    }
}
