package org.openinsula.arena.gwt.client.xml;

import com.google.gwt.xml.client.Node;

/**
 * @author Lucas K Mogari
 */
public interface NodeParser<T> {

	public T parse(Node node);

}
