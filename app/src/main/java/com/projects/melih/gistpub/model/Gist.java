package com.projects.melih.gistpub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by melihmg on 21/02/2017
 */

public class Gist implements Parcelable {

    @SerializedName("url")
    private String url;
    @SerializedName("forks_url")
    private String forksUrl;
    @SerializedName("commits_url")
    private String commitsUrl;
    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("public")
    private boolean isPublic;
    @SerializedName("owner")
    private Contributor owner;
    @SerializedName("truncated")
    private boolean isTruncated;
    @SerializedName("comments")
    private int comments;
    @SerializedName("comments_url")
    private String commentsUrl;
    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("git_pull_url")
    private String gitPullUrl;
    @SerializedName("git_push_url")
    private String gitPushUrl;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("files")
    private Object filesObject;

    private ArrayList<File> rawData;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getForksUrl() {
        return forksUrl;
    }

    public void setForksUrl(String forksUrl) {
        this.forksUrl = forksUrl;
    }

    public String getCommitsUrl() {
        return commitsUrl;
    }

    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Contributor getOwner() {
        return owner;
    }

    public void setOwner(Contributor owner) {
        this.owner = owner;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getGitPullUrl() {
        return gitPullUrl;
    }

    public void setGitPullUrl(String gitPullUrl) {
        this.gitPullUrl = gitPullUrl;
    }

    public String getGitPushUrl() {
        return gitPushUrl;
    }

    public void setGitPushUrl(String gitPushUrl) {
        this.gitPushUrl = gitPushUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getFilesObject() {
        return filesObject;
    }

    public ArrayList<File> getRawData() {
        return rawData;
    }

    public void setRawData(ArrayList<File> rawData) {
        this.rawData = rawData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.forksUrl);
        dest.writeString(this.commitsUrl);
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.owner, flags);
        dest.writeByte(this.isTruncated ? (byte) 1 : (byte) 0);
        dest.writeInt(this.comments);
        dest.writeString(this.commentsUrl);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.gitPullUrl);
        dest.writeString(this.gitPushUrl);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeList(this.rawData);
    }

    public Gist() {
    }

    protected Gist(Parcel in) {
        this.url = in.readString();
        this.forksUrl = in.readString();
        this.commitsUrl = in.readString();
        this.id = in.readString();
        this.description = in.readString();
        this.isPublic = in.readByte() != 0;
        this.owner = in.readParcelable(Contributor.class.getClassLoader());
        this.isTruncated = in.readByte() != 0;
        this.comments = in.readInt();
        this.commentsUrl = in.readString();
        this.htmlUrl = in.readString();
        this.gitPullUrl = in.readString();
        this.gitPushUrl = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.rawData = in.readArrayList(File.class.getClassLoader());
    }

    public static final Creator<Gist> CREATOR = new Creator<Gist>() {
        @Override
        public Gist createFromParcel(Parcel source) {
            return new Gist(source);
        }

        @Override
        public Gist[] newArray(int size) {
            return new Gist[size];
        }
    };
}
