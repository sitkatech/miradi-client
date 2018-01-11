/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import java.util.Comparator;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Measurement;

abstract public class NodeSorter implements Comparator<TreeTableNode>
{
	public NodeSorter()
	{
	}
	
	public int compare(TreeTableNode nodeA, TreeTableNode nodeB)
	{
		int typeSortLocationA = getTypeSortLocation(getTypeName(nodeA));
		int typeSortLocationB = getTypeSortLocation(getTypeName(nodeB));
		int diff = typeSortLocationA - typeSortLocationB;
		if(diff != 0)
			return diff;

		ORef refA = nodeA.getObjectReference();
		ORef refB = nodeB.getObjectReference();
		if(refA.isValid() && refB.isInvalid())
			return -1;
		
		if(refA.isInvalid() && refB.isValid())
			return 1;

		if (!shouldSortChildren(refA) || !shouldSortChildren(refB))
			return compareTasks(nodeA, nodeB);
		
		return compareNodes(nodeA, nodeB);
	}

	public String getTypeName(TreeTableNode node)
	{
		return node.getObject().getTypeName();
	}

	public boolean shouldSortChildren(ORef childRef)
	{		
		return true;
	}
	
	private int compareTasks(TreeTableNode nodeA, TreeTableNode nodeB)
	{
		try
		{
			Integer indexOfA = getIndexOfChild(nodeA);
			Integer indexOfB = getIndexOfChild(nodeB);
			
			return indexOfA.compareTo(indexOfB);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog();
			return 0;
		}
	}
	
	private int getIndexOfChild(TreeTableNode childNode) throws Exception
	{
		TreeTableNode parentNode = childNode.getParentNode();
		for (int index = 0; index < parentNode.getChildCount(); ++index)
		{
			if (parentNode.getChild(index).equals(childNode))
				return index;
		}
		
		return -1;
	}

	private int compareNodes(TreeTableNode nodeA, TreeTableNode nodeB)
	{
		String labelA = nodeA.toString();
		String labelB = nodeB.toString();
		int comparisonResult = labelA.compareToIgnoreCase(labelB);
		if (shouldReverseSort(nodeA, nodeB))
			return -comparisonResult;
		
		return comparisonResult;
	}

	private boolean shouldReverseSort(TreeTableNode nodeA, TreeTableNode nodeB)
	{
		if (Measurement.is(nodeA.getType()) && Measurement.is(nodeB.getType()))
			return true;
		
		return false;
	}
			
	private int getTypeSortLocation(final String typeName)
	{
		final String[] sortOrder = getNodeSortOrder();
		for(int index = 0; index < sortOrder.length; ++index)
		{
			if(typeName.equals(sortOrder[index]))
			{
				return index;
			}
		}
		
		EAM.logError("NodeSorter unknown type: " + typeName);
		return sortOrder.length;
	}
	
	abstract protected String[] getNodeSortOrder();
}