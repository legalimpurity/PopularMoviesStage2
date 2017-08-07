package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rajatkhanna on 01/08/17.
 */

public class MovieObject implements Parcelable {

    // Same as is from api, as they will always be Unique
    private long _id;

    private ArrayList <ReviewObject> ReviewObjs;
    private ArrayList <TrailerVideoObject> TrailerVideoObjs;


    private String OrignalTitle;
    private String MoviePosterImageThumbnailUrl;
    private String PlotSynopsis;
    private String UserRating;
    private String ReleaseDate;

    public MovieObject(String orignalTitle, String moviePosterImageThumbnailUrl, String plotSynopsis, String userRating, String releaseDate) {
        OrignalTitle = orignalTitle;
        MoviePosterImageThumbnailUrl = moviePosterImageThumbnailUrl;
        PlotSynopsis = plotSynopsis;
        UserRating = userRating;
        ReleaseDate = releaseDate;
    }

    // New constructor, will remove the old one soon
    public MovieObject(long _id, String orignalTitle, String moviePosterImageThumbnailUrl, String plotSynopsis, String userRating, String releaseDate) {
        this._id = _id;
        OrignalTitle = orignalTitle;
        MoviePosterImageThumbnailUrl = moviePosterImageThumbnailUrl;
        PlotSynopsis = plotSynopsis;
        UserRating = userRating;
        ReleaseDate = releaseDate;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public ArrayList<ReviewObject> getReviewObjs() {
        return ReviewObjs;
    }

    public void setReviewObjs(ArrayList<ReviewObject> reviewObjs) {
        ReviewObjs = reviewObjs;
    }

    public ArrayList<TrailerVideoObject> getTrailerVIdeoObjs() {
        return TrailerVideoObjs;
    }

    public void setTrailerVIdeoObjs(ArrayList<TrailerVideoObject> TrailerVideoObjs) {
        TrailerVideoObjs = TrailerVideoObjs;
    }

    public String getOrignalTitle() {
        return OrignalTitle;
    }

    public void setOrignalTitle(String orignalTitle) {
        OrignalTitle = orignalTitle;
    }

    public String getMoviePosterImageThumbnailUrl() {
        return MoviePosterImageThumbnailUrl;
    }

    public void setMoviePosterImageThumbnailUrl(String moviePosterImageThumbnailUrl) {
        MoviePosterImageThumbnailUrl = moviePosterImageThumbnailUrl;
    }

    public String getPlotSynopsis() {
        return PlotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        PlotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return UserRating;
    }

    public void setUserRating(String userRating) {
        UserRating = userRating;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.OrignalTitle,
                this.MoviePosterImageThumbnailUrl,
                this.PlotSynopsis,
                this.UserRating,
                this.ReleaseDate
        });
//        How to write to arraylist of custom objects in Parcelable ?
//        dest.readTypedList(ReviewObjs);
//        dest.readTypedList(TrailerVideoObjs);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };

    public MovieObject(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.OrignalTitle = data[0];
        this.MoviePosterImageThumbnailUrl = data[1];
        this.PlotSynopsis = data[2];
        this.UserRating = data[3];
        this.ReleaseDate = data[4];
    }

}