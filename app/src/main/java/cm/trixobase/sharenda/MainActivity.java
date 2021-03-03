package cm.trixobase.sharenda;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cm.trixobase.frontend.EventActivity;
import cm.trixobase.frontend.InfoActivity;
import cm.trixobase.frontend.PhoneActivity;
import cm.trixobase.frontend.WebActivity;
import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.library.common.manager.Manager;
import cm.trixobase.library.common.manager.Response;
import cm.trixobase.library.common.widget.Toast;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 24/02/21.
 */

public class MainActivity extends AppCompatActivity {

    private Service service;

    private IBinder binderService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderService = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new Service(MainActivity.this, (ViewGroup) findViewById(R.id.tv_loading_id).getParent());
        service.initializeSetting(result);

        Intent intent = service.getIntentService();
        startService(intent);

        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private Response.onFrontResult result = new Response.onFrontResult() {
        @Override
        public void onTrue(Response response) {
            stopLoading();
        }

        @Override
        public void onFalse(Response response) {
            showToast(response.getMessage());
            finish();
        }

        @Override
        public void onMessage(String message) {
            showToast(message);
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(connection);
        service.closeDatabase();
        super.onDestroy();
    }

    private void stopLoading() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2500);
                    runOnUiThread(() -> doResume());
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    showLog(e.getMessage());
                }
            }
        }.start();
    }

    private void doResume() {
        service.startUpdateApplication(binderService, InfoActivity.class);
        boolean isFirstTime = Manager.getMetaData(service.getContext(), AttributeName.FIRST_TIME, true);
        if (!isFirstTime) {
            goToHome();
            return;
        }

        final LinearLayout ll_loading = findViewById(R.id.ll_loading_id);
        final LinearLayout ll_rules = findViewById(R.id.ll_rule_id);
        final CheckBox cb_validation = findViewById(R.id.cb_validation_id);
        final TextView tv_rules = findViewById(R.id.tv_rule_id);
        final Button bt_continue = findViewById(R.id.bt_bottom_id);

        ll_loading.setVisibility(View.GONE);
        ll_rules.setVisibility(View.VISIBLE);
        tv_rules.setOnClickListener(view -> goToRules());
        cb_validation.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked())
                bt_continue.setVisibility(View.VISIBLE);
            else bt_continue.setVisibility(View.INVISIBLE);
        });
        bt_continue.setOnClickListener(view -> goToWelcome());
    }

    private void goToRules() {
        if (service.phoneIsConnected()) {
            Intent intent = new Intent(service.getContext(), WebActivity.class);
            intent.putExtra(AttributeName.URL, service.getUrlPolicy());
            startActivity(intent);
        }
        else showToast(getString(R.string.warning_internet_check));
    }

    private void goToHome() {
        startActivity(new Intent(service.getContext(), getHomeClass()));
        finish();
    }

    private Class getHomeClass() {
        return AttributeName.Interface_Phone.equalsIgnoreCase(Manager.getMetaData(MainActivity.this, AttributeName.Interface_To_show, AttributeName.Interface_Phone))
                ? PhoneActivity.class : EventActivity.class;
    }

    private void goToWelcome() {
        startActivity(new Intent(service.getContext(), WelcomeActivity.class));
        finish();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.builder(service.getContext()).setMessage(message).showShort());
    }

    private void showLog(String message) {
        runOnUiThread(() -> Manager.showLog(MainActivity.class, message));
    }

}