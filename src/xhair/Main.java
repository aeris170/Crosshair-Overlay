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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * Main Class.
 *
 * @author Doga Oruc
 * @version 1.1
 * @since 1.0
 */
public class Main {

	private static final String EXTRACT_DIRECTORY = "c:\\programdata\\CrosshairOverlay";
	private static final String REGISTRY_EXTENSION_PATH = ".xhc";
	private static final String REGISTRY_ICON_PATH = "crosshairconfig\\DefaultIcon";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			}
			Settings.initializeSettings();
			Main.createAndShowTray();
		});
		if (System.getProperty("os.name").startsWith("Windows")) {
			setCrosshairConfigFileIcon();
		}
	}

	/**
	 * Creates and shows the tray icon and it's components.
	 */
	private static void createAndShowTray() {
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
			ex.printStackTrace();
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
			} catch (final HeadlessException ex) {
				ex.printStackTrace();
			}
		});

		final MenuItem settingsItem = new MenuItem("Settings");
		settingsItem.addActionListener(e -> new Settings());

		final MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> {
			for (final TrayIcon ti : tray.getTrayIcons()) {
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
		} catch (final AWTException ex) {
			ex.printStackTrace();
			System.err.println("TrayIcon could not be added.");
		}
		trayIcon.displayMessage("Minimised", "Crosshair-Overlay is running in System Tray.", MessageType.INFO);
	}

	private static void setCrosshairConfigFileIcon() {
		File dir = new File(EXTRACT_DIRECTORY);
		dir.mkdir();
		try (final JarFile jar = new JarFile(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()))) {
			final Enumeration<JarEntry> enumEntries = jar.entries();
			final List<JarEntry> entryList = Collections.list(enumEntries);
			final Iterator<JarEntry> listIterator = entryList.iterator();
			while (listIterator.hasNext()) {
				final JarEntry file = listIterator.next();
				File f = new File(dir + File.separator, file.getName());
				if (file.getName().equals("fileicon.ico")) {
					try (final InputStream is = jar.getInputStream(file); final FileOutputStream fos = new FileOutputStream(f)) {
						while (is.available() > 0) {
							fos.write(is.read());
						}
					}
					break;
				}
			}
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
		Advapi32Util.registryCreateKey(WinReg.HKEY_CLASSES_ROOT, REGISTRY_EXTENSION_PATH);
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CLASSES_ROOT, REGISTRY_EXTENSION_PATH, "", "crosshairconfig");
		Advapi32Util.registryCreateKey(WinReg.HKEY_CLASSES_ROOT, REGISTRY_ICON_PATH);
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CLASSES_ROOT, REGISTRY_ICON_PATH, "", EXTRACT_DIRECTORY + File.separator + "fileicon.ico");
	}

}