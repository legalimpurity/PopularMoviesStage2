package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.ReviewObject;


public class JsonUtils {

    public static ArrayList<MovieObject> getMovieObjectsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String MOVIE_LIST = "results";

        final String APIID_ATTRIBUTE = "id";
        final String TITLE_ATTRIBUTE = "title";
        final String POSTER_URL = "poster_path";
        final String PLOT_SYNOPSIS = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";

        ArrayList<MovieObject> parsedMoviesData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray moviesArray = movieJson.getJSONArray(MOVIE_LIST);

        parsedMoviesData = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJSONObj = moviesArray.getJSONObject(i);
            parsedMoviesData.add(new MovieObject(movieJSONObj.getLong(APIID_ATTRIBUTE),
                    movieJSONObj.getString(TITLE_ATTRIBUTE),
                    movieJSONObj.getString(POSTER_URL),
                    movieJSONObj.getString(PLOT_SYNOPSIS),
                    movieJSONObj.getString(USER_RATING),
                    movieJSONObj.getString(RELEASE_DATE)
            ));
        }

        return parsedMoviesData;
    }

    public static ArrayList<ReviewObject> getReviewObjectsFromJson(Context context, String reviewsJsonStr) throws JSONException {

        final String REVIEWS_LIST = "results";

        final String AUTHOR_ATTRIBUTE = "author";
        final String CONTENT_ATTRIBUTE = "content";
        final String APIID_ATTRIBUTE = "id";

        ArrayList<ReviewObject> parsedMoviesData;

        JSONObject movieJson = new JSONObject(reviewsJsonStr);

        JSONArray moviesArray = movieJson.getJSONArray(REVIEWS_LIST);

        parsedMoviesData = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJSONObj = moviesArray.getJSONObject(i);
            parsedMoviesData.add(new ReviewObject(i,movieJSONObj.getString(APIID_ATTRIBUTE),
                    movieJSONObj.getString(AUTHOR_ATTRIBUTE),
                    movieJSONObj.getString(CONTENT_ATTRIBUTE)
            ));
        }

        return parsedMoviesData;
    }

}
