/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.project.FactorCommandHelper;

public class PasteWithoutLinks extends Paste 
{
	public void pasteCellsIntoProject(TransferableEamList list, FactorCommandHelper factorCommandHelper) throws Exception 
	{
		factorCommandHelper.pasteFactorsOnlyIntoProject(list, getLocation());
	}
}
