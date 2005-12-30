/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.IdAssigner;

public class NodeAnnotation 
{
	public NodeAnnotation()
	{
		id = IdAssigner.INVALID_ID;
		annotation = ANNOTATION_NONE_STRING;
	}
	
	public NodeAnnotation(int idToUse, String annotationToUse)
	{
		id = idToUse;
		annotation = annotationToUse;
	}
	
	public int getId()
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

	int id;
	String annotation;
}
