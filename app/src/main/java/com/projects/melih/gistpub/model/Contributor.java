package com.projects.melih.gistpub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by melihmg on 19/02/2017
 */
public class Contributor implements Parcelable {
    @SerializedName("id")
    private Long id;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("events_url")
    private String eventsUrl;

    @SerializedName("followers_url")
    private String followersUrl;

    @SerializedName("following_url")
    private String followingUrl;

    @SerializedName("gists_url")
    private String gistsUrl;

    @SerializedName("gravatar_id")
    private String gravatarId;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("login")
    private String login;

    @SerializedName("organizations_url")
    private String organizationsUrl;

    @SerializedName("received_events_url")
    private String receivedEventsUrl;

    @SerializedName("repos_url")
    private String reposUrl;

    @SerializedName("site_admin")
    private Boolean isSiteAdmin;

    @SerializedName("starred_url")
    private String starredUrl;

    @SerializedName("subscriptions_url")
    private String subscriptionsUrl;

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getGistsUrl() {
        return gistsUrl;
    }

    public void setGistsUrl(String gistsUrl) {
        this.gistsUrl = gistsUrl;
    }

    public String getGravatarId() {
        return gravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOrganizationsUrl() {
        return organizationsUrl;
    }

    public void setOrganizationsUrl(String organizationsUrl) {
        this.organizationsUrl = organizationsUrl;
    }

    public String getReceivedEventsUrl() {
        return receivedEventsUrl;
    }

    public void setReceivedEventsUrl(String receivedEventsUrl) {
        this.receivedEventsUrl = receivedEventsUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public Boolean getSiteAdmin() {
        return isSiteAdmin;
    }

    public void setSiteAdmin(Boolean siteAdmin) {
        isSiteAdmin = siteAdmin;
    }

    public String getStarredUrl() {
        return starredUrl;
    }

    public void setStarredUrl(String starredUrl) {
        this.starredUrl = starredUrl;
    }

    public String getSubscriptionsUrl() {
        return subscriptionsUrl;
    }

    public void setSubscriptionsUrl(String subscriptionsUrl) {
        this.subscriptionsUrl = subscriptionsUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.eventsUrl);
        dest.writeString(this.followersUrl);
        dest.writeString(this.followingUrl);
        dest.writeString(this.gistsUrl);
        dest.writeString(this.gravatarId);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.login);
        dest.writeString(this.organizationsUrl);
        dest.writeString(this.receivedEventsUrl);
        dest.writeString(this.reposUrl);
        dest.writeValue(this.isSiteAdmin);
        dest.writeString(this.starredUrl);
        dest.writeString(this.subscriptionsUrl);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    public Contributor() {
    }

    protected Contributor(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.avatarUrl = in.readString();
        this.eventsUrl = in.readString();
        this.followersUrl = in.readString();
        this.followingUrl = in.readString();
        this.gistsUrl = in.readString();
        this.gravatarId = in.readString();
        this.htmlUrl = in.readString();
        this.login = in.readString();
        this.organizationsUrl = in.readString();
        this.receivedEventsUrl = in.readString();
        this.reposUrl = in.readString();
        this.isSiteAdmin = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.starredUrl = in.readString();
        this.subscriptionsUrl = in.readString();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Contributor> CREATOR = new Creator<Contributor>() {
        @Override
        public Contributor createFromParcel(Parcel source) {
            return new Contributor(source);
        }

        @Override
        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };
}