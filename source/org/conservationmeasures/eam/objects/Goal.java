/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.NodeAnnotation;


public class Goal extends NodeAnnotation 
{
	public Goal(BaseId id, String annotationToUse) 
	{
		super(id, annotationToUse);
	}
	
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Goal))
			return false;
		return ((Goal)obj).getAnnotation().equals(getAnnotation());
	}
}
