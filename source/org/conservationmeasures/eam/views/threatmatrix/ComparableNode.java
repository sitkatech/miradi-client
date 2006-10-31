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
	
	public ComparableNode(int oldRowToUse, String label)
	{
		index = -1;
		oldRow=oldRowToUse;
		object= label;
	}
	
	public ComparableNode(int indexToUse, ConceptualModelNode conceptualModelNodeToUse)
	{
		index = indexToUse;
		object = conceptualModelNodeToUse;
	}

	public int compare(Object object1, Object object2)
	{
		int test1 = ((ComparableNode)object1).getOldRow();
		int test2 = ((ComparableNode)object2).getOldRow();
		if (test1==-1 || test2==-1) 
			object1.toString().compareTo(object2.toString());
	
		BaseId baseId1 = ((ComparableNode) object1).getNode().getId();
		BaseId baseId2 = ((ComparableNode) object2).getNode().getId();
		
		return baseId1.compareTo(baseId2);
	}

	public int compareTo(Object objectToUse)
	{
		if (objectToUse instanceof String || object instanceof String ) 
			return object.toString().compareToIgnoreCase(objectToUse.toString());
		
		
		BaseId baseId1 = ((ComparableNode) objectToUse).getNode().getId();
		BaseId baseId2 = ((ConceptualModelNode)object).getId();
		return baseId1.compareTo(baseId2);
	}

	public boolean equals(Object objectToUse)
	{
		if (index==-1) return false;
		BaseId baseId1 = ((ConceptualModelNode) objectToUse).getId();
		BaseId baseId2 = ((ConceptualModelNode)object).getId();
		return baseId1.compareTo(baseId2)==0;
	}

	public Object getObject()
	{
		return object;
	}
	
	public String toString() 
	{
		if (index == -1) 
			return object.toString();
		
		return ((ConceptualModelNode)object).getLabel();
	}

	public int getOldRow() {
		return oldRow;
	}
	
	public int getIndex() {
		return index;
	}
	
	
	public ConceptualModelNode getNode() {
		return (ConceptualModelNode)object;
	}
	
	int index;
	int oldRow;
	Object object;

}
