package org.bailedout.android.tonal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.Random;

public class Tonal extends ActionBarActivity {

  public static final String SHARED_PREFS = "Tonal";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tonal);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, new TonalFragment()).commit();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.tonal, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class TonalFragment extends Fragment {

    private static final String TAG = TonalFragment.class.getSimpleName();

    public TonalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.fragment_tonal, container, false);

      final SharedPreferences sp = inflater.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

      final CheckBox cb = (CheckBox) view.findViewById(R.id.enabled);
      final TextView tv = (TextView) view.findViewById(R.id.enabled_text);

      cb.setChecked(sp.getBoolean("enabled", true));
      tv.setText(cb.isChecked() ? R.string.dtmf_enabled : R.string.dtmf_disabled);
      cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
          sp.edit().putBoolean("enabled", isChecked).commit();
          tv.setText(isChecked ? R.string.dtmf_enabled : R.string.dtmf_disabled);
        }
      });

      final int toneDuration = sp.getInt("tone duration", 500);
      final int pauseDuration = sp.getInt("pause duration", 500);

      final TextView toneDurationTextView = (TextView) view.findViewById(R.id.tone_duration_time);
      toneDurationTextView.setText(Integer.toString(toneDuration));
      final SeekBar sb1 = (SeekBar) view.findViewById(R.id.tone_duration_seek_bar);
      sb1.setProgress(toneDuration);
      sb1.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
          sp.edit().putInt("tone duration", progress).commit();
          toneDurationTextView.setText(Integer.toString(progress));
        }
      });

      final TextView pauseDurationTextView = (TextView) view.findViewById(R.id.pause_duration_time);
      pauseDurationTextView.setText(Integer.toString(pauseDuration));
      final SeekBar sb2 = (SeekBar) view.findViewById(R.id.pause_duration_seek_bar);
      sb2.setProgress(pauseDuration);
      sb2.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
          sp.edit().putInt("pause duration", progress).commit();
          pauseDurationTextView.setText(Integer.toString(progress));
        }
      });

      final Button b = (Button) view.findViewById(R.id.test);
      b.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
          b.setEnabled(false);
          new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {
              int toneDuration = Integer.parseInt(toneDurationTextView.getText().toString());
              int pauseDuration = Integer.parseInt(pauseDurationTextView.getText().toString());
              String phoneNumber = new BigInteger(16, new Random(System.currentTimeMillis())).toString();
              Log.d(TAG, "Testing " + phoneNumber);
              new Dialer(toneDuration, pauseDuration).dial(phoneNumber);
              return null;
            }

            @Override
            protected void onPostExecute(Void result) {
              b.setEnabled(true);
            }
          }.execute();
        }


      });
      return view;
    }

    private static class EmptyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
      @Override
      public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
      }

      @Override
      public void onStartTrackingTouch(final SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(final SeekBar seekBar) {
      }
    }
  }


}
