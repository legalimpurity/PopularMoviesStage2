package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.moviedetailfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.R;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters.ReviewsListAdapter;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.ReviewClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.ReviewObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks.ReviewsLoader;

/**
 * Created by rajatkhanna on 10/08/17.
 */

public class ReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    private static final int REVIEWS_DATA_LOADER = 23;

    private static final String ARG_MOVIE_OBJECT = "ARG_MOVIE_OBJECT";

    private MovieObject mo;
    private ReviewsListAdapter reviewsListAdapter;

    @BindView(R.id.review_list_recycler_view) RecyclerView review_list_recycler_view;

    public ReviewsFragment() {
    }

    public static ReviewsFragment newInstance(MovieObject mo) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE_OBJECT, mo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_reviews, container, false);
        ButterKnife.bind(this,rootView);
        mo = getArguments().getParcelable(ARG_MOVIE_OBJECT);
        ReviewClickListener rcl = new ReviewClickListener() {
            @Override
            public void onMovieCLick(ReviewObject bookName) {

            }
        };
        reviewsListAdapter = new ReviewsListAdapter(getActivity(),rcl);

        review_list_recycler_view.setAdapter(reviewsListAdapter);
        review_list_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadReviewsData(getActivity());
        return rootView;
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new ReviewsLoader(getContext(), args) ;
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // No need to implement
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (null == data) {
            showErrorMessage();
        } else {
            ArrayList<ReviewObject> realdata = (ArrayList<ReviewObject>) data;
            reviewsListAdapter.setReviewssData(realdata);
            showMovies();
        }
    }

    private void loadReviewsData(FragmentActivity act) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(ReviewsLoader.MOVIE_API_ID, mo.getApiId()+"");

        LoaderManager loaderManager = act.getSupportLoaderManager();
        Loader<String> reviewsLoader = loaderManager.getLoader(REVIEWS_DATA_LOADER);
        if (reviewsLoader == null) {
            loaderManager.initLoader(REVIEWS_DATA_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(REVIEWS_DATA_LOADER, queryBundle, this);
        }
    }
    private void showErrorMessage()
    {
    }

    private void showMovies()
    {
    }

}
