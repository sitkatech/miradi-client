/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import org.conservationmeasures.eam.diagram.renderers.ProjectScopeRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;

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
		return new ProjectScopeRenderer();
	}

}
