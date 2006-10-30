/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class ComparableNode implements Comparable, Comparator
{
	
	public ComparableNode(String lable)
	{
		index = -1;
		object= null;
	}
	
	public ComparableNode(int indexToUse, ConceptualModelNode conceptualModelNodeToUse)
	{
		index = indexToUse;
		object = conceptualModelNodeToUse;
	}

	public int compare(Object object1, Object object2)
	{
		int test1 = ((ComparableNode)object1).getIndex();
		int test2 = ((ComparableNode)object2).getIndex();
		if (test1==-1 || test2==-1) return -1;
		
		BaseId baseId1 = ((ComparableNode) object1).getNode().getId();
		BaseId baseId2 = ((ComparableNode) object2).getNode().getId();
		
		return baseId1.compareTo(baseId2);
	}

	public int compareTo(Object objectToUse)
	{
		int test1 = ((ComparableNode)objectToUse).getIndex();
		if (index==-1 || test1==-1) return -1;
		
		BaseId baseId1 = ((ComparableNode) objectToUse).getNode().getId();
		BaseId baseId2 = object.getId();
		return baseId1.compareTo(baseId2);
	}

	public boolean equals(Object objectToUse)
	{
		if (index==-1) return false;
		BaseId baseId1 = ((ConceptualModelNode) objectToUse).getId();
		BaseId baseId2 = object.getId();
		return baseId1.compareTo(baseId2)==0;
	}

	public Object getObject()
	{
		return object;
	}
	
	public String toString() 
	{
		if (object==null) 
			return "";
		return object.getLabel();
	}

	public int getIndex() {
		return index;
	}
	
	public ConceptualModelNode getNode() {
		return object;
	}
	
	int index;
	ConceptualModelNode object;

}
