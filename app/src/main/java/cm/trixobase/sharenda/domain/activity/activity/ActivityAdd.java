package cm.trixobase.sharenda.domain.activity.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.Group;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.domain.ui.UiActivity;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.ActivityManager;
import cm.trixobase.sharenda.system.manager.Manager;

public class ActivityAdd extends AppCompatActivity {

    private long groupId = -1;
    private String titleToSave;
    private String typeToSave;
    private String dateToSave;
    private String beginHourToSave;
    private int timeToSave;
    private boolean Mode_Auto_Activate;

    private int pageCalled;

    private EditText et_title;
    private TextView tv_date, tv_date_dialog, tv_hour, tv_hour_dialog;
    private CheckBox cb_usingDate, cb_usingFixDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.label_title_activity_event_add));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_title = findViewById(R.id.et_title_event_id);
        tv_date = findViewById(R.id.tv_date_event_id);
        tv_date_dialog = findViewById(R.id.tv_date_dial_event_id);
        tv_hour = findViewById(R.id.tv_begin_hour_event_id);
        tv_hour_dialog = findViewById(R.id.tv_hour_dialog_id);
        cb_usingDate = findViewById(R.id.cb_usingdate_id);
//        cb_usingFixDate = findViewById(R.id.cb_usingFixDate_id);
        cb_usingFixDate = new CheckBox(this);
        cb_usingFixDate.setChecked(false);

        pageCalled = Manager.getData(this, AttributeName.Page_Event_To_Show, 0);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        c.add(Calendar.MINUTE, 10);

        View.OnClickListener dateListener = view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        dateToSave = Manager.date.getDate(dayOfMonth, monthOfYear, year);
                        tv_date_dialog.setText(Manager.date.computeDateToDisplay(getResources(), dateToSave));
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        };
        tv_date.setOnClickListener(dateListener);
        tv_date_dialog.setOnClickListener(dateListener);

        View.OnClickListener timeListener = view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                    (view12, hourOfDay, minute) -> {
                        beginHourToSave = Manager.time.getTime(hourOfDay, minute);
                        tv_hour_dialog.setText(beginHourToSave);
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        };
        tv_hour.setOnClickListener(timeListener);
        tv_hour_dialog.setOnClickListener(timeListener);

        Spinner sp_type = findViewById(R.id.sp_type_event_id);
        ArrayAdapter<CharSequence> categoryAdapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_activitiy_types,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(categoryAdapter);
        sp_type.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeToSave = adapterView.getSelectedItem().toString();
                switch (typeToSave) {
                    case "Enjoy":
                    case "Loisirs":
                        typeToSave = AttributeName.Category_Enjoy;
                        break;
                    case "Work":
                    case "Travail":
                        typeToSave = AttributeName.Category_Work;
                        break;
                    case "Business":
                        typeToSave = AttributeName.Category_Business;
                        break;
                    case "Studies":
                    case "Etudes":
                        typeToSave = AttributeName.Category_Studies;
                        break;
                    case "Family":
                    case "Famille":
                        typeToSave = AttributeName.Category_Family;
                        break;
                    case "Sport":
                        typeToSave = AttributeName.Category_Sport;
                        break;
                    default:
                        typeToSave = AttributeName.Category_Any;
                        break;
                }
                if (typeToSave.equalsIgnoreCase(AttributeName.Category_Any))
                    initializeInviteAdapter();
                else setInviteAdapter(typeToSave);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ScrollView layout = findViewById(R.id.ll_date_id);

        cb_usingDate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!compoundButton.isChecked()) {
                layout.setVisibility(View.VISIBLE);
                computeTimeToDisplay(tv_date_dialog, tv_hour_dialog);
            } else layout.setVisibility(View.INVISIBLE);
        });

        TextView tv_message = findViewById(R.id.tv_activity_add_message_id);
        tv_message.setOnClickListener(view -> {
            if (cb_usingDate.isChecked()) {
                cb_usingDate.setChecked(false);
                layout.setVisibility(View.VISIBLE);
                computeTimeToDisplay(tv_date_dialog, tv_hour_dialog);

            } else {
                cb_usingDate.setChecked(true);
                layout.setVisibility(View.INVISIBLE);
            }
        });

        Spinner sp_time = findViewById(R.id.sp_time_event_id);
        ArrayAdapter<CharSequence> timeAdapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_activity_time,
                        android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_time.setAdapter(timeAdapter);
        sp_time.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String timeSelected = adapterView.getSelectedItem().toString();
                switch (timeSelected) {
                    case "Moins d'une heure":
                    case "Less than hour":
                        timeToSave = 50;
                        break;
                    case "1 ou 2 heures":
                    case "1 or 2 hours":
                        timeToSave = 100;
                        break;
                    case "2 ou 3 heures":
                    case "2 or 3 hours":
                        timeToSave = 150;
                        break;
                    case "Plus de 3 heures":
                    case "More than 3 hours":
                        timeToSave = 200;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setInviteAdapter(String typeToSave) {
        List<UiGroup> groups = Group.getAllByCategory(this, typeToSave);
        if (!groups.isEmpty()) {
            List<CharSequence> nameList = new ArrayList<>();
            nameList.add(getString(R.string.event_type_any));
            for (UiGroup group : groups) {
                nameList.add(group.getName());
            }
            Spinner sp_invite = findViewById(R.id.sp_invite_event_id);
            ArrayAdapter<CharSequence> invitesAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, nameList);
            invitesAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            sp_invite.setAdapter(invitesAdapter);
            sp_invite.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0)
                        groupId = groups.get(i - 1).getId();
                    else groupId = -1;

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            Manager.showCustomToastMessage(getBaseContext(), getCurrentFocus(), getString(R.string.error_event_group_for_type));
            initializeInviteAdapter();
        }
    }

    private void initializeInviteAdapter() {
        Spinner sp_invites = findViewById(R.id.sp_invite_event_id);
        ArrayAdapter<CharSequence> inviteAdapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_empty_invite,
                        android.R.layout.simple_spinner_item);
        inviteAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_invites.setAdapter(inviteAdapter);
    }

    private void doSave() {
        if (canSave()) {
            if (insertRecord() == -1) {
                showMessage(getResources().getString(R.string.error_save));
            } else {
                showMessage(getResources().getString(R.string.success_activity_saved));
                Manager.saveData(getBaseContext(), AttributeName.Page_Event_To_Show, 0);
                switch (pageCalled) {
                    case 3:
                        finish();
                        break;
                    case 0:
                    case 1:
                        EventActivity.doFinish();
                        Intent i = new Intent(getBaseContext(), EventActivity.class);
                        startActivity(i);
                        finish();
                        break;
                }
            }
        }
    }

    private boolean canSave() {
        TextView tvTitle = findViewById(R.id.tv_title_event_id);
        ImageView ivTitle = findViewById(R.id.iv_title_event_id);

        TextView tvBeginHour = findViewById(R.id.tv_begin_hour_event_id);
        ImageView ivBeginHour = findViewById(R.id.iv_begin_hour_event_id);

        titleToSave = et_title.getText().toString().toLowerCase();

        if (titleToSave.isEmpty()) {
            showError(getResources().getString(R.string.error_activity_title_save), tvTitle, ivTitle);
            return false;
        } else {
            titleToSave = titleToSave.substring(0, 1).toUpperCase().concat(titleToSave.substring(1, titleToSave.length()));
            ivTitle.setImageResource(R.drawable.ic_action_accept);
            tvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (!cb_usingDate.isChecked()) {
            Mode_Auto_Activate = false;
            if (beginHourToSave.isEmpty() || dateToSave.isEmpty() || Manager.date.dateIsPassed(dateToSave, beginHourToSave)) {
                showError(getResources().getString(R.string.error_activity_time_save), tvBeginHour, ivBeginHour);
                return false;
            } else {
                tvBeginHour.setTextColor(getResources().getColor(R.color.colorAccent));
                ivBeginHour.setImageResource(R.drawable.ic_action_accept);
            }
        } else {
            Mode_Auto_Activate = true;
            dateToSave = "";
            beginHourToSave = "";
            timeToSave = 0;
        }
        return true;
    }

    private long insertRecord() {
        UiActivity activity = UiActivity.builder().build()
                .setOwner(Sharenda.OwnerNumber)
                .setTitle(titleToSave)
                .setIdGroup(groupId)
                .setType(typeToSave)
                .setDate(dateToSave)
                .setBeginHour(beginHourToSave)
                .setTime(timeToSave)
                .setMode(Mode_Auto_Activate)
                .setModeHour((cb_usingDate.isChecked() && cb_usingFixDate.isChecked()));
        activity.setId(-1);

        return ActivityManager.builder(getBaseContext()).withActivity(activity).save();
    }

    private void showError(String message, TextView textView, ImageView imageView) {
        textView.setTextColor(getResources().getColor(R.color.text_widget_form_error));
        imageView.setImageResource(R.drawable.ic_action_cancel);
        showMessage(message);
    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(this, message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_action_save_id) {
            doSave();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void computeTimeToDisplay(TextView tv_date, TextView tv_hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        dateToSave = Manager.date.getDate(calendar);
        beginHourToSave = Manager.time.getTime(calendar);
        tv_date.setText(Manager.date.computeDateToDisplay(getResources(), dateToSave));
        tv_hour.setText(beginHourToSave);
    }

}
