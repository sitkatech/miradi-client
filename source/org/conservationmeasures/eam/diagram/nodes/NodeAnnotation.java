/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.main.EAM;

public class NodeAnnotation 
{
	public NodeAnnotation()
	{
		annotation = "";
	}
	
	public NodeAnnotation(String annotationToUse)
	{
		annotation = annotationToUse;
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
	String annotation;
}
