package brian.soundpool.factory;

import android.app.Activity;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("");

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Thread(runnablePlaySound).start();

    }

    private void msleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void playSound(Sound sound) {
        sound.play(false);
        msleep(3000);
        sound.stop();
    }

    private void updateMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(message + "\r\n");
            }
        });

    }

    private Runnable runnablePlaySound = new Runnable() {
        @Override
        public void run() {

            int[] rids = new int[] {
                    R.raw.dtmf_0,
                    R.raw.tone_dial,
                    R.raw.tone_busy
            };

            updateMessage("creating SoundPool ...");

            // create SoundPool
            SoundPool soundPool = SoundFactory.createSoundPool(rids.length);

            updateMessage("creating Sounds ...");
            // create sounds
            Sound dtmf = SoundFactory.createSound(soundPool, MainActivity.this, rids[0]);
            Sound dialTone = SoundFactory.createSound(soundPool, MainActivity.this, rids[1]);
            Sound busyTone = SoundFactory.createSound(soundPool, MainActivity.this, rids[2]);

            // wait for loading sounds.
            msleep(500);

            updateMessage("playing Sounds ...");
            // play sounds
            playSound(dialTone);
            playSound(busyTone);
            playSound(dtmf);


            // release resources
            soundPool.release();
            soundPool = null;

            updateMessage("flow finished");
        }
    };


}
