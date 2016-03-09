package edu.ucsb.cs56.projects.games.pacman;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

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

	public void play() throws UnsupportedAudioFileException, LineUnavailableException{
		if(this.audioClip.isRunning()) return;
		this.audioClip.setMicrosecondPosition(0);
		this.audioClip.start();
	}
}