package com.homedepot.hr.hr.staffingforms.util;

import com.homedepot.ta.aa.dao.builder.DataConverter;
import com.homedepot.ta.aa.dao.exceptions.QueryException;


//This class is used as part of DAO 2.0 Annotation @DAOConverter(DB2Trimmer.class) to remove any trailing spaces from Strings.
//MTS1876 06/14/2016

public class DB2Trimmer implements DataConverter {

	@Override
	public Boolean compare(Class<?> srcClass, Class<?> destinationClass) {
		{
			if (srcClass.equals(String.class)
					&& destinationClass.equals(String.class)) {
				return true;
			}
			return false;
		}
	}

	@Override
	public Object convert(Object source, Class<?> srcClass,
			Class<?> destinationClass) throws QueryException {
		if (source != null) {
			return source.toString().trim();
		}
		return source;
	}

}
