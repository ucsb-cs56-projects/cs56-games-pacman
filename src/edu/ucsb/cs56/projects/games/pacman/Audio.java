package edu.ucsb.cs56.projects.games.pacman;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

/**
 * An audio class that supports importing an audio asset that is provided as an input stream.
 * This allows the same clip to be run multiple times in concert/succession.
 *
 * @author Ryan Tse
 * @author Chris Beser
 * @version CS56 W16
 */
public class Audio {
	private AudioFormat audioFormat;
	private int audioSize;
	private byte[] audioData;
	private DataLine.Info audioInfo;
	private Clip audioClip;

	public Audio(InputStream inputStream) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.loadStream(inputStream));
		this.audioFormat = audioInputStream.getFormat();
		this.audioSize = (this.audioFormat.getFrameSize() * (int)audioInputStream.getFrameLength());
		this.audioData = new byte[this.audioSize];
		this.audioInfo  = new DataLine.Info(Clip.class, this.audioFormat, this.audioSize);
		audioInputStream.read(this.audioData, 0, this.audioSize);
		this.audioClip = (Clip) AudioSystem.getLine(this.audioInfo);
		this.audioClip.open(this.audioFormat, this.audioData, 0, this.audioSize);
	}
       /**
	*Gets the sound bytes from the stream
	*
	*@param inputStream the stream used to get the sound bytes
	*@return ByteArrayInputStream
	*/
	public ByteArrayInputStream loadStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte buffer[] = new byte[1024];
		int lengthRead;

		while((lengthRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
			byteArrayOutputStream.write(buffer, 0, lengthRead);
		}

		byteArrayOutputStream.flush();

		inputStream.close();
		byteArrayOutputStream.close();
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

        /**
	 *Plays a sound clip once and will not replay until the clip has finished
	 *
	 *@throws UnsupportedAudioFileException
	 *@throws LineUnavailableException
         */
	public void play() throws UnsupportedAudioFileException, LineUnavailableException {
		this.play(true);
	}
        /**
	 *Plays a sound clip once and will not replay until the clip has finished
	 *
	 *@param playOnlyOnce boolean that dictates whether the sound should replay
	 *@throws UnsupportedAudioFileException
	 *@throws LineUnavailableException
         */
	public void play(boolean playOnlyOnce) throws UnsupportedAudioFileException, LineUnavailableException {
		if(playOnlyOnce && this.audioClip.isRunning()) return;
		this.audioClip.setMicrosecondPosition(0);
		this.audioClip.start();
	}
}
