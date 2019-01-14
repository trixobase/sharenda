package cm.trixobase.sharenda.system.view.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.domain.activity.group.GroupEditActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;

/**
 * Created by noumianguebissie on 6/9/18.
 */

public class ListViewGroupMemberAdapter extends BaseAdapter {

    public class ViewHolder {
        ImageView iv_contact;
        TextView tv_name, tv_number;
        LinearLayout ll_group_del;
    }

    private final Context context;
    private List<UiContact> contactsList;
    private LayoutInflater inflater;

    public ListViewGroupMemberAdapter(Context context, List<UiContact> contacts) {
        this.context = context;
        this.contactsList = contacts;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public UiContact getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactsList.indexOf(getItem(position));
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.listview_members_item, null);
            holder.iv_contact = itemView.findViewById(R.id.iv_group_edit_avatar_id);
            holder.tv_name = itemView.findViewById(R.id.tv_group_edit_name_id);
            holder.tv_number = itemView.findViewById(R.id.tv_group_edit_groups_id);
            holder.ll_group_del = itemView.findViewById(R.id.ll_group_edit_del_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        UiContact contactToDisplay = contactsList.get(position);

        holder.tv_name.setText(Manager.compute.contactName(contactToDisplay.getName()));
        holder.tv_number.setText(contactToDisplay.getNumber());
        holder.ll_group_del.setOnClickListener(view -> {
            if (GroupEditActivity.membersNumberToSave.size() > Sharenda.Min_Contact_For_Group) {
                contactsList.remove(position);
                GroupEditActivity.setSubtitle(contactsList.size());
                GroupEditActivity.membersNumberToSave.remove(position);
                notifyDataSetChanged();
            } else Manager.showToastLongMessage(context, context.getString(R.string.error_group_contact_save_del));
        });

        switch (contactToDisplay.getOperator()) {
            case BaseName.Operator_Camtel:
                holder.iv_contact.setImageResource(R.drawable.lg_camtel);
                break;
            case BaseName.Operator_Nexttel:
                holder.iv_contact.setImageResource(R.drawable.lg_nexttel);
                break;
            case BaseName.Operator_Orange:
                holder.iv_contact.setImageResource(R.drawable.lg_orange);
                break;
            case BaseName.Operator_Mtn:
                holder.iv_contact.setImageResource(R.drawable.lg_mtn);
                break;
            default:
                holder.iv_contact.setImageResource(R.drawable.ic_action_phone);
                break;
        }

        return itemView;
    }

}
