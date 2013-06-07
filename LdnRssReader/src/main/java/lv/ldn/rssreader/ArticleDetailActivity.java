package lv.ldn.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import lv.ldn.rssreader.rss.domain.Article;

public class ArticleDetailActivity extends Activity {

    public static final String URL = "url";

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        article = (Article) getIntent().getSerializableExtra(URL);

        initView();
    }

  public void initView(){
      TextView title = (TextView) findViewById(R.id.article_title);
      title.setText(article.getTitle());

      TextView author = (TextView) findViewById(R.id.article_author);
      author.setText(article.getAuthor());

      TextView description = (TextView) findViewById(R.id.article_detail);
      description.setMovementMethod(LinkMovementMethod.getInstance());
      description.setText(Html.fromHtml(article.getDescription()));
  }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(URL, article);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        article = (Article) savedInstanceState.getSerializable(URL);
    }

}
