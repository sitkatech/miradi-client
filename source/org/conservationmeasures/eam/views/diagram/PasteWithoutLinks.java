/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.TransferableMiradiList;

public class PasteWithoutLinks extends Paste 
{
	public void paste(TransferableMiradiList list, DiagramPaster diagramPaster) throws Exception 
	{
		diagramPaster.pasteMiradiDataFlavorWithoutLinks(list, getLocation());
	}
}
