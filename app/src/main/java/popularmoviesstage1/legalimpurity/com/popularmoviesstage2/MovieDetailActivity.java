package popularmoviesstage1.legalimpurity.com.popularmoviesstage2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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


    @BindView(R.id.movie_poster) ImageView movie_poster;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) ViewPager container;
    @BindView(R.id.tabs) TabLayout tabs;

    private MovieObject mo;
    private MovieDetailPagerAdapter movieDetailPagerAdapter;

    private Menu menu;

    private boolean bookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent() != null && getIntent().getExtras() != null) {
            mo = (MovieObject) getIntent().getExtras().getParcelable(MOVIE_OBJECT_KEY);
        }
        setView(this);
        loadReviewsAndTrailerData(this);
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        switch (id)
        {
            case TRAILERS_DATA_LOADER : return new TrailersLoader(this, args);
            case REVIEWS_DATA_LOADER : return new ReviewsLoader(this, args);
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
            showMovies();
        }
    }

    private void loadReviewsAndTrailerData(FragmentActivity act) {

        LoaderManager loaderManager = act.getSupportLoaderManager();

        Bundle reviewsBundle = new Bundle();
        reviewsBundle.putString(ReviewsLoader.MOVIE_API_ID, mo.getApiId()+"");

        Loader<String> reviewsLoader = loaderManager.getLoader(REVIEWS_DATA_LOADER);
        if (reviewsLoader == null) {
            loaderManager.initLoader(REVIEWS_DATA_LOADER, reviewsBundle, this);
        } else {
            loaderManager.restartLoader(REVIEWS_DATA_LOADER, reviewsBundle, this);
        }

        Bundle trailerBundle = new Bundle();
        trailerBundle.putString(TrailersLoader.MOVIE_API_ID, mo.getApiId()+"");

        Loader<String> trailersLoader = loaderManager.getLoader(TRAILERS_DATA_LOADER);
        if (trailersLoader == null) {
            loaderManager.initLoader(TRAILERS_DATA_LOADER, trailerBundle, this);
        } else {
            loaderManager.restartLoader(TRAILERS_DATA_LOADER, trailerBundle, this);
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
            act.getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_API_ID + " = ? ",new String[]{String.valueOf(mo.getApiId())});
        else
            act.getContentResolver().insert(
                MoviesContract.MoviesEntry.CONTENT_URI,
                mo.getContentValues());

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
