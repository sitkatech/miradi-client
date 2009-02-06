/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogs.treeRelevancy;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class StrategyRelevancyNode extends AbstractRelevancyNode
{
	public StrategyRelevancyNode(Project project, Strategy strategyToUse) throws Exception
	{
		super(project);
		
		strategy = strategyToUse;
		rebuild();
	}
	
	@Override
	public TreeTableNode getChild(int index)
	{
		return activityChildren.get(index);
	}

	@Override
	public int getChildCount()
	{
		return activityChildren.size();
	}

	@Override
	public BaseObject getObject()
	{
		return strategy;
	}

	@Override
	public ORef getObjectReference()
	{
		return strategy.getRef();
	}

	@Override
	public Object getValueAt(int column)
	{
		return null;
	}

	@Override
	public void rebuild() throws Exception
	{
		activityChildren = new Vector();
		ORefList activityRefs = strategy.getActivityRefs();
		for (int index = 0; index < activityRefs.size(); ++index)
		{
			Task task = Task.find(getProject(), activityRefs.get(index));
			ActivityRelevancyNode activityChildNode = new ActivityRelevancyNode(getProject(), task);
			activityChildren.add(activityChildNode);
		}
	}

	@Override
	public String toRawString()
	{
		return strategy.toString();
	}

	private Strategy strategy;
	private Vector<ActivityRelevancyNode> activityChildren;
}
