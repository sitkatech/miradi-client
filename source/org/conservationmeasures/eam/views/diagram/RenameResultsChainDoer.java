/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

public class RenameResultsChainDoer extends RenameDiagramObjectDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		return getDiagramView().isResultsChainTab();
	}	
}
