/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectChainObject;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringObjectiveNode extends MonitoringNode
{
	public MonitoringObjectiveNode(Project projectToUse, Objective objectiveToUse) throws Exception
	{
		project = projectToUse;
		objective = objectiveToUse;
		children = new Vector();
		
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return objective;
	}

	public static HashSet getAllUpstreamIndicators(Factor factor)
	{
		HashSet indicatorIds = getFactorIndicatorIds(factor);
		
		ProjectChainObject chainObject = factor.getProjectChainBuilder();
		FactorSet chain = chainObject.buildUpstreamChainAndGetFactors(factor);
		Iterator chainNodesIterator = chain.iterator();
		while(chainNodesIterator.hasNext())
		{
			Factor chainFactor = (Factor)chainNodesIterator.next();
			indicatorIds.addAll(getFactorIndicatorIds(chainFactor));
		}
		indicatorIds.remove(new IndicatorId(BaseId.INVALID.asInt()));
		return indicatorIds;
	}

	static public HashSet getFactorIndicatorIds(Factor factor)
	{
		IdList ids = factor.getDirectOrIndirectIndicators();
		HashSet indicatorIds = new HashSet(); 
		for(int i = 0; i < ids.size(); ++i)
			indicatorIds.add(ids.get(i));
		return indicatorIds;
	}

	public ORef getObjectReference()
	{
		return objective.getRef();
	}
	
	public int getType()
	{
		return objective.getType();
	}
	
	public String toString()
	{
		return objective.toString();
	}
	
	public int getChildCount()
	{
		return children.size();
	}

	public TreeTableNode getChild(int index)
	{
		return (MonitoringNode)children.get(index);
	}

	public Object getValueAt(int column)
	{
		if(column == COLUMN_ITEM_LABEL)
			return objective.getLabel();
		return "";
	}

	public void rebuild() throws Exception
	{
		children = getAllUpstreamIndicatorsForObjectiveOwner(objective);
	}

	static public Vector getAllUpstreamIndicatorsForObjectiveOwner(Objective objective)
	{
		Vector children = new Vector();
		Project project = objective.getProject();
		Factor owner = (Factor)objective.getOwner();
		HashSet indicatorIds = getAllUpstreamIndicators(owner);
		
		Iterator iter = indicatorIds.iterator();
		while(iter.hasNext())
		{
			BaseId id = (BaseId)iter.next();
			Indicator indicator = (Indicator)project.findObject(ObjectType.INDICATOR, id);
			if(indicator == null)
				throw new RuntimeException("Missing Indicator " + id);
			children.add(new MonitoringIndicatorNode(project, indicator));
		}
		Collections.sort(children, new IgnoreCaseStringComparator());
		return children;
	}


	Project project;
	Objective objective;
	Vector children;
}
