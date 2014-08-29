package ssk.project.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import ssk.project.twitterreader.Authenticated;
import ssk.project.twitterreader.Tweet;
import ssk.project.twitterreader.Twitter;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

public class MainActivity extends ListActivity {

	private ListActivity activity;
	final static String screenName = "katyperry";
	
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
		
		private Twitter jsonToTwitter(String result) {
			Twitter twits = null;
			if (result != null && result.length() > 0) {
				try {
					Gson gson = new Gson();
					twits = gson.fromJson(result, Twitter.class);
				} catch (IllegalStateException ex) {
					
				}
			}
			return twits;
		}
		
		private Authenticated jsonToAuthenticated(String rawAuthorization) {
			Authenticated auth = null;
			if (rawAuthorization != null && rawAuthorization.length() > 0) {
				try {
					Gson gson = new Gson();
					auth = gson.fromJson(rawAuthorization, Authenticated.class);
				} catch (IllegalStateException ex) {
					
				}
			}
			return auth;
		}
		
		private String getResponseBody(HttpRequestBase request) {
			StringBuilder sb = new StringBuilder();
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
				HttpResponse response = httpClient.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				String reason = response.getStatusLine().getReasonPhrase();
				
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					
					BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
					String line = null;
					while ((line = bReader.readLine()) != null) {
						sb.append(line);
					}
				} else {
					sb.append(reason);
				}
			} catch (UnsupportedEncodingException ex) {	
			} catch (ClientProtocolException ex1) {
			} catch (IOException e2) {
			}
			return sb.toString();
		}
		
		private String getTwitterStream(String screenName) {
			String results = null;
			try {
				String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
				String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
				String combined = urlApiKey + ":" + urlApiSecret;
				String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
				
				HttpPost httpPost = new HttpPost(TwitterTokenURL);
				httpPost.setHeader("Authorization", "Basic " + base64Encoded);
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
				String rawAuthorization = getResponseBody(httpPost);
				Authenticated auth = jsonToAuthenticated(rawAuthorization);
				
				if (auth != null && auth.token_type.equals("bearer")) {
					HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);
					httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
					httpGet.setHeader("Content-Type", "application/json");
					results = getResponseBody(httpGet);
				}
			} catch (UnsupportedEncodingException ex) {
				
			} catch (IllegalStateException ex1) {
				
			}
			return results;
		}
	}
}
