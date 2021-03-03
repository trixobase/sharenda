package cm.trixobase.frontend.domain.fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import cm.trixobase.frontend.R;
import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.frontend.domain.adapter.MessageAdapter;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 30/11/20.
 */

public class InfoMessageFragment extends Fragment {

    private Service service;

    private ListView listView;
    private TextView tv_empty;

    private InfoMessageFragment() {
    }

    public static class Builder {

        private InfoMessageFragment instance;

        private Builder(Service service) {
            instance = new InfoMessageFragment();
            instance.service = service;
        }

        public InfoMessageFragment build() {
            return instance;
        }
    }

    public static Builder builder(Service service) {
        return new Builder(service);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_message, container, false);

        listView = view.findViewById(R.id.listview_id);
        tv_empty = view.findViewById(R.id.tv_empty_list_id);

        listView.setAdapter(new MessageAdapter(service));
        new MyAsyncTask().execute();

        return view;
    }

    private class MyAsyncTask extends AsyncTask<Void, ContentValues, ContentValues> {

        private MessageAdapter mAdapter;
        private List<ContentValues> mData;

        private MyAsyncTask() {
            mAdapter = (MessageAdapter) listView.getAdapter();
            mData = service.getAllOf(AttributeName.ENTITY_MESSAGE, "");
            tv_empty.setText(mData.isEmpty() ? getString(R.string.any_message) : "");
        }

        @Override
        protected ContentValues doInBackground(Void... voids) {
            for (ContentValues item : mData) {
                publishProgress(item);
            }
            return null;
        }

        protected void onProgressUpdate(ContentValues... item) {
            mAdapter.add(item[0]);
        }

        @Override
        protected void onPostExecute(ContentValues result) {
            super.onPostExecute(result);
        }

    }
}