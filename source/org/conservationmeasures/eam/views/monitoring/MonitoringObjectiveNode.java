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
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringObjectiveNode extends MonitoringNode
{
	public MonitoringObjectiveNode(Project projectToUse, Objective objectiveToUse)
	{
		project = projectToUse;
		objective = objectiveToUse;
		children = new Vector();
		
		HashSet indicatorIds = new HashSet();
		indicatorIds.addAll(getAlUpstreamIndicators(getNodesWithThisObjective(objective.getId())));
		
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
	}
	
	public EAMObject getObject()
	{
		return objective;
	}

	private HashSet getAlUpstreamIndicators(FactorSet nodes)
	{
		HashSet indicatorIds = new HashSet(); 
		Iterator objectiveNodesIterator = nodes.iterator();
		while(objectiveNodesIterator.hasNext())
		{
			Factor nodeWithObjective = (Factor)objectiveNodesIterator.next();
			for(int i = 0; i < nodeWithObjective.getIndicators().size(); ++i)
				indicatorIds.add(nodeWithObjective.getIndicators().get(i));
			FactorSet nodesInChain = project.getDiagramModel().getAllUpstreamNodes(nodeWithObjective);
			Iterator chainNodesIterator = nodesInChain.iterator();
			while(chainNodesIterator.hasNext())
			{
				Factor nodeInChain = (Factor)chainNodesIterator.next();
				for(int i = 0; i < nodeInChain.getIndicators().size(); ++i)
					indicatorIds.add(nodeInChain.getIndicators().get(i));
			}
		}
		indicatorIds.remove(new IndicatorId(BaseId.INVALID.asInt()));
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
	
	// FIXME: Use new ChainManager getOwner method instead
	private FactorSet getNodesWithThisObjective(BaseId objectiveId)
	{
		FactorSet result = new FactorSet();
		FactorId[] allNodeIds = project.getFactorPool().getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			Factor node = project.findNode(allNodeIds[i]);
			if(node.getObjectives().contains(objectiveId))
				result.attemptToAdd(node);
		}
		return result;
	}
	
	public void rebuild() throws Exception
	{
	}


	Project project;
	Objective objective;
	Vector children;
}
