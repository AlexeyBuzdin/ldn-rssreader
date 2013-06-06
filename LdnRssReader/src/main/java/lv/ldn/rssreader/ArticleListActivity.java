package lv.ldn.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import lv.ldn.rssreader.adapter.ArticleListAdapter;
import lv.ldn.rssreader.db.DbAdapter;
import lv.ldn.rssreader.rss.RssService;
import lv.ldn.rssreader.rss.domain.Article;

public class ArticleListActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String BLOG_URL = "http://habrahabr.ru/rss/hubs/"; //http://blog.nerdability.com/feeds/posts/default
    private RssService rssService;

    private DbAdapter dba;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);
        this.listView = (ListView) findViewById(R.id.listView);

        dba = new DbAdapter(this);
        listView.setOnItemClickListener(this);
        refreshList();
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Article selected = (Article) listView.getAdapter().getItem(i);
        
        //mark article as read
        dba.openToWrite();
        dba.markAsRead(selected.getGuid());
        dba.close();
        selected.setRead(true);
        ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();

        Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
        detailIntent.putExtra(ArticleDetailActivity.URL, selected.getGuid());
        startActivity(detailIntent);
    }

    private void refreshList(){
        rssService = new RssService(this);
        rssService.execute(BLOG_URL);
    }

    public void setListAdapter(ArticleListAdapter adapter) {
        listView.setAdapter(adapter);
    }

}
