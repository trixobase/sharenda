package cm.trixobase.sharenda.domain.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.core.User;
import cm.trixobase.sharenda.domain.activity.WelcomeActivity;
import cm.trixobase.sharenda.domain.ui.UiUser;
import cm.trixobase.sharenda.system.manager.Manager;

public class Registration extends AppCompatActivity {

    private static String pageCalled;

    private long userId = -1;
    private String civility = "";
    private String surname = "";
    private String habit = "";
    private String civilStatus = "";
    private long realAge;
    private long realPassword;

    private EditText et_surname, et_age, et_password, et_confirm;
    private Spinner sp_status, sp_habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.sharenda_launcher_to_show));

        pageCalled = PreferenceManager.getDefaultSharedPreferences(this).getString(AttributeName.Page_Registration_To_Show, AttributeName.Page_Registration_Registration);

        et_surname = findViewById(R.id.et_surmane_id);
        et_age = findViewById(R.id.et_age_id);
        et_password = findViewById(R.id.et_password_id);
        et_confirm = findViewById(R.id.et_confirm_id);

        RadioGroup rg_group = findViewById(R.id.rg_sexe_id);
        View.OnClickListener listener = view -> {
            if (view.getId() == R.id.r_mr_id) {
                civility = BaseName.Civility_Mr;
            } else {
                civility = BaseName.Civility_Mrs;
            }
        };
        for (int i = 0; i < rg_group.getChildCount(); i++) {
            RadioButton radio = (RadioButton) rg_group.getChildAt(i);
            radio.setOnClickListener(listener);
        }

        sp_habit = findViewById(R.id.sp_attitude_id);
        sp_habit.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                habit = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> attitudeAdapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_attitude,
                        android.R.layout.simple_spinner_item);
        attitudeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_habit.setAdapter(attitudeAdapter);

        sp_status = findViewById(R.id.sp_civil_status_id);
        sp_status.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                civilStatus = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_civil_status,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_status.setAdapter(adapter);

        Button finish = findViewById(R.id.bt_finish_id);
        finish.setOnClickListener(view -> {
            if (canPass()) {
                doSave();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pageCalled.equalsIgnoreCase(AttributeName.Page_Registration_Setting))
            refreshView();
    }

    private boolean canPass() {
        surname = et_surname.getText().toString();
        String age = et_age.getText().toString();
        realAge = age.isEmpty() ? 0 : Long.valueOf(age);
        String password = et_password.getText().toString();
        realPassword = password.isEmpty() ? 0 : Long.valueOf(password);
        String confirm = et_confirm.getText().toString();

        TextView tvSurname = findViewById(R.id.tv_surname_id);
        TextView tvAge = findViewById(R.id.tv_age_id);
        TextView tvSex = findViewById(R.id.tv_sexe_id);
        TextView tvCivilStatus = findViewById(R.id.tv_civil_status_id);
        TextView tv_attitude = findViewById(R.id.tv_attitude_id);

        ImageView imgSurname = findViewById(R.id.iv_surname_id);
        ImageView imgAge = findViewById(R.id.iv_age_id);
        ImageView imgSex = findViewById(R.id.iv_sexe_id);
        ImageView imgCivilStatus = findViewById(R.id.iv_civil_status_id);
        ImageView iv_attitude = findViewById(R.id.iv_attitude_id);

        if (civility.isEmpty()) {
            showError(tvSex, imgSex);
            showMessage(getResources().getString(R.string.error_user_sexe_save));
            return false;
        } else showAsValidate(tvSex, imgSex);

        if (realAge < 13 || realAge > 80) {
            showError(tvAge, imgAge);
            showMessage(getResources().getString(R.string.error_user_age_save));
            return false;
        } else showAsValidate(tvAge, imgAge);

        if (civilStatus.isEmpty()) {
            showError(tvCivilStatus, imgCivilStatus);
            showMessage(getResources().getString(R.string.error_user_status_save));
            return false;
        } else showAsValidate(tvCivilStatus, imgCivilStatus);

        if (habit.isEmpty()) {
            showError(tv_attitude, iv_attitude);
            showMessage(getResources().getString(R.string.error_user_comportment_save));
            return false;
        } else showAsValidate(tv_attitude, iv_attitude);

        if (surname.isEmpty()) {
            showError(tvSurname, imgSurname);
            showMessage(getResources().getString(R.string.error_user_surname_save));
            return false;
        } else {
            surname = Manager.formatName(surname);
            showAsValidate(tvSurname, imgSurname);
        }

        if (password.isEmpty()) {
            showMessage(getResources().getString(R.string.error_user_password_save));
            et_password.setHintTextColor(getResources().getColor(R.color.text_widget_form_error));
            return false;
        } else et_password.setHintTextColor(getResources().getColor(R.color.text_widget_form));

        if (password.length() < 4) {
            showMessage(getResources().getString(R.string.error_user_shot_password_save));
            et_password.setTextColor(getResources().getColor(R.color.text_widget_form_error));
            return false;
        } else et_password.setTextColor(getResources().getColor(R.color.text_widget_form));

        if (password.length() > 4) {
            showMessage(getResources().getString(R.string.error_user_long_password_save));
            et_password.setTextColor(getResources().getColor(R.color.text_widget_form_error));
            return false;
        } else {
            et_password.setTextColor(getResources().getColor(R.color.text_widget_form));
            et_password.setHintTextColor(getResources().getColor(R.color.text_widget_form));
        }

        if (confirm.isEmpty()) {
            showMessage(getResources().getString(R.string.error_user_empty_confirmation_save));
            et_confirm.setHintTextColor(getResources().getColor(R.color.text_widget_form_error));
            return false;
        } else et_confirm.setHintTextColor(getResources().getColor(R.color.text_widget_form));

        if (!password.equalsIgnoreCase(confirm)) {
            showMessage(getResources().getString(R.string.error_user_confirm_save));
            et_password.setTextColor(getResources().getColor(R.color.text_widget_form_error));
            et_confirm.setTextColor(getResources().getColor(R.color.text_widget_form_error));
            return false;
        } else {
            et_password.setTextColor(getResources().getColor(R.color.text_widget_form));
            et_confirm.setTextColor(getResources().getColor(R.color.text_widget_form));
        }
        return true;
    }

    private void doSave() {
        if (insertRecord() == -1) {
            showMessage(getResources().getString(R.string.error_save));
        } else {
            switch (pageCalled) {
                case AttributeName.Page_Registration_Registration:
                    RegistrationHomeActivity.doFinish();
                    Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                    i.putExtra(AttributeName.User_Surname, surname);
                    startActivity(i);
                    finish();
                    break;
                default:
                    onBackPressed();
                    break;
            }

        }
    }

    private long insertRecord() {
        UiUser user = UiUser.builder().build()
                .setCivility(civility)
                .setSurname(surname)
                .setAge(realAge)
                .setCivilStatus(civilStatus)
                .setPassword(realPassword)
                .setHabit(habit);
        user.setId(userId);
        return User.builder().withData(user.getData()).build().save(this);
    }

    private void showError(TextView textView, ImageView img) {
        textView.setTextColor(getResources().getColor(R.color.text_widget_form_error));
        img.setImageResource(R.drawable.ic_action_cancel);
    }

    private void showAsValidate(TextView textView, ImageView imageView) {
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_accept));
    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(getBaseContext(), message);
    }

    private void refreshView() {
        UiUser user = User.getOwner(getBaseContext());
        userId = user.getId();
        civility = user.getCivility();
        realAge = user.getAge();
        civilStatus = user.getCivilStatus();
        habit = user.getHabit();
        surname = user.getSurname();

        TextView tv_title = findViewById(R.id.tv_registration_title_id);
        tv_title.setText(getString(R.string.label_title_registration_edit));

        RadioButton radio;
        switch (civility) {
            case BaseName.Civility_Mr:
                radio = findViewById(R.id.r_mr_id);
                radio.setChecked(true);
                break;
            default:
                radio = findViewById(R.id.r_me_id);
                radio.setChecked(true);
                break;
        }

        et_age.setText(String.valueOf(realAge));

        int civilPosition;
        switch (civilStatus) {
            case "Elève":
            case "Pupil":
                civilPosition = 0;
                break;
            case "Etudiant":
            case "Student":
                civilPosition = 1;
                break;
            default:
                civilPosition = 2;
                break;
        }
        sp_status.setSelection(civilPosition);

        int HabitPosition;
        switch (habit) {
            case "Sérieux":
            case "Serious":
                HabitPosition = 0;
                break;
            case "Fétard":
            case "Roisterer":
                HabitPosition = 2;
                break;
            default:
                HabitPosition = 1;
                break;
        }
        sp_habit.setSelection(HabitPosition);

        et_surname.setText(surname);

    }

}
