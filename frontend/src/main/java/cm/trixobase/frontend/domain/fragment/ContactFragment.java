package cm.trixobase.frontend.domain.fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.frontend.PhoneActivity;
import cm.trixobase.frontend.R;
import cm.trixobase.frontend.domain.adapter.ContactAdapter;
import cm.trixobase.library.common.manager.Response;
import cm.trixobase.library.common.widget.ProgressBox;
import cm.trixobase.library.common.widget.SnackBar;
import cm.trixobase.library.service.Service;
import dmax.dialog.SpotsDialog;

/*
 * Powered by Trixobase Enterprise on 08/06/18.
 * updated on 01/03/21.
 */

public class ContactFragment extends Fragment {

    private PhoneActivity activity;
    private Service service;
    private List<ContentValues> contacts;

    private SpotsDialog progressBox;

    private ListView listView;
    private LinearLayout ll_search;
    private EditText et_search;
    private TextView tv_empty;

    public ContactFragment(PhoneActivity activity) {
        this.activity = activity;
        this.service = activity.getService();
        this.contacts = new ArrayList<>();
    }

    private static class MyAsyncTask extends AsyncTask<ContentValues, ContentValues, Integer> {

        private static ContactAdapter adapter;

        private MyAsyncTask(ListView listView) {
            this.adapter = (ContactAdapter) listView.getAdapter();
        }

        @Override
        protected Integer doInBackground(ContentValues... values) {
            adapter.add(values[0]);
            return 0;
        }

        protected void onProgressUpdate(ContentValues... item) {
            adapter.refresh();
        }

        @Override
        protected void onPostExecute(Integer result) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_contact, null);

        listView = view.findViewById(R.id.lv_phone_contact_id);
        ll_search = view.findViewById(R.id.ll_search_id);
        et_search = view.findViewById(R.id.et_search_id);
        tv_empty = view.getRootView().findViewById(R.id.tv_phone_contact_id);

        ContactAdapter adapter = new ContactAdapter(activity);
        listView.setAdapter(adapter);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        blockWidgets();
        service.getAllContacts(activity, resultContact);
        return view;
    }

    private Response.onFrontResult resultContact = new Response.onFrontResult() {
        @Override
        public void onTrue(Response response) {
            activeWidgets();
            contacts = response.getResult();
            if (contacts.isEmpty()) {
                ll_search.setVisibility(View.GONE);
                tv_empty.setText(getResources().getString(R.string.error_empty_contact));
            } else {
                ll_search.setVisibility(View.VISIBLE);
                tv_empty.setText("");
                for (ContentValues contact : contacts) {
                    new MyAsyncTask(listView).execute(contact);
                }
            }
        }

        @Override
        public void onFalse(Response response) {
            activeWidgets();
            showMessage(response.getMessage());
        }

        @Override
        public void onMessage(String message) {
            activeWidgets();
            showMessage(message);
        }
    };

    private void blockWidgets() {
        progressBox = ProgressBox.builder(service.getContext()).build();
        progressBox.show();
    }

    private void activeWidgets() {
        if (progressBox != null && progressBox.isShowing())
            progressBox.dismiss();
    }

    private void showMessage(String message) {
        SnackBar.builder(service.getContext(), service.getView()).setMessage(message).showShort();
    }

}