package alserdar.widewall.fcm_service_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartFirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context , FirebaseBackgroundServiceForMessages.class));
        context.startService(new Intent(context , FirebaseBackgroundServiceForNotifications.class));

    }
}
