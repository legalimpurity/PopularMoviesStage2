package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.tasks.callbacks;

import java.util.ArrayList;

import popularmoviesstage1.legalimpurity.com.popularmoviesstage2.objects.MovieObject;

/**
 * Created by rajatkhanna on 03/08/17.
 */

public interface MovieApiRespondedBack {
    public void onApiResponded(ArrayList<MovieObject> response);
}
