package com.projects.melih.gistpub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by melo on 15/07/2017
 */

public class File implements Parcelable {
    @SerializedName("filename")
    private String filename;
    @SerializedName("type")
    private String type;
    @SerializedName("language")
    private String language;
    @SerializedName("raw_url")
    private String rawUrl;
    @SerializedName("size")
    private String size;

    public String getFilename() {
        return filename;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public String getSize() {
        return size;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filename);
        dest.writeString(this.type);
        dest.writeString(this.language);
        dest.writeString(this.rawUrl);
        dest.writeString(this.size);
    }

    public File() {
    }

    protected File(Parcel in) {
        this.filename = in.readString();
        this.type = in.readString();
        this.language = in.readString();
        this.rawUrl = in.readString();
        this.size = in.readString();
    }

    public static final Creator<File> CREATOR = new Creator<File>() {
        @Override
        public File createFromParcel(Parcel source) {
            return new File(source);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };
}
