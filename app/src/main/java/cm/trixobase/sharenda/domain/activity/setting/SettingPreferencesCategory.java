package cm.trixobase.sharenda.domain.activity.setting;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.common.SettingName;
import cm.trixobase.sharenda.common.TranslateElement;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.manager.UserManager;

public class SettingPreferencesCategory extends AppCompatActivity {

    private String category;
    private String day;
    private int dayPosition = 0;
    private String hourMin;
    private String minMin;
    private String hourMax;
    private String minMax;
    private String time;

    private Spinner sp_day;
    private EditText et_hourMin, et_hourMax, et_minMin, et_minMax, et_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_preferences_category);

        category =  getIntent().getStringExtra(AttributeName.Category);
        String beginHourMin = UserManager.getBeginHourMinOfType(this, category);
        String beginHourMax = UserManager.getBeginHourMaxOfType(this, category);
        time = String.valueOf(UserManager.getTimeMinOfType(this, category));
        hourMin = beginHourMin.substring(0, 2);
        minMin = beginHourMin.substring(3, 5);
        hourMax = beginHourMax.substring(0, 2);
        minMax = beginHourMax.substring(3, 5);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); getSupportActionBar().setTitle(R.string.subtitle_activity_settings_preferences);
        getSupportActionBar().setSubtitle("(" + Manager.getTranslate(getResources(), TranslateElement.Category, category) + ")");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp_day = findViewById(R.id.sp_preferences_day_id);
        sp_day.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day = adapterView.getSelectedItem().toString();
                switch (day) {
                    case "Lundi":
                    case "Monday":
                        day = AttributeName.Day_Monday;
                        break;
                    case "Mardi":
                    case "Tuesday":
                        day = AttributeName.Day_Tuesday;
                        break;
                    case "Mercredi":
                    case "Wednesday":
                        day = AttributeName.Day_Wednesday;
                        break;
                    case "Jeudi":
                    case "Thursday":
                        day = AttributeName.Day_Thursday;
                        break;
                    case "Vendredi":
                    case "Friday":
                        day = AttributeName.Day_Friday;
                        break;
                    case "Samedi":
                    case "Saturday":
                        day = AttributeName.Day_Saturday;
                        break;
                    case "Dimanche":
                    case "Sunday":
                        day = AttributeName.Day_Sunday;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> dayAdapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_day,
                        android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_day.setAdapter(dayAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        et_hourMin = findViewById(R.id.et_preferences_hour_min_hour_id);
        et_hourMin.setText(hourMin);

        et_minMin = findViewById(R.id.et_preferences_hour_min_min_id);
        et_minMin.setText(minMin);

        et_hourMax = findViewById(R.id.et_preferences_hour_max_hour_id);
        et_hourMax.setText(hourMax);

        et_minMax = findViewById(R.id.et_preferences_hour_max_min_id);
        et_minMax.setText(minMax);

        et_time = findViewById(R.id.et_preferences_time_id);
        et_time.setText(time);

        switch (UserManager.getDateDayOfType(this, category)) {
            case AttributeName.Day_Monday:
                dayPosition = 0;
                break;
            case AttributeName.Day_Tuesday:
                dayPosition = 1;
                break;
            case AttributeName.Day_Wednesday:
                dayPosition = 2;
                break;
            case AttributeName.Day_Thursday:
                dayPosition = 3;
                break;
            case AttributeName.Day_Friday:
                dayPosition = 4;
                break;
            case AttributeName.Day_Saturday:
                dayPosition = 5;
                break;
            case AttributeName.Day_Sunday:
                dayPosition = 6;
                break;
        }

        sp_day.setSelection(dayPosition);
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
                if (canSave()) {
                    if (insertRecord() == -1)
                        showMessage(getString(R.string.error_save));
                    else {
                        showMessage(getString(R.string.success_save));
                        onBackPressed();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean canSave() {
        time = et_time.getText().toString();
        hourMin = et_hourMin.getText().toString();
        minMin = et_minMin.getText().toString();
        hourMax = et_hourMax.getText().toString();
        minMax = et_minMax.getText().toString();

        if (hourMin.isEmpty()) {
            hourMin = "00";
            return false;
        }
        if (hourMax.isEmpty()) {
            hourMax = "00";
            return false;
        }
        if (minMin.isEmpty()) {
            minMin = "00";
            return false;
        }
        if (minMax.isEmpty()) {
            minMax = "00";
            return false;
        }
        if (time.isEmpty()) {
            time = String.valueOf(UserManager.getTimeMinOfType(this, category));
            return false;
        }
        if (Integer.valueOf(time) > 300) {
            et_time.setText("300");
            return false;
        }
        if (Integer.valueOf(hourMin) > 23) {
            et_hourMin.setText("23");
            return false;
        }
        if (Integer.valueOf(minMin) > 59) {
            et_minMin.setText("59");
            return false;
        }
        if (Integer.valueOf(hourMax) > 23) {
            et_hourMax.setText("23");
            return false;
        }
        if (Integer.valueOf(minMax) > 59) {
            et_minMax.setText("59");
            return false;
        }
        if (hourMax.length() == 1)
            hourMax = 0 + hourMax;
        if (hourMin.length() == 1)
            hourMin = 0 + hourMin;
        if (minMax.length() == 1)
            minMax = 0 + minMax;
        if (minMin.length() == 1)
            minMin = 0 + minMin;
        return true;
    }

    private long insertRecord() {
        long result = -1;
        try {
            SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(this).edit();
            switch (category) {
                case AttributeName.Category_Business:
                    preferences.putString(SettingName.Date_Business_Setted, day);
                    preferences.putInt(SettingName.Time_Business_Setted, Integer.valueOf(time));
                    preferences.putString(SettingName.BeginHour_Business_Setted, hourMin.concat(":").concat(minMin));
                    preferences.putString(SettingName.EndHour_Business_Setted, hourMax.concat(":").concat(minMax));
                    preferences.apply();
                    result = 0;
                    break;
                case AttributeName.Category_Work:
                    preferences.putString(SettingName.Date_Work_Setted, day);
                    preferences.putInt(SettingName.Time_Work_Setted, Integer.valueOf(time));
                    preferences.putString(SettingName.BeginHour_Work_Setted, hourMin.concat(":").concat(minMin));
                    preferences.putString(SettingName.EndHour_Work_Setted, hourMax.concat(":").concat(minMax));
                    preferences.apply();
                    result = 0;
                    break;
                case AttributeName.Category_Studies:
                    preferences.putString(SettingName.Date_Studies_Setted, day);
                    preferences.putInt(SettingName.Time_Studies_Setted, Integer.valueOf(time));
                    preferences.putString(SettingName.BeginHour_Studies_Setted, hourMin.concat(":").concat(minMin));
                    preferences.putString(SettingName.EndHour_Studies_Setted, hourMax.concat(":").concat(minMax));
                    preferences.apply();
                    result = 0;
                    break;
                case AttributeName.Category_Family:
                    preferences.putString(SettingName.Date_Family_Setted, day);
                    preferences.putInt(SettingName.Time_Family_Setted, Integer.valueOf(time));
                    preferences.putString(SettingName.BeginHour_Family_Setted, hourMin.concat(":").concat(minMin));
                    preferences.putString(SettingName.EndHour_Family_Setted, hourMax.concat(":").concat(minMax));
                    preferences.apply();
                    result = 0;
                    break;
                case AttributeName.Category_Sport:
                    preferences.putString(SettingName.Date_Sport_Setted, day);
                    preferences.putInt(SettingName.Time_Sport_Setted, Integer.valueOf(time));
                    preferences.putString(SettingName.BeginHour_Sport_Setted, hourMin.concat(":").concat(minMin));
                    preferences.putString(SettingName.EndHour_Sport_Setted, hourMax.concat(":").concat(minMax));
                    preferences.apply();
                    result = 0;
                    break;
                case AttributeName.Category_Enjoy:
                    preferences.putString(SettingName.Date_Enjoy_Setted, day);
                    preferences.putInt(SettingName.Time_Enjoy_Setted, Integer.valueOf(time));
                    preferences.putString(SettingName.BeginHour_Enjoy_Setted, hourMin.concat(":").concat(minMin));
                    preferences.putString(SettingName.EndHour_Enjoy_Setted, hourMax.concat(":").concat(minMax));
                    preferences.apply();
                    result = 0;
                    break;
            }
            Log.e(Sharenda.Log, "Preferences success save !!");
        } catch (Exception e) {
            Log.e(Sharenda.Log, "Fail to save Preferences Setting: " + e);
        }
        return result;
    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(getBaseContext(), message);
    }
}
