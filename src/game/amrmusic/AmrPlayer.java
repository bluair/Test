package game.amrmusic;

import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

public class AmrPlayer implements Runnable {

	private Thread thread;

	Player player;
	MusicPlayerListener listener;

	public AmrPlayer(MusicPlayerListener listener) {
		this.listener = listener;
	}

	public void start() throws Exception {

		// Play now.
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		try {
			InputStream is = getClass().getResourceAsStream("/evo.amr");
			player = Manager.createPlayer(is, "audio/amr");

//			InputStream is = getClass().getResourceAsStream("/evo.mp3");
//			player = Manager.createPlayer(is,"audio/mpeg");

			player.realize();
			VolumeControl vc = (VolumeControl) player.getControl("VolumeControl");
			if (vc != null) {
				vc.setLevel(40);
			}
			player.prefetch();
			player.setLoopCount(-1);
			player.start();
			listener.handlePlayerStarted();
		} catch (Exception e) {
			listener.handlePlayerRunError(e.getMessage());
		}
	}

	public void stop() {
		try {
			if (player != null) {
				player.stop();
				player.deallocate();
			}
		} catch (Throwable e) {
		}
	}
}
