package xhair;

import java.awt.Color;
import java.io.Serializable;

public class CrosshairSerializationData implements Serializable {

	private static final long serialVersionUID = 3432129052361971682L;

	private int width;
	private int height;
	private int imageIndex;
	private Color fillColor;
	private Color outlineColor;

	public CrosshairSerializationData() {}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}
}