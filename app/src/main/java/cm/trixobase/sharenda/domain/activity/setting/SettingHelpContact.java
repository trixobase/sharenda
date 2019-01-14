package cm.trixobase.sharenda.domain.activity.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import cm.trixobase.sharenda.R;

public class SettingHelpContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_help_contact_us);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(R.string.label_text_setting_help_contact_us);

        TextView copyrights = findViewById(R.id.tv_trixobase_copyrights_id);
        copyrights.setText(String.format(getString(R.string.label_text_trixobase_copyrights), 2007));

    }
}
