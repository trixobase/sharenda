package cm.trixobase.frontend.domain.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.frontend.PhoneActivity;
import cm.trixobase.frontend.R;
import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.library.common.constants.BaseName;
import cm.trixobase.library.common.domain.UiContact;
import cm.trixobase.library.common.manager.Manager;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 07/05/18.
 * updated on 01/03/21.
 */

public class ContactAdapter extends BaseAdapter implements Filterable {

    private PhoneActivity activity;
    private Service service;
    private List<ContentValues> contactsList;
    private List<ContentValues> contactsFilter;

    private LayoutInflater inflater;
    private ValueFilter valueFilter;

    public class ViewHolder {
        ImageView iv_contact;
        TextView tv_name, tv_number;
        LinearLayout layout_call, layout_sms;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<ContentValues> filterList = new ArrayList<>();
                for (int i = 0; i < contactsFilter.size(); i++) {
                    if ((contactsFilter.get(i).getAsString(BaseName.NAME).toLowerCase())
                            .contains(constraint.toString().toLowerCase())
                            || contactsFilter.get(i).getAsString(BaseName.NUMBER)
                            .contains(constraint.toString())) {
                        filterList.add(contactsFilter.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = contactsFilter.size();
                results.values = contactsFilter;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactsList = (List<ContentValues>) results.values;
            notifyDataSetChanged();
        }
    }

    public ContactAdapter(PhoneActivity activity) {
        this.activity = activity;
        this.service = activity.getService();
        this.contactsList = new ArrayList<>();
        this.contactsFilter = new ArrayList<>();

        this.inflater = (LayoutInflater) service.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null)
            valueFilter = new ValueFilter();
        return valueFilter;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public ContentValues getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactsList.indexOf(getItem(position));
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.listview_contact_item, null);
            holder.iv_contact = itemView.findViewById(R.id.iv_contact_avatar_id);
            holder.tv_name = itemView.findViewById(R.id.tv_name_id);
            holder.tv_number = itemView.findViewById(R.id.tv_number_id);
            holder.layout_call = itemView.findViewById(R.id.ll_contact_call_id);
            holder.layout_sms = itemView.findViewById(R.id.ll_contact_sms_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        Context context = service.getContext();
        UiContact contactToDisplay = UiContact.builder(contactsList.get(position)).build();

        service.setPicture(holder.iv_contact, contactToDisplay.getPictureOperator());

        if (contactToDisplay.getNumber().contains("690867833"))
            service.setPicture(holder.iv_contact, R.drawable.iv_trixobase);

        holder.tv_name.setText(contactToDisplay.getDisplayName());
        holder.tv_number.setText(contactToDisplay.getNumber());

        holder.layout_sms.setOnClickListener(view -> {
            Manager.setMetaDada(context, AttributeName.Page_Sms_To_Show, AttributeName.Page_Sms_Contact);
//            Intent i = new Intent(context, SendSmsActivity.class);
//            i.putExtra(AttributeName.Contact_Name, contactToDisplay.getName());
//            i.putExtra(AttributeName.Contact_Number, contactToDisplay.getNumber());
//            context.startActivity(i);
        });
        holder.layout_call.setOnClickListener(view -> {
            service.sendCall(activity, contactToDisplay.getNumber());
        });

        return itemView;
    }

    public void add(ContentValues data) {
        contactsList.add(data);
        contactsFilter.add(data);
    }

    public void refresh() {
        notifyDataSetChanged();
    }

}
