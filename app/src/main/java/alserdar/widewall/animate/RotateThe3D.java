package alserdar.widewall.animate;

import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class RotateThe3D {

    public static void startRotation(float start, float end , final ImageView image) {
        // Calculating center point
        final float centerX = image.getWidth() / 2.0f;
        final float centerY = image.getHeight() / 2.0f;
        //Log.d(TAG, "centerX="+centerX+", centerY="+centerY);
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        //Z axis is scaled to 0
        AnimateThe3D rotation =new AnimateThe3D(start, end, centerX, centerY, 0f, true);
        rotation.setDuration(2000);
        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //Uniform rotation
        rotation.setInterpolator(new LinearInterpolator());
        image.startAnimation(rotation);
    }
}
