package lv.ldn.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

import java.io.Serializable;
import java.net.URL;

import lv.ldn.rssreader.db.DbAdapter;
import lv.ldn.rssreader.rss.domain.Article;

public class ArticleDetailActivity extends Activity {

    public static final String URL = "url";

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        Article article = (Article) getIntent().getSerializableExtra(URL);

        url = article.getUrl().toString();

        initView();
    }

  public void initView(){

      final WebView myWebView = (WebView) findViewById(R.id.webView);
      myWebView.getSettings().setJavaScriptEnabled(true);
      myWebView.loadUrl(url);
  }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(URL, url);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        url = savedInstanceState.getString(URL);
    }

}
