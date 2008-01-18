/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;


public class PasteWithoutLinks extends Paste 
{
	public void paste(DiagramPaster diagramPaster) throws Exception 
	{
		diagramPaster.pasteFactors(getLocation());
		diagramPaster.wrapExistingLinksForDiagramFactorsInAllDiagramObjects();
	}
}
