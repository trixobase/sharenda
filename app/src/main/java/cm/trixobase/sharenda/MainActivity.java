package cm.trixobase.sharenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.domain.activity.home.PhoneActivity;
import cm.trixobase.sharenda.domain.activity.registration.RegistrationHomeActivity;
import cm.trixobase.sharenda.system.Sharenda;
import cm.trixobase.sharenda.system.manager.Manager;
import cm.trixobase.sharenda.system.database.RequestHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sharenda.setLanguage(this);
        setContentView(R.layout.splash);

        ImageView iv = findViewById(R.id.imageView_logo_id);
        iv.setImageResource(R.drawable.sharenda_logo_circle);

        String interfaceToDisplay = Manager.getData(this, AttributeName.Interface_To_show, AttributeName.Interface_Event);

        Thread chrono = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    Log.e(Sharenda.Log, "Main Error - " + e);
                } finally {
                    if (!RequestHandler.UserHasRegistered(getBaseContext())) {
                        goToRegistrationHomeActivity();
                    } else {
                        switch (interfaceToDisplay) {
                            case AttributeName.Interface_Phone:
                                goToPhoneActivity();
                                break;
                            case AttributeName.Interface_Event:
                                goToEventActivity();
                                break;
                        }
                    }
                }
            }
        };
        chrono.start();
    }

    private void goToRegistrationHomeActivity() {
        Intent i = new Intent(getBaseContext(), RegistrationHomeActivity.class);
        Manager.saveData(getBaseContext(), AttributeName.Page_Registration_To_Show, AttributeName.Page_Registration_Registration);
        startActivity(i);
    }

    private void goToPhoneActivity() {
        Intent i = new Intent(getBaseContext(), PhoneActivity.class);
        startActivity(i);
    }

    private void goToEventActivity() {
        Intent i = new Intent(getBaseContext(), EventActivity.class);
        startActivity(i);
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}
