package cm.trixobase.sharenda.domain.activity.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.SettingName;
import cm.trixobase.sharenda.core.Activity;
import cm.trixobase.sharenda.domain.activity.activity.ActivityAdd;
import cm.trixobase.sharenda.domain.activity.group.GroupMembersActivity;
import cm.trixobase.sharenda.domain.activity.setting.SettingActivity;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.UserManager;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.PhoneProcess;
import cm.trixobase.sharenda.system.view.pageadapter.MyEventPageAdapter;

public class EventActivity extends AppCompatActivity {

    private static android.app.Activity activity;

    private int tabToSelect = 0;
    private UiActivity priorityActivity;
    private List<UiActivity> ownerActivities = new ArrayList<>();
    private List<UiActivity> foreignActivities = new ArrayList<>();
    private List<UiActivity> historyActivities = new ArrayList<>();

    private ViewPager eventPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_event);
        activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tl_tabLayout_id);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        Manager.saveData(this, AttributeName.Interface_To_show, AttributeName.Interface_Event);

        tabToSelect = Manager.getData(this, AttributeName.Page_Event_To_Show, 0);
        if (tabToSelect == 1) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancelAll();
            Manager.saveData(this, AttributeName.Page_Event_To_Show, 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        computeActivitiesToShow(Activity.getAll(this));
        PagerAdapter eventAdapter = new MyEventPageAdapter(getSupportFragmentManager(),
                getResources().getString(R.string.label_title_event_1),
                getResources().getString(R.string.label_title_event_2),
                getResources().getString(R.string.label_title_event_3),
                ownerActivities,
                priorityActivity,
                foreignActivities,
                historyActivities);

        eventPager.setAdapter(eventAdapter);
        eventPager.setClipToPadding(false);
        eventPager.setPageMargin(12);

        tabLayout.setupWithViewPager(eventPager);
        tabLayout.getTabAt(tabToSelect).select();

    }

    private void computeActivitiesToShow(List<UiActivity> activities) {
        int maxActivitiesToShow = Manager.getData(this, SettingName.Max_Activity_To_Show, Sharenda.Max_History_Activity_To_Show);
        priorityActivity = null;
        ownerActivities.clear();
        foreignActivities.clear();
        historyActivities.clear();
        try {
            for (UiActivity activity : activities) {
                if (activity.isPassed())
                        historyActivities.add(activity);
                else {
                    if (Sharenda.OwnerNumber.equalsIgnoreCase(activity.getOwner()))
                        ownerActivities.add(activity);
                    else foreignActivities.add(activity);
                }
            }
        } catch (Exception e) {
            Log.e(Sharenda.Log, "test if activity is passed - " + e);
        }
        if (!ownerActivities.isEmpty()) {
            int item = -1;
            int priority = -1;
            for (UiActivity activity : ownerActivities) {
                int activityPriority = UserManager.getPriorityOfType(this, activity.getType());
                if (activity.mustBeingDoToday() && activityPriority > priority) {
                    priority = activityPriority;
                    item = ownerActivities.indexOf(activity);
                }
            }
            if (item != -1) {
                priorityActivity = ownerActivities.get(item);
                ownerActivities.remove(priorityActivity);
            } else {
                priorityActivity = ownerActivities.get(0);
                ownerActivities.remove(priorityActivity);
            }
        }
        historyActivities = Activity.reverse(historyActivities);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_event_add_id:
                intent = new Intent(getBaseContext(), ActivityAdd.class);
                startActivity(intent);
                return true;
            case R.id.action_event_phone_id:
                intent = new Intent(getBaseContext(), PhoneActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_event_invite_id:
                if (PhoneProcess.VerifyContactsAreSufficient(getBaseContext())) {
                    Manager.saveData(this, AttributeName.Page_Group_Members_To_Show, AttributeName.Page_Group_Members_Invitation);
                    intent = new Intent(getBaseContext(), GroupMembersActivity.class);
                    startActivity(intent);
                } else showMessage(getString(R.string.label_message_fragment_contact_null));
                return true;
            case R.id.action_event_settings:
                Manager.saveData(this, AttributeName.Interface_To_show, AttributeName.Interface_Event);
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
