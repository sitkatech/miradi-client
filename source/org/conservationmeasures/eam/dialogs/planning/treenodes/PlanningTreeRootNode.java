/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.Vector;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
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

		ViewData viewData = project.getCurrentViewData();
		CodeList visibleRowCodes = RowManager.getVisibleRowCodes(viewData);

		boolean isConceptualModelVisible = visibleRowCodes.contains(ConceptualModelDiagram.OBJECT_NAME);
		boolean isResultsChainVisible = visibleRowCodes.contains(ResultsChainDiagram.OBJECT_NAME);
		
		boolean includeConceptualModelItems = isConceptualModelVisible || !isResultsChainVisible;
		boolean includeResultsChainItems = isResultsChainVisible || !isConceptualModelVisible;
		
		if(includeConceptualModelItems)
			addConceptualModel();
		if(includeResultsChainItems)
			addResultsChainDiagrams();
		
		pruneUnwantedLayers(visibleRowCodes);
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
		createAndAddChildren(resultsChainRefs, null);
	}

}
