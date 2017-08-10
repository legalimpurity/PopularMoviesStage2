package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.net.URL;
import java.util.ArrayList;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.MoviesJsonUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;

public class FetchMoviesLoader extends AsyncTaskLoader {

    public static final String SORT_BY_PARAM = "SORT_BY_PARAM";

    private Context context;
    private Bundle args;

    public FetchMoviesLoader(Context context, Bundle args) {
        super(context);
        this.context = context;
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            return;
        }
        forceLoad();
    }

    @Override
    public ArrayList<MovieObject> loadInBackground() {
        String sortByParam = args.getString(SORT_BY_PARAM);

        if (sortByParam == null || TextUtils.isEmpty(sortByParam)) {
            return null;
        }

        URL sortByRequestUrl = NetworkUtils.buildSortByUrl(sortByParam);

        try {
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(sortByRequestUrl);
            ArrayList<MovieObject> output = MoviesJsonUtils.getMovieObjectsFromJson(context,jsonMoviesResponse);
            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
