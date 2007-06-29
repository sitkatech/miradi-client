/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class ResultsChainDiagramPanel extends DiagramPanel
{
	public ResultsChainDiagramPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, ObjectType.RESULTS_CHAIN_DIAGRAM);
	}

}
