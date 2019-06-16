package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlaneDisplayer extends Canvas {

	Double[][] mapData;
	Double Height;
	Double Width;
	Double h;
	Double w;
	StringProperty PlaneFileName;

	public PlaneDisplayer() {
		PlaneFileName = new SimpleStringProperty();
	}

	public void setMapdata(Double[][] mapdata) {
		this.mapData = mapdata;

	}

	public String getPlaneFileName() {
		return PlaneFileName.get();
	}

	public void setPlaneFileName(String planeFileName) {
		PlaneFileName.set(planeFileName);
	}

	public void drawPlane(Double x, Double y) {
		if (mapData != null) {
			Height = getHeight();
			Width = getWidth();
			h = Height / mapData.length;
			w = Width / mapData[0].length;
			GraphicsContext gc = getGraphicsContext2D();
			Image img = null;
			try {
				img = new Image(new FileInputStream(PlaneFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			gc.clearRect(0, 0, Width, Height);
			gc.drawImage(img, x, y, w * 20, h * 20);

		}

	}

}
