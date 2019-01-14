package cm.trixobase.sharenda.domain.activity.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import cm.trixobase.sharenda.MainActivity;
import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.media.ImagePicker;
import cm.trixobase.sharenda.system.media.ImageProcess;

public class GroupAddActivity extends AppCompatActivity {

    private static Activity activity;

    private static final int PICK_IMAGE_ID = 234;
    private String groupName = "";
    private String groupCategory = "";
    private Bitmap groupPicture;

    private ImageView iv_group, iv_group_name_flag, iv_group_category_flag;
    private TextView tv_camera, tv_name, tv_group_category;
    private EditText et_group_name;
    private Spinner sp_category;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.label_title_activity_group_add));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv_group = findViewById(R.id.iv_camera_id);
        iv_group_name_flag = findViewById(R.id.iv_groupe_name_flag_id);
        iv_group_category_flag = findViewById(R.id.iv_group_category_id);
        tv_camera = findViewById(R.id.tv_camera_id);
        tv_name = findViewById(R.id.tv_group_name_id);
        tv_group_category = findViewById(R.id.tv_group_category_id);
        sp_category = findViewById(R.id.sp_category_id);
        et_group_name = findViewById(R.id.et_name_group_id);
        fab = findViewById(R.id.fab);

        View.OnClickListener listener = view -> {
            Intent i = ImagePicker.getPickImageIntent(getBaseContext());
            startActivityForResult(i, PICK_IMAGE_ID);
        };

        sp_category.setOnFocusChangeListener((view, b) -> {
            if (b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sp_category.getWindowToken(), 0);
                MainActivity.hideSoftKeyboard(this, view);
            }
        });
        tv_camera.setOnClickListener(listener);
        iv_group.setOnClickListener(listener);
        tv_name.requestFocus();
        iv_group.setImageResource(R.drawable.ic_camera);
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

        loadCategories();

        fab.setImageResource(R.drawable.ic_arrow_next_1);
        fab.setOnClickListener(view -> {
            if (canPass()) {
                goToNextStep();
            }
        });

    }

    private boolean canPass() {
        groupName = et_group_name.getText().toString();
        if (groupName.isEmpty()) {
            showToastMessage(getResources().getString(R.string.error_group_name_save), tv_name, iv_group_name_flag);
            return false;
        } else {
            groupName = groupName.replaceFirst(groupName.substring(0, 1), groupName.substring(0, 1).toUpperCase());
            iv_group_name_flag.setImageResource(R.drawable.ic_action_accept);
            tv_name.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (groupCategory.equalsIgnoreCase(getString(R.string.group_category_any))) {
            showToastMessage(getString(R.string.error_group_category_save), tv_group_category, iv_group_category_flag);
            return false;
        } else {
            iv_group_category_flag.setImageResource(R.drawable.ic_action_accept);
            tv_group_category.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        return true;
    }

    private void goToNextStep() {
        Intent i = new Intent(getBaseContext(), GroupMembersActivity.class);
        if (groupPicture != null) {
                ImageProcess.saveGroupPicture(this, groupName, groupPicture);
        }
        i.putExtra(AttributeName.Group_Name, groupName);
        i.putExtra(AttributeName.Group_Category, groupCategory);
        startActivity(i);
    }

    private void showToastMessage(String message, TextView textView, ImageView imageView) {
        textView.setTextColor(getResources().getColor(R.color.text_widget_form_error));
        imageView.setImageResource(R.drawable.ic_action_cancel);
        Manager.showToastShortMessage(getBaseContext(), message);
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

    private void loadCategories() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.list_group_categories,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter);

    }

    public static void doFinish() {
        activity.finish();
    }

}
