package org.openinsula.arena.appengine.gwt.client.util;

import org.openinsula.arena.appengine.gwt.client.AbstractGwtTestCase;

public class StringUtilsGwtTestCase extends AbstractGwtTestCase {

	public void testHasText() {
		assertTrue(StringUtils.hasText("e"));
		assertFalse(StringUtils.hasText(""));
		assertFalse(StringUtils.hasText(" "));
		assertFalse(StringUtils.hasText(null));
	}

	public void testHasLength() {
		assertTrue(StringUtils.hasLength(" "));
		assertTrue(StringUtils.hasLength("e"));
		assertFalse(StringUtils.hasLength(""));
		assertFalse(StringUtils.hasLength(null));
	}

}
