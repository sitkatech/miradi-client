/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeRootNode extends AbstractPlanningTreeNode
{
	public PlanningTreeRootNode(Project projectToUse, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
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

		boolean isConceptualModelVisible = visibleRows.contains(ConceptualModelDiagram.OBJECT_NAME);
		boolean isResultsChainVisible = visibleRows.contains(ResultsChainDiagram.OBJECT_NAME);
		
		boolean includeConceptualModelItems = isConceptualModelVisible || !isResultsChainVisible;
		boolean includeResultsChainItems = isResultsChainVisible || !isConceptualModelVisible;
		
		if(includeConceptualModelItems)
			addConceptualModel();
		if(includeResultsChainItems)
			addResultsChainDiagrams();
		
		pruneUnwantedLayers(visibleRows);
	}
	
	public boolean isAlwaysExpanded()
	{
		return true;
	}

	private void addConceptualModel() throws Exception
	{
		children.add(new PlanningTreeConceptualModelNode(project, visibleRows));
	}

	private void addResultsChainDiagrams() throws Exception
	{
		ORefList resultsChainRefs = project.getResultsChainDiagramPool().getORefList();
		createAndAddChildren(resultsChainRefs, null);
	}

}
