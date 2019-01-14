package cm.trixobase.sharenda.system.view.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.FileProcess;
import cm.trixobase.sharenda.system.media.ImageProcess;

/**
 * Created by noumianguebissie on 5/16/18.
 */

public class ListViewActivityAdapter extends BaseAdapter {

    public class ViewHolder {
        ImageView iv_contact, iv_category;
        TextView tv_title, tv_date;
    }

    private final Context context;
    private List<UiActivity> activities;
    private LayoutInflater inflater;
    private int dateColor = 0;
    private boolean canShowPicture;

    public ListViewActivityAdapter(Context context, List<UiActivity> activities, boolean showPicture) {
        this.context = context;
        this.activities = activities;
        this.canShowPicture = showPicture;
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public UiActivity getItem(int i) {
        return activities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return activities.indexOf(activities.get(i));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.listview_event_item, null);
            holder.iv_contact = itemView.getRootView().findViewById(R.id.iv_avatar_owner_id);
            holder.tv_title = itemView.getRootView().findViewById(R.id.tv_title_lv_id);
            holder.tv_date = itemView.getRootView().findViewById(R.id.tv_date_lv_id);
            holder.iv_category = itemView.getRootView().findViewById(R.id.iv_flag_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        final UiActivity activityToDisplay = activities.get(position);
        final Resources ress = itemView.getResources();

        UiContact activityOwner = Contact.getByNumber(context, activityToDisplay.getOwner());

        if (canShowPicture) {
            Bitmap bm = ImageProcess.getContactPicture(context, activityOwner.getName());
            if (bm != null)
                holder.iv_contact.setImageBitmap(bm);
            else holder.iv_contact.setImageResource(R.drawable.ic_camera);
        }

        holder.tv_title.setText(activityToDisplay.getTitle().concat(" (").concat(activityToDisplay.getInvites(context)).concat(")"));

        holder.tv_date.setText(Manager.date.computeDateToDisplay(
                ress,
                activityToDisplay.getDate(),
                activityToDisplay.getBeginHour()));

        dateColor = Manager.date.computeDateTextColor(
                activityToDisplay.getDate(),
                activityToDisplay.getBeginHour());
        switch (dateColor) {
            case 1:
                holder.tv_date.setTextColor(itemView.getResources().getColor(R.color.text_widget_form_error));
                break;
            case 2:
                holder.tv_date.setTextColor(itemView.getResources().getColor(R.color.colorOrange));
                break;
            case 3:
                holder.tv_date.setTextColor(itemView.getResources().getColor(R.color.colorGreen));
                break;
            default:
                holder.tv_date.setTextColor(itemView.getResources().getColor(R.color.colorBlue));
                break;

        }

        switch (activityToDisplay.getType()) {
            case AttributeName.Category_Enjoy:
                holder.iv_category.setImageResource(R.drawable.ic_category_enjoy);
                break;
            case "Games":
            case "Jeux":
                holder.iv_category.setImageResource(R.drawable.ic_category_advert);
                break;
            case AttributeName.Category_Work:
                holder.iv_category.setImageResource(R.drawable.ic_category_work);
                break;
            case AttributeName.Category_Studies:
                holder.iv_category.setImageResource(R.drawable.ic_category_read);
                break;
            case AttributeName.Category_Sport:
                holder.iv_category.setImageResource(R.drawable.ic_category_sport);
                break;
            case AttributeName.Category_Family:
                holder.iv_category.setImageResource(R.drawable.ic_category_family);
                break;
            case AttributeName.Category_Business:
                holder.iv_category.setImageResource(R.drawable.ic_category_business);
                break;
            default:
                holder.iv_category.setImageResource(R.drawable.ic_category_personnal);
                break;
        }

        return itemView;
    }

    public void add(UiActivity newActivity) {
        activities.add(newActivity);
        notifyDataSetChanged();
    }

}