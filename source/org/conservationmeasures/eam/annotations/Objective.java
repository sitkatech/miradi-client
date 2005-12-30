/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;



public class Objective extends NodeAnnotation
{
	public Objective(int id, String objective)
	{
		super(id, objective);
	}
	
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Objective))
			return false;
		return ((Objective)obj).getAnnotation().equals(getAnnotation());
	}
}
