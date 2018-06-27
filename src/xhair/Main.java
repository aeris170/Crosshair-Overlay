package xhair;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main Class.
 *
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.01
 * @since 1.0
 */
public class Main {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {} // swallow
			Settings.initializeSettings();
			createAndShowTray(Overlay.get());
		});
	}

	/**
	 * Creates and shows the tray icon and it's components.
	 *
	 * @param overlay the crosshair-overlay
	 */
	private static void createAndShowTray(Overlay overlay) {
		if (!SystemTray.isSupported()) {
			System.exit(-1);
		}
		final PopupMenu popup = new PopupMenu();
		TrayIcon trayIcon = null;
		BufferedImage bf = null;
		try {
			bf = ImageIO.read(Main.class.getResourceAsStream("/trayIcon.png"));
			trayIcon = new TrayIcon(bf, "Crosshair-Overlay " + Overlay.VERSION);
		} catch (IOException | IllegalArgumentException ex) {
			System.exit(-2);
		}
		final SystemTray tray = SystemTray.getSystemTray();

		final CheckboxMenuItem hideItem = new CheckboxMenuItem("Hide Icon");
		hideItem.addItemListener(e -> {
			if (hideItem.getState()) {
				Overlay.get().hideIcon();
			} else {
				Overlay.get().showIcon();
			}
		});
		
		final Image aboutImage = new ImageIcon(bf).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

		final MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener(e -> {
			try {
				JOptionPane.showMessageDialog(null, "Written to cheat in a game called TABG :)", "About", JOptionPane.OK_OPTION, new ImageIcon(aboutImage));
			} catch (HeadlessException ex) {
				ex.printStackTrace();
			}
		});

		final MenuItem settingsItem = new MenuItem("Settings");
		settingsItem.addActionListener(e -> {
			new Settings();
		});

		final MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> {
			for (TrayIcon ti : tray.getTrayIcons()) {
				tray.remove(ti);
			}
			System.exit(0);
		});

		popup.add(hideItem);
		popup.addSeparator();
		popup.add(aboutItem);
		popup.add(settingsItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
		trayIcon.setToolTip("Crosshair-Overlay " + Overlay.VERSION);
		trayIcon.setImageAutoSize(true);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
		trayIcon.displayMessage("Minimised", "Crosshair-Overlay is running in System Tray.", MessageType.INFO);
	}
}