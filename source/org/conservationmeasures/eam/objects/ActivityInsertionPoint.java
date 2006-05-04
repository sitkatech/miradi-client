/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.project.IdAssigner;

public class ActivityInsertionPoint
{
	public ActivityInsertionPoint()
	{
		interventionId = IdAssigner.INVALID_ID;
		index = -1;
	}
	
	public ActivityInsertionPoint(int interventionIdToAddTo, int childIndexToInsertAt)
	{
		interventionId = interventionIdToAddTo;
		index = childIndexToInsertAt;
	}
	
	public boolean isValid()
	{
		return (getInterventionId() != IdAssigner.INVALID_ID);	
	}
	
	public int getInterventionId()
	{
		return interventionId;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	int interventionId;
	int index;
}
