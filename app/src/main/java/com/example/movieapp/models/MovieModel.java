
package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieModel implements Parcelable {
    //    Model class for our movies

    private float vote_average;
    private final String overview;
    private String title;
    private String poster_path;
    private String release_date;
    private int movie_id;
    private String original_language;

    //constructor


    public MovieModel(String title, String poster_path, String release_date, int movie_id, float vote_average, String overview, String original_language) {
        this.vote_average = vote_average;
        this.overview = overview;
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.original_language = original_language;
    }

    protected MovieModel(Parcel in) {
        vote_average = in.readFloat();
        overview = in.readString();
        title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        movie_id = in.readInt();
        original_language = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
    //getter
    public float getVote_average() {
        return vote_average;
    }
    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }



    public String getOriginal_language() {
        return original_language;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(vote_average);
        dest.writeString(overview);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(original_language);
        dest.writeInt(movie_id);
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "title='" + title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", release_date='" + release_date + '\'' +
                ", movie_id=" + movie_id +
                ", vote_average=" + vote_average +
                ", movie_overview='" + overview + '\'' +
                ", original_language='" + original_language + '\'' +
                '}';
    }
}
