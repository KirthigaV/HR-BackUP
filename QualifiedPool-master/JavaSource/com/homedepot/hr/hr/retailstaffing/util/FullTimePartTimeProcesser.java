package com.homedepot.hr.hr.retailstaffing.util;

import com.homedepot.ta.aa.dao.builder.DataConverter;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class FullTimePartTimeProcesser implements DataConverter {
	@Override
	public Boolean compare(Class<?> srcClass, Class<?> destinationClass) {
		{
			if (srcClass.equals(String.class)
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

		String availIndictor = "";

		if (source instanceof String && destinationClass.equals(String.class) && source.toString().length() < 2) {
			availIndictor = source.toString();
		} else if (source instanceof String && destinationClass.equals(String.class)) {
			if (source.toString().toString().equals("YY")) {
				availIndictor = "B";
			} else if (source.toString().toString().equals("YN")) {
				availIndictor = "FT";
			} else {
				availIndictor = "PT";
			}
		}

		return availIndictor;
	}
}
