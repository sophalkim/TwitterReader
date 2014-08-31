package ssk.project.practice3;

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
		
		private Authenticated jsonToAuthenticated(String raw) {
			Authenticated auth = null;
			if (raw != null && raw.length() > 0) {
				try {
					Gson gson = new Gson();
					auth = gson.fromJson(raw, Authenticated.class);
				} catch (IllegalStateException ex) {
					ex.printStackTrace();
				}
			}
			return auth;
		}
		
		private String getResponseBody(HttpRequestBase request) {
			StringBuilder sb = new StringBuilder();
			try {
				DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
				HttpResponse response = client.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				String reason = response.getStatusLine().getReasonPhrase();
				
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader bf = new BufferedReader(new InputStreamReader(is));
					String line = "";
					while ((line = bf.readLine()) != null) {
						sb.append(line);
					}
					bf.close();
				} else {
					sb.append(reason);
				}
			} catch (UnsupportedEncodingException ex) {
			} catch (ClientProtocolException ex1) {			
			} catch (IOException ex2) {		
			}
			return sb.toString();
		}
		
		private String getTwitterStream(String screenName) {
			String results = null;
			try {
				String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
				String urlSecretKey = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
				String combined = urlApiKey + ":" + urlSecretKey;
				String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
				
				HttpPost post = new HttpPost(TwitterTokenURL);
				post.setHeader("Authorization", "Basic " + base64Encoded);
				post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				post.setEntity(new StringEntity("grant_type=client_credentials"));
				String raw = getResponseBody(post);
				Authenticated auth = jsonToAuthenticated(raw);
				
				if (auth != null && auth.token_type.equals("bearer")) {
					HttpGet get = new HttpGet(TwitterStreamURL + screenName);
					get.setHeader("Authorization", "Bearer " + auth.access_token);
					get.setHeader("Content-Type", "application/json");
					results = getResponseBody(get);
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			return results;
		}
		
	}
}
