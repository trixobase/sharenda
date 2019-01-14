package cm.trixobase.sharenda.domain.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.common.TranslateElement;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.core.Group;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.domain.activity.home.PhoneActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.PhoneProcess;
import cm.trixobase.sharenda.system.view.listview.ListViewContactGroupAdapter;

public class GroupMembersActivity extends AppCompatActivity {

    private static String groupNameToSave;
    private static String groupCategoryToSave;
    public static ArrayList<String> membersNumberToSave = new ArrayList<>();
    private static int minContact;
    private static int maxContact;

    private static List<UiContact> members = new ArrayList<>();
    private static List<String> numbers = new ArrayList<>();
    private static String pageCalled;
    private static long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageCalled = PreferenceManager.getDefaultSharedPreferences(this).getString(AttributeName.Page_Group_Members_To_Show, AttributeName.Page_Group_Members_Contacts);
        groupId = getIntent().getLongExtra(AttributeName.Group_Id, -1);
        groupNameToSave = getIntent().getStringExtra(AttributeName.Group_Name);
        groupCategoryToSave = getIntent().getStringExtra(AttributeName.Group_Category);

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<UiContact> contacts = Contact.getAll(this);
        membersNumberToSave.clear();

        if (pageCalled.equalsIgnoreCase(AttributeName.Page_Group_Members_Contacts_To_Add)) {
                for (int id : getIntent().getBundleExtra(AttributeName.Group_Members_Numbers)
                        .getIntegerArrayList(AttributeName.Group_Members_Numbers)) {
                    membersNumberToSave.add(String.valueOf(id));
                }
                for (String number : membersNumberToSave) {
                    for (UiContact member : contacts) {
                        if (number.equalsIgnoreCase(member.getNumber())) {
                            contacts.remove(member);
                            break;
                        }
                    }
                }
                minContact = 1;
        }

        Collections.sort(contacts);
        members = contacts;

        ListView lv_contacts = findViewById(R.id.lv_contacts_id);
        ListViewContactGroupAdapter myAdapter = new ListViewContactGroupAdapter(this, members);
        lv_contacts.setAdapter(myAdapter);

        EditText et_search = findViewById(R.id.et_group_member_search_id);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        switch (pageCalled) {
            case AttributeName.Page_Group_Members_Invitation:
                getSupportActionBar().setTitle(R.string.label_text_invitation);
                getSupportActionBar().setSubtitle(R.string.subtitle_activity_group_send_invitation);
                minContact = 1;
                maxContact = Sharenda.Max_Contact_For_Invitation;
                break;
            case AttributeName.Page_Group_Members_Home:
            case AttributeName.Page_Group_Members_Contacts:
            case AttributeName.Page_Group_Members_Contacts_To_Add:
                getSupportActionBar().setTitle(Manager.compute.titleActivity(groupNameToSave, Manager.getTranslate(getResources(), TranslateElement.Category, groupCategoryToSave)));
                getSupportActionBar().setSubtitle(R.string.subtitle_activity_group_add_members);
                maxContact = Sharenda.Max_Contact_For_Group;
                if (pageCalled.equalsIgnoreCase(AttributeName.Page_Group_Members_Contacts_To_Add))
                    minContact = 0;
                else minContact = Sharenda.Min_Contact_For_Group;
                break;
        }

    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(getBaseContext(), message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (pageCalled) {
            case AttributeName.Page_Group_Members_Invitation:
                getMenuInflater().inflate(R.menu.menu_group_invite, menu);
                return true;
            default:
                getMenuInflater().inflate(R.menu.menu_save, menu);
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_action_save_id:
                doSave();
                return true;
            case R.id.ic_action_invite_id:
                sendInvite();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void doSave() {
        if (canSave()) {
            if (insertRecord() == -1) {
                showMessage(getResources().getString(R.string.error_save));
            } else {
                Intent i = new Intent();
                switch (pageCalled) {
                    case AttributeName.Page_Group_Members_Home:
                        i = new Intent(this, EventActivity.class);
                        GroupAddActivity.doFinish();
                        startActivity(i);
                        finish();
                        break;
                    case AttributeName.Page_Group_Members_Contacts_To_Add:
                        i = new Intent(this, GroupEditActivity.class);
                        GroupEditActivity.doFinish();
                        i.putExtra(AttributeName.Group_Id, groupId);
                        showMessage(getResources().getString(R.string.success_group_saved));
                        startActivity(i);
                        finish();
                        break;
                    case AttributeName.Page_Group_Members_Contacts:
                        GroupAddActivity.doFinish();
                        PhoneActivity.doFinish();
                        Manager.saveData(getBaseContext(), AttributeName.Page_Phone_To_Show, 1);
                        i = new Intent(getBaseContext(), PhoneActivity.class);
                        startActivity(i);
                        finish();
                        showMessage(getResources().getString(R.string.success_group_saved));
                        break;
                }
            }
        }
    }

    private long insertRecord() {
        UiGroup group = UiGroup.builder()
                .identifiedById(groupId).build()
                .setName(groupNameToSave)
                .setCategory(groupCategoryToSave)
                .setMembersNumber(membersNumberToSave);
        return Group.builder(this).withData(group.getData()).build().save();
    }

    private void sendInvite() {
        if (canSend()) {
            long result = -1;
            try {
                result = PhoneProcess.sendSmsMessage(numbers, Sharenda.SMS_Invitation_Message);
            } catch (Exception e) {
                showMessage(getResources().getString(R.string.error_send_message));
                Log.e(Sharenda.Log, "SMS non envoyé: " + e);
            } finally {
                if (result != -1) {
                    if (numbers.size() == 1)
                        showMessage(getResources().getString(R.string.success_invitation_send_message));
                    else
                        showMessage(getResources().getString(R.string.success_invitation_send_messages));
                } else showMessage(getResources().getString(R.string.error_send_message));
                onBackPressed();
            }
        }
    }

    private boolean canSend() {
        return canSave();
    }

    private boolean canSave() {
        if (membersNumberToSave.size() < minContact) {
            showMessage(getResources().getString(R.string.error_group_contact_save_min) + " " + minContact + ", " + getResources().getString(R.string.label_text_actual) + " " + membersNumberToSave.size());
            return false;
        }
        if (membersNumberToSave.size() > maxContact) {
            showMessage(getResources().getString(R.string.error_group_contact_save_max) + " " + maxContact + ", " + getResources().getString(R.string.label_text_actual) + " " + membersNumberToSave.size());
            return false;
        }
        return true;
    }

}
