/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

public class ThreatRatingCriterion
{
	public ThreatRatingCriterion(int idToUse, String labelToUse)
	{
		id = idToUse;
		label = labelToUse;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	int id;
	String label;
}
