package org.openinsula.arena.gwt.json.client;

import junit.framework.TestCase;

public class JsonVOTest extends TestCase {

	public void testAlreadyCastedJson() {
		JsonVO mock = new JsonVO() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getJsonPrefix() {
				return "mock";
			}
		};

		String json = "{\"mock\":{}}";
		assertSame(json, mock.castJson(json));
	}

	public void testCastJson() {
		JsonVO mock = new JsonVO() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getJsonPrefix() {
				return "mock";
			}
		};

		String json = "{\"mockField\":{}}";
		String expected = "{\"mock\":{\"mockField\":{}}}";
		String actual = mock.castJson(json);

		assertEquals(expected, actual);
		assertSame(actual, mock.castJson(actual));
	}

	public void testRemoveNotCastedJson() {
		JsonVO mock = new JsonVO() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getJsonPrefix() {
				return "mock";
			}
		};

		String json = "{\"mockField\":{}}";
		assertSame(json, mock.removeJsonCast(json));
	}

	public void testRemoveCastedJson() {
		JsonVO mock = new JsonVO() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getJsonPrefix() {
				return "mock";
			}
		};

		String json = "{\"mock\":{\"mockField\":{}}}";
		String expected = "{\"mockField\":{}}";
		assertEquals(expected, mock.removeJsonCast(json));
	}

}