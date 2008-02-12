/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.RowManager;

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
