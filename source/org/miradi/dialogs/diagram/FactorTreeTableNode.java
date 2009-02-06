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
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.dialogs.viability.nodes.ViabilityIndicatorNode;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;

public class FactorTreeTableNode extends TreeTableNode
{
	public FactorTreeTableNode(Project projectToUse, ORef factorRef) throws Exception
	{
		project = projectToUse;
		factor = (Factor) project.findObject(factorRef);
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return factor;
	}

	public TreeTableNode getChild(int index)
	{
		return indicators[index];
	}

	public int getChildCount()
	{
		return indicators.length;
	}

	public ORef getObjectReference()
	{
		return factor.getRef();
	}
	
	public int getType()
	{
		return factor.getType();
	}
	
	public boolean isAlwaysExpanded()
	{
		return true;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	@Override
	public String toRawString()
	{
		return "";
	}
	
	public BaseId getId()
	{
		return factor.getId();
	}
	public void rebuild() throws Exception
	{
		ORefList indicatorRefs = factor.getIndicatorRefs();
		indicators = new ViabilityIndicatorNode[indicatorRefs.size()];
		for (int i = 0; i < indicatorRefs.size(); ++i)
		{
			Indicator thisIndicator = (Indicator) project.findObject(indicatorRefs.get(i));
			indicators[i] = new ViabilityIndicatorNode(project, this, thisIndicator);
		}
		
		sortChildren(indicators);
	}
	
	private ViabilityIndicatorNode[] indicators;
	private Project project;
	private Factor factor;
}
