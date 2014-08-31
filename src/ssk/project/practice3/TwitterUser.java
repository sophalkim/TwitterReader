package ssk.project.practice3;

import com.google.gson.annotations.SerializedName;

public class TwitterUser {

	@SerializedName("screen_name")
	private String screenName;
	
	@SerializedName("profile_image_url")
	private String profileImageUrl;

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}
