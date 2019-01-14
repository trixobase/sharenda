package cm.trixobase.sharenda.domain.fragment.event;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.system.view.listview.ListViewActivityAdapter;

/**
 * Created by noumianguebissie on 5/15/18.
 */

@SuppressLint("ValidFragment")
public class HistoricFragment extends Fragment {

    private List<UiActivity> activities;
    private ListView lv_activity;

    public HistoricFragment(List<UiActivity> activityList) {
        this.activities = activityList;
    }

    @Override
    public void onStart() {
        super.onStart();
        new MyTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_historic, null);

        TextView textView = view.getRootView().findViewById(R.id.tv_nothing_historic_id);
        if (activities.isEmpty())
            textView.setText(getResources().getString(R.string.label_text_fragment_historic_null));
         else textView.setText(getResources().getString(R.string.label_text_fragment_historic_null_not));
        textView.setTextSize(16);

        lv_activity = view.getRootView().findViewById(R.id.lv_event_historic_id);
        lv_activity.setAdapter(new ListViewActivityAdapter(getContext(), new ArrayList<>(), true));
        return view;
    }

    public class MyTask extends AsyncTask<Void, UiActivity, UiActivity> {

        private ListViewActivityAdapter adapter;
        private List<UiActivity> activityList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            adapter = (ListViewActivityAdapter) lv_activity.getAdapter();
            activityList.addAll(activities);
        }

        @Override
        protected UiActivity doInBackground(Void... voids) {
            for (UiActivity item : activityList) {
                publishProgress(item);
            }
            return null;
        }

        protected void onProgressUpdate(UiActivity... item) {
            adapter.add(item[0]);
        }

        @Override
        protected void onPostExecute(UiActivity result) {
            super.onPostExecute(result);
        }
    }

}