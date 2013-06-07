package lv.ldn.rssreader.adapter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import lv.ldn.rssreader.R;
import lv.ldn.rssreader.rss.domain.Article;


public class ArticleListAdapter extends ArrayAdapter<Article> {

	public ArticleListAdapter(Activity activity, List<Article> articles) {
		super(activity, 0, articles);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        // TODO 1: Inflate list item View
		Activity activity = (Activity) getContext();
		LayoutInflater inflater = activity.getLayoutInflater();

		View rowView = inflater.inflate(R.layout.article_list_item, null);

        // TODO 2: Fill article info
        Article article = getItem(position);

		TextView textView = (TextView) rowView.findViewById(R.id.article_title_text);
		textView.setText(article.getTitle());
		
		TextView dateView = (TextView) rowView.findViewById(R.id.article_listing_smallprint);
		String pubDate = article.getPubDate();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss Z");
		Date pDate;
		try {
			pDate = df.parse(pubDate);
			pubDate = "published " + getDateDifference(pDate) + " by " + article.getAuthor();
		} catch (ParseException e) {
			Log.e("DATE PARSING", "Error parsing date..");
			pubDate = "published by " + article.getAuthor();
		}
		dateView.setText(pubDate);

		// TODO 3: Set read article background
		if (!article.isRead()){
			LinearLayout row = (LinearLayout) rowView.findViewById(R.id.article_row_layout);
			row.setBackgroundColor(Color.WHITE);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
		}
		return rowView;

	}

    public static String getDateDifference(Date thenDate){
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(thenDate);


        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffMinutes<60){
            if (diffMinutes==1)
                return diffMinutes + " minute ago";
            else
                return diffMinutes + " minutes ago";
        } else if (diffHours<24){
            if (diffHours==1)
                return diffHours + " hour ago";
            else
                return diffHours + " hours ago";
        }else if (diffDays<30){
            if (diffDays==1)
                return diffDays + " day ago";
            else
                return diffDays + " days ago";
        }else {
            return "a long time ago..";
        }
    }
}