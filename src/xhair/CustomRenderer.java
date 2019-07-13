package xhair;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.UIManager;

/**
 * The Class CustomRenderer. Used to decorate Crosshair Selection ComboBox.
 *
 * @author Doga Oruc
 * @version 1.0
 * @since 1.0
 */
public class CustomRenderer extends DefaultListCellRenderer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1395980151667453312L;

	/** The background color of n'th elements where n % 2 == 0. */
	private Color currentBackground = new Color(0, 100, 255, 15);

	/** The default background. */
	private Color defaultBackground = (Color) UIManager.get("List.background");

	/* (non-Javadoc)
	 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.
	 * swing.JList, java.lang.Object, int, boolean, boolean) */
	@Override
	public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		try {
			if (index != -1) {
				setText("Crosshair " + index);
			} else {
				setText("Selected Crosshair");
			}
			setIcon(new ImageIcon(((ImageIcon) value).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
			if (!isSelected) {
				setBackground(index % 2 == 0 ? currentBackground : defaultBackground);
			}
		} catch (final IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		return this;
	}
}
