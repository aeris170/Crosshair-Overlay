package xhair;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.UIManager;

/**
 * The Class CustomRenderer. Used to decorate Crosshair Selection ComboBox.
 *
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class CustomRenderer extends DefaultListCellRenderer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1395980151667453312L;

	/** The background color of n'th elements where n % 2 == 0. */
	private Color background = new Color(0, 100, 255, 15);

	/** The default background. */
	private Color defaultBackground = (Color) UIManager.get("List.background");


	/* (non-Javadoc)
	 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		try {
			if (index != -1) {
				this.setText("Crosshair " + index);
			} else {
				this.setText("Selected Crosshair");
			}
			this.setIcon((Icon) value);
			this.setSize(50, 50);
			if (!isSelected) {
				this.setBackground(index % 2 == 0 ? background : defaultBackground);
			}
		} catch (IllegalArgumentException ex) {} // swallow
		return this;
	}
}
