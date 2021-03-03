package cm.trixobase.sharenda;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cm.trixobase.frontend.domain.AttributeName;
import cm.trixobase.library.common.manager.Manager;
import cm.trixobase.library.common.manager.Response;
import cm.trixobase.library.common.widget.DialogBox;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 07/02/21.
 * updated on 20/02/21.
 */

public class WelcomeActivity extends AppCompatActivity {

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        service = new Service(WelcomeActivity.this, (ViewGroup) findViewById(R.id.constraintLayout).getParent());

        Button bt_begin = findViewById(R.id.bt_top_id);
        bt_begin.setOnClickListener(view -> goToHome());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Manager.setMetaDada(service.getContext(), AttributeName.FIRST_TIME, false);
    }

    private void goToHome() {
//        startActivity(new Intent(getBaseContext(), HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        DialogBox.builder(this)
                .setContent(getString(R.string.quit_application))
                .setButton(getString(R.string.cancel), getString(R.string._yes))
                .setActions(new Response.onFrontResult() {
                    @Override
                    public void onTrue(Response response) {
                        finish();
                    }
                    @Override
                    public void onFalse(Response response) {

                    }
                    @Override
                    public void onMessage(String message) {
                    }
                })
                .build().show();
    }

}