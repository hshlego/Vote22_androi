package org.techtown.push.buttonnaviation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class ElectionFirebase extends FirebaseMessagingService {
    private static final String TAG = "FMS";
    private static final String[] candidate_names = {
            "", "이재명", "윤석열", "심상정", "안철수"
    };

    public ElectionFirebase() {}

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        String contents = data.get("contents");

        showNoti(contents, data);
    }

    private void showNoti(String contents, Map<String, String> data) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(
                    "channel2", "Channel2", NotificationManager.IMPORTANCE_DEFAULT
            ));
            builder = new NotificationCompat.Builder(this, "channel2");
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        Intent intent = null;
        if(contents.equals("SNS")) {
            intent = new Intent(this, CandidateActivity.class);
            String media = data.get("media");
            String candidate_num = data.get("candidate_num");
            intent.putExtra("media", media);
            intent.putExtra("candidate_num", candidate_num);
            intent.putExtra("page_num", 1);
            builder.setContentTitle(candidate_names[Integer.parseInt(candidate_num)] + " 후보의 " + media +
                    (media.equals("Twitter")?"가":"이") + " 업데이트 되었습니다");
        } else if(contents.equals("Survey")) {
            intent = new Intent(this, MainActivity.class);
            builder.setContentTitle("새로운 여론조사 결과가 등록되었습니다");
        } else if(contents.equals("Video")) {
            intent = new Intent(this, CandidateActivity.class);
            String candidate_num = data.get("candidate_num");
            intent.putExtra("candidate_num", candidate_num);
            intent.putExtra("page_num", 0);
            builder.setContentTitle(candidate_names[Integer.parseInt(candidate_num)] + " 후보의 유튜브가 업데이트 되었습니다");
        } else return;

        builder.setContentText("터치하면 어플리케이션으로 이동합니다.");
        Intent[] intents = new Intent[1];
        intents[0] = intent;
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 101, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.main_icon);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification noti = builder.build();
        manager.notify(2, noti);
    }
}