/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.dialogs.viability.ViabilityIndicatorNode;
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

	public String toString()
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
	}
	
	private ViabilityIndicatorNode[] indicators;
	private Project project;
	private Factor factor;
}
