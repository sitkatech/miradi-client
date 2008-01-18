/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.ORef;

public class DiagramCopyPaster extends DiagramPaster
{
	public DiagramCopyPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(diagramPanelToUse, modelToUse, transferableListToUse);
	}

	public void pasteFactors(Point startPoint) throws Exception
	{	
		dataHelper = new PointManipulater(startPoint, transferableList.getUpperMostLeftMostCorner());
		createNewFactors();	
		createNewDiagramFactors();
	}
	
	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{	
		pasteFactors(startPoint);
		createNewFactorLinks();
		createNewDiagramLinks();		
		selectNewlyPastedItems();
	}
	
	public ORef getDiagramFactorWrappedRef(ORef oldWrappedRef) throws Exception
	{
		return (ORef) getOldToNewObjectRefMap().get(oldWrappedRef);
	}
	
	public ORef getFactorLinkRef(ORef oldWrappedFactorLinkRef)
	{
		return (ORef) getOldToNewObjectRefMap().get(oldWrappedFactorLinkRef);
	}
}
