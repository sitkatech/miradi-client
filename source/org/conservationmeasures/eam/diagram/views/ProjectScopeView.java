/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.views;

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
