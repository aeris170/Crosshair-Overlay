package xhair;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Settings Class.
 *
 * @author Doga Oruc
 * @version 1.0
 * @since 1.0
 */
public class Settings extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4568652071397810155L;

	// Preference key names
	/** The Key Constant WIDTH. */
	@SuppressWarnings("hiding")
	private static final String WIDTH = "width";

	/** The Key Constant HEIGHT. */
	@SuppressWarnings("hiding")
	private static final String HEIGHT = "height";

	/** The Key Constant XHAIR. */
	private static final String XHAIR = "img";

	/** The Key Constant XHAIR_FILL_RED. */
	private static final String XHAIR_FILL_R = "xhair_fill_red";

	/** The Key Constant XHAIR_FILL_GREEN. */
	private static final String XHAIR_FILL_G = "xhair_fill_green";

	/** The Key Constant XHAIR_FILL_BLUE. */
	private static final String XHAIR_FILL_B = "xhair_fill_blue";

	/** The Key Constant XHAIR_FILL_ALPHA. */
	private static final String XHAIR_FILL_A = "xhair_fill_alpha";

	/** The Key Constant XHAIR_OUTLINE_RED. */
	private static final String XHAIR_OUTLINE_R = "xhair_outline_red";

	/** The Key Constant XHAIR_OUTLINE_GREEN. */
	private static final String XHAIR_OUTLINE_G = "xhair_outline_green";

	/** The Key Constant XHAIR_OUTLINE_BLUE. */
	private static final String XHAIR_OUTLINE_B = "xhair_outline_blue";

	/** The Key Constant XHAIR_OUTLINE_ALPHA. */
	private static final String XHAIR_OUTLINE_A = "xhair_outline_alpha";

	/** The settings pane. */
	private JTabbedPane settingsPane;

	/** The settings panel 2. */
	private JPanel settingsPanel1, settingsPanel2, settingsPanel3;

	/** The height adjuster. */
	private SpinSlider widthAdjuster, heightAdjuster;

	/** The outline chooser. */
	private JColorChooser fillChooser, outlineChooser;

	/** The load button. */
	private JButton saveButton, loadButton;

	/** The images. */
	private JComboBox<ImageIcon> images;

	/**
	 * Instantiates a new settings.
	 */
	public Settings() {
		super("Crosshair-Overlay " + Overlay.VERSION + " Settings");
		try {
			super.setIconImage(ImageIO.read(getClass().getResourceAsStream("/trayIcon.png")));
		} catch (final IOException ex) {
			ex.printStackTrace();
			System.exit(-2);
		}
		settingsPane = new JTabbedPane();

		settingsPanel1 = new JPanel();
		settingsPanel1.setLayout(new BorderLayout());

		settingsPanel2 = new JPanel();
		settingsPanel2.setLayout(new BorderLayout());

		settingsPanel3 = new JPanel();
		settingsPanel3.setLayout(new BorderLayout());

		final JPanel center1 = new JPanel();
		center1.setLayout(new BoxLayout(center1, BoxLayout.Y_AXIS));

		final JPanel center2 = new JPanel();
		center2.setLayout(new BoxLayout(center2, BoxLayout.Y_AXIS));

		final JPanel center3 = new JPanel();
		center3.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		widthAdjuster = new SpinSlider(SpinSlider.WIDTH_ADJUSTER);
		heightAdjuster = new SpinSlider(SpinSlider.HEIGHT_ADJUSTER);

		fillChooser = new JColorChooser();
		fillChooser.setPreviewPanel(new JPanel());
		outlineChooser = new JColorChooser();
		outlineChooser.setPreviewPanel(new JPanel());

		saveButton = new JButton("Save Current Crosshair Configuration");
		loadButton = new JButton("Load A Crosshair Configuration");

		images = new JComboBox<>(CrosshairImageBank.supplyAllImagesAsIcons());
		images.setMaximumRowCount(6);
		images.setRenderer(new CustomRenderer());

		widthAdjuster.setBorder(BorderFactory.createTitledBorder("Crosshair Width"));
		heightAdjuster.setBorder(BorderFactory.createTitledBorder("Crosshair Height"));

		fillChooser.setBorder(BorderFactory.createTitledBorder("Crosshair Fill Color"));
		fillChooser.setSize(50, 50);
		outlineChooser.setBorder(BorderFactory.createTitledBorder("Crosshair Outline Color"));

		images.setBorder(BorderFactory.createTitledBorder("Crosshair Image"));

		fillChooser.getSelectionModel().addChangeListener(e -> Overlay.get().setCrosshairFillColor(fillChooser.getColor()));

		outlineChooser.getSelectionModel().addChangeListener(e -> Overlay.get().setCrosshairOutlineColor(outlineChooser.getColor()));

		saveButton.addActionListener(e -> {
			JFileChooser fc = new JFileChooser();
			fc.removeChoosableFileFilter(fc.getFileFilter());
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Crosshair Overlay Crosshair Configuration (*.xhc)", "xhc"));
			fc.setSelectedFile(new File("myCrosshairConfig.xhc"));
			if (fc.showSaveDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				fc.setSelectedFile(selectedFile.toString().endsWith(".xhc") ? selectedFile : new File(selectedFile.toString() + ".xhc"));
				try (FileOutputStream file = new FileOutputStream(selectedFile.toString()); ObjectOutputStream out = new ObjectOutputStream(file)) {
					out.writeObject(supplyCurrentInstance());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "Error while saving crosshair!", "ERROR", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		loadButton.addActionListener(e -> {
			JFileChooser fc = new JFileChooser();
			fc.removeChoosableFileFilter(fc.getFileFilter());
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Crosshair Overlay Crosshair Configuration (*.xhc)", "xhc"));
			if (fc.showSaveDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				try (FileInputStream file = new FileInputStream(selectedFile.toString()); ObjectInputStream in = new ObjectInputStream(file)) {
					processInstanceData((CrosshairSerializationData) in.readObject());
				} catch (IOException | ClassNotFoundException ex) {
					JOptionPane.showMessageDialog(this, "Error while loading crosshair!", "ERROR", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		images.addActionListener(e -> Overlay.get().setCrosshairImage(images.getSelectedIndex() + 1));

		center1.add(widthAdjuster);
		center1.add(heightAdjuster);
		center1.add(images);

		center2.add(fillChooser);
		center2.add(outlineChooser);

		center3.add(saveButton, gbc);
		center3.add(Box.createVerticalStrut(40), gbc);
		center3.add(loadButton, gbc);

		settingsPanel1.add(center1, BorderLayout.CENTER);
		settingsPanel1.add(new SouthPanel(), BorderLayout.SOUTH);

		settingsPanel2.add(center2, BorderLayout.CENTER);
		settingsPanel2.add(new SouthPanel(), BorderLayout.SOUTH);

		settingsPanel3.add(center3, BorderLayout.CENTER);
		settingsPanel3.add(new SouthPanel(), BorderLayout.SOUTH);

		settingsPane.add(settingsPanel1, "Crosshair Image / Crosshair Image Size");
		settingsPane.add(settingsPanel2, "Crosshair Fill Color / Crosshair Outline Color");
		settingsPane.add(settingsPanel3, "Crosshair Save / Load");

		super.add(settingsPane);
		super.setResizable(false);
		super.pack();
		super.setLocationByPlatform(true);
		retrieveSettingsFromHierarchicalNode();
		super.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				retrieveSettingsFromHierarchicalNode();
				dispose();
			}
		});
		super.setVisible(true);
	}

	/**
	 * Initialise settings to accurately represent saved data.
	 */
	public static void initializeSettings() {
		// Retrieve the user preference node for the this class
		final Preferences prefs = Preferences.userNodeForPackage(Settings.class);

		final Overlay o = Overlay.get();
		o.setCrosshairWidth(prefs.getInt(Settings.WIDTH, 250));
		o.setCrosshairHeight(prefs.getInt(Settings.HEIGHT, 250));
		o.setCrosshairImage(prefs.getInt(Settings.XHAIR, 1));
		o.setCrosshairFillColor(new Color(prefs.getInt(Settings.XHAIR_FILL_R, 255), prefs.getInt(Settings.XHAIR_FILL_G, 255), prefs.getInt(Settings.XHAIR_FILL_B, 255),
		        prefs.getInt(Settings.XHAIR_FILL_A, 255)));
		o.setCrosshairOutlineColor(new Color(prefs.getInt(Settings.XHAIR_OUTLINE_R, 0), prefs.getInt(Settings.XHAIR_OUTLINE_G, 0),
		        prefs.getInt(Settings.XHAIR_OUTLINE_B, 0), prefs.getInt(Settings.XHAIR_OUTLINE_A, 255)));
	}

	/**
	 * Retrieves the user specified settings from the hierarchical node for this
	 * class.
	 */
	void retrieveSettingsFromHierarchicalNode() {
		// Retrieve the user preference node for the this class
		final Preferences prefs = Preferences.userNodeForPackage(getClass());

		widthAdjuster.setValue(prefs.getInt(Settings.WIDTH, 250));
		heightAdjuster.setValue(prefs.getInt(Settings.HEIGHT, 250));
		images.setSelectedIndex(prefs.getInt(Settings.XHAIR, 1) - 1);
		fillChooser.setColor(new Color(prefs.getInt(Settings.XHAIR_FILL_R, 255), prefs.getInt(Settings.XHAIR_FILL_G, 255), prefs.getInt(Settings.XHAIR_FILL_B, 255),
		        prefs.getInt(Settings.XHAIR_FILL_A, 255)));
		outlineChooser.setColor(new Color(prefs.getInt(Settings.XHAIR_OUTLINE_R, 0), prefs.getInt(Settings.XHAIR_OUTLINE_G, 0), prefs.getInt(Settings.XHAIR_OUTLINE_B, 0),
		        prefs.getInt(Settings.XHAIR_OUTLINE_A, 255)));
	}

	/**
	 * Saves the user specified settings to the hierarchical node for this class.
	 */
	void saveNewSettingsToHierarchicalNode() {
		try {
			// Retrieve the user preference node for this class
			final Preferences prefs = Preferences.userNodeForPackage(getClass());

			// Set the value of the preferences
			prefs.putInt(Settings.WIDTH, widthAdjuster.getValue());
			prefs.putInt(Settings.HEIGHT, heightAdjuster.getValue());
			prefs.putInt(Settings.XHAIR, images.getSelectedIndex() + 1);

			Color c = fillChooser.getColor();
			prefs.putInt(Settings.XHAIR_FILL_R, c.getRed());
			prefs.putInt(Settings.XHAIR_FILL_G, c.getGreen());
			prefs.putInt(Settings.XHAIR_FILL_B, c.getBlue());
			prefs.putInt(Settings.XHAIR_FILL_A, c.getAlpha());

			c = outlineChooser.getColor();
			prefs.putInt(Settings.XHAIR_OUTLINE_R, c.getRed());
			prefs.putInt(Settings.XHAIR_OUTLINE_G, c.getGreen());
			prefs.putInt(Settings.XHAIR_OUTLINE_B, c.getBlue());
			prefs.putInt(Settings.XHAIR_OUTLINE_A, c.getAlpha());

			// Save the changes to registry(if windows, else idk where)
			prefs.flush();
		} catch (final BackingStoreException ex) {
			ex.printStackTrace();
		}
	}

	private CrosshairSerializationData supplyCurrentInstance() {
		CrosshairSerializationData csd = new CrosshairSerializationData();
		csd.setWidth(widthAdjuster.getValue());
		csd.setHeight(heightAdjuster.getValue());
		csd.setImageIndex(images.getSelectedIndex());
		csd.setFillColor(fillChooser.getColor());
		csd.setOutlineColor(outlineChooser.getColor());
		return csd;
	}

	private void processInstanceData(CrosshairSerializationData csd) {
		widthAdjuster.setValue(csd.getWidth());
		heightAdjuster.setValue(csd.getHeight());
		images.setSelectedIndex(csd.getImageIndex());
		fillChooser.setColor(csd.getFillColor());
		outlineChooser.setColor(csd.getOutlineColor());
	}

	/**
	 * SouthPanel class is created because I needed to supply the "OK" and "Cancel"
	 * buttons more than once. In order to supply the same two buttons without
	 * obfuscating the code, this class is created.
	 */
	private class SouthPanel extends JPanel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 2933793441501685839L;

		private JButton applyButton, cancelButton;

		/**
		 * Instantiates a new south panel.
		 */
		public SouthPanel() {
			super();

			applyButton = new JButton("  OK  ");
			applyButton.addActionListener(e -> {
				saveNewSettingsToHierarchicalNode();
				dispose();
			});

			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(e -> {
				retrieveSettingsFromHierarchicalNode();
				dispose();
			});

			super.add(Box.createHorizontalStrut(25));
			super.add(applyButton);
			super.add(Box.createHorizontalStrut(25));
			super.add(cancelButton);
			super.add(Box.createHorizontalStrut(25));
		}
	}

}
