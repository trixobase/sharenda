package cm.trixobase.sharenda.domain.activity.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.domain.activity.home.PhoneActivity;
import cm.trixobase.sharenda.system.Sharenda;

public class SettingLanguage extends AppCompatActivity {

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.subtitle_activity_settings_language);
        getSupportActionBar().setSubtitle(R.string.subtitle_activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RadioButton fr = findViewById(R.id.rb_settings_language_fr_id);
        RadioButton en = findViewById(R.id.rb_settings_language_en_id);

        fr.setOnClickListener(view -> {
                    language = Sharenda.Language_French;
                    en.setChecked(false);
                }
        );

        en.setOnClickListener(view -> {
                    language = Sharenda.Language_English;
                    fr.setChecked(false);
                }
        );

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        language = preferences.getString(Sharenda.Language, Sharenda.Language_Default);

        switch (language) {
            case Sharenda.Language_French:
                fr.setChecked(true);
                break;
            case Sharenda.Language_English:
                en.setChecked(true);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_action_save_id:
                Sharenda.setLanguage(getBaseContext(), language);
                SettingActivity.doFinish();
                Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
