package ifn372.sevencolors.dementiawatch;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.os.Handler;
import android.widget.Toast;
import ifn372.sevencolors.backend.carerApi.model.Carer;
import ifn372.sevencolors.dementiawatch.webservices.CheckService;

/**
 * Created by zach on 29/08/15.
 */
public class OutOfBoundCheck extends IntentService {
    public OutOfBoundCheck() {
        super("OutOfBoundCheck");
    }

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String carer_id = "3";

        try {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(OutOFBoundCheck.this, "Checking running", Toast.LENGTH_SHORT).show();
                    CheckService checkService = new CheckService();
                    Context context = getApplicationContext();
                    try {
                        Carer carer = checkService.execute(carer_id).get();
                        if (carer.getPatientSafety()) {
                            //Toast.makeText(OutOFBoundCheck.this, "Done First", Toast.LENGTH_SHORT).show();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.cancel(400);
                        } else {
                            Toast.makeText(OutOfBoundCheck.this, "Caution! Your patient is out of bound!" , Toast.LENGTH_SHORT).show();
                            PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
                            Resources r = getResources();
                            Notification notification = new android.support.v7.app.NotificationCompat.Builder(context)
                                    .setTicker(r.getString(R.string.notification_title))
                                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                                    .setContentTitle(r.getString(R.string.notification_title))
                                    .setContentText(r.getString(R.string.notification_text))
                                    .setContentIntent(pi)
                                    .setAutoCancel(false)
                                    .setVisibility(1)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), 10)
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(400, notification);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OutOfBoundCheck.this, "Oh! Something seems to have broken!", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }
}
