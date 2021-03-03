package cm.trixobase.frontend;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import cm.trixobase.library.common.constants.BaseName;
import cm.trixobase.library.common.widget.ProgressBox;
import cm.trixobase.library.service.Service;
import dmax.dialog.SpotsDialog;

/*
 * Powered by Trixobase Enterprise on 17/10/20.
 * updated on 14/02/21.
 */

public class WebActivity extends AppCompatActivity {

    private Service service;

    private SpotsDialog progressDialog;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        LinearLayout toolbar = findViewById(R.id.linear);
        service = new Service(WebActivity.this, (ViewGroup) toolbar.getParent());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.wv_page_id);
        progressDialog = ProgressBox.builder(WebActivity.this).build();
        progressDialog.setCancelable(true);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progressDialog != null)
                    progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (progressDialog != null)
                    progressDialog = null;
            }

        });
        webView.loadUrl(getIntent().getStringExtra(BaseName.URL));

        new Timer().schedule(new loaderTask(), 12000);
    }

    class loaderTask extends TimerTask {
        public void run() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                webView.setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                super.onBackPressed();
        }
        return true;
    }

    public Service getService() {
        return service;
    }

}