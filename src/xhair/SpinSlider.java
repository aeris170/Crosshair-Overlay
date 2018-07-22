package xhair;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * The Class SpinSlider.
 *
 * @author trashgod
 * @version 1.0
 * @since 1.0
 * @see "https://stackoverflow.com/questions/6067898"
 */
public class SpinSlider extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4628802037851293886L;

	/** If the slider adjusts the crosshair width. */
	public static final boolean WIDTH_ADJUSTER = true;

	/** If the slider adjusts the crosshair height. */
	public static final boolean HEIGHT_ADJUSTER = false;

	boolean sliderFired = false;
	JSpinner spinner;
	JSlider slider;

	/**
	 * Instantiates a new spin slider.
	 *
	 * @param isWidthAdjuster flag to know whether this slider will adjust
	 *                        crosshair width, or height
	 */
	public SpinSlider(final boolean isWidthAdjuster) {
		setLayout(new FlowLayout());

		spinner = new JSpinner();
		slider = new JSlider(0, 500, 250);

		slider.setMinorTickSpacing(25);
		slider.setMajorTickSpacing(100);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.addChangeListener(e -> {
			final JSlider s = (JSlider) e.getSource();
			if(isWidthAdjuster) {
				Overlay.get().setCrosshairWidth(s.getValue());
			} else {
				Overlay.get().setCrosshairHeight(s.getValue());
			}
			if(!sliderFired) {
				spinner.setValue(s.getValue());
			}
		});
		this.add(slider);

		spinner.setModel(new SpinnerNumberModel(250, 0, 500, 1));
		spinner.setEditor(new JSpinner.NumberEditor(spinner, "0'%'"));
		spinner.addChangeListener(e -> {
			final JSpinner s = (JSpinner) e.getSource();
			if(isWidthAdjuster) {
				Overlay.get().setCrosshairWidth((int) s.getValue());
			} else {
				Overlay.get().setCrosshairHeight((int) s.getValue());
			}
			sliderFired = true;
			slider.setValue((int) s.getValue());
			sliderFired = false;
		});
		this.add(spinner);
	}

	/**
	 * Gets the value of the slider.
	 *
	 * @return the value of the slider
	 */
	public int getValue() {
		return slider.getValue();
	}

	/**
	 * Sets the value of the slider.
	 *
	 * @param value the new value
	 */
	public void setValue(final int value) {
		slider.setValue(value);
	}
}