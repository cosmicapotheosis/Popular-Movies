package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("vote_count")
    private int vote_count;
    @SerializedName("id")
    private int id;
    @SerializedName("video")
    private boolean video;
    @SerializedName("vote_average")
    private float vote_average;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("original_language")
    private String original_language;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("genre_ids")
    private int[] genre_ids;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;

    public Movie(int vote_count, int id, boolean video, float vote_average, String title, float popularity, String poster_path,
                 String original_language, String original_title, int[] genre_ids, String backdrop_path, boolean adult, String overview, String release_date) {
        this.vote_count = vote_count;
        this.id = id;
        this.video = video;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    // Parcelable stuff

    protected Movie(Parcel in) {
        vote_count = in.readInt();
        id = in.readInt();
        // https://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
        video = in.readByte() != 0;
        vote_average = in.readFloat();
        title = in.readString();
        popularity = in.readByte();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        genre_ids = in.createIntArray();
        backdrop_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(vote_count);
        parcel.writeInt(id);
        // https://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeFloat(vote_average);
        parcel.writeString(title);
        parcel.writeFloat(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeIntArray(genre_ids);
        parcel.writeString(backdrop_path);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(release_date);
    }
}