/**
 * 
 */
package ru.zkipo.vss.libraryplayer.thread;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import ru.zkipo.vss.libraryplayer.flac.utils.FLACPlayer;

/**
 * @author Novaman
 *
 */
public class PlayerThread implements Runnable {

	private String aFilePath;
	private FLACPlayer aPlayer;

	@Override
	public void run() {
		try {
			aPlayer.decode(aFilePath);
		} catch (IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public PlayerThread(String aFilePath, FLACPlayer aPlayer) {
		super();
		this.aFilePath = aFilePath;
		this.aPlayer = aPlayer;
	}

}
