package lv.ldn.rssreader.rss;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import lv.ldn.rssreader.ArticleListActivity;
import lv.ldn.rssreader.adapter.ArticleListAdapter;
import lv.ldn.rssreader.db.DbAdapter;
import lv.ldn.rssreader.rss.domain.Article;
import lv.ldn.rssreader.rss.parser.RssHandler;


public class RssService extends AsyncTask<String, Void, List<Article>> {

	private ProgressDialog progress;
	private ArticleListActivity articleList;

	public RssService(ArticleListActivity articleList) {
		this.articleList = articleList;
		progress = new ProgressDialog(articleList);
		progress.setMessage("Loading...");
	}


	protected void onPreExecute() {
		Log.e("ASYNC", "PRE EXECUTE");
		progress.show();
	}


	protected  void onPostExecute(final List<Article>  articles) {
		Log.e("ASYNC", "POST EXECUTE");
        articleList.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (Article a : articles){
					Log.d("DB", "Searching DB for GUID: " + a.getGuid());
					DbAdapter dba = new DbAdapter(articleList);
		            dba.openToRead();
		            Article fetchedArticle = dba.getBlogListing(a.getGuid());
		            dba.close();
					if (fetchedArticle == null){
						Log.d("DB", "Found entry for first time: " + a.getTitle());
						dba = new DbAdapter(articleList);
			            dba.openToWrite();
			            dba.insertBlogListing(a.getGuid());
			            dba.close();
					}else{
						a.setDbId(fetchedArticle.getDbId());
						a.setOffline(fetchedArticle.isOffline());
						a.setRead(fetchedArticle.isRead());
					}
				}
				ArticleListAdapter adapter = new ArticleListAdapter(articleList, articles);
				articleList.setListAdapter(adapter);
				adapter.notifyDataSetChanged();
				
			}
		});
		progress.dismiss();
	}


	@Override
	protected List<Article> doInBackground(String... urls) {
		String feed = urls[0];

		URL url = null;
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			url = new URL(feed);
			RssHandler rh = new RssHandler();

			xr.setContentHandler(rh);
			xr.parse(new InputSource(url.openStream()));


			Log.e("ASYNC", "PARSING FINISHED");
			return rh.getArticleList();

		} catch (IOException e) {
			Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
		} catch (SAXException e) {
			Log.e("RSS Handler SAX", e.toString());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.e("RSS Handler Parser Config", e.toString());
		}

		return null;

	}
}