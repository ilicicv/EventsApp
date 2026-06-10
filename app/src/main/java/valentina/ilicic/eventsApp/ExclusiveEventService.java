package valentina.ilicic.eventsApp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ExclusiveEventService extends Service {

    private static final String TAG = "ExclusiveEventService";
    private static final String CHANNEL_ID = "ExclusiveEventChannel";
    private static final long REGISTRATION_WINDOW = 5 * 60 * 1000;
    private static final long REPEAT_INTERVAL = 24 * 60 * 60 * 1000;
    private final ExclusiveEventBinder binder = new ExclusiveEventBinder();
    private final HTTPHelper httpHelper = new HTTPHelper();
    private Thread countdownThread;
    private boolean isCounting = false;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] categories = {"Concert", "Festival", "Party", "Exhibition", "Stand-Up & Theater"};
                    String[] locations = {"Stadion Grbavica", "Dom Mladih", "Skenderija", "Skenderija", "Trg Republike"};
                    String[] names = {"Specijalni Rock Spektakl", "Zatvoreni Techno Rave", "Stand-up Nastup", "Privatna Izložba Slika", "Open Air Festival", "Jazz Veče", "Elektronski Muzički Festival", "Komedija Noc", "Klasična Muzika u Parku", "Indie Rock Koncert", "Hip-Hop Showcase", "Foto Izložba", "Improv Teatar", "Drum and Bass Party", "Akustični Koncert"};
                    String[] description = {"Iskoristite ekskluzivni prozor za brzu prijavu.", "Gostovanje svetski poznatih izvođača.", "Broj mesta je ograničen, požurite sa prijavom.", "Pogledajte postavku pre zvaničnog otvaranja.", "Najbolja zabava u gradu koja traje večeras."};

                    Random random = new Random();
                    int randName = random.nextInt(names.length);
                    int randCat = random.nextInt(categories.length);
                    int randLoc = random.nextInt(locations.length);

                    JSONObject newEvent = new JSONObject();
                    String generisanoIme = names[randName] + " # " + (System.currentTimeMillis() % 1000);
                    newEvent.put("name", generisanoIme);
                    newEvent.put("description", description[randName % description.length]);
                    newEvent.put("location", locations[randLoc]);
                    newEvent.put("category", categories[randCat]);
                    newEvent.put("promoted", false); // IZMIJENJENO

                    Calendar calendar = Calendar.getInstance();
                    Random randomDays = new Random();
                    int daysInFuture = randomDays.nextInt(7) + 1;
                    calendar.add(Calendar.DAY_OF_YEAR, daysInFuture);

                    String timeNow = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.getTime());
                    newEvent.put("eventTime", timeNow);

                    String url = HTTPHelper.BASE_URL + "/events";
                    JSONObject response = httpHelper.postJSONObjectFromURL(url, newEvent);

                    if (response != null) {

                        final String eventName = response.getString("name");
                        long expirationTime = System.currentTimeMillis() + REGISTRATION_WINDOW;

                        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                        prefs.edit()
                                .putLong("expiration_time", expirationTime)
                                .putString("exclusive_event_name", eventName)
                                .apply();

                        try {
                            JSONArray serverEvents = httpHelper.getJSONArrayFromURL(HTTPHelper.BASE_URL + "/events");
                            if (serverEvents != null) {
                                for (int i = 0; i < serverEvents.length(); i++) {
                                    JSONObject obj = serverEvents.getJSONObject(i);
                                    if (obj.has("name") && obj.getString("name").equals(eventName)) {
                                        String mongoId = obj.getString("_id");
                                        EventsTable eventsTable = dbHelper.getEventsTable();
                                        eventsTable.insertEvent(dbHelper.getWritableDatabase(), mongoId, eventName,
                                                obj.getString("description"), obj.getString("location"),
                                                obj.getString("eventTime"), obj.getString("category"), 0, 100); // IZMIJENJENO NA 0
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {}

                        binder.setEventStatus(1);
                        binder.setRegistrationOpen(true);
                        binder.setRemainingTime(REGISTRATION_WINDOW);

                        startCountdown(expirationTime);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                regOpenNotification(eventName, expirationTime);
                            }
                        });

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent serviceIntent = new Intent(getApplicationContext(), ExclusiveEventService.class);
                                startService(serviceIntent);
                            }
                        }, REPEAT_INTERVAL);

                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON GREŠKA: Greška u parsiranju JSON-a unutar servisa: " + e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, "IOException: Server je nedostupan: " + e.getMessage(), e);
                }
            }
        }).start();

        return START_STICKY;
    }

    private void startCountdown(long expirationTime) {
        if (countdownThread != null && countdownThread.isAlive()) {
            isCounting = false;
        }
        isCounting = true;
        countdownThread = new Thread(() -> {
            while (isCounting) {
                long timeLeft = expirationTime - System.currentTimeMillis();
                if (timeLeft <= 0) {
                    isCounting = false;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        binder.setEventStatus(0);
                        binder.setRegistrationOpen(false);
                        binder.setRemainingTime(0);

                        long nextOpportunityTime = System.currentTimeMillis() + REPEAT_INTERVAL;
                        regClosedNotification(nextOpportunityTime);
                    });
                    break;
                }
                binder.setRemainingTime(timeLeft);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        countdownThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void regOpenNotification(String eventName, long expirationTime) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("event_name", eventName);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Novi ekskluzivni događaj!")
                .setContentText("Prijave za: " + eventName + " su otvorene!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setUsesChronometer(true)
                .setChronometerCountDown(true)
                .setWhen(expirationTime);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    private void regClosedNotification(long nextOpportunityMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String nextTime = sdf.format(new Date(nextOpportunityMillis));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Prijave su zatvorene!")
                .setContentText("Isteklo vreme za prijavu. Sledeća prilika je u " + nextTime + "h!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(2, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Ekskluzivni Događaji";
            String description = "Kanali za obaveštenja o ekskluzivnim događajima";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}