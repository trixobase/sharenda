package cm.trixobase.sharenda.system.view.listview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.domain.activity.SendSmsActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.ImageProcess;

/**
 * Created by noumianguebissie on 6/9/18.
 */

public class ListViewContactAdapter extends BaseAdapter implements Filterable {

    private static class ViewHolder {
        ImageView iv_contact;
        TextView tv_name, tv_number;
        LinearLayout layout_call, layout_sms;
    }

    private final Context context;
    private List<UiContact> contactsList;
    private List<UiContact> contactsFilter;
    private LayoutInflater inflater;
    private ValueFilter valueFilter;

    public ListViewContactAdapter(Context context, List<UiContact> contacts) {
        this.context = context;
        this.contactsList = contacts;
        this.contactsFilter = contacts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
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
        ViewHolder holder;
        if (itemView == null) {
            holder = new ListViewContactAdapter.ViewHolder();
            itemView = inflater.inflate(R.layout.listview_contact_item, null);
            holder.iv_contact = itemView.findViewById(R.id.iv_contact_avatar_id);
            holder.tv_name = itemView.findViewById(R.id.tv_contact_name_id);
            holder.tv_number = itemView.findViewById(R.id.tv_contact_number_id);
            holder.layout_call = itemView.findViewById(R.id.ll_contact_call_id);
            holder.layout_sms = itemView.findViewById(R.id.ll_contact_sms_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        UiContact contactToDisplay = contactsList.get(position);

        Bitmap contactPicture = ImageProcess.getContactPicture(context, contactToDisplay.getName());
        if (contactPicture != null) {
            holder.iv_contact.setImageBitmap(contactPicture);
        } else {
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
        }

        if (contactToDisplay.getNumber().equalsIgnoreCase(BaseName.Trixobase_Number_To_Display))
            holder.iv_contact.setImageResource(R.drawable.trixobase);

        holder.tv_name.setText(contactToDisplay.getDisplayName());
        holder.tv_number.setText(contactToDisplay.getNumber());

        View.OnClickListener editListener = (View view) -> {
//            Intent i = new Intent(context, ContactEditActivity.class);
//            i.putExtra(AttributeName.Contact_Id, contactToDisplay.getIdentification());
//            i.putExtra(AttributeName.Interface_Page_To_show, PhoneActivity.PAGE_CONTACT);
//            context.startActivity(i);
        };
        View.OnClickListener smsListener = view -> {
            Manager.saveData(context, AttributeName.Page_Sms_To_Show, AttributeName.Page_Sms_Contact);
            Intent i = new Intent(context, SendSmsActivity.class);
            i.putExtra(AttributeName.Contact_Name, contactToDisplay.getName());
            i.putExtra(AttributeName.Contact_Number, contactToDisplay.getNumber());
            context.startActivity(i);
        };
        View.OnClickListener callListener = view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactToDisplay.getNumber()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, Sharenda.Log + "Call permission", Toast.LENGTH_SHORT).show();
            } else context.startActivity(callIntent);
        };

        holder.iv_contact.setOnClickListener(editListener);
        holder.tv_name.setOnClickListener(editListener);
        holder.tv_number.setOnClickListener(editListener);
        holder.layout_sms.setOnClickListener(smsListener);
        holder.layout_call.setOnClickListener(callListener);

        return itemView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null)
            valueFilter = new ValueFilter();
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<UiContact> filterList = new ArrayList<>();
                for (int i = 0; i < contactsFilter.size(); i++) {
                    if ((contactsFilter.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())
                            || contactsFilter.get(i).getNumber()
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


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactsList = (List<UiContact>) results.values;
            notifyDataSetChanged();
        }
    }

    public void add(UiContact contact) {
        contactsList.add(contact);
        notifyDataSetChanged();
    }

}