/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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