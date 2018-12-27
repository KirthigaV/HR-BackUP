package com.homedepot.hr.hr.retailstaffing.util;

import com.homedepot.ta.aa.dao.builder.DataConverter;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

//This class is used as part of DAO 2.0 Annotation @DAOConverter(RemoveTrailingComma.class) for trimming ',' character provided it is the last character.
//MTS1876 06/14/2016

public class RemoveTrailingComma implements DataConverter {

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

		String cleanString = "";

		if (source instanceof String && destinationClass.equals(String.class)) {
			// The below logic is for trimming ',' character provided it is the last
			// character
			if (source != null && source.toString().length() > 0) {
				if((source.toString().trim().lastIndexOf(",") + 1) == source.toString().trim().length()) {
					cleanString = source.toString().substring(0, source.toString().lastIndexOf(","));
				} else {
					cleanString = source.toString();
				}
			}
		}
		return cleanString;
	}

}
