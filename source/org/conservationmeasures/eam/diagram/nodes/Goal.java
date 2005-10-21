/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

public class Goal extends NodeAnnotation 
{
	public Goal() 
	{
		super();
	}

	public Goal(String annotationToUse) 
	{
		super(annotationToUse);
	}
	
	public boolean hasGoal()
	{
		return hasAnnotation();
	}
	
	public String getLabel()
	{
		return getAnnotation();
	}

	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Goal))
			return false;
		return ((Goal)obj).getAnnotation().equals(getAnnotation());
	}
}
