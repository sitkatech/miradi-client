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
		BaseId baseId1 = ((ComparableNode) object1).getNode().getId();
		BaseId baseId2 = ((ComparableNode) object2).getNode().getId();
		
		return baseId1.compareTo(baseId2);
	}

	public int compareTo(Object objectToUse)
	{
		BaseId baseId1 = ((ComparableNode) objectToUse).getNode().getId();
		BaseId baseId2 = ((ConceptualModelNode)object).getId();
		return baseId1.compareTo(baseId2);
	}

	public boolean equals(Object objectToUse)
	{
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
		return object.toString();
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
