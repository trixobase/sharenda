package cm.trixobase.frontend;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.frontend.domain.adapter.PhonePagerAdapter;
import cm.trixobase.library.common.manager.Manager;
import cm.trixobase.library.common.widget.DialogBox;
import cm.trixobase.library.common.widget.Toast;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 08/06/18.
 */

public class PhoneActivity extends AppCompatActivity {

    private Service service;

    private List<ContentValues> contactList = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager phonePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        AppBarLayout toolbar = findViewById(R.id.toolbar);
        phonePager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        service = new Service(PhoneActivity.this, (ViewGroup) toolbar.getParent());
        Manager.setMetaDada(PhoneActivity.this, AttributeName.Interface_To_show, AttributeName.Interface_Phone);

        phonePager.setClipToPadding(false);
        phonePager.setPageMargin(12);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setView(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        phonePager.setAdapter(new PhonePagerAdapter(getSupportFragmentManager(), PhoneActivity.this));
        tabLayout.setupWithViewPager(phonePager);
        tabLayout.getTabAt(getView()).select();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (cm.trixobase.library.account.R.id.ic_menu_info_id == item.getItemId()) {
            DialogBox.builder(service.getContext())
                    .setContentView(R.layout.dialog_frame_account)
                    .build().show();
        } else onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        service.closeDatabase();
        super.onDestroy();
    }

    public Service getService() {
        return service;
    }

    private void setView(int view) {
        Manager.setMetaDada(service.getContext(), AttributeName.View_Activity_Phone, view);
    }

    private int getView() {
        return Manager.getMetaData(service.getContext(), AttributeName.View_Activity_Phone, 0);
    }

    private void showToast(String message) {
        Toast.builder(service.getContext()).setMessage(message).showShort();
    }
}