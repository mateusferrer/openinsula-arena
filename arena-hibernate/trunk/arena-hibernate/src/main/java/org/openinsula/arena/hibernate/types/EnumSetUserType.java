package org.openinsula.arena.hibernate.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.EnumSet;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

public class EnumSetUserType<E extends Enum<E>> implements UserType, ParameterizedType {
	protected final Log logger = LogFactory.getLog(getClass());

	private Class<E> klazz;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setParameterValues(final Properties params) {
		String className = params.getProperty("enumClass");
		
		if (className == null) {
			throw new MappingException("enumClass parameter not specified");
		}
		
		try {
			klazz = (Class<E>) Class.forName(className);
		} catch (ClassNotFoundException ex) {
            throw new MappingException("enumClass " + className + " not found.", ex);
		} catch (ClassCastException ex) {
            throw new MappingException("enumClass " + className + " is not a Java 5 Enum.", ex);
		}
		
		if (klazz.getEnumConstants().length > 63) {
			throw new MappingException("enumClass" + className + " exceeded the maximum of 63 enums.");
		}
	}
	
	@Override
	public int[] sqlTypes() {
		return new int[] { Types.BIGINT };
	}
	
	@Override
	public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException, SQLException {
    	long value = rs.getLong(names[0]);
    	
    	if (!rs.wasNull()) {
    		E[] enumConstants = klazz.getEnumConstants();
    		EnumSet<E> enumSet = EnumSet.noneOf(klazz);
    		
    		for (int i = 0; i < enumConstants.length; i++) {
    			if ((value & ((long)Math.pow(2, i))) != 0) {
    				enumSet.add(enumConstants[i]);
    			}
    		}
    		
    		return enumSet;
    	}
    	
		return null;
	}

    @SuppressWarnings("unchecked")
    @Override
	public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException, SQLException {
    	if (value == null) {
    		st.setNull(index, Types.INTEGER);
    	} else {
    		EnumSet<E> enumSet = (EnumSet<E>) value;

    		long result = 0L;
    		
    		for (Enum e: enumSet) {
    			result = result | (long)Math.pow(2, e.ordinal());
    		}
    		
    		st.setLong(index, result);
    	}
	}

    @Override
	public Class<?> returnedClass() {
		return EnumSet.class;
	}

    @SuppressWarnings("unchecked")
    @Override
	public Object deepCopy(final Object value) throws HibernateException {
        return ((EnumSet<E>)value).clone();
    }

    @Override
	public boolean isMutable() {
		return true;
	}

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return original;
    }

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object x, final Object y) throws HibernateException {
		if (x == null && y == null) {
			return true;
		} else if (x != null && y != null) {
			EnumSet<E> enumSetX = (EnumSet<E>)x;
			EnumSet<E> enumSetY = (EnumSet<E>)y;
			
			if (enumSetX.size() != enumSetY.size()) {
				return false;
			} else {
				for (Enum e: enumSetX) {
					if (!enumSetY.contains(e)) {
						return false;
					}
				}
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int hashCode(final Object x) throws HibernateException {
		return x.hashCode();
	}
}