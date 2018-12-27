package com.homedepot.hr.hr.retailstaffing.util;

import com.homedepot.ta.aa.dao.builder.DataConverter;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

//This class is used as part of DAO 2.0 Annotation @DAOConverter(InterviewDeclineReason.class) to take the Decline Reason Code code and return the  description.
//MTS1876 06/14/2016

public class InterviewDeclineReason implements DataConverter {

	@Override
	public Boolean compare(Class<?> srcClass, Class<?> destinationClass) {
		{
			if (srcClass.equals(Integer.class)
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

		String declineDesc = "";

		if (source instanceof String && destinationClass.equals(String.class)) {
			declineDesc = source.toString();
		} else if (source instanceof Integer && destinationClass.equals(String.class)) {
			switch ((Integer) source) {
			case 0:
				declineDesc = " ";
				break;
			case 1:
				declineDesc = "PAY";
				break;
			case 2:
				declineDesc = "BENEFITS";
				break;
			case 3:
				declineDesc = "HOURS";
				break;
			case 4:
				declineDesc = "ACCEPTED ANOTHER OFFER";
				break;
			case 5:
				declineDesc = "OTHER";
				break;
			case 6:
				declineDesc = "LOCATION";
				break;
			case 7:
				declineDesc = "REFUSED DRUG TEST";
				break;
			}
		}

		return declineDesc;
	}

}
