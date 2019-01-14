package cm.trixobase.sharenda.domain.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.User;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.domain.activity.home.PhoneActivity;
import cm.trixobase.sharenda.domain.activity.registration.Registration;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.ImagePicker;
import cm.trixobase.sharenda.system.media.ImageProcess;

public class SettingActivity extends AppCompatActivity {

    private static Activity activity;
    private static Intent intent;
    private static final int PICK_IMAGE_ID = 460;

    private static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_setting));
        getSupportActionBar().setTitle(R.string.subtitle_activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_option = findViewById(R.id.ll_settings_picture_id);
        ll_option.setOnClickListener(view -> {
            intent = ImagePicker.getPickImageIntent(getBaseContext());
            startActivityForResult(intent, PICK_IMAGE_ID);
        });

        ll_option = findViewById(R.id.ll_settings_account_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), Registration.class);
            Manager.saveData(getBaseContext(), AttributeName.Page_Registration_To_Show, AttributeName.Page_Registration_Setting);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_settings_preferences_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), SettingPreferences.class);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_settings_language_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), SettingLanguage.class);
            startActivity(intent);
        });

        ll_option = findViewById(R.id.ll_settings_help_id);
        ll_option.setOnClickListener(view -> {
            intent = new Intent(getBaseContext(), SettingHelp.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setAccountView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap picture = ImagePicker.getImageFromResult(this, resultCode, data);
                if (picture != null) {
                    ImageProcess.saveContactPicture(getBaseContext(), name, picture);
                    ImageView image = findViewById(R.id.iv_setting_picture_contact_id);
                    image.setImageBitmap(picture);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void setAccountView() {
        name = User.getOwner(getBaseContext()).getSurname();
        TextView tv_name = findViewById(R.id.tv_setting_profil_name_id);
        tv_name.setText(name);

        Bitmap contactPicture = ImageProcess.getContactPicture(getBaseContext(), name);
        if (contactPicture != null) {
            ImageView image = findViewById(R.id.iv_setting_picture_contact_id);
            image.setImageBitmap(contactPicture);
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String interfaceToShow = preferences.getString(AttributeName.Interface_To_show, AttributeName.Interface_Event);
        Intent intent;
        switch (interfaceToShow) {
            case AttributeName.Interface_Phone:
                intent = new Intent(getBaseContext(), PhoneActivity.class);
                startActivity(intent);
                finish();
                break;
            case AttributeName.Interface_Event:
                intent = new Intent(getBaseContext(), EventActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public static void doFinish() {
        if (activity != null)
            activity.finish();
    }

}
