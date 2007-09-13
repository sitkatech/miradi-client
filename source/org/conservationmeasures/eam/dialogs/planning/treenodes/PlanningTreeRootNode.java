package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.planning.RowManager;

public class PlanningTreeRootNode extends AbstractPlanningTreeNode
{
	public PlanningTreeRootNode(Project projectToUse) throws Exception
	{
		super(projectToUse);
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

	public boolean attemptToAdd(ORef refToAdd)
	{
		return false;
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
		
		ViewData viewData = project.getCurrentViewData();
		pruneUnwantedLayers(RowManager.getVisibleRowCodes(viewData));
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

	private void addGoals() throws Exception
	{
		ORefList goalRefs = project.getGoalPool().getORefList();
		for(int i = 0; i < goalRefs.size(); ++i)
		{
			ORef goalRef = goalRefs.get(i);
			if(!attemptToAddToChildren(goalRef))
				children.add(new PlanningTreeGoalNode(project, goalRef));
		}
		
	}
	
	private void addObjectives() throws Exception
	{
		ORefList objectiveRefs = project.getObjectivePool().getORefList();
		for(int i = 0; i < objectiveRefs.size(); ++i)
		{
			ORef objectiveRef = objectiveRefs.get(i);
			if(!attemptToAddToChildren(objectiveRef))
				children.add(new PlanningTreeObjectiveNode(project, objectiveRef));
		}
		
	}
	
	private void addStrategies() throws Exception
	{
		ORefList strategyRefs = project.getStrategyPool().getORefList();
		for(int i = 0; i < strategyRefs.size(); ++i)
		{
			ORef strategyRef = strategyRefs.get(i);
			Strategy strategy = (Strategy)project.findObject(strategyRef);
			if(strategy.isStatusDraft())
				continue;
			
			if(!attemptToAddToChildren(strategyRef))
				children.add(new PlanningTreeStrategyNode(project, strategyRef));
		}
	}
	
	private void addIndicators() throws Exception
	{
		ORefList indicatorRefs = project.getIndicatorPool().getORefList();
		for(int i = 0; i < indicatorRefs.size(); ++i)
		{
			ORef indicatorRef = indicatorRefs.get(i);
			if(!attemptToAddToChildren(indicatorRef))
				children.add(new PlanningTreeIndicatorNode(project, indicatorRef));
		}
		
	}
	
	private void addActivitiesAndMethods() throws Exception
	{
		ORefList taskRefs = project.getTaskPool().getORefList();
		for(int i = 0; i < taskRefs.size(); ++i)
		{
			ORef taskRef = taskRefs.get(i);
			Task task = (Task)project.findObject(taskRef);
			if(!task.isActivity() && !task.isMethod())
				continue;
			
			if(!attemptToAddToChildren(taskRef))
				children.add(new PlanningTreeTaskNode(project, taskRef));
		}
	}
	
}
