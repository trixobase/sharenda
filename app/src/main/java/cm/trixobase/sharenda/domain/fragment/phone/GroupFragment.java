package cm.trixobase.sharenda.domain.fragment.phone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.view.listview.ListViewGroupAdapter;

/**
 * Created by noumianguebissie on 6/8/18.
 */

@SuppressLint("ValidFragment")
public class GroupFragment extends Fragment {

    private List<UiGroup> groups;
    private int[] number = new int[6];

    public GroupFragment(List<UiGroup> groups, int[] tab) {
        this.groups = groups;
        this.number = tab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_group, null);

        TextView tv_work, tv_business, tv_family, tv_study, tv_sport, tv_enjoy;

        tv_work = view.findViewById(R.id.tv_group_list_work_id);
        tv_business = view.findViewById(R.id.tv_group_list_business_id);
        tv_family = view.findViewById(R.id.tv_group_list_family_id);
        tv_study = view.findViewById(R.id.tv_group_list_study_id);
        tv_sport = view.findViewById(R.id.tv_group_list_sport_id);
        tv_enjoy = view.findViewById(R.id.tv_group_list_enjoy_id);
        ListView lv_groups = view.findViewById(R.id.lv_phone_group_id);
        tv_work.requestFocus();

        tv_work.setText(String.valueOf(number[0]));
        tv_business.setText(String.valueOf(number[1]));
        tv_family.setText(String.valueOf(number[2]));
        tv_study.setText(String.valueOf(number[3]));
        tv_sport.setText(String.valueOf(number[4]));
        tv_enjoy.setText(String.valueOf(number[5]));

        lv_groups.setAdapter(new ListViewGroupAdapter(getContext(), groups));

        if (groups.isEmpty()) {
            TextView textView = view.getRootView().findViewById(R.id.tv_phone_group_id);
            textView.setText(getResources().getString(R.string.label_message_fragment_group_null));
        }

        return view;
    }
}