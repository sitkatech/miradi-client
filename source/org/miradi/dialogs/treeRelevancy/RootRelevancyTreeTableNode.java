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

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;

public class RootRelevancyTreeTableNode extends AbstractRelevancyNode
{
	public RootRelevancyTreeTableNode(Project project, Desire desireToUse) throws Exception
	{
		super(project);
		
		desire = desireToUse;
		strategyRefs = project.getStrategyPool().getNonDraftStrategyRefs();
		rebuild();
	}
	
	@Override
	public TreeTableNode getChild(int index)
	{
		return strategyChildren[index];
	}

	@Override
	public int getChildCount()
	{
		return strategyChildren.length;
	}

	@Override
	public BaseObject getObject()
	{
		return desire;
	}

	@Override
	public ORef getObjectReference()
	{
		return getObject().getRef();
	}
	
	@Override
	public int getType()
	{
		return getObjectReference().getObjectType();
	}
	
	@Override
	public Object getValueAt(int column)
	{
		return null;
	}

	@Override
	public void rebuild() throws Exception
	{
		strategyChildren = new StrategyRelevancyNode[strategyRefs.size()];
		for (int index = 0; index < strategyRefs.size(); ++index)
		{
			Strategy strategy = Strategy.find(getProject(), strategyRefs.get(index));
			StrategyRelevancyNode strategyChildNode = new StrategyRelevancyNode(getProject(), strategy);
			strategyChildren[index] = strategyChildNode;
		}
		
		sortChildren(strategyChildren);
	}

	private Desire desire;
	private ORefList strategyRefs;
	private StrategyRelevancyNode[] strategyChildren;
}
