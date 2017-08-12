package popularmoviesstage1.legalimpurity.com.popularmoviesstage2.Utils;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public final class PicassoWrapper {

    // The only downfall to this is that if the movie poster is updated, it will take a while to get updated.
    public static void UsePicassoWrapper (final Activity act, final String imageUrl, final ImageView imageView, final int placeholderResource)
    {
        Picasso.with(act)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(placeholderResource)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(act)
                                .load(imageUrl)
                                .placeholder(placeholderResource)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }
}
