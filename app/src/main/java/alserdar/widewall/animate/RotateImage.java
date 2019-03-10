package alserdar.widewall.animate;

import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

public class RotateImage {

    public static void startRotateMyButton(final Button flip)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                flip.animate().rotationBy(360).withEndAction(this).setDuration(3000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };

        flip.animate().rotationBy(360).withEndAction(runnable).setDuration(3000).setInterpolator(new LinearInterpolator()).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               stopRotateMyButton(flip);
            }
        }, 3000);
    }

    public static void stopRotateMyButton(final Button flip)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                flip.animate().rotationBy(360).withEndAction(this).setDuration(3000)
                        .setInterpolator(new LinearInterpolator()).cancel();
            }
        };

        flip.animate().rotationBy(360).withEndAction(runnable).setDuration(3000).setInterpolator(new LinearInterpolator()).cancel();

    }
}
