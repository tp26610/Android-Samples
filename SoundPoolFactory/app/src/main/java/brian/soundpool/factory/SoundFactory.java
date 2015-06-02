package brian.soundpool.factory;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoundFactory {

	private static final boolean DEBUG = true; 
	
	
	private static final int PRIORITY = 1;//the priority of the sound. Currently has no effect. Use a value of 1 for future compatibility.




	private static void trace(String message) {
		if(DEBUG)
			Log.d("SoundFactory", message);
	}

	private static Sound newSound(
			final SoundPool soundPool,
			final int soundId) {
		
		return new Sound() {

			private int streamId = 0;
			
			@Override
			public void play(boolean looping) {
				trace("newSound play >> soundId = " + soundId);
				streamId = playSound(soundPool, soundId, looping);
				trace("newSound play >> streamId = " + streamId);
			}

			@Override
			public void stop() {
				trace("newSound stop >> streamId = " + streamId);
				soundPool.stop(streamId);
			}
			
		};
	}
	
	public static SoundPool createSoundPool(int maxStreams) {
		trace("createSoundPool");
		int srcQuality =0;// no effect , default for 0
        SoundPool soundPool= new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, srcQuality);        
        return soundPool;
	}
	
	public static Sound createSound(SoundPool soundPool, Context context, int rid) {
		trace("loadSound");
		int soundId = soundPool.load(context, rid, PRIORITY);				
		return newSound(soundPool, soundId);
	}
	
	public static Sound createSound(SoundPool soundPool, String path) {
		trace("createSound");
		int soundId = soundPool.load(path, PRIORITY);
		return newSound(soundPool, soundId);
	}
	
	private static int playSound(SoundPool soundPool, int soundId, boolean looping) {
		trace("playSound");

		int loop_mode = looping ? -1 : 0;
		
		 int streamId = soundPool.play(
	        		soundId, // soundID	a soundID returned by the load() function
	        		0.99f, // leftVolume	left volume value (range = 0.0 to 1.0)
	        		0.99f, // rightVolume	right volume value (range = 0.0 to 1.0)
	        		10, // priority	stream priority (0 = lowest priority)
	        		loop_mode, // loop mode (0 = no loop, -1 = loop forever)
	        		1); // playback rate (1.0 = normal playback, range 0.5 to 2.0)
		 
		 if(streamId == 0) {
			 throw new SoundException("playSound exception");
		 }
		 
		 return streamId;
	}

	
}
