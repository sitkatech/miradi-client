/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.cellviews.CellViewFactory;
import org.jgraph.graph.GraphLayoutCache;

public class PartialGraphLayoutCache extends GraphLayoutCache
{
	public PartialGraphLayoutCache(DiagramModel diagramModel)
	{
		super(diagramModel, new CellViewFactory(), PARTIAL);
	}
	
	static final boolean PARTIAL = true;
}