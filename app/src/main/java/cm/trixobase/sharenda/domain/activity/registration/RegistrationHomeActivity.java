package cm.trixobase.sharenda.domain.activity.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.domain.activity.PolicyActivity;

public class RegistrationHomeActivity extends Activity {

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_home);
        activity = this;

        ImageView iv = findViewById(R.id.imageView_logo_id);
        iv.setImageResource(R.drawable.sharenda_logo_circle);

        Button continueButton = findViewById(R.id.button_continue_id);
        continueButton.setOnClickListener(view -> {
            Intent i = new Intent(getBaseContext(), Registration.class);
            startActivity(i);
        });
    }

    public void goToPolicyActivity(View v) {
        Intent i = new Intent(this, PolicyActivity.class);
        startActivity(i);
    }

    public static void doFinish() {
        if (activity != null)
        activity.finish();
    }

}
