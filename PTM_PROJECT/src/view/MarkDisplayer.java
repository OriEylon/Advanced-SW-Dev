package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MarkDisplayer extends Canvas {
	Double[][] mapData;
	Double Height;
	Double Width;
	Double h;
	Double w;
	StringProperty markFileName;

	public MarkDisplayer() {
		markFileName = new SimpleStringProperty();
	}

	public void setMapData(Double[][] mapData) {
		this.mapData = mapData;
	}

	public String getMarkFileName() {
		return markFileName.get();
	}

	public void setMarkFileName(String markFileName) {
		this.markFileName.set(markFileName);
	}

	public void drawMark(Double x, Double y) {
		if (mapData != null) {
			Height = getHeight();
			Width = getWidth();
			h = Height / mapData.length;
			w = Width / mapData[0].length;
			GraphicsContext gc = getGraphicsContext2D();
			Image img = null;
			try {
				img = new Image(new FileInputStream(markFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			gc.clearRect(0, 0, Width, Height);
			gc.drawImage(img, x-10, y-10, w * 20, h * 20);

		}

	}

}
