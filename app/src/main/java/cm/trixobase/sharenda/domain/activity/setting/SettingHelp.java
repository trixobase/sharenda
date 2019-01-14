package cm.trixobase.sharenda.domain.activity.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.domain.activity.PolicyActivity;

public class SettingHelp extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_help);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.subtitle_activity_settings_help);
        getSupportActionBar().setSubtitle(R.string.subtitle_activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_option = findViewById(R.id.ll_settings_help_contact_us_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), SettingHelpContact.class);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_settings_help_politic_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), PolicyActivity.class);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_settings_help_about_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), SettingHelpAbout.class);
            startActivity(intent);
        });

    }
}
