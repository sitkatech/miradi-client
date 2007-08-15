/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorDataHelper;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.ORef;

public class DiagramAliasPaster extends DiagramPaster
{
	public DiagramAliasPaster(DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(modelToUse, transferableListToUse);
	}
	
	public void pasteFactors(Point startPoint) throws Exception
	{
		dataHelper = new FactorDataHelper(startPoint, transferableList.getUpperMostLeftMostCorner());
		createNewDiagramFactors();
	}

	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{
		pasteFactors(startPoint);
		createNewDiagramLinks();
	}

	public ORef getDiagramFactorWrappedRef(ORef oldWrappedRef)
	{
		return oldWrappedRef;
	}
	
	public ORef getFactorLinkRef(ORef oldWrappedFactorLinkRef)
	{
		return oldWrappedFactorLinkRef;
	}
}
