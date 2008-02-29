/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cellviews;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.miradi.diagram.renderers.ProjectScopeRenderer;

public class ProjectScopeView extends RectangleCellView 
{
	public ProjectScopeView(Object cell) 
	{
		super(cell);
		GraphConstants.setSizeable(getAttributes(), false);
		GraphConstants.setMoveable(getAttributes(), false);
	}

	public CellViewRenderer getRenderer()
	{
		return projectScopeRenderer;
	}

	private static ProjectScopeRenderer projectScopeRenderer = new ProjectScopeRenderer();
}
