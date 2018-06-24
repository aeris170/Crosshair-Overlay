package xhair;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JWindow;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

/**
 * The Class Overlay. Crosshair-Overlay itself.
 *
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public final class Overlay extends JWindow {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2940736930889920645L;

	/** The Constant VERSION. */
	public static final String VERSION = "1.0";
	
	/** The one and single instance of Overlay. */
	private static Overlay INSTANCE = null;

	/** The xhair. */
	private Crosshair xhair;

	/**
	 * Instantiates a new overlay. Private.
	 */
	private Overlay() {
		xhair = new Crosshair();
		super.setBackground(new Color(255, 255, 255, 0));
		super.setAlwaysOnTop(true);
		super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		super.setLocation(0, 0);
		super.setContentPane(xhair);
		super.setVisible(true);
		setTransparent(this);
	}

	/**
	 * Sets the crosshair width.
	 *
	 * @param crosshairWidth the new crosshair width
	 */
	public void setCrosshairWidth(final int crosshairWidth) {
		xhair.setCrosshairWidth(crosshairWidth);
		repaint();
	}

	/**
	 * Sets the crosshair height.
	 *
	 * @param crosshairHeight the new crosshair height
	 */
	public void setCrosshairHeight(final int crosshairHeight) {
		xhair.setCrosshairHeight(crosshairHeight);
		repaint();
	}

	/**
	 * Sets the crosshair image.
	 *
	 * @param index the new crosshair image
	 */
	public void setCrosshairImage(final int index) {
		try {
			xhair.setCrosshairImage(CrosshairImageBank.getImage(index));
			repaint();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Hide icon.
	 */
	public void hideIcon() {
		xhair.hideIcon();
		repaint();
	}

	/**
	 * Show icon.
	 */
	public void showIcon() {
		xhair.showIcon();
		repaint();
	}

	/**
	 * Accesses the one and only instance of the overlay.
	 *
	 * @return the overlay
	 */
	public static Overlay get() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			INSTANCE = new Overlay();
			return INSTANCE;
		}
	}

	/**
	 * Sets a window transparent.
	 *
	 * @param w the new transparent
	 */
	private static void setTransparent(Component w) {
		WinDef.HWND hwnd = new HWND();
		hwnd.setPointer(Native.getComponentPointer(w));
		int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
		wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
		User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
	}

	/**
	 * The Inner Class Crosshair.
	 */
	private class Crosshair extends JPanel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -314893765598426021L;

		/** boolean to indicate whether the crosshair should be shown or not. */
		private boolean show = true;

		/** The crosshair height. */
		private int crosshairWidth, crosshairHeight;
		
		/** The crosshair image. */
		private BufferedImage crosshairImage;

		/**
		 * Instantiates a new crosshair. Private.
		 */
		private Crosshair() {}

		/**
		 * Gets the crosshair width.
		 *
		 * @return the crosshair width
		 */
		public int getCrosshairWidth() {
			return crosshairWidth;
		}

		/**
		 * Gets the crosshair height.
		 *
		 * @return the crosshair height
		 */
		public int getCrosshairHeight() {
			return crosshairHeight;
		}

		/**
		 * Sets the crosshair width.
		 *
		 * @param crosshairWidth the new crosshair width
		 */
		public void setCrosshairWidth(final int crosshairWidth) {
			this.crosshairWidth = crosshairWidth;
		}

		/**
		 * Sets the crosshair height.
		 *
		 * @param crosshairHeight the new crosshair height
		 */
		public void setCrosshairHeight(final int crosshairHeight) {
			this.crosshairHeight = crosshairHeight;
		}

		/**
		 * Sets the crosshair image.
		 *
		 * @param crosshairImage the new crosshair image
		 */
		public void setCrosshairImage(final BufferedImage crosshairImage) {
			this.crosshairImage = crosshairImage;
		}

		/**
		 * Sets the flag to hide the crosshair's icon.
		 */
		public void hideIcon() {
			show = false;
		}

		/**
		 * Sets the flag to show the crosshair's icon.
		 */
		public void showIcon() {
			show = true;
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {
			if (show) {
				final int crosshaitStartDrawX = (getWidth() - getCrosshairWidth()) / 2;
				final int crosshaitStartDrawY = (getHeight() - getCrosshairHeight()) / 2;
				g.drawImage(crosshairImage, crosshaitStartDrawX, crosshaitStartDrawY, getCrosshairWidth(), getCrosshairHeight(), this);
			}
		}
	}
}
