/**
 * 
 */
package ru.zkipo.vss.libraryplayer.flac.utils;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.apps.Player;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

/**
 * @author Novaman
 *
 */
public class FLACPlayer implements PCMProcessor {

	private AudioFormat fmt;

	private DataLine.Info info;

	private SourceDataLine line;

	private Vector listeners = new Vector();

	public void addListener(LineListener listener)

	{

		listeners.add(listener);

	}

	/**
	 * 
	 * Decode and play an input FLAC file.
	 * 
	 * @param inFileName
	 *            The input FLAC file name
	 * 
	 * @throws IOException
	 *             Thrown if error reading file
	 * 
	 * @throws LineUnavailableException
	 *             Thrown if error playing file
	 */

	public void decode(String inFileName) throws IOException,
			LineUnavailableException {

		// System.out.println("Play [" + inFileName + "]");

		FileInputStream is = new FileInputStream(inFileName);

		FLACDecoder decoder = new FLACDecoder(is);

		decoder.addPCMProcessor(this);

		try {

			decoder.decode();

		} catch (EOFException e) {

			// skip

		}

		line.drain();

		line.close();

		// We're going to clear out the list of listeners as well, so that
		// everytime through

		// things are basically at the same starting point.

		listeners.clear();

	}

	/**
	 * 
	 * Process the StreamInfo block.
	 * 
	 * @param streamInfo
	 *            the StreamInfo block
	 * 
	 * @see org.kc7bfi.jflac.PCMProcessor#processStreamInfo(org.kc7bfi.jflac.metadata.StreamInfo)
	 */

	@Override
	public void processStreamInfo(StreamInfo streamInfo) {

		try {

			fmt = streamInfo.getAudioFormat();

			info = new DataLine.Info(SourceDataLine.class, fmt,
					AudioSystem.NOT_SPECIFIED);

			line = (SourceDataLine) AudioSystem.getLine(info);

			// Add the listeners to the line at this point, it's the only

			// way to get the events triggered.

			int size = listeners.size();

			for (int index = 0; index < size; index++)

				line.addLineListener((LineListener) listeners.get(index));

			line.open(fmt, AudioSystem.NOT_SPECIFIED);

			line.start();

		} catch (LineUnavailableException e) {

			e.printStackTrace();

		}

	}

	/**
	 * 
	 * Process the decoded PCM bytes.
	 * 
	 * @param pcm
	 *            The decoded PCM data
	 * 
	 * @see org.kc7bfi.jflac.PCMProcessor#processPCM(org.kc7bfi.jflac.util.ByteSpace)
	 */

	@Override
	public void processPCM(ByteData pcm) {

		line.write(pcm.getData(), 0, pcm.getLen());

	}

	public void removeListener(LineListener listener)

	{

		listeners.removeElement(listener);

	}

	/**
	 * 
	 * The main routine.
	 * 
	 * <p>
	 * args[0] is the input file name
	 * 
	 * @param args
	 *            Command line arguments
	 */

	public static void main(String[] args) {

		try {

			Player decoder = new Player();

			for (int i = 0; i < args.length; i++)

				decoder.decode(args[i]);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (LineUnavailableException e) {

			e.printStackTrace();

		}

		System.exit(0);

	}

}
