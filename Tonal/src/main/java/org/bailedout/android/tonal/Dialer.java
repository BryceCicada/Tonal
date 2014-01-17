package org.bailedout.android.tonal;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class Dialer {
  private final int mToneDuration;
  private final int mPauseDuration;

  public Dialer(final int toneDuration, final int pauseDuration) {
    mToneDuration = toneDuration;
    mPauseDuration = pauseDuration;
  }


  public void dial(final String phoneNumber) {
    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_DTMF, 100);
    try {
      for (char c : phoneNumber.toCharArray()) {
        tg.startTone(getTone(c), mToneDuration);
        Thread.sleep(mToneDuration + mPauseDuration);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    tg.release();
  }

  private int getTone(final char c) {
    int r;
    switch (c) {
      case '0':
        r = ToneGenerator.TONE_DTMF_0;
        break;
      case '1':
        r = ToneGenerator.TONE_DTMF_1;
        break;
      case '2':
        r = ToneGenerator.TONE_DTMF_2;
        break;
      case '3':
        r = ToneGenerator.TONE_DTMF_3;
        break;
      case '4':
        r = ToneGenerator.TONE_DTMF_4;
        break;
      case '5':
        r = ToneGenerator.TONE_DTMF_5;
        break;
      case '6':
        r = ToneGenerator.TONE_DTMF_6;
        break;
      case '7':
        r = ToneGenerator.TONE_DTMF_7;
        break;
      case '8':
        r = ToneGenerator.TONE_DTMF_8;
        break;
      case '9':
        r = ToneGenerator.TONE_DTMF_9;
        break;
      default:
        r = ToneGenerator.TONE_CDMA_SIGNAL_OFF;
        break;
    }
    return r;
  }
}
