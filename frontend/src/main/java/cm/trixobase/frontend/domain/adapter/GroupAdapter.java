package cm.trixobase.frontend.domain.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cm.trixobase.frontend.R;
import cm.trixobase.library.common.domain.UiGroup;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 18/05/18.
 * updated on 01/03/21.
 */

public class GroupAdapter extends BaseAdapter {

    private Service service;
    private List<ContentValues> groups;

    private LayoutInflater inflater;

    private static class ViewHolder {
        ImageView iv_group;
        TextView tv_name, tv_members_size;
        LinearLayout layout_sms;
    }

    public GroupAdapter(Service service, List<ContentValues> groups) {
        this.service = service;
        this.groups = groups;
        this.inflater = (LayoutInflater) service.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public ContentValues getItem(int i) {
        return groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return groups.indexOf(groups.get(i));
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        itemView = inflater.inflate(R.layout.listview_group_item, null);
        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            holder.iv_group = itemView.getRootView().findViewById(R.id.iv_avatar_group_id);
            holder.tv_name = itemView.getRootView().findViewById(R.id.tv_name_group_id);
            holder.tv_members_size = itemView.getRootView().findViewById(R.id.tv_members_group_id);
            holder.layout_sms = itemView.getRootView().findViewById(R.id.ll_group_sms_id);
            itemView.setTag(holder);
        }
        notifyDataSetChanged();

        holder = (ViewHolder) itemView.getTag();

        final UiGroup groupToDisplay = UiGroup.builder(groups.get(position)).build();

        holder.tv_name.setText(groupToDisplay.getName());
//        holder.tv_members_size.setText(
//                groupToDisplay.getMembersCount()
//                        .concat(" (")
//                        .concat(Manager.getTranslate(itemView.getResources(), TranslateElement.Category, groupToDisplay.getCategory()))
//                        .concat(")"));

        Bitmap bm = service.getPicture(groupToDisplay.getName());
        if (bm != null)
            service.setPicture(holder.iv_group, bm);
        else service.setPicture(holder.iv_group, groupToDisplay.getPictureCategory());

        View.OnClickListener editListener = view -> {
//            Intent i = new Intent(service.getContext(), GroupEditActivity.class);
//            i.putExtra(AttributeName.NAME, groupToDisplay.getName());
//            service.getContext().startActivity(i);
        };
        holder.iv_group.setOnClickListener(editListener);
        holder.tv_name.setOnClickListener(editListener);
        holder.tv_members_size.setOnClickListener(editListener);

        holder.layout_sms.setOnClickListener(view -> {
//            Manager.setMetaDada(service.getContext(), AttributeName.Page_Sms_To_Show, AttributeName.Page_Sms_Group);
//            Intent i = new Intent(service.getContext(), SendSmsActivity.class);
//            i.putExtra(AttributeName.NAME, groupToDisplay.getName());
//            service.getContext().startActivity(i);
        });

        return itemView;
    }

}
