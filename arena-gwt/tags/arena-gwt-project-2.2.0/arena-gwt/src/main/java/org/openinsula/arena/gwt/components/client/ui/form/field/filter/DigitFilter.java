package org.openinsula.arena.gwt.components.client.ui.form.field.filter;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Lucas K Mogari
 */
public class DigitFilter extends CommonAllowedKeysKeyboardListener {

	@Override
	public boolean allow(Widget sender, char keyCode, int modifiers) {
		return Character.isDigit(keyCode) || super.allow(sender, keyCode, modifiers);
	}

}
