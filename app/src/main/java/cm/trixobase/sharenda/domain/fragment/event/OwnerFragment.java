package cm.trixobase.sharenda.domain.fragment.event;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.view.listview.ListViewActivityAdapter;

@SuppressLint("ValidFragment")
public class OwnerFragment extends Fragment {

    private List<UiActivity> activities;
    private final UiActivity priorityActivity;
    private int dateColor = 0;

    private Resources resources;
    private ListView lv_activity;
    private TextView tv_date;
    private ImageView imgCategory;

    public OwnerFragment(List<UiActivity> ownerActivityList, UiActivity priorityActivity) {
        this.activities = ownerActivityList;
        this.priorityActivity = priorityActivity;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_owner, container, false);
        resources = view.getResources();

        final TextView title = view.getRootView().findViewById(R.id.tv_title_owner_id);
        if(priorityActivity != null) {
            title.setText(priorityActivity.getTitle().concat(" (").concat(priorityActivity.getInvites(getContext())).concat(")"));

            imgCategory = view.getRootView().findViewById(R.id.iv_flag_priority_id);
            tv_date = view.getRootView().findViewById(R.id.tv_date_owner_id);

            tv_date.setText(Manager.date.computeDateToDisplay(
                    resources,
                    priorityActivity.getDate(),
                    priorityActivity.getBeginHour()));
            dateColor = Manager.date.computeDateTextColor(
                    priorityActivity.getDate(),
                    priorityActivity.getBeginHour());
            colorDateText();
            computePictureTypeToShow(priorityActivity.getType());
        } else {
            title.setText(getResources().getString(R.string.label_text_fragment_owner_null));
        }

        lv_activity = view.getRootView().findViewById(R.id.lv_status_owner_id);
        lv_activity.setAdapter(new ListViewActivityAdapter(getContext(), new ArrayList<>(), false));

        return view;
    }

    private void colorDateText() {
        switch (dateColor) {
            case 1:
                tv_date.setTextColor(resources.getColor(R.color.text_widget_form_error));
                break;
            case 2:
                tv_date.setTextColor(resources.getColor(R.color.colorOrange));
                break;
            case 3:
                tv_date.setTextColor(resources.getColor(R.color.colorGreen));
                break;
            default:
                tv_date.setTextColor(resources.getColor(R.color.colorBlue));
                break;
        }
    }

    private void computePictureTypeToShow(String type) {
        switch (type) {
            case AttributeName.Category_Enjoy:
                imgCategory.setImageResource(R.drawable.ic_category_enjoy);
                break;
            case "Games":
            case "Jeux":
                imgCategory.setImageResource(R.drawable.ic_category_advert);
                break;
            case AttributeName.Category_Work:
                imgCategory.setImageResource(R.drawable.ic_category_work);
                break;
            case AttributeName.Category_Studies:
                imgCategory.setImageResource(R.drawable.ic_category_read);
                break;
            case AttributeName.Category_Sport:
                imgCategory.setImageResource(R.drawable.ic_category_sport);
                break;
            case AttributeName.Category_Family:
                imgCategory.setImageResource(R.drawable.ic_category_family);
                break;
            case AttributeName.Category_Business:
                imgCategory.setImageResource(R.drawable.ic_category_business);
                break;
            default:
                imgCategory.setImageResource(R.drawable.ic_category_personnal);
                break;
        }
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
