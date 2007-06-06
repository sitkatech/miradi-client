/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanGoal extends TreeTableNode
{
	public StratPlanGoal(Project projectToUse, Goal goalToUse) throws Exception
	{
		project = projectToUse;
		if(goalToUse == null)
			EAM.logError("Attempted to create tree node for null goal");
		goal = goalToUse;
		rebuild();
		
	}
	
	public BaseObject getObject()
	{
		return goal;
	}
	
	public TreeTableNode getChild(int index)
	{
		return (TreeTableNode)objectiveVector.get(index);
	}

	public int getChildCount()
	{
		return objectiveVector.size();
	}
	
	public ORef getObjectReference()
	{
		return goal.getRef();
	}

	public BaseId getId()
	{
		return goal.getId();
	}

	public int getType()
	{
		return goal.getType();
	}

	public Object getValueAt(int column)
	{
		if(column == StrategicPlanTreeTableModel.labelColumn)
			return toString();
		return "";
	}

	public String toString()
	{
		return goal.toString();
	}

	public void rebuild() throws Exception
	{
		objectiveVector = getObjectiveNodes(goal);
	}

	static public Vector getObjectiveNodes(Goal goal) throws Exception
	{
		Project project = goal.getProject();
		Vector objectiveVector = new Vector();
		FactorSet relatedNodes = new ChainManager(project).findAllFactorsRelatedToThisGoal(goal.getId());
		Iterator iter = relatedNodes.iterator();
		while(iter.hasNext())
		{
			Factor node = (Factor)iter.next();
			IdList objectiveIds = node.getObjectives();
			for(int i = 0; i < objectiveIds.size(); ++i)
			{
				if(objectiveIds.get(i).isInvalid())
					continue;
				Objective objective = (Objective)project.findObject(ObjectType.OBJECTIVE, objectiveIds.get(i));
				objectiveVector.add(new StratPlanObjective(project, objective));
			}
		}
		Collections.sort(objectiveVector, new IgnoreCaseStringComparator());
		return 	objectiveVector;
	}

	Project project;
	Goal goal;
	Vector objectiveVector;
}
