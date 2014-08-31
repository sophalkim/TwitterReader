package ssk.project.practice3;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

public class MainActivity extends ListActivity {

	private ListActivity activity;
	final static String screenName = "nasa";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		downloadTweets();
	}
	
	public void downloadTweets() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadTwitterTask().execute(screenName);
		} else {
			Log.v("downloadTweets()", "No Network Connection");
		}
	}
	
	public class DownloadTwitterTask extends AsyncTask<String, Void, String> {

		final static String CONSUMER_KEY = "CIVZ9ru4o1OxePUeOlJi4qYV5";
		final static String CONSUMER_SECRET = "sSzDMSHp3dxsQuKGiXNqje8FcNX7kNFFGTWyruhMsABG5ai2jO";
		final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
		final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
		
		
		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;
			if (screenNames.length > 0) {
				result = getTwitterStream(screenNames[0]);
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Twitter twits = jsonToTwitter(result);
			for (Tweet tweet : twits) {
				Log.i("DownloadTwitterTask", tweet.getText());
			}
			ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(activity, android.R.layout.simple_list_item_1, twits);
			setListAdapter(adapter);
		}
		
		private Twitter jsonToTwitter(String json) {
			Twitter twits = null;
			if (json != null && json.length() > 0) {
				try {
					Gson gson = new Gson();
					twits = gson.fromJson(json, Twitter.class);
				} catch (IllegalStateException ex) {
					ex.printStackTrace();
				}
			}
			return twits;
		}
		
	}
}
