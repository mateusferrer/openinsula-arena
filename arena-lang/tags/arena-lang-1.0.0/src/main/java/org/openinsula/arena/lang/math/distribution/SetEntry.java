/*
 *  (C) Copyright 2008 Insula Tecnologia da Informacao Ltda.
 * 
 *  This file is part of Arena-Lang.
 *
 *  Arena-Lang is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Arena-Lang is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Arena-Lang.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openinsula.arena.lang.math.distribution;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class SetEntry<T> extends Entry<T> {
	
	public SetEntry(final T... valuesArray) {
		super(valuesArray);
	}

	@Override
	protected Collection<T> prepareValues(final List<T> referenceValues) {
		return new LinkedHashSet<T>(referenceValues);
	}

}
