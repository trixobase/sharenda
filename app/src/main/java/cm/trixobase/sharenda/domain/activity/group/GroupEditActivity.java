package cm.trixobase.sharenda.domain.activity.group;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.trixobase.sharenda.MainActivity;
import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.core.Contact;
import cm.trixobase.sharenda.core.Group;
import cm.trixobase.sharenda.domain.activity.home.PhoneActivity;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.domain.ui.UiGroup;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.ImagePicker;
import cm.trixobase.sharenda.system.media.ImageProcess;
import cm.trixobase.sharenda.system.view.listview.ListViewGroupMemberAdapter;

public class GroupEditActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    private Intent intent;
    public static final String PAGE = "page_group_edit";
    private static final int PICK_IMAGE_ID = 23542;

    private String newGroupName;
    private String oldGroupName;
    private String groupCategory;
    private Bitmap groupPicture;
    public static ArrayList<String> membersNumberToSave = new ArrayList<>();
    private List<UiContact> members = new ArrayList<>();

    private UiGroup groupToSave;
    private UiGroup groupToUpdate;
    private long groupId;
    private int categoryPosition = 0;
    private boolean groupOldPicturePresent = false;

    private static Resources resources;
    private ImageView iv_group, iv_group_name_flag, iv_group_category_flag;
    private TextView tv_camera, tv_name;
    @SuppressLint("StaticFieldLeak")
    private static TextView tv_subtitle;
    private TextView tv_group_category;
    private EditText et_group_name;
    private Spinner sp_category;
    private Button bt_member_add;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        activity = this;
        toolbar = findViewById(R.id.toolbar);
        iv_group = findViewById(R.id.iv_camera_id);
        iv_group_name_flag = findViewById(R.id.iv_groupe_name_flag_id);
        iv_group_category_flag = findViewById(R.id.iv_group_category_id);
        tv_camera = findViewById(R.id.tv_camera_id);
        tv_name = findViewById(R.id.tv_group_name_id);
        tv_group_category = findViewById(R.id.tv_group_category_id);
        tv_subtitle = findViewById(R.id.tv_members_id);
        sp_category = findViewById(R.id.sp_category_id);
        et_group_name = findViewById(R.id.et_name_group_id);
        bt_member_add = findViewById(R.id.bt_group_edit_add_member_id);
        resources = tv_name.getResources();

        setupListener();

    }

    private void setupListener() {
        View.OnClickListener listener = view -> {
            intent = ImagePicker.getPickImageIntent(getBaseContext());
            startActivityForResult(intent, PICK_IMAGE_ID);
        };
        iv_group.setOnClickListener(listener);
        tv_camera.setOnClickListener(listener);
        sp_category.setOnFocusChangeListener((view, b) -> {
            if (b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sp_category.getWindowToken(), 0);
            }
        });
        tv_camera.setOnClickListener(listener);
        iv_group.setOnClickListener(listener);
        tv_name.requestFocus();
        iv_group.setImageResource(R.drawable.ic_camera);
        bt_member_add.setOnClickListener(view -> goToAddMembers());
        sp_category.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupCategory = adapterView.getSelectedItem().toString();
                switch (groupCategory) {
                    case "Enjoy":
                    case "Loisirs":
                        groupCategory = AttributeName.Category_Enjoy;
                        break;
                    case "Work":
                    case "Travail":
                        groupCategory = AttributeName.Category_Work;
                        break;
                    case "Business":
                        groupCategory = AttributeName.Category_Business;
                        break;
                    case "Studies":
                    case "Etudes":
                        groupCategory = AttributeName.Category_Studies;
                        break;
                    case "Family":
                    case "Famille":
                        groupCategory = AttributeName.Category_Family;
                        break;
                    case "Sport":
                        groupCategory = AttributeName.Category_Sport;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MainActivity.hideSoftKeyboard(GroupEditActivity.this, tv_name);
    }

    @Override
    public void onResume() {
        super.onResume();
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(R.string.subtitle_activity_group_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupId = getIntent().getLongExtra(AttributeName.Group_Id, -1);
        groupToSave = Group.getById(getBaseContext(), groupId);
        groupToUpdate = groupToSave;
        members = Contact.getAllByGroup(getBaseContext(), groupId);
        Collections.sort(members);
        oldGroupName = groupToSave.getName();
        newGroupName = oldGroupName;
        groupCategory = groupToSave.getCategory();
        getSupportActionBar().setTitle(newGroupName);

        setPicture();
        loadCategories();
        showMembers();
    }

    private void setPicture() {
        groupPicture = ImageProcess.getGroupPicture(getBaseContext(), oldGroupName);
        if (groupPicture != null) {
            iv_group.setImageBitmap(groupPicture);
            groupOldPicturePresent = true;
        } else iv_group.setImageResource(R.drawable.ic_camera);
    }

    private void showMembers() {
        membersNumberToSave.clear();
        for (UiContact contact : members) {
            membersNumberToSave.add(contact.getNumber());
        }
        setSubtitle(members.size());
        et_group_name.setText(oldGroupName);
        sp_category.setSelection(categoryPosition);
        ListView lv_members = findViewById(R.id.lv_group_members_id);
        lv_members.setAdapter(new ListViewGroupMemberAdapter(getBaseContext(), members));
    }

    @SuppressLint("SetTextI18n")
    public static void setSubtitle(int membersSize) {
        String format = String.valueOf(membersSize);
        if (format.length() == 1)
            format = 0 + format;
        tv_subtitle.setText(resources.getString(R.string.label_subtitle_members) + " " + format);
    }

    private void loadCategories() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_group_categories,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter);

        switch (groupCategory) {
            case AttributeName.Category_Enjoy:
                categoryPosition = 6;
                break;
            case AttributeName.Category_Work:
                categoryPosition = 4;
                break;
            case AttributeName.Category_Studies:
                categoryPosition = 2;
                break;
            case AttributeName.Category_Sport:
                categoryPosition = 5;
                break;
            case AttributeName.Category_Family:
                categoryPosition = 3;
                break;
            case AttributeName.Category_Business:
                categoryPosition = 1;
                break;
        }
    }

    private boolean canSave() {
        newGroupName = et_group_name.getText().toString();
        if (newGroupName.isEmpty()) {
            showError(getResources().getString(R.string.error_group_name_save), tv_name, iv_group_name_flag);
            return false;
        } else {
            newGroupName = Manager.formatName(newGroupName);
            iv_group_name_flag.setImageResource(R.drawable.ic_action_accept);
            tv_name.setTextColor(resources.getColor(R.color.colorAccent));
        }
        if (groupCategory.equalsIgnoreCase(getString(R.string.group_category_any))) {
            showError(getString(R.string.error_group_category_save), tv_group_category, iv_group_category_flag);
            return false;
        } else {
            iv_group_category_flag.setImageResource(R.drawable.ic_action_accept);
            tv_group_category.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (membersNumberToSave.size() < Sharenda.Min_Contact_For_Group) {
            Toast.makeText(this, getString(R.string.error_group_contact_save_min) + " " + + Sharenda.Min_Contact_For_Group + ", " +  resources.getString(R.string.label_text_actual) + " " + membersNumberToSave.size(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (membersNumberToSave.size() > Sharenda.Max_Contact_For_Group) {
            Toast.makeText(this, getString(R.string.error_group_contact_save_max) + " " + + Sharenda.Max_Contact_For_Group + ", " +  resources.getString(R.string.label_text_actual) + " " + membersNumberToSave.size(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void goToAddMembers() {
        Bundle bundle = new Bundle();
        newGroupName = et_group_name.getText().toString();
        ArrayList<Integer> list = new ArrayList<>();
        for (String id : membersNumberToSave)
            list.add(Integer.valueOf(id));
        bundle.putIntegerArrayList(AttributeName.Group_Members_Numbers, list);
        savePicture();
        intent = new Intent(getBaseContext(), GroupMembersActivity.class);
        intent.putExtra(AttributeName.Interface_To_show, GroupEditActivity.PAGE);
        intent.putExtra(AttributeName.Group_Id, groupId);
        intent.putExtra(AttributeName.Group_Name, newGroupName);
        intent.putExtra(AttributeName.Group_Category, groupCategory);
        intent.putExtra(AttributeName.Group_Members_Numbers, bundle);
        Manager.saveData(this, AttributeName.Page_Group_Members_To_Show, AttributeName.Page_Group_Members_Contacts_To_Add);
        startActivity(intent);
    }

    private void showError(String message, TextView textView, ImageView imageView) {
        textView.setTextColor(getResources().getColor(R.color.text_widget_form_error));
        imageView.setImageResource(R.drawable.ic_action_cancel);
        showMessage(message);
    }

    private void showMessage(String message) {
        Manager.showToastLongMessage(getBaseContext(), message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        intent = new Intent(getBaseContext(), PhoneActivity.class);
        switch (item.getItemId()) {
            case R.id.ic_action_group_edit_del_id:
                 getConfirmationAndDelete();
                return true;
            case R.id.ic_action_group_edit_save_id:
                if (canSave()) {
                    if (insertRecord() == -1)
                        showMessage(resources.getString(R.string.error_save));
                    else {
                        PhoneActivity.doFinish();
                        startActivity(intent);
                        showMessage(resources.getString(R.string.success_group_edited));
                        onBackPressed();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getConfirmationAndDelete() {
        AlertDialog.Builder box = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
        box.setTitle(resources.getString(R.string.label_text_dialog_del));
        box.setPositiveButton(R.string.label_text_response_no, (dialogInterface, i) -> {
        }).setNegativeButton(R.string.label_text_response_yes, (dialogInterface, i) -> {
            if (Group.builder(getBaseContext()).build().delete(groupToUpdate.getId()) == -1) {
                showMessage(resources.getString(R.string.error_delete));
            }
            else {
                intent = new Intent(getBaseContext(), PhoneActivity.class);
                PhoneActivity.doFinish();
                startActivity(intent);
                showMessage(resources.getString(R.string.success_group_deleted));
                finish();
            }
        }).show();
    }

    private long insertRecord() {
        savePicture();
        groupToSave.setName(newGroupName)
                .setCategory(groupCategory)
                .setMembersNumber(membersNumberToSave);

        return Group.builder(this).withData(groupToSave.getData()).build().save();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap picture = ImagePicker.getImageFromResult(this, resultCode, data);
                if (picture != null) {
                    groupPicture = picture;
                    ImageView image = super.findViewById(R.id.iv_camera_id);
                    image.setImageBitmap(groupPicture);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void savePicture() {
        if (groupPicture != null) {
            if (groupOldPicturePresent)
                ImageProcess.updateGroupPicture(this, oldGroupName, newGroupName, groupPicture);
            else ImageProcess.saveGroupPicture(this, newGroupName, groupPicture);
        }
    }

    public static void doFinish() {
        activity.finish();
    }

}
