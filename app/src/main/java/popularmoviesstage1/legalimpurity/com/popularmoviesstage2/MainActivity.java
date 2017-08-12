package popularmoviesstage1.legalimpurity.com.popularmoviesstage2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkStateReceiver;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.NetworkUtils;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.adapters.MovieListAdapter;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.contentprovider.MoviesContract;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.listeners.MovieClickListener;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks.FetchMoviesLoader;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks {

    @BindView(R.id.movie_list_recycler_view) RecyclerView movie_list_recycler_view;
    @BindView(R.id.no_internet_text_view) TextView no_internet_text_view;

    private MovieListAdapter mAdapter;
    private ArrayList<MovieObject> movies_list;

    private String selectedApi = "popular";

    private static final int MOVIES_DATA_LOADER = 22;
    private static final int OFFLINE_BOOKMARKS_DATA_LOADER = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        findViews(this);
        setAdapter(this);
        selectedApi = "popular";
        if(NetworkUtils.isNetworkAvailable(this)) {
            // Will be called from networkAvailable Function
            //  loadMoviesData(this, selectedApi);
        }
        else
        {
            movie_list_recycler_view.setVisibility(View.GONE);
            no_internet_text_view.setVisibility(View.VISIBLE);
        }
        addNetworkStateReceiver(this);
    }

    private void addNetworkStateReceiver(final Activity act)
    {
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(new NetworkStateReceiver.NetworkStateReceiverListener() {
            @Override
            public void networkAvailable() {
                movie_list_recycler_view.setVisibility(View.VISIBLE);
                no_internet_text_view.setText(R.string.no_internet);
                no_internet_text_view.setVisibility(View.GONE);
                loadMoviesData(act,selectedApi);
            }

            @Override
            public void networkUnavailable() {
            }
        });
        act.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void findViews(Activity act)
    {
        movie_list_recycler_view = (RecyclerView) act.findViewById(R.id.movie_list_recycler_view);
        no_internet_text_view = (TextView) act.findViewById(R.id.no_internet_text_view);
    }

    private void setAdapter(final Activity act)
    {
        int numberOfColumns = 2;
        if(act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            numberOfColumns = 2;
        }
        else{
            numberOfColumns = 4;
        }
        RecyclerView.LayoutManager moviesLayoutManager = new GridLayoutManager(act,numberOfColumns);
        movie_list_recycler_view.setLayoutManager(moviesLayoutManager);

        movie_list_recycler_view.setHasFixedSize(true);

        movies_list = new ArrayList<MovieObject>();
        mAdapter = new MovieListAdapter(act,movies_list, new MovieClickListener() {
            @Override
            public void onMovieCLick(MovieObject movieItem) {
                Intent movieDetailActivityClickIntent = new Intent(act,MovieDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(MovieDetailActivity.MOVIE_OBJECT_KEY,movieItem);
                movieDetailActivityClickIntent.putExtras(extras);
                act.startActivity(movieDetailActivityClickIntent);
            }
        });

        movie_list_recycler_view.setAdapter(mAdapter);

    }

    private void loadMoviesData(Activity act, String sort_by) {
        LoaderManager loaderManager = getSupportLoaderManager();
        Bundle queryBundle = new Bundle();

        if(sort_by.equalsIgnoreCase("bookmarks"))
        {
            Loader moviesLoader = loaderManager.getLoader(OFFLINE_BOOKMARKS_DATA_LOADER);
            if (moviesLoader == null) {
                loaderManager.initLoader(OFFLINE_BOOKMARKS_DATA_LOADER, queryBundle, this);
            } else {
                loaderManager.restartLoader(OFFLINE_BOOKMARKS_DATA_LOADER, queryBundle, this);
            }
        }
        else {
            queryBundle.putString(FetchMoviesLoader.SORT_BY_PARAM, sort_by);

            Loader moviesLoader = loaderManager.getLoader(MOVIES_DATA_LOADER);
            if (moviesLoader == null) {
                loaderManager.initLoader(MOVIES_DATA_LOADER, queryBundle, this);
            } else {
                loaderManager.restartLoader(MOVIES_DATA_LOADER, queryBundle, this);
            }
        }
    }

    private void showErrorMessage()
    {
        no_internet_text_view.setText(R.string.api_error);
        no_internet_text_view.setVisibility(View.VISIBLE);
        movie_list_recycler_view.setVisibility(View.GONE);
    }

    private void showMovies()
    {
        no_internet_text_view.setVisibility(View.GONE);
        movie_list_recycler_view.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
        switch(id)
        {
            case MOVIES_DATA_LOADER:
                return new FetchMoviesLoader(this, args);
            case OFFLINE_BOOKMARKS_DATA_LOADER:
                return new CursorLoader(this,
                MoviesContract.MoviesEntry.CONTENT_URI,
                MoviesContract.MOVIES_PROJECTION,
                null,
                null,
                null);
            default:return new FetchMoviesLoader(this, args);
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
        } else {
            ArrayList<MovieObject> realdata;

            if(loader.getId() == MOVIES_DATA_LOADER)
            {
                realdata = (ArrayList<MovieObject>) data;
            }
            else
            {
                Cursor mCursor = (Cursor) data;
                realdata = new ArrayList<MovieObject>();
                for(int i = 0; i < mCursor.getCount(); i++)
                {
                    mCursor.moveToPosition(i);
                    realdata.add(new MovieObject(mCursor));
                }
            }

            mAdapter.setMoviesData(realdata);
            showMovies();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.change_sort_order) {
            CharSequence colors[] = new CharSequence[] {getString(R.string.most_popular), getString(R.string.highest_rated), getString(R.string.bookmarks)};

            final Activity act = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pick_an_order);
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which)
                    {
                        case 0: selectedApi = "popular";
                            loadMoviesData(act,selectedApi);
                            break;
                        case 1: selectedApi = "top_rated";
                            loadMoviesData(act,selectedApi);
                            break;
                        case 2: selectedApi = "bookmarks";
                            loadMoviesData(act,selectedApi);
                            break;
                        default:selectedApi = "bookmarks";
                            loadMoviesData(act,selectedApi);
                    }
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}