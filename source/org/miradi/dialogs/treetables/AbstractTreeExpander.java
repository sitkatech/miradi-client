/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.treetables;

import java.util.Vector;

import javax.swing.tree.TreePath;

abstract public class AbstractTreeExpander
{
	public Vector<TreePath> getFullyExpandedTreePathList(TreePath pathToRoot) throws Exception
	{
		Vector<TreePath> fullyExpandedTreePathList = new Vector<TreePath>();
		recursivelyGetFullyExpandedTreePaths(fullyExpandedTreePathList, pathToRoot);
		
		return fullyExpandedTreePathList;
	}
	
	private void recursivelyGetFullyExpandedTreePaths(Vector<TreePath> fullyExpandedTreePathList, TreePath treePath)
	{
		fullyExpandedTreePathList.add(treePath);
		TreeTableNode node = (TreeTableNode) treePath.getLastPathComponent();
		for(int childIndex = 0; childIndex < node.getChildCount(); ++childIndex)
		{
			TreeTableNode childNode = node.getChild(childIndex);
			if (includeChildNode(childNode))
			{
				TreePath thisTreePath = treePath.pathByAddingChild(childNode);
				recursivelyGetFullyExpandedTreePaths(fullyExpandedTreePathList, thisTreePath);
			}
		}
	}

	abstract public boolean includeChildNode(TreeTableNode childNode);
}
