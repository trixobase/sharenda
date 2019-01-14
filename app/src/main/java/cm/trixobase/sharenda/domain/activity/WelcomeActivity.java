package cm.trixobase.sharenda.domain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cm.trixobase.sharenda.R;
import cm.trixobase.sharenda.common.AttributeName;
import cm.trixobase.sharenda.domain.activity.home.EventActivity;
import cm.trixobase.sharenda.system.manager.Manager;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String name = getIntent().getStringExtra(AttributeName.User_Surname);
        TextView tv_letter = findViewById(R.id.tv_welcome_letter_title_id);
        TextView tv_message = findViewById(R.id.tv_welcome_message_title_id);

        tv_letter.setText(name.substring(0, 1).toUpperCase());
        tv_message.setText(name.substring(1, name.length()).concat(getString(R.string.label_message_welcome_title)));

        Button beginButton = findViewById(R.id.bt_button_begin_id);
        beginButton.setOnClickListener(view -> {
            Manager.saveData(getBaseContext(), AttributeName.Page_Group_Members_To_Show, AttributeName.Page_Group_Members_Home);
            Intent i = new Intent(getApplicationContext(), EventActivity.class);
            startActivity(i);
        });
    }

}
