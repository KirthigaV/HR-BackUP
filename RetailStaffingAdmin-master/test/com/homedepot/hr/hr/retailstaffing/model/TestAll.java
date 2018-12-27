package com.homedepot.hr.hr.retailstaffing.model;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all
 * of the tests within its package as well as within any subpackages of its
 * package.
 *
 * @generatedBy CodePro at 6/15/15 2:22 PM
 * @author 581324
 * @version $Revision: 1.0 $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	UserProfileManagerTest.class,
	RetailStaffingInterviewManagerTest.class,
	RetailStaffingRequisitionManagerTest.class,
	RetailStaffingITIManagerTest.class,
	PhoneScreenManagerTest.class,
	LocationManagerTest.class,
})
public class TestAll {

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 6/15/15 2:22 PM
	 */
	public static void main(String[] args) {
		JUnitCore.runClasses(new Class[] { TestAll.class });
	}
}
