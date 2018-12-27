package com.homedepot.hr.hr.retailstaffing.util;

import java.sql.Date;

import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.ta.aa.dao.builder.DataConverter;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

//This class is used as part of DAO 2.0 Annotation @DAOConverter(DAODateConverterToDateTO.class) takes a SQL Date and Converts to a DateTO.
//MTS1876 06/14/2016

public class DAODateConverterToDateTO implements DataConverter {

	@Override
	public Boolean compare(Class<?> srcClass, Class<?> destinationClass) {
		{
			if (srcClass.equals(Date.class)
					&& destinationClass.equals(DateTO.class)) {
				return true;
			} else if (srcClass.equals(DateTO.class)
					&& destinationClass.equals(DateTO.class)) {
				return true;
			}
			return false;
		}
	}

	@Override
	public Object convert(Object source, Class<?> srcClass,
			Class<?> destinationClass) throws QueryException {

		DateTO theDate = null;

		if (source instanceof DateTO && destinationClass.equals(DateTO.class)) {
			theDate = (DateTO) source;
		} else if (source instanceof Date && destinationClass.equals(DateTO.class)) {
			theDate = Util.converDatetoDateTO((Date) source);
		}

		return theDate;
	}

}
