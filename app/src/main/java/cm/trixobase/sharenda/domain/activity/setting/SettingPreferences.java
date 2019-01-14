package cm.trixobase.sharenda.domain.activity.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.User;
import cm.trixobase.sharenda.domain.activity.activity.ActivityAdd;
import cm.trixobase.sharenda.domain.activity.group.GroupMembersActivity;
import cm.trixobase.sharenda.domain.activity.home.PhoneActivity;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.manager.UserManager;
import cm.trixobase.sharenda.system.media.PhoneProcess;

public class SettingPreferences extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.subtitle_activity_settings_preferences);
        getSupportActionBar().setSubtitle(R.string.subtitle_activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_option = findViewById(R.id.ll_preferences_work_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(this, SettingPreferencesCategory.class);
            intent.putExtra(AttributeName.Category, AttributeName.Category_Work);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_preferences_studies_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(this, SettingPreferencesCategory.class);
            intent.putExtra(AttributeName.Category, AttributeName.Category_Studies);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_preferences_business_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(this, SettingPreferencesCategory.class);
            intent.putExtra(AttributeName.Category, AttributeName.Category_Business);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_preferences_family_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(this, SettingPreferencesCategory.class);
            intent.putExtra(AttributeName.Category, AttributeName.Category_Family);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_preferences_sport_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(this, SettingPreferencesCategory.class);
            intent.putExtra(AttributeName.Category, AttributeName.Category_Sport);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_preferences_enjoy_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(this, SettingPreferencesCategory.class);
            intent.putExtra(AttributeName.Category, AttributeName.Category_Enjoy);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences_reset_id:
                UserManager.builder(getBaseContext()).resetAttributes();
                Manager.showToastLongMessage(getBaseContext(), getString(R.string.success_preferences_reset));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
