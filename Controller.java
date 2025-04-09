package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Controller implements Initializable{
	
	@FXML
	private Pane panel;
	@FXML
	private Label label;
	@FXML
	private Button oynat, durdur, onceki, sonraki, basasar;
	@FXML
	private ProgressBar gosterge;
	@FXML
	private Slider sesayar;
	
	
	private Media medya;
	private MediaPlayer medyaoynatici;
	
	private File directory;
	private File[] files;
	
	private ArrayList<File> playlist;
	
	private int sarkiadi;
	
	
	private Timer zamanlayici;
	private TimerTask task;
	
	private boolean caliyor;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		playlist = new ArrayList<File>();
		
		directory = new File("music");
		
		files = directory.listFiles();
		
		if(files != null) {
			
			for(File file : files) {
				
				playlist.add(file);
			}
		}
		
		medya = new Media(playlist.get(sarkiadi).toURI().toString());
		medyaoynatici = new MediaPlayer(medya);
		
		label.setText(playlist.get(sarkiadi).getName());
		
	
		sesayar.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				
				medyaoynatici.setVolume(sesayar.getValue() * 0.01);			
			}
		});
		
		gosterge.setStyle("-fx-accent: #00FF00;");
	}
	
	public void baslat() {
		
		zamanlayici = new Timer();
		
		task = new TimerTask() {
			
			public void run() {
				
				caliyor = true;
				double current = medyaoynatici.getCurrentTime().toSeconds();
				double end = medya.getDuration().toSeconds();
				gosterge.setProgress(current/end);
				
				if(current/end == 1) {
					
					iptalet();
				}
			}
		};
		
		zamanlayici.scheduleAtFixedRate(task, 0, 1000);
	}
	
	public void iptalet() {
		
		caliyor = false;
		zamanlayici.cancel();
	}


	public void oynat() {
		
		baslat();
		medyaoynatici.setVolume(sesayar.getValue() * 0.01);
		medyaoynatici.play();
	}
	
	public void durdur() {
		
		iptalet();
		medyaoynatici.pause();
	}
	
	public void onceki() {
		
		if(sarkiadi > 0) {
			
			sarkiadi--;
			
			medyaoynatici.stop();
			
			if(caliyor) {
				
				iptalet();
			}
			
			medya = new Media(playlist.get(sarkiadi).toURI().toString());
			medyaoynatici = new MediaPlayer(medya);
			
			label.setText(playlist.get(sarkiadi).getName());
			
			oynat();
		}
		else {
			
			sarkiadi = playlist.size() - 1;
			
			medyaoynatici.stop();
			
			if(caliyor) {
				
				iptalet();
			}
			
			medya = new Media(playlist.get(sarkiadi).toURI().toString());
			medyaoynatici = new MediaPlayer(medya);
			
			label.setText(playlist.get(sarkiadi).getName());
			
			oynat();
		}
	}
	
   	public void sonraki() {
		
		if(sarkiadi < playlist.size() - 1) {
			
			sarkiadi++;
			
			medyaoynatici.stop();
			
			if(caliyor) {
				
				iptalet();
			}
			
			medya = new Media(playlist.get(sarkiadi).toURI().toString());
			medyaoynatici = new MediaPlayer(medya);
			
			label.setText(playlist.get(sarkiadi).getName());
			
			oynat();
		}
		else {
			
			sarkiadi = 0;
			
			medyaoynatici.stop();
			
			medya = new Media(playlist.get(sarkiadi).toURI().toString());
			medyaoynatici = new MediaPlayer(medya);
			
			label.setText(playlist.get(sarkiadi).getName());
			
			oynat();
		}
	}
   	
   	
    public void basasar() {
		
		    gosterge.setProgress(0);
		    medyaoynatici.seek(Duration.seconds(0));
	}
	
	
	
}
