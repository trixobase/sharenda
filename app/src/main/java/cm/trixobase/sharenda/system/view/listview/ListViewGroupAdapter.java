package cm.trixobase.sharenda.system.view.listview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.TranslateElement;
import cm.trixobase.sharenda.domain.activity.group.GroupEditActivity;
import cm.trixobase.sharenda.domain.activity.SendSmsActivity;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.ImageProcess;

/**
 * Created by noumianguebissie on 5/18/18.
 */

public class ListViewGroupAdapter extends BaseAdapter {

    private ViewHolder holder;

    private static class ViewHolder {
        ImageView iv_group;
        TextView tv_name, tv_members_size;
        LinearLayout layout_sms;
    }

    private final Context context;
    private List<UiGroup> groups;

    private final LayoutInflater inflater;

    public ListViewGroupAdapter(Context context, List<UiGroup> groups) {
        this.context = context;
        this.groups = groups;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public UiGroup getItem(int i) {
        return groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return groups.indexOf(groups.get(i));
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        if (itemView == null) {
            holder = new ListViewGroupAdapter.ViewHolder();
            itemView = inflater.inflate(R.layout.listview_groups_item, null);
            holder.iv_group = itemView.getRootView().findViewById(R.id.iv_avatar_group_id);
            holder.tv_name = itemView.getRootView().findViewById(R.id.tv_name_group_id);
            holder.tv_members_size = itemView.getRootView().findViewById(R.id.tv_members_group_id);
            holder.layout_sms = itemView.getRootView().findViewById(R.id.ll_group_sms_id);
            itemView.setTag(holder);
        }
        notifyDataSetChanged();

        holder = (ViewHolder) itemView.getTag();

        final UiGroup groupToDisplay = groups.get(position);

        holder.tv_name.setText(groupToDisplay.getName());
        holder.tv_members_size.setText(
                groupToDisplay.getMembersCount()
                .concat(" (")
                .concat(Manager.getTranslate(itemView.getResources(), TranslateElement.Category, groupToDisplay.getCategory()))
                .concat(")"));

        Bitmap bm = ImageProcess.getGroupPicture(itemView.getContext(), groupToDisplay.getName());
        if(bm != null) {
            holder.iv_group.setImageBitmap(bm);
        }
        else {
            switch (groupToDisplay.getCategory()) {
                case AttributeName.Category_Business:
                    holder.iv_group.setImageResource(R.drawable.iv_business);
                    break;
                case AttributeName.Category_Work:
                    holder.iv_group.setImageResource(R.drawable.iv_work);
                    break;
                case AttributeName.Category_Sport:
                    holder.iv_group.setImageResource(R.drawable.iv_sport);
                    break;
                case AttributeName.Category_Studies:
                    holder.iv_group.setImageResource(R.drawable.iv_study);
                    break;
                case AttributeName.Category_Family:
                    holder.iv_group.setImageResource(R.drawable.iv_familiy);
                    break;
                case AttributeName.Category_Enjoy:
                    holder.iv_group.setImageResource(R.drawable.iv_enjoy);
                    break;
            }
        }

        View.OnClickListener editListener = view -> {
            Intent i = new Intent(context, GroupEditActivity.class);
            i.putExtra(AttributeName.Group_Id, groupToDisplay.getId());
            context.startActivity(i);
        };
        holder.iv_group.setOnClickListener(editListener);
        holder.tv_name.setOnClickListener(editListener);
        holder.tv_members_size.setOnClickListener(editListener);

        holder.layout_sms.setOnClickListener(view -> {
            Manager.saveData(context, AttributeName.Page_Sms_To_Show, AttributeName.Page_Sms_Group);
            Intent i = new Intent(context, SendSmsActivity.class);
            i.putExtra(AttributeName.Group_Id, groupToDisplay.getId());
            context.startActivity(i);
        });

        return itemView;
    }

}
