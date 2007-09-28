package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
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
		
		ViewData viewData = project.getCurrentViewData();
		pruneUnwantedLayers(RowManager.getVisibleRowCodes(viewData));
	}
	
	public boolean isAlwaysExpanded()
	{
		return true;
	}

	private void addConceptualModel() throws Exception
	{
		children.add(new PlanningTreeConceptualModelNode(project));
	}

	private void addResultsChainDiagrams() throws Exception
	{
		ORefList resultsChainRefs = project.getResultsChainDiagramPool().getORefList();
		for(int i = 0; i < resultsChainRefs.size(); ++i)
			children.add(new PlanningTreeResultsChainNode(project, resultsChainRefs.get(i)));
	}

}
