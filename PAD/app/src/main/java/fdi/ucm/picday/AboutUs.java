package fdi.ucm.picday;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutUs extends Activity {

    private WebView aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        aboutUs = (WebView) findViewById(R.id.webview);
        aboutUs.loadUrl("file:///android_asset/www/index.html");

    }
}
