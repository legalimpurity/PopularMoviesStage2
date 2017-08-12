package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rajatkhanna on 12/08/17.
 */

public final class MyPreferences {

    public static final String PREFERENCES_KEY = "PopularMoviesStage2+_prefs";

    public static final String PROPERTY_SORTING_ORDER = "03fd5e7c19ccad6b8aafa7f29587f67e";

    public static int getInt(Context context, String id) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        int registrationId = prefs.getInt(id, -1);
        return registrationId;
    }

    public static String getString(Context context, String id) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(id, "");
        return registrationId;
    }

    public static void setInt(Context context, String key, int val) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static void setString(Context context, String key, String val) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, val);
        editor.commit();
    }

}
