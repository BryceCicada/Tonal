package org.bailedout.android.tonal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class TonalReceiver extends BroadcastReceiver {
  public static final String TAG = TonalReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    SharedPreferences sp = context.getSharedPreferences(Tonal.SHARED_PREFS, Context.MODE_PRIVATE);
    if (sp.getBoolean("enabled", true)) {
      int toneDuration = sp.getInt("tone duration", 500);
      int pauseDuration = sp.getInt("pause duration", 500);

      String phoneNumber = getResultData();
      if (phoneNumber == null) {
        phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
      }

      // Stop anything else handling this call.
      setResultData(null);

      Dialer d = new Dialer(toneDuration, pauseDuration);
      d.dial(phoneNumber);
    }
  }
}