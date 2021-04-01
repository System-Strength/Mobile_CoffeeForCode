package co.ex.coffeeforcodeapp.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.ex.coffeeforcodeapp.R;
import co.ex.coffeeforcodeapp.Activitys.ShoppingCartActivity;

public class CDCMessasingService extends FirebaseMessagingService {

    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        mostrarNotificacao(notification);
    }

    public void mostrarNotificacao(RemoteMessage.Notification notification) {
        NotificationCompat.Builder buildernotify = new NotificationCompat.Builder(this);
        String titulo = notification.getTitle();
        String mensagem = notification.getBody();

        Notification notificacao = buildernotify.setContentTitle(titulo) .setContentText(mensagem)
        .setSmallIcon(R.drawable.frappuccino_category)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacao);

        Intent intent = new Intent(this, ShoppingCartActivity.class);

        PendingIntent pendingIntent = PendingIntent .getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
    }

}
