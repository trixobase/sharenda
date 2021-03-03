package cm.trixobase.frontend;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.frontend.domain.fragment.InfoMessageFragment;
import cm.trixobase.frontend.domain.fragment.InfoTeamFragment;
import cm.trixobase.library.common.manager.Manager;
import cm.trixobase.library.common.widget.DialogBox;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 24/06/19.
 * updated on 20/02/21.
 */

public class InfoActivity extends AppCompatActivity {

    private Service service;

    private TextView tv_title1, tv_title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ConstraintLayout toolbar = findViewById(R.id.constraint);
        service = new Service(InfoActivity.this, (ViewGroup) toolbar.getParent());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_title1 = findViewById(R.id.tv_one_id);
        tv_title2 = findViewById(R.id.tv_two_id);

        tv_title1.setOnClickListener(view -> setFragment(1));
        tv_title2.setOnClickListener(view -> setFragment(2));

        doResume();
    }

    private void setFragment(int view) {
        tv_title1.setTextColor(1 == view ? getResources().getColor(R.color.tab_layout_text_selected) : getResources().getColor(R.color.tab_layout_text_no_selected));
        tv_title2.setTextColor(2 == view ? getResources().getColor(R.color.tab_layout_text_selected) : getResources().getColor(R.color.tab_layout_text_no_selected));
        setFragment(1 == view ? InfoTeamFragment.builder().build() : InfoMessageFragment.builder(service).build());
        setView(1 == view ? AttributeName.View_Fragment_Team : AttributeName.View_Fragment_Message);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void setView(int view) {
        Manager.setMetaDada(service.getContext(), AttributeName.View_Activity_info, view);
    }

    private int getView() {
        return Manager.getMetaData(service.getContext(), AttributeName.View_Activity_info, 1);
    }

    private void doResume() {
        switch (getView()) {
            case AttributeName.View_Fragment_Message:
                setFragment(2);
                break;
            case AttributeName.View_Fragment_Team:
            default:
                setFragment(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (getView()) {
            case AttributeName.View_Fragment_Message:
                setFragment(1);
                break;
            case AttributeName.View_Fragment_Team:
            default:
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.ic_menu_info_id == item.getItemId()) {
            DialogBox.builder(service.getContext())
                    .setContent(getString(R.string.displaying_message))
                    .build().show();
        } else super.onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        service.closeDatabase();
        super.onDestroy();
    }

}