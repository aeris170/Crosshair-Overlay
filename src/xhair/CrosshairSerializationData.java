package xhair;

import java.awt.Color;
import java.io.Serializable;

/**
 * Serialization data of crosshairs. This class is used to save a crosshair
 * configuration.
 *
 * @author Doga Oruc
 * @version 1.1
 * @since 1.1
 */
public class CrosshairSerializationData implements Serializable {

	private static final long serialVersionUID = 3432129052361971682L;

	/** The crosshair width. */
	private int width;

	/** The crosshair height. */
	private int height;

	/** The crosshair image index. */
	private int imageIndex;

	/** The crosshair's fill color. */
	private Color fillColor;

	/** The crosshair's outline color. */
	private Color outlineColor;

	/** Instantiates a new crosshair. Bean. */
	public CrosshairSerializationData() {}

	/**
	 * Gets the CrosshairSerializationData's width.
	 *
	 * @return the CrosshairSerializationData's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the CrosshairSerializationData's height.
	 *
	 * @return the CrosshairSerializationData's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the CrosshairSerializationData's image index.
	 *
	 * @return the CrosshairSerializationData's image index
	 */
	public int getImageIndex() {
		return imageIndex;
	}

	/**
	 * Gets the CrosshairSerializationData's fill color.
	 *
	 * @return the CrosshairSerializationData's fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Gets the CrosshairSerializationData's outline color.
	 *
	 * @return the CrosshairSerializationData's outline color
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Sets the CrosshairSerializationData's width.
	 *
	 * @param width the new CrosshairSerializationData's width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Sets the CrosshairSerializationData's height.
	 *
	 * @param height the new CrosshairSerializationData's height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}

	/**
	 * Sets the CrosshairSerializationData's fill color.
	 *
	 * @param fillColor the new CrosshairSerializationData's fill color
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * Sets the CrosshairSerializationData's outline color.
	 *
	 * @param outlineColor the new CrosshairSerializationData's outline color
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}
}