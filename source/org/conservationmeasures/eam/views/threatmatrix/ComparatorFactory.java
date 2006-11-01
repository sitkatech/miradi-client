/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

public interface ComparatorFactory
{
	public Comparable createComparable(int rowIndex, Object object);
	public int getOldRow();
}
