package cm.trixobase.frontend;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cm.trixobase.library.common.widget.DialogBox;

/*
 * Powered by Trixobase Enterprise on 08/01/21.
 * updated on 07/02/21.
 */

public class AboutActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Button bt_trixobase = findViewById(R.id.bt_trixobase_id);
            bt_trixobase.setOnClickListener(view -> DialogBox.builder(AboutActivity.this)
                    .setTitle("Team's developers")
                    .setContentView(R.layout.dialog_frame_trixobase).build().show());

        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

}