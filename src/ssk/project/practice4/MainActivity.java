package ssk.project.practice4;

import android.app.ListActivity;
import android.os.Bundle;

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
		
	}
}
