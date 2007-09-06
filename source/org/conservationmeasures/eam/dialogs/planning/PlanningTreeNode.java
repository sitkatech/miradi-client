/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ORefListWithoutDuplicates;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.TreeTableNode;

public class PlanningTreeNode extends TreeTableNode
{
	public PlanningTreeNode(Project projectToUse, ORef nodeRefToUse)
	{
		project = projectToUse;
		nodeRef = nodeRefToUse;
		rebuild();
	}
	
	public TreeTableNode getChild(int index)
	{
		return subNodes[index];
	}

	public int getChildCount()
	{
		return subNodes.length;
	}

	public BaseObject getObject()
	{
		return nodeObject;
	}

	public ORef getObjectReference()
	{
		return nodeObject.getRef();
	}

	public int getType()
	{
		return getObjectReference().getObjectType();
	}
	
	public boolean isAlwaysExpanded()
	{
		if (getType() == ProjectMetadata.getObjectType())
			return true;
		
		return false;
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild()
	{
		nodeObject = project.findObject(nodeRef);
		ORefList subNodeObjectRefs = retrieveFilteredSubNodes(nodeObject);
		
		subNodes = new PlanningTreeNode[subNodeObjectRefs.size()];
		for (int i = 0; i < subNodeObjectRefs.size(); ++i)
		{
			subNodes[i] = new PlanningTreeNode(project, subNodeObjectRefs.get(i));
		}
	}

	public String toString()
	{
		return getNodeLabel();
	}
	
	private String getNodeLabel()
	{
		if (getType() == ResultsChainDiagram.getObjectType())
			return "RC: " + nodeObject.getLabel();
		
		if (getType() == ConceptualModelDiagram.getObjectType())
			return "Conceptual Model: " + nodeObject.getLabel();
		
		return nodeObject.getLabel();
	}

	public ORefList retrieveFilteredSubNodes(BaseObject object)
	{
		ORefListWithoutDuplicates filteredList = new ORefListWithoutDuplicates();
		
		ORefList rawList = retrieveSubNodes(object);
		for(int i = 0; i < rawList.size(); ++i)
		{
			ORef ref = rawList.get(i);
			if(shouldIncludeRef(ref))
			{
				filteredList.add(ref);
			}
			else
			{
				BaseObject child = project.findObject(ref);
				filteredList.addAll(retrieveFilteredSubNodes(child));
			}
		}
		
		return filteredList;
	}
	
	public ORefList retrieveSubNodes(BaseObject object)
	{
		switch (object.getType())
		{
			case ObjectType.PROJECT_METADATA :
				return getChildrenOfProject((ProjectMetadata)object);
	
			case ObjectType.CONCEPTUAL_MODEL_DIAGRAM :
				return getChildrenOfConceptualModel((ConceptualModelDiagram) object);
				
			case ObjectType.RESULTS_CHAIN_DIAGRAM :
				return getChildrenOfResultsChain((ResultsChainDiagram) object);
				
			case ObjectType.GOAL :
				return getChildrenOfGoal((Goal)object);
			
			case ObjectType.OBJECTIVE :
				return getChildrenOfObjective((Objective)object);
				
			case ObjectType.STRATEGY :
				return getChildrenOfStrategy((Strategy)object);
				
			case ObjectType.INDICATOR:
				return getChildrenOfIndicator((Indicator)object);
			
			case ObjectType.TASK :
				return getChildrenOfTask((Task)object);
			
			default :
				throw new RuntimeException("Don't know how to get subnodes for type: " + object.getType());
		}
	}

	private ORefList getChildrenOfProject(ProjectMetadata projectMetadata)
	{
		return projectMetadata.getAllDiagramObjectRefs();
	}
	
	private ORefList getChildrenOfConceptualModel(ConceptualModelDiagram diagram)
	{
		return diagram.getAllGoalRefs();
	}
	
	private ORefList getChildrenOfResultsChain(ResultsChainDiagram resultsChain)
	{
		return resultsChain.getAllGoalRefs();
	}

	private ORefList getChildrenOfGoal(Goal goal)
	{
		return goal.getObjectivesUpstreamOfGoal();
	}

	private ORefList getChildrenOfObjective(Objective objective)
	{
		ORefList relatedItems = new ORefList();
		relatedItems.addAll(objective.getRelatedStrategies());
		relatedItems.addAll(objective.getRelatedIndicators());
		return relatedItems;
	}

	private ORefList getChildrenOfStrategy(Strategy strategy)
	{
		return strategy.getActivities();
	}

	private ORefList getChildrenOfIndicator(Indicator indicator)
	{
		return indicator.getMethods();
	}

	private ORefList getChildrenOfTask(Task task)
	{
		return task.getSubtasks();
	}
	
	private boolean shouldIncludeRef(ORef ref)
	{

		try
		{
			BaseObject thisObject = project.findObject(ref);

			String tag = ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES;
			ViewData data = project.getCurrentViewData();
			CodeList hiddenTypes = new CodeList(data.getData(tag));
			return (!hiddenTypes.contains(thisObject.getTypeName()));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	Project project;
	TreeTableNode[] subNodes;
	ORef nodeRef;
	BaseObject nodeObject;
}
