/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;


public class NodeAnnotations
{
	public NodeAnnotations()
	{
		annotations = new Vector();
	}

	public int size()
	{
		return annotations.size();
	}
	
	public boolean hasAnnotation()
	{
		return (size() != 0);
	}
	
	public NodeAnnotation getAnnotation(int i)
	{
		return (NodeAnnotation)annotations.get(i);
	}
	
	public void addAnnotation(NodeAnnotation annotation)
	{
		annotations.add(annotation);
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof NodeAnnotations))
			return false;
		if(!getClass().equals(obj.getClass()))
			return false;
		NodeAnnotations other = (NodeAnnotations)obj;
		return annotations.equals(other.annotations);
	}
	
	Vector annotations;
}
