package rs.ac.uns.ftn.pma.event_organizer.services;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import rs.ac.uns.ftn.pma.event_organizer.R;

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = FCMService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String dataTitle = null;
        String dataMessage = null;
        String notificationTitle = null;
        String notificationBody = null;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload" + remoteMessage.getData().get("message"));
            dataTitle = remoteMessage.getData().get("title");
            dataMessage = remoteMessage.getData().get("message");
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification body" + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }

        sendNotification(notificationTitle, notificationBody, dataTitle, dataMessage);
    }

    private void sendNotification(String notificationTitle, String notificationBody,
                                  String dataTitle, String dataMessage) {
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
