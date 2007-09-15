/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.reports.ChainManager;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringGoalNode extends MonitoringNode
{
	public MonitoringGoalNode(Project projectToUse, Goal goalToUse) throws Exception
	{
		project = projectToUse;
		goal = goalToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return goal;
	}

	public ORef getObjectReference()
	{
		return goal.getRef();
	}
	
	public int getType()
	{
		return goal.getType();
	}
	
	public String toString()
	{
		return goal.toString();
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
			return goal.getLabel();
		return "";
	}
	
	public void rebuild() throws Exception
	{
		children = new Vector();
		FactorSet relatedNodes = new ChainManager(project).findAllFactorsRelatedToThisGoal(goal.getId());
		children.addAll(createIndicatorNodes(project, relatedNodes));
		children.addAll(createObjectiveNodes(project, relatedNodes));
	}
	
	static public Vector createObjectiveNodes(Project project, FactorSet relatedNodesToUse) throws Exception
	{
		Vector result = new Vector();
		Iterator iter = relatedNodesToUse.iterator();
		while(iter.hasNext())
		{
			Factor node = (Factor)iter.next();
			IdList objectiveIds = node.getObjectives();
			for(int i = 0; i < objectiveIds.size(); ++i)
			{
				if(objectiveIds.get(i).isInvalid())
					continue;
				Objective objective = (Objective)project.findObject(ObjectType.OBJECTIVE, objectiveIds.get(i));
				result.add(new MonitoringObjectiveNode(project, objective));
			}
		}
		Collections.sort(result, new IgnoreCaseStringComparator());
		return result;
	}

	static public Vector createIndicatorNodes(Project project, FactorSet relatedNodesToUse)
	{
		Iterator iter = relatedNodesToUse.iterator();
		Vector result = new Vector();
		while (iter.hasNext())
		{
			Factor node = (Factor)iter.next();
			if(!node.isTarget())
				continue;
			
			IdList indicators =node.getDirectOrIndirectIndicators();
			for(int i = 0; i < indicators.size(); ++i)
			{
				Indicator indicator = project.getIndicatorPool().find(indicators.get(i));
				result.add(new MonitoringIndicatorNode(project, indicator));
			}
		}
		Collections.sort(result, new IgnoreCaseStringComparator());
		return result;
	}

	Goal goal;
	Project project;
	Vector children;
}
