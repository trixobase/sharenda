package cm.trixobase.sharenda.domain.activity;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.common.TranslateElement;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.core.Group;
import cm.trixobase.sharenda.core.User;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.PhoneProcess;

public class SendSmsActivity extends AppCompatActivity {

    private String pageToDisplay;
    private String title1 = "Euh..";
    private String title2 = "Error";
    private String message;
    private List<String> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageToDisplay = PreferenceManager.getDefaultSharedPreferences(this).getString(AttributeName.Page_Sms_To_Show, AttributeName.Page_Sms_Contact);
        numbers.clear();

        switch (pageToDisplay) {
            case AttributeName.Page_Sms_Group:
                long groupId = getIntent().getLongExtra(AttributeName.Group_Id, -1);
                UiGroup group = Group.getById(this, groupId);
                if (group != null) {
                    title1 = group.getName();
                    title2 = Manager.getTranslate(getResources(), TranslateElement.Category, group.getCategory());
                    for (UiContact member : Contact.getAllByGroup(this, groupId)) {
                        numbers.add(member.getNumber());
                    }
                }
                break;
            case AttributeName.Page_Sms_Contact:
                title1 = getIntent().getStringExtra(AttributeName.Contact_Name);
                title2 = getIntent().getStringExtra(AttributeName.Contact_Number);
                numbers.add(title2);
                break;
            case AttributeName.Page_Sms_Trixo:
                title1 = BaseName.Trixobase_Name;
                title2 = BaseName.Trixobase_Number_To_Display;
                numbers.add(BaseName.Trixobase_Number);
                LinearLayout ll_trixo = findViewById(R.id.ll_sms_send_id);
                TextView textView = findViewById(R.id.tv1_trixo_feature_id);
                textView.setText(getString(R.string.label_message_sms_1) + " " + User.getOwner(getBaseContext()).getSurname() + ".");
                ll_trixo.setVisibility(View.VISIBLE);
                break;
        }

        final TextView tv_message = findViewById(R.id.tv_group_send_sms_message_id);
        setTextView(tv_message, 0);

        EditText et_message = findViewById(R.id.et_group_send_sms_message_id);
        et_message.setOnKeyListener((view, i, keyEvent) -> {
            if (et_message.length() >70) {
                message = et_message.getText().toString();
                message = message.substring(0, 69);
                et_message.setText(message);
            }
            setTextView(tv_message, et_message.length());
            return false;
        });

        Button bt_send = findViewById(R.id.bt_group_send_sms_send_id);
        bt_send.setOnClickListener(view -> sendMessage(view.getRootView().findViewById(R.id.et_group_send_sms_message_id)));

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportActionBar().setTitle(Manager.compute.titleActivity(title1, title2));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_group_send_sms));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTextView(TextView textView, int carac) {
        textView.setText(String.format(getString(R.string.label_text_phone_compute_message), carac));
    }

    private void sendMessage(EditText editText) {
        if (canSend(editText)) {
            if (PhoneProcess.sendSmsMessage(numbers, message) == -1)
                showMessage(getString(R.string.error_send_message));
            else {
                switch (pageToDisplay) {
                    case AttributeName.Page_Sms_Contact:
                    case AttributeName.Page_Sms_Trixo:
                        showMessage(getString(R.string.success_contact_send_message));
                        break;
                    case AttributeName.Page_Sms_Group:
                        showMessage(getString(R.string.success_group_send_message));
                        break;
                }
            }
            onBackPressed();
        }
    }

    private boolean canSend(EditText editText) {
        message = editText.getText().toString();
        if (message.isEmpty()) {
            showMessage(getResources().getString(R.string.error_send_message_empty));
            return false;
        }
        if (message.length() > 70) {
            showMessage(getResources().getString(R.string.error_send_message_long));
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(getBaseContext(), message);
    }

}
