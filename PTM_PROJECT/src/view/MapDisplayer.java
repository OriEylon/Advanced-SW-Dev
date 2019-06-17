package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapDisplayer extends Canvas {
	Double[][] mapData;
	Double Height;
	Double Width;
	public Double h;
	public Double w;

	public void setMapData(Double[][] mapData) {
		this.mapData = mapData;
		redraw();
	}

	public void redraw() {
		if (mapData != null) {
			Height = getHeight();
			Width = getWidth();
			h = Height / mapData.length;
			w = Width / mapData[0].length;
			GraphicsContext gc = getGraphicsContext2D();

			for (int i = 0; i < mapData.length; i++) {
				for (int j = 0; j < mapData[i].length; j++) {
					int r = (int) (255 - mapData[i][j]);
					int g = (int) (mapData[i][j] - 0);
					gc.setFill(Color.rgb(r, g, 0));
					gc.fillRect(j * w, i * h, w, h);
				}
			}
		}
	}

}
