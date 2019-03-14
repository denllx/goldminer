package goldminer;
import javax.sound.sampled.*;
import java.io.*;

public class SoundPlayer {
	File file;
	AudioInputStream stream;
	AudioFormat format;
	DataLine.Info info;
	Clip clip;
	SoundPlayer(){
		
	}
	
	public void loadSound(String filename)
	{
		file=new File(filename);
		try {
			stream=AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		format=stream.getFormat();
		
	}
	
	public void playSound()
	{
		info=new DataLine.Info(Clip.class, format);
		try {
			clip=(Clip)AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(stream);
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clip.start();
	}
}
