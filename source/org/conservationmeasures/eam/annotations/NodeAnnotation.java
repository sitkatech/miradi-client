/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;

public class NodeAnnotation 
{
	public NodeAnnotation()
	{
		id = new BaseId();
		annotation = ANNOTATION_NONE_STRING;
	}
	
	public NodeAnnotation(BaseId idToUse, String annotationToUse)
	{
		id = idToUse;
		annotation = annotationToUse;
	}
	
	public BaseId getId()
	{
		return id;
	}

	public boolean hasAnnotation()
	{
		return annotation.length() > 0 && !annotation.equals(ANNOTATION_NONE_STRING);
	}

	public String toString()
	{
		return String.valueOf(annotation).toString();
	}
	
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof NodeAnnotation))
			return false;
		return ((NodeAnnotation)obj).annotation.equals(annotation);
	}
	
	public String getAnnotation()
	{
		return annotation;
	}
	
	public void setAnnotation(String value)
	{
		annotation = value;
	}
	
	public static final String ANNOTATION_NONE_STRING = EAM.text("None");

	BaseId id;
	String annotation;
}
