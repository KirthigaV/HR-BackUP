/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: DeptResponse.java
 * Application: RetailStaffingRequest
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.DeptDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Department Drop Downs
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("DepartmentList")
public class DeptResponse implements Serializable {

	private static final long serialVersionUID = 4624285057235302936L;
	
	@XStreamImplicit
	@XStreamAlias("DepartmentDetail")
	private List<DeptDetailsTO> deptList;

	public void setDeptList(List<DeptDetailsTO> deptList) {
		this.deptList = deptList;
	}

	public List<DeptDetailsTO> getDeptList() {
		return deptList;
	}

}
