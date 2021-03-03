package cm.trixobase.frontend.domain.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cm.trixobase.frontend.R;
import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.frontend.domain.adapter.GroupAdapter;
import cm.trixobase.library.common.constants.BaseName;
import cm.trixobase.library.common.widget.SnackBar;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 08/06/18.
 * updated on 01/03/21.
 */

public class GroupFragment extends Fragment {

    private Service service;

    private List<ContentValues> groups;
    private int[] numbers;

    public GroupFragment(Service service) {
        this.service = service;
        this.groups = service.getAll(BaseName.ENTITY_GROUP);
        this.numbers = getGroupCount();
    }

    private int[] getGroupCount() {
        int[] groupCount = new int[6];
        for (int i=0 ; i<groups.size() ; i++) {
            groupCount[i] = 0;
        }

        for (ContentValues group : groups) {
            String groupName = group.getAsString(AttributeName.NAME);
            switch (groupName) {
                case AttributeName.CATEGORY_WORK:
                    groupCount[0] = service.getContactByGroup(groupName).size();
                    break;
                case AttributeName.CATEGORY_BUSINESS:
                    groupCount[1] = service.getContactByGroup(groupName).size();
                    break;
                case AttributeName.CATEGORY_FAMILY:
                    groupCount[2] = service.getContactByGroup(groupName).size();
                    break;
                case AttributeName.CATEGORY_STUDY:
                    groupCount[3] = service.getContactByGroup(groupName).size();
                    break;
                case AttributeName.CATEGORY_SPORT:
                    groupCount[4] = service.getContactByGroup(groupName).size();
                    break;
                case AttributeName.CATEGORY_ENJOY:
                    groupCount[5] = service.getContactByGroup(groupName).size();
                    break;
            }
        }
        return groupCount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_group, null);

        TextView tv_work, tv_business, tv_family, tv_study, tv_sport, tv_enjoy, tv_empty;

        tv_work = view.findViewById(R.id.tv_group_list_work_id);
        tv_business = view.findViewById(R.id.tv_group_list_business_id);
        tv_family = view.findViewById(R.id.tv_group_list_family_id);
        tv_study = view.findViewById(R.id.tv_group_list_study_id);
        tv_sport = view.findViewById(R.id.tv_group_list_sport_id);
        tv_enjoy = view.findViewById(R.id.tv_group_list_enjoy_id);

        ListView lv_groups = view.findViewById(R.id.lv_phone_group_id);
        tv_empty = view.getRootView().findViewById(R.id.tv_phone_group_id);

        tv_work.setText(String.valueOf(numbers[0]));
        tv_business.setText(String.valueOf(numbers[1]));
        tv_family.setText(String.valueOf(numbers[2]));
        tv_study.setText(String.valueOf(numbers[3]));
        tv_sport.setText(String.valueOf(numbers[4]));
        tv_enjoy.setText(String.valueOf(numbers[5]));

        lv_groups.setAdapter(new GroupAdapter(service, groups));
        tv_empty.setText(groups.isEmpty() ? getResources().getString(R.string.error_empty_group) : "");

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(contain -> showMessage(getString(R.string.available_soon)));

        return view;
    }

    private void showMessage(String message) {
        SnackBar.builder(service.getContext(), service.getView()).setMessage(message).showShort();
    }

}