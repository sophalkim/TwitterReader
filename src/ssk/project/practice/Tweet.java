package ssk.project.practice;

import ssk.project.twitterreader.TwitterUser;

import com.google.gson.annotations.SerializedName;

public class Tweet {

	@SerializedName("created_at")
	private String DateCreated;
	
	@SerializedName("id")
	private String Id;
	
	@SerializedName("text")
	private String Text;
	
	@SerializedName("in_reply_to_status_id")
	private String InReplyToStatusId;
	
	@SerializedName("in_reply_to_user_id")
	private String InReplyToUserId;
	
	@SerializedName("in_reply_to_screen_name")
	private String InReplyToScreenName;
	
	@SerializedName("user")
	private TwitterUser User;

	public String getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public String getInReplyToStatusId() {
		return InReplyToStatusId;
	}

	public void setInReplyToStatusId(String inReplyToStatusId) {
		InReplyToStatusId = inReplyToStatusId;
	}

	public String getInReplyToUserId() {
		return InReplyToUserId;
	}

	public void setInReplyToUserId(String inReplyToUserId) {
		InReplyToUserId = inReplyToUserId;
	}

	public String getInReplyToScreenName() {
		return InReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		InReplyToScreenName = inReplyToScreenName;
	}

	public TwitterUser getUser() {
		return User;
	}

	public void setUser(TwitterUser user) {
		User = user;
	}
	
	
}
