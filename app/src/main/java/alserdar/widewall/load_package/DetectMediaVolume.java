package alserdar.widewall.load_package;

import android.content.Context;
import android.media.AudioManager;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;

public class DetectMediaVolume {

    public static void detectMe(Context context)
    {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        assert audio != null;
        switch( audio.getRingerMode() ) {
            case AudioManager.RINGER_MODE_NORMAL:
                break;
            case AudioManager.RINGER_MODE_SILENT:
                new OurToast().myToastPic(context , "" , R.mipmap.mute);
                new OurToast().myToastPic(context , "" , R.mipmap.audio_speaker);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                break;
        }

    }
}
