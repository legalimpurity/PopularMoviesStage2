package popularmoviesstage1.legalimpurity.com.popularmoviesstage2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.PicassoWrapper;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters.MovieDetailPagerAdapter;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider.MoviesContract;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.ReviewObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.TrailerVideoObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks.ReviewsLoader;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks.TrailersLoader;

public class MovieDetailActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks
{

    public static final String MOVIE_OBJECT_KEY = "0a46c76c98b80b4ed6befbe3760b28b1";

    private static final int TRAILERS_DATA_LOADER = 24;
    private static final int REVIEWS_DATA_LOADER = 23;

    private static final int OFFLINE_TRAILERS_DATA_LOADER = 25;
    private static final int OFFLINE_REVIEWS_DATA_LOADER = 26;

    private static final String SAVED_INSTANCE_DATA_LOADED_KEY = "SAVED_INSTANCE_DATA_LOADED_KEY";
    private static final String SAVED_INSTANCE_MOVIE_OBJECT = "SAVED_INSTANCE_MOVIE_OBJECT";


    @BindView(R.id.movie_poster) ImageView movie_poster;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) ViewPager container;
    @BindView(R.id.tabs) TabLayout tabs;

    private MovieObject mo;
    private int REVIEWS_LOADER;
    private int TRAILER_LOADER;
    private boolean bookmarked;
    // If it reaches 2 then, both api's responded
    private int wasDataLoadedPerfectlyForReviewsAndTrailers = 0;

    private MovieDetailPagerAdapter movieDetailPagerAdapter;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkForSavedInstanceState(this,savedInstanceState);
        setView(this);
    }

    private void checkForSavedInstanceState(AppCompatActivity act, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_INSTANCE_MOVIE_OBJECT))
            {
                mo = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE_OBJECT);

                if (savedInstanceState.containsKey(SAVED_INSTANCE_DATA_LOADED_KEY)) {
                    if (savedInstanceState.getInt(SAVED_INSTANCE_DATA_LOADED_KEY) != 2) {
                        loadReviewsAndTrailerData(act);
                    }
                    else
                    {
                        wasDataLoadedPerfectlyForReviewsAndTrailers = 2;
                    }
                }
                else
                    loadReviewsAndTrailerData(act);
            }
            else
                processFlow(act);
        }
        else
        {
            processFlow(act);
            loadReviewsAndTrailerData(act);
        }
    }

    private void processFlow(AppCompatActivity act)
    {
        if(getIntent() != null && getIntent().getExtras() != null) {
            mo = (MovieObject) getIntent().getExtras().getParcelable(MOVIE_OBJECT_KEY);
        }
        else
            NavUtils.navigateUpFromSameTask(act);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_DATA_LOADED_KEY, wasDataLoadedPerfectlyForReviewsAndTrailers);
        outState.putParcelable(SAVED_INSTANCE_MOVIE_OBJECT,mo);
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
//        MoviesContract.MoviesEntry.COLUMN_API_ID + " = ? "
//        new String[]{String.valueOf(mo.getApiId())}
        switch (id)
        {
            case TRAILERS_DATA_LOADER : return new TrailersLoader(this, args);
            case REVIEWS_DATA_LOADER : return new ReviewsLoader(this, args);
            case OFFLINE_TRAILERS_DATA_LOADER: return new CursorLoader(this,
                    MoviesContract.TrailerVideosEntry.CONTENT_URI,
                    MoviesContract.TRAILER_PROJECTION,
                    MoviesContract.TrailerVideosEntry.COLUMN_FOREIGN_ID + "=?",
                    new String[]{String.valueOf(mo.getApiId())},
                    null);
            case OFFLINE_REVIEWS_DATA_LOADER: return new CursorLoader(this,
                    MoviesContract.ReviewEntry.CONTENT_URI,
                    MoviesContract.REVIEWS_PROJECTION,
                    MoviesContract.TrailerVideosEntry.COLUMN_FOREIGN_ID + "=?",
                    new String[]{String.valueOf(mo.getApiId())},
                    null);
            default : return null;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // No need to implement
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (null == data) {
            showErrorMessage();
        } else
        {
            if(loader.getId() == TRAILERS_DATA_LOADER) {
                ArrayList<TrailerVideoObject> trailersdata = (ArrayList<TrailerVideoObject>) data;
                mo.setTrailerVideoObjs(trailersdata);
            }
            else if(loader.getId() == REVIEWS_DATA_LOADER)
            {
                ArrayList<ReviewObject> reviewsdata = (ArrayList<ReviewObject>) data;
                mo.setReviewObjs(reviewsdata);
            }
            else if(loader.getId() == OFFLINE_REVIEWS_DATA_LOADER)
            {
                Cursor mCursor = (Cursor) data;
                ArrayList<ReviewObject> reviewsdata = new ArrayList<ReviewObject>();
                for(int i = 0; i < mCursor.getCount(); i++)
                {
                    mCursor.moveToPosition(i);
                    reviewsdata.add(new ReviewObject(mCursor));
                }
                mo.setReviewObjs(reviewsdata);
            }
            else if(loader.getId() == OFFLINE_TRAILERS_DATA_LOADER)
            {
                Cursor mCursor = (Cursor) data;
                ArrayList<TrailerVideoObject> trailersdata = new ArrayList<TrailerVideoObject>();
                for(int i = 0; i < mCursor.getCount(); i++)
                {
                    mCursor.moveToPosition(i);
                    trailersdata.add(new TrailerVideoObject(mCursor));
                }
                mo.setTrailerVideoObjs(trailersdata);
            }
            wasDataLoadedPerfectlyForReviewsAndTrailers++;
            showMovies();
        }
    }

    private void loadReviewsAndTrailerData(AppCompatActivity act) {

        LoaderManager loaderManager = act.getSupportLoaderManager();
        if(NetworkUtils.isNetworkAvailable(this)) {
            REVIEWS_LOADER = REVIEWS_DATA_LOADER;
            TRAILER_LOADER = TRAILERS_DATA_LOADER;
        }
        else
        {
            REVIEWS_LOADER = OFFLINE_REVIEWS_DATA_LOADER;
            TRAILER_LOADER = OFFLINE_TRAILERS_DATA_LOADER;
        }

        Bundle reviewsBundle = new Bundle();
        reviewsBundle.putLong(ReviewsLoader.MOVIE_API_ID, mo.getApiId());

        Loader<String> reviewsLoader = loaderManager.getLoader(REVIEWS_LOADER);
        if (reviewsLoader == null) {
            loaderManager.initLoader(REVIEWS_LOADER, reviewsBundle, this);
        } else {
            loaderManager.restartLoader(REVIEWS_LOADER, reviewsBundle, this);
        }

        Bundle trailerBundle = new Bundle();
        trailerBundle.putLong(TrailersLoader.MOVIE_API_ID, mo.getApiId());

        Loader<String> trailersLoader = loaderManager.getLoader(TRAILER_LOADER);
        if (trailersLoader == null) {
            loaderManager.initLoader(TRAILER_LOADER, trailerBundle, this);
        } else {
            loaderManager.restartLoader(TRAILER_LOADER, trailerBundle, this);
        }
    }

    private void showErrorMessage()
    {

    }

    private void showMovies()
    {
            movieDetailPagerAdapter.setUpdatedMovieObject(mo);
    }


    private void setView(Activity act)
    {
        PicassoWrapper.UsePicassoWrapper(act,
                NetworkUtils.MOVIES_IMAGE_URL_HIGHER_RESOLUTION+mo.getMoviePosterImageThumbnailUrl(),
                movie_poster,
                R.drawable.ic_local_movies_grey_24dp);
        toolbar_layout.setTitle(mo.getOrignalTitle());

        movieDetailPagerAdapter = new MovieDetailPagerAdapter(this,mo);

        container.setAdapter(movieDetailPagerAdapter);
        tabs.setupWithViewPager(container);
    }



    private void shareData(final Activity act)
    {

        String sharer_content = "";

        sharer_content = sharer_content + act.getResources().getString(R.string.movie_title) + " : \n";
        sharer_content = sharer_content + mo.getOrignalTitle() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.release_date) + " : \n";
        sharer_content = sharer_content + mo.getReleaseDate() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.vote_average) + " : \n";
        sharer_content = sharer_content + mo.getUserRating() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.plot_synopsis) + " : \n";
        sharer_content = sharer_content + mo.getPlotSynopsis() + "\n\n";

        sharer_content = sharer_content + act.getResources().getString(R.string.shared_from_themoviedb);

        final String finalSharer_content = sharer_content;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, act.getResources().getString(R.string.shared_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, finalSharer_content);
        act.startActivity(Intent.createChooser(intent, act.getResources().getString(R.string.app_name)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_detail_menu, menu);
        this.menu = menu;
        checkBookmark();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_share: shareData(this);
                return true;
            case R.id.action_bookmark:toggleBookmark(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleBookmark(final Activity act)
    {
        if(bookmarked)
        {
            act.getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_API_ID + " = ? ", new String[]{String.valueOf(mo.getApiId())});
            act.getContentResolver().delete(MoviesContract.ReviewEntry.CONTENT_URI, MoviesContract.ReviewEntry.COLUMN_FOREIGN_ID + " = ? ", new String[]{String.valueOf(mo.getApiId())});
            act.getContentResolver().delete(MoviesContract.TrailerVideosEntry.CONTENT_URI, MoviesContract.TrailerVideosEntry.COLUMN_FOREIGN_ID + " = ? ", new String[]{String.valueOf(mo.getApiId())});
        }
        else
        {
            act.getContentResolver().insert(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    mo.getContentValues());
            if(wasDataLoadedPerfectlyForReviewsAndTrailers != 2)
                Toast.makeText(act,R.string.partially_saved_error,Toast.LENGTH_LONG);
            else {
                act.getContentResolver().bulkInsert(
                        MoviesContract.ReviewEntry.CONTENT_URI,
                        mo.getReviewsContentValues());
                act.getContentResolver().bulkInsert(
                        MoviesContract.TrailerVideosEntry.CONTENT_URI,
                        mo.getTrailerContentValues());
            }
        }
        bookmarked = !bookmarked;
        updateMenuTitles();
    }


    private void updateMenuTitles() {
        MenuItem bookmarkMenuItem = menu.findItem(R.id.action_bookmark);
        if (bookmarked) {
            bookmarkMenuItem.setTitle(R.string.action_bookmark);
            bookmarkMenuItem.setIcon(R.drawable.ic_bookmark_white_24dp);
        } else {
            bookmarkMenuItem.setTitle(R.string.action_not_bookmark);
            bookmarkMenuItem.setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
    }

    private void checkBookmark()
    {
        bookmarked = mo.isBookmarked();
        updateMenuTitles();
    }

}
