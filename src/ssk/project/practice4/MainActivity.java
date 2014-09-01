package ssk.project.practice4;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends ListActivity {

	/**
	 * Name of Twitter account that you are following.
	 */
	String screenName = "maroon5";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		downloadTweets();
	}
	
	/**
	 * Checks for Internet Connection, then Creates new DownloadTask for downloading
	 * Tweets.
	 */
	public void downloadTweets() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			new DownloadTwitterTask().execute(screenName);
		} else {
			Log.v("DownloadTweets()", "No Internet Connection");
		}
	}
	
	private class DownloadTwitterTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			return null;
		}
		
	}
	
	
}
