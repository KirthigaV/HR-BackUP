package com.homedepot.hr.hr.staffingforms.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: SlotCounter.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

/**
 * This class represents a series of "counter" values for a given begin time and calendar id
 * (record in the HR_REQN_SCH table)
 */
public class SlotCounter implements Serializable
{
    private static final long serialVersionUID = -1824839585403911830L;
    
	/** the total number of slots for a given begin time and calendar id */
	private int mSlotCount;
	/** the current maximum sequence number for a given begin time and calendar id */
	private short mMaxSeq;
	
	/**
	 * @param slotCount total number of slots for a given begin time and calendar id
	 * @param maxSeq current maximum sequence number for a given begin time and calendar id
	 */
	public SlotCounter(int slotCount, short maxSeq)
	{
		mSlotCount = slotCount;
		mMaxSeq = maxSeq;
	} // end constructor
	
	/**
	 * @return the total number of slots for a given begin time and calendar id
	 */
	public int getSlotCount()
	{
		return mSlotCount;
	} // end function getSlotCount()
	
	/**
	 * @param slotCount the total number of slots for a given begin time and calendar id
	 */
	public void setSlotCount(int slotCount)
	{
		mSlotCount = slotCount;
	} // end function setSlotCount()
	
	/**
	 * Convenience method to increment the slot count by 1
	 */
	public void incrementSlotCount()
	{
		mSlotCount++;
	} // end function incrementSlotCount()
	
	/**
	 * @return the current maximum sequence number for a given begin time and calendar id
	 */
	public short getMaxSeq()
	{
		return mMaxSeq;
	} // end function getMaxSeq()
	
	/**
	 * @param maxSeq the current maximum sequence number for a given begin time and calendar id
	 */
	public void setMaxSeq(short maxSeq)
	{
		mMaxSeq = maxSeq;
	} // end function setMaxSeq()

	/**
	 * convenience method to get the next sequence number
	 * 
	 * @return the next sequence number
	 */
	public short getNextSeq()
	{
		return ++mMaxSeq; // max seq + 1
	} // end function getNextSeq()
} // end class SlotCounter