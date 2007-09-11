package org.conservationmeasures.eam.dialogs.planning;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class PlanningTreeRootNode extends TreeTableNode
{
	public PlanningTreeRootNode(Project projectToUse) throws Exception
	{
		project = projectToUse;
		rebuild();
	}
	
	public TreeTableNode getChild(int index)
	{
		return children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public BaseObject getObject()
	{
		return null;
	}

	public ORef getObjectReference()
	{
		return ORef.INVALID;
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public String toString()
	{
		return null;
	}

	public void rebuild() throws Exception
	{
		children = new Vector();
		addConceptualModel();
		addResultsChainDiagrams();
		addGoals();
		addObjectives();
		addStrategies();
		addIndicators();
		addActivitiesAndMethods();
	}

	private void addConceptualModel()
	{
		children.add(new PlanningTreeConceptualModelNode(project));
	}

	private void addResultsChainDiagrams()
	{
		ORefList resultsChainRefs = project.getResultsChainDiagramPool().getORefList();
		for(int i = 0; i < resultsChainRefs.size(); ++i)
			children.add(new PlanningTreeResultsChainNode(project, resultsChainRefs.get(i)));
	}

	private void addGoals()
	{
		ORefList goalRefs = project.getGoalPool().getORefList();
		for(int i = 0; i < goalRefs.size(); ++i)
		{
			ORef goalRef = goalRefs.get(i);
			if(!attemptToAddObjectToChildren(goalRef))
				children.add(new PlanningTreeGoalNode(project, goalRef));
		}
		
	}
	
	private void addObjectives()
	{
		ORefList objectiveRefs = project.getObjectivePool().getORefList();
		for(int i = 0; i < objectiveRefs.size(); ++i)
		{
			ORef objectiveRef = objectiveRefs.get(i);
			if(!attemptToAddObjectToChildren(objectiveRef))
				children.add(new PlanningTreeObjectiveNode(project, objectiveRef));
		}
		
	}
	
	private void addStrategies()
	{
		ORefList strategyRefs = project.getStrategyPool().getORefList();
		for(int i = 0; i < strategyRefs.size(); ++i)
		{
			ORef strategyRef = strategyRefs.get(i);
			Strategy strategy = (Strategy)project.findObject(strategyRef);
			if(strategy.isStatusDraft())
				continue;
			
			if(!attemptToAddObjectToChildren(strategyRef))
				children.add(new PlanningTreeStrategyNode(project, strategyRef));
		}
	}
	
	private void addIndicators()
	{
		ORefList indicatorRefs = project.getIndicatorPool().getORefList();
		for(int i = 0; i < indicatorRefs.size(); ++i)
		{
			ORef indicatorRef = indicatorRefs.get(i);
			if(!attemptToAddObjectToChildren(indicatorRef))
				children.add(new PlanningTreeIndicatorNode(project, indicatorRef));
		}
		
	}
	
	private void addActivitiesAndMethods()
	{
		ORefList taskRefs = project.getTaskPool().getORefList();
		for(int i = 0; i < taskRefs.size(); ++i)
		{
			ORef taskRef = taskRefs.get(i);
			Task task = (Task)project.findObject(taskRef);
			if(!task.isActivity() && !task.isMethod())
				continue;
			
			if(!attemptToAddObjectToChildren(taskRef))
				children.add(new PlanningTreeTaskNode(project, taskRef));
		}
	}
	
	private boolean attemptToAddObjectToChildren(ORef refToAdd)
	{
		boolean wasAdded = false;
		for(int i = 0; i < children.size(); ++i)
		{
			if(children.get(i).attemptToAdd(refToAdd))
				wasAdded = true;
		}
		
		return wasAdded;
	}

	Project project;
	Vector<AbstractPlanningTreeNode> children;
}
