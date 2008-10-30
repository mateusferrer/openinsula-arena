package org.openinsula.arena.gwt.atom.client;

import org.openinsula.arena.gwt.http.client.xml.XmlRequestCallback;

import com.google.gwt.xml.client.Document;

/**
 * @author Lucas K Mogari
 * @author Eduardo Rebola
 */
public abstract class EntryRequestCallback<T extends BaseEntry<T>> extends XmlRequestCallback {

	private T entry;

	public EntryRequestCallback() {
	}

	public EntryRequestCallback(final T entry) {
		this.entry = entry;
	}

	protected abstract void doWithEntry(T entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openinsula.arena.gwt.client.rest.xml.XmlRequestCallback#doWithXml
	 * (com.google.gwt.xml.client.Document)
	 */
	@Override
	protected final void doWithXml(final Document document) {
		doWithEntry(entry.parse(document));
	}

	void setEntry(T entry) {
		this.entry = entry;
	}

}
