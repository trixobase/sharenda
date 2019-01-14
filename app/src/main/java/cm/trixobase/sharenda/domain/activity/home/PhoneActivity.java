package cm.trixobase.sharenda.domain.activity.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.core.Group;
import cm.trixobase.sharenda.domain.activity.SendSmsActivity;
import cm.trixobase.sharenda.domain.activity.group.GroupAddActivity;
import cm.trixobase.sharenda.domain.activity.group.GroupMembersActivity;
import cm.trixobase.sharenda.domain.activity.setting.SettingActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.PhoneProcess;
import cm.trixobase.sharenda.system.view.pageadapter.MyPhonePageAdapter;

public class PhoneActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    private List<UiContact> contactList = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager phonePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_phone);
        activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phonePager = findViewById(R.id.vp_phone);
        tabLayout = findViewById(R.id.tl_phone_id);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        Manager.saveData(this, AttributeName.Interface_To_show, AttributeName.Interface_Phone);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                prefs.putInt(AttributeName.Page_Phone_To_Show, tab.getPosition());
                prefs.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int tabToSelect = PreferenceManager.getDefaultSharedPreferences(this).getInt(AttributeName.Page_Phone_To_Show, 0);
        List<UiGroup> groupList = Group.getAll(getBaseContext());
        contactList = Contact.getAll(getBaseContext());
        int[] countGroupsByCategory = new int[6];

        setupGroupListToDisplay(groupList, countGroupsByCategory);
        PagerAdapter phoneAdapter = new MyPhonePageAdapter(getSupportFragmentManager(),
                getResources().getString(R.string.label_title_phone_1),
                getResources().getString(R.string.label_title_phone_2),
                contactList,
                groupList,
                countGroupsByCategory);

        phonePager.setAdapter(phoneAdapter);
        phonePager.setClipToPadding(false);
        phonePager.setPageMargin(12);

        tabLayout.setupWithViewPager(phonePager);
        tabLayout.getTabAt(tabToSelect).select();

    }

    private void setupGroupListToDisplay(List<UiGroup> groupList, int[] countMembersByCategory) {
        for (int i = 0; i < 6; i++) {
            countMembersByCategory[i] = 0;
        }
        for (UiGroup group : groupList) {
            group.setMembersCount(Contact.getAllByGroup(getBaseContext(), group.getId()).size());
            switch (group.getCategory()) {
                case AttributeName.Category_Work:
                    countMembersByCategory[0] = countMembersByCategory[0] + 1;
                    break;
                case AttributeName.Category_Business:
                    countMembersByCategory[1] = countMembersByCategory[1] + 1;
                    break;
                case AttributeName.Category_Family:
                    countMembersByCategory[2] = countMembersByCategory[2] + 1;
                    break;
                case AttributeName.Category_Studies:
                    countMembersByCategory[3] = countMembersByCategory[3] + 1;
                    break;
                case AttributeName.Category_Sport:
                    countMembersByCategory[4] = countMembersByCategory[4] + 1;
                    break;
                case AttributeName.Category_Enjoy:
                    countMembersByCategory[5] = countMembersByCategory[5] + 1;
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_phone_add_contact_id:
                Manager.saveData(this, AttributeName.Page_Sms_To_Show, AttributeName.Page_Sms_Trixo);
                Intent intent = new Intent(getBaseContext(), SendSmsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_phone_add_group_id:
                if (!contactList.isEmpty()) {
                    Manager.saveData(this, AttributeName.Page_Group_Members_To_Show, AttributeName.Page_Group_Members_Contacts);
                    intent = new Intent(getBaseContext(), GroupAddActivity.class);
                    startActivity(intent);
                } else showMessage(getString(R.string.label_message_fragment_contact_null));
                return true;
            case R.id.action_phone_activity_id:
                intent = new Intent(getBaseContext(), EventActivity.class);
                Manager.saveData(getBaseContext(), AttributeName.Page_Event_To_Show, 0);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_phone_invite_id:
                if (!contactList.isEmpty()) {
                    Manager.saveData(this, AttributeName.Page_Group_Members_To_Show, AttributeName.Page_Group_Members_Invitation);
                    intent = new Intent(getBaseContext(), GroupMembersActivity.class);
                    startActivity(intent);
                } else showMessage(getString(R.string.label_message_fragment_contact_null));
                return true;
            case R.id.action_phone_settings_id:
                Manager.saveData(this, AttributeName.Interface_To_show, AttributeName.Interface_Phone);
                intent = new Intent(getBaseContext(), SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(getBaseContext(), message);
    }

    public static void doFinish() {
        if (activity != null)
            activity.finish();
    }

}

