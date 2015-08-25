/**
 * 
 */
package ru.zkipo.vss.libraryplayer.dispatcher;

import org.eclipse.swt.widgets.Display;

import ru.zkipo.vss.libraryplayer.flac.utils.FLACPlayer;
import ru.zkipo.vss.libraryplayer.thread.PlayerThread;

/**
 * @author novaman
 * 
 *         Должен управлять воспроизедением. Наверно надо сюда сунуть
 *         какой-нибуть аудиострим вместо строки //TODO подумать на эту тему )))
 *
 */
public final class PlayerActionDispatcher {

	private static String aCurrentTrack = null;;
	private static PlayerThread aPlayer = null;
	private static Display aDisplay;

	public static Runnable StartPlay(String aFileName) {
		aCurrentTrack = aFileName;

		if (aPlayer == null || aCurrentTrack == null || aCurrentTrack.isEmpty())
			return null;

		FLACPlayer fp = new FLACPlayer();
		return new PlayerThread(aCurrentTrack, fp);

		// aDisplay.asyncExec(aPlayer);
	}

	public static void setPlayerThread(PlayerThread in) {
		aPlayer = in;
	}

	public static void setDisplay(Display in) {
		aDisplay = in;
	}

	public static void PausePlay() {

	}

	public static void StopPlay() {

	}

}
