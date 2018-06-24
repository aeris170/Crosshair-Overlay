package xhair;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Class Settings.
 *
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class Settings extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4568652071397810155L;

	// Preference key names
	/** The Key Constant WIDTH. */
	private static final String WIDTH = "width";
	
	/** The Key Constant HEIGHT. */
	private static final String HEIGHT = "height";
	
	/** The Key Constant XHAIR. */
	private static final String XHAIR = "img";

	private JPanel settingsPanel;
	private SpinSlider widthAdjuster, heightAdjuster;
	private JComboBox<ImageIcon> images;
	private JButton applyButton, cancelButton;

	/**
	 * Instantiates a new settings.
	 */
	public Settings() {
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JPanel south = new JPanel(new GridLayout(1, 0));

		widthAdjuster = new SpinSlider(SpinSlider.WIDTH_ADJUSTER);
		heightAdjuster = new SpinSlider(SpinSlider.HEIGHT_ADJUSTER);

		images = new JComboBox<ImageIcon>(CrosshairImageBank.supplyAllImagesAsIcons());
		images.setMaximumRowCount(3);
		images.setRenderer(new CustomRenderer());

		widthAdjuster.setBorder(BorderFactory.createTitledBorder("Crosshair Width"));
		heightAdjuster.setBorder(BorderFactory.createTitledBorder("Crosshair Height"));

		images.setBorder(BorderFactory.createTitledBorder("Crosshair Image"));

		images.addActionListener(e -> {
			Overlay.get().setCrosshairImage((int) images.getSelectedIndex() + 1);
		});

		applyButton = new JButton("  OK  ");
		applyButton.addActionListener(e -> {
			saveNewSettingsToHierarchialNode();
			dispose();
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
			retrieveSettingsFromHierarchialNode();
			dispose();
		});

		center.add(widthAdjuster);
		center.add(heightAdjuster);
		center.add(images);

		south.add(Box.createHorizontalStrut(25));
		south.add(applyButton);
		south.add(Box.createHorizontalStrut(25));
		south.add(cancelButton);
		south.add(Box.createHorizontalStrut(25));

		settingsPanel.add(center, BorderLayout.CENTER);
		settingsPanel.add(south, BorderLayout.SOUTH);

		super.add(settingsPanel);
		super.setResizable(false);
		super.pack();
		super.setLocationByPlatform(true);
		retrieveSettingsFromHierarchialNode();
		super.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				retrieveSettingsFromHierarchialNode();
				dispose();
			}
		});
		super.setVisible(true);
	}

	/**
	 * Initialize settings to accurately represent saved data.
	 */
	public static void initializeSettings() {
		// Retrieve the user preference node for the this class
		Preferences prefs = Preferences.userNodeForPackage(Settings.class);

		Overlay o = Overlay.get();
		o.setCrosshairWidth(prefs.getInt(WIDTH, 250));
		o.setCrosshairHeight(prefs.getInt(HEIGHT, 250));
		o.setCrosshairImage(prefs.getInt(XHAIR, 1));
	}

	/**
	 * Retrieves the user specified settings from the hierarchial node for this
	 * class.
	 */
	private void retrieveSettingsFromHierarchialNode() {
		// Retrieve the user preference node for the this class
		Preferences prefs = Preferences.userNodeForPackage(getClass());

		widthAdjuster.setValue(prefs.getInt(WIDTH, 250));
		heightAdjuster.setValue(prefs.getInt(HEIGHT, 250));
		images.setSelectedIndex(prefs.getInt(XHAIR, 0));
	}

	/**
	 * Saves the user specified settings to the hierarchial node for this class.
	 */
	private void saveNewSettingsToHierarchialNode() {
		try {
			// Retrieve the user preference node for this class
			Preferences prefs = Preferences.userNodeForPackage(getClass());

			// Set the value of the preferences
			prefs.putInt(WIDTH, widthAdjuster.getValue());
			prefs.putInt(HEIGHT, heightAdjuster.getValue());
			prefs.putInt(XHAIR, images.getSelectedIndex());

			// Save the changes to registry(if windows, else idk where)
			prefs.flush();
		} catch (BackingStoreException ex) {
			ex.printStackTrace();
		}
	}
}
