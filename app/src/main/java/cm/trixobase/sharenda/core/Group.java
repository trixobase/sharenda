package cm.trixobase.sharenda.core;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.ColumnName;
import cm.trixobase.sharenda.common.TableName;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.DomainObject;
import cm.trixobase.sharenda.system.database.RequestHandler;

/**
 * Created by noumianguebissie on 5/11/18.
 */

public class Group extends RequestHandler {

    @Override
    protected String getTableName() {
        return TableName.GROUP;
    }

    public static class Builder extends DomainObject.Builder<Group> {

        private Builder(Context context) {
            super();
            instanceContext = context;
        }

        @Override
        protected Group newInstance() {
            return new Group();
        }

        @Override
        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            List<String> numbers = new ArrayList<>();
            String groupName = newData.getAsString(AttributeName.Group_Name);
            for (long i = 0; i<newData.getAsLong(AttributeName.Group_Members_Size) ; i++) {
                numbers.add(newData.getAsString(AttributeName.Group_Members_Numbers + String.valueOf(i)));
            }
            instance.setName(groupName)
                    .setCategory(newData.getAsString(AttributeName.Group_Category))
                    .setMembers(numbers);
            return this;
        }
    }

    private Group setName(String name) {
        this.name = name;
        return this;
    }

    private Group setCategory(String category) {
        this.category = category;
        return this;
    }

    private Group setMembers(List<String> numbers) {
        this.numbersList = numbers;
        return this;
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static List<UiGroup> getAll(Context context) {
        startTransaction(context);
        List<UiGroup> groupList = RequestHandler.getAllGroups();
        stopTransaction();
        return groupList;
    }

    public static List<UiGroup> getAllByCategory(Context context, String category) {
        startTransaction(context);
        List<UiGroup> groupList = RequestHandler.getAllGroupsByCategory(category);
        stopTransaction();
        return groupList;
    }

    public static UiGroup getById(Context context, long groupId) {
        startTransaction(context);
        UiGroup group = RequestHandler.getGroupById(groupId);
        stopTransaction();
        return group;
    }

    private String name;
    private String category;
    private List<String> numbersList;

    public long save() {
        getData().put(ColumnName.GROUP_NAME, name);
        getData().put(ColumnName.GROUP_CATEGORY, category);

        startTransaction(instanceContext);
        if (getId() != -1) {
            instanceDatabase.delete(TableName.GROUP_MEMBERS, ColumnName.GROUP_MEMBERS_GROUP_ID + " = ?", new String[] {String.valueOf(getId())});
        }
        long groupId = super.save();
        if (groupId != -1)
            GroupMembers.builder().build().save(groupId, numbersList);
        stopTransaction();
        return groupId;
    }

    public long delete(long groupId) {
        startTransaction(instanceContext);
        long success = super.delete(groupId);
        if (success != -1) {
            instanceDatabase.delete(TableName.GROUP_MEMBERS, ColumnName.GROUP_MEMBERS_GROUP_ID + " = ?", new String[] {String.valueOf(groupId)});
            Activity.deleteGroupInActivities(groupId);
        }
        stopTransaction();
        return success;
    }

}
