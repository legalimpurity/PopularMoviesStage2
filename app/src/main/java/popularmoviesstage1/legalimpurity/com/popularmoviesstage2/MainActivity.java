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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils.MyPreferences;
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
    private ActionBar ab;

    private MovieListAdapter mAdapter;
    private ArrayList<MovieObject> movies_list;

    final String optionValues[] = new String[] {"popular", "top_rated", "bookmarks"};
    private String options[];

    private int selectedApi_code = 0;
    private boolean wasDataLoadedPerfectlyForSelectedApiCode = false;

    private static final int MOVIES_DATA_LOADER = 22;
    private static final int OFFLINE_BOOKMARKS_DATA_LOADER = 23;

    private static final String SAVED_INSTANCE_DATA_LOADED_KEY = "SAVED_INSTANCE_DATA_LOADED_KEY";
    private static final String SAVED_INSTANCE_MOVIE_LIST = "SAVED_INSTANCE_MOVIE_LIST";
    private static final String SAVED_INSTANCE_API_CODE = "SAVED_INSTANCE_API_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        options = new String[] {getString(R.string.most_popular), getString(R.string.highest_rated), getString(R.string.bookmarks)};

        findViews(this);
        setAdapter(this);

        checkForSavedInstanceState(savedInstanceState);
        addNetworkStateReceiver(this);
    }

    private void checkForSavedInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_INSTANCE_DATA_LOADED_KEY)) {
                boolean wasDataLoadedPerfectlyForSelectedApiCode = savedInstanceState.getBoolean(SAVED_INSTANCE_DATA_LOADED_KEY);
                //Check was their internet in last instance state ?
                if(wasDataLoadedPerfectlyForSelectedApiCode)
                {
                    if (savedInstanceState.containsKey(SAVED_INSTANCE_API_CODE) && savedInstanceState.containsKey(SAVED_INSTANCE_MOVIE_LIST))
                    {
                        restoreDatafromSavedInstance(savedInstanceState);
                    }
                }
                // if no internet was the app on bookmarks screen?
                else if(savedInstanceState.containsKey(SAVED_INSTANCE_API_CODE))
                {
                    if(savedInstanceState.getInt(SAVED_INSTANCE_API_CODE) == 2)
                        restoreDatafromSavedInstance(savedInstanceState);
                }
                else
                    processFlow();
            }
        }
        else
            processFlow();
    }

    private void restoreDatafromSavedInstance(Bundle savedInstanceState)
    {
        selectedApi_code = savedInstanceState.getInt(SAVED_INSTANCE_API_CODE);
        movies_list = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIE_LIST);
        updateTitleBar();
        processLoader();
    }

    // Reaching here means app was created freshly or was killed by the system in background or there was no internet last time the app was killed.
    private void processFlow()
    {
        selectedApi_code = MyPreferences.getInt(this,MyPreferences.PROPERTY_SORTING_ORDER);

        if(selectedApi_code == -1)
            selectedApi_code = 0;

        updateTitleBar();

        if(NetworkUtils.isNetworkAvailable(this)) {
            // Will be called from networkAvailable Function
            //  loadMoviesData(this, selectedApi);
        }
        else
        {
            // Movies gotta load without internet also, if app was on bookmarks before termination
            if(selectedApi_code == 2)
                loadMoviesData(this,2);
            else {
                no_internet_text_view.setText(R.string.no_internet);
                wasDataLoadedPerfectlyForSelectedApiCode = false;
                showErrorMessage();
            }
        }
    }

    private void updateTitleBar()
    {
        ab.setTitle(options[selectedApi_code]);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_INSTANCE_DATA_LOADED_KEY, wasDataLoadedPerfectlyForSelectedApiCode);
        outState.putParcelableArrayList(SAVED_INSTANCE_MOVIE_LIST, movies_list);
        outState.putInt(SAVED_INSTANCE_API_CODE, selectedApi_code);
    }


    private void addNetworkStateReceiver(final Activity act)
    {
        final NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(new NetworkStateReceiver.NetworkStateReceiverListener() {
            @Override
            public void networkAvailable() {
                showMovies();
                // Just get the data when network is available or when the app was started. wasDataLoadedPerfectlyForSelectedApiCode should be false so loadMoviesData should load data.
                loadMoviesData(act,selectedApi_code);
                networkStateReceiver.removeListener(this);
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
        ab = getSupportActionBar();
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

    private void loadMoviesData(Activity act, int newSelectedApi_code) {
        if(newSelectedApi_code != selectedApi_code || wasDataLoadedPerfectlyForSelectedApiCode == false) {
            selectedApi_code = newSelectedApi_code;
            MyPreferences.setInt(act,MyPreferences.PROPERTY_SORTING_ORDER,selectedApi_code);
            LoaderManager loaderManager = getSupportLoaderManager();
            Bundle queryBundle = new Bundle();

            if (selectedApi_code == 2) {
                Loader moviesLoader = loaderManager.getLoader(OFFLINE_BOOKMARKS_DATA_LOADER);
                if (moviesLoader == null) {
                    loaderManager.initLoader(OFFLINE_BOOKMARKS_DATA_LOADER, queryBundle, this);
                } else {
                    loaderManager.restartLoader(OFFLINE_BOOKMARKS_DATA_LOADER, queryBundle, this);
                }
            } else {
                if (NetworkUtils.isNetworkAvailable(this)) {
                    queryBundle.putString(FetchMoviesLoader.SORT_BY_PARAM, optionValues[selectedApi_code]);

                    Loader moviesLoader = loaderManager.getLoader(MOVIES_DATA_LOADER);
                    if (moviesLoader == null) {
                        loaderManager.initLoader(MOVIES_DATA_LOADER, queryBundle, this);
                    } else {
                        loaderManager.restartLoader(MOVIES_DATA_LOADER, queryBundle, this);
                    }
                } else {
                    wasDataLoadedPerfectlyForSelectedApiCode = false;
                    no_internet_text_view.setText(R.string.no_internet);
                    showErrorMessage();
                }
            }
        }
    }

    private void showErrorMessage()
    {
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
            no_internet_text_view.setText(R.string.api_error);
            wasDataLoadedPerfectlyForSelectedApiCode = false;
            showErrorMessage();
        } else {
            ArrayList<MovieObject> realdata;

            if(loader.getId() == MOVIES_DATA_LOADER && (selectedApi_code == 0 || selectedApi_code == 1))
            {
                realdata = (ArrayList<MovieObject>) data;
                no_internet_text_view.setText(R.string.change_order_zero);
                movies_list = realdata;
                processLoader();
            }
            else if (loader.getId() == OFFLINE_BOOKMARKS_DATA_LOADER && (selectedApi_code == 2))
            {
                Cursor mCursor = (Cursor) data;
                realdata = new ArrayList<MovieObject>();
                for(int i = 0; i < mCursor.getCount(); i++)
                {
                    mCursor.moveToPosition(i);
                    realdata.add(new MovieObject(mCursor));

                }
                no_internet_text_view.setText(R.string.bookmarks_zero);
                movies_list = realdata;
                processLoader();
            }
        }
    }


    private void processLoader()
    {
        mAdapter.setMoviesData(movies_list);
        wasDataLoadedPerfectlyForSelectedApiCode = true;
        if(movies_list != null && movies_list.size() == 0)
            showErrorMessage();
        else
            showMovies();
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


            final Activity act = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pick_an_order);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.setTitle(options[selectedApi_code]);
                    loadMoviesData(act,which);
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}