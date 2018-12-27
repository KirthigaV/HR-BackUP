package com.homedepot.hr.hr.retailstaffing.util;

import java.sql.Date;

import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.ta.aa.dao.builder.DataConverter;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

//This class is used as part of DAO 2.0 Annotation @DAOConverter(DoNotConsiderFlagConverter.class) takes a SQL Date, if it does not equal 0001-01-01 then sets do not consider flag to N else set it to Y.
//MTS1876 06/14/2016

public class DoNotConsiderFlagConverter implements DataConverter {

	@Override
	public Boolean compare(Class<?> srcClass, Class<?> destinationClass) {
		{
			if (srcClass.equals(Date.class)
					&& destinationClass.equals(String.class)) {
				return true;
			} else if (srcClass.equals(String.class)
					&& destinationClass.equals(String.class)) {
				return true;
			}
			return false;
		}
	}

	@Override
	public Object convert(Object source, Class<?> srcClass,
			Class<?> destinationClass) throws QueryException {

		if (source instanceof Date && destinationClass.equals(String.class)) {
			if (source.toString().equals("0001-01-01")) {
				return "N";	
			} else {
				return "Y";
			}
		}
		return source;
	}

}
