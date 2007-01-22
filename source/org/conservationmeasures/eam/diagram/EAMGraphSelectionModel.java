/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphSelectionModel;

public class EAMGraphSelectionModel extends DefaultGraphSelectionModel
{
	public EAMGraphSelectionModel(JGraph graphToUse)
	{
		super(graphToUse);
	}

}