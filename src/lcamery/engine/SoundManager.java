package lcamery.engine;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	private HashMap<String, Clip> clipMap;
	private HashMap<String, SourceDataLine> lineMap;

	public SoundManager() {
		clipMap = new HashMap<String, Clip>();
		lineMap = new HashMap<String, SourceDataLine>();
	}

	public void addClip(String s) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File file = new File(s);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		AudioInputStream stream = AudioSystem.getAudioInputStream(in);
		Clip clip = AudioSystem.getClip();
		clip.open(stream);
		clipMap.put(s,clip);
	}
	
	public void playLine(String s) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File file = new File(s);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		AudioInputStream stream = AudioSystem.getAudioInputStream(in);
		AudioFormat format = stream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		lineMap.put(s, line);
		line.open(format);
		line.start();
		byte[] bytesBuffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = stream.read(bytesBuffer)) != -1){
			line.write(bytesBuffer, 0, bytesRead);
		}
		line.drain();
		line.close();
		stream.close();
		System.out.println("ALL DONE!");
	}
	
	public void playSound(String s) throws LineUnavailableException, IOException{
		Clip clip = clipMap.get(s);
		clip.start();
		LineListener listener = new LineListener() {
			public void update(LineEvent event){
				if (event.getType() == Type.STOP){
					clip.flush();
					clip.setFramePosition(0);
				}
			}
		};
		clip.addLineListener(listener);
	}
	
	public void cleanup(){
		System.out.println("HERE TOO!");
		Collection c1 = clipMap.values();
		Iterator itr = c1.iterator();
		while (itr.hasNext()){
			Clip c = (Clip) itr.next();
			c.close();
		}
		Collection c2 = lineMap.values();
		Iterator itr2 = c2.iterator();
		while (itr2.hasNext()){
			SourceDataLine line = (SourceDataLine) itr.next();
			line.drain();
			line.close();
		}
	}

}
