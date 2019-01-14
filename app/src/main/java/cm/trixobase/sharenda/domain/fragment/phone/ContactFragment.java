package cm.trixobase.sharenda.domain.fragment.phone;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.system.view.listview.ListViewContactAdapter;

/**
 * Created by noumianguebissie on 6/8/18.
 */

@SuppressLint("ValidFragment")
public class ContactFragment extends Fragment {

    private static List<UiContact> contacts;
    private static ListView lv_contact;

    private EditText et_search;
    private ListViewContactAdapter myAdapter;

    public ContactFragment(List<UiContact> contacts) {
        this.contacts = new ArrayList<>();
        this.contacts.addAll(contacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_contact, null);
        lv_contact = view.findViewById(R.id.lv_phone_contact_id);
        et_search = view.findViewById(R.id.et_phone_search_id);

        myAdapter = new ListViewContactAdapter(getContext(),  new ArrayList<>());
        lv_contact.setAdapter(myAdapter);

        if (contacts.isEmpty()) {
            TextView textView = view.getRootView().findViewById(R.id.tv_phone_contact_id);
            textView.setText(getResources().getString(R.string.label_message_fragment_contact_null));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                myAdapter.getFilter().filter(arg0);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        et_search.setText("");
        new ListViewAsyncTask().execute();
    }

    public class ListViewAsyncTask extends AsyncTask<Void, UiContact, UiContact> {

        private ListViewContactAdapter adapter;
        private List<UiContact> contactList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            adapter = (ListViewContactAdapter) lv_contact.getAdapter();
            contactList = contacts;
        }

        @Override
        protected UiContact doInBackground(Void... voids) {
            for (UiContact item : contactList) {
                publishProgress(item);
            }
            return null;
        }

        protected void onProgressUpdate(UiContact... item) {
            adapter.add(item[0]);
        }

        @Override
        protected void onPostExecute(UiContact result) {
            super.onPostExecute(result);
        }
    }

}