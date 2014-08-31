package ssk.project.practice2;

import ssk.project.practice.TwitterUser;

import com.google.gson.annotations.SerializedName;

public class Tweet {

	@SerializedName("created_at")
	private String DateCreated;
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("text")
	private String text;
	
	@SerializedName("in_reply_to_status_id")
	private String InReplyToStatusId;
	
	@SerializedName("in_reply_to_user_id")
	private String InReplyToUserId;
	
	@SerializedName("in_reply_to_screen_name")
	private String InReplyToScreenName;
	
	@SerializedName("user")
	private TwitterUser user;

	public String getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
		return user;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}
}
