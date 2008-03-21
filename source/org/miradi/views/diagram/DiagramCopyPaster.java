/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.views.diagram;

import java.awt.Point;

import org.miradi.diagram.DiagramModel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;

public class DiagramCopyPaster extends DiagramPaster
{
	public DiagramCopyPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(diagramPanelToUse, modelToUse, transferableListToUse);
	}

	public void pasteFactors(Point startPoint) throws Exception
	{	
		dataHelper = new PointManipulater(startPoint, transferableList.getUpperMostLeftMostCorner());
		createNewFactorsAndContents();	
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
	
	protected boolean canPastTypeInDiagram(int type)
	{
		return true;
	}
	
	protected boolean shouldCreateObject(ORef ref)
	{
		return true;
	}
}
