package afyapepe.mobile.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import afyapepe.mobile.R;

public class Registactiv extends AppCompatActivity {
    private WebView webn;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registactiv);






          try {


              webn = (WebView) findViewById(R.id.webviewn); //Grabbing web view id from xml layout
              WebSettings webn2 = webn.getSettings(); //Attempting to enable JavaScript and any other caching that may be needed
              webn2.setJavaScriptEnabled(true);
              webn.loadUrl("http://afyapepe.com:8100/register"); //Loading URL into the web view container


          }

          catch (Exception e) {


              Toast.makeText(this, "Afya Pepe failed to retrieve data. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
          }
            webn.setWebViewClient(new WebViewClient());



        }
        //Trying to keep everything from the URL to be loaded in the container

        @Override
        public void onBackPressed()
        {
            if(webn.canGoBack())
            {
                webn.goBack();
            }
            else
            {
                super.onBackPressed();
            }


    }




    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            pbar.setVisibility(View.GONE);

        }

    }




public String errors()
{
    String err="Afya Pepe failed to retrieve data. Please check your internet connection and try again";
    return err;

}



}
