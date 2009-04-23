/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objects.Stress;
import org.miradi.objects.Task;

public class DiagramCopyPaster extends DiagramPaster
{
	public DiagramCopyPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(diagramPanelToUse, modelToUse, transferableListToUse);
	}

	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{	
		pasteFactors(startPoint);
		createNewFactorLinks();
		createNewDiagramLinks();		
		updateAutoCreatedThreatStressRatings();
		selectNewlyPastedItems();
	}
	
	public void pasteFactors(Point startPoint) throws Exception
	{	
		dataHelper = new PointManipulater(startPoint, transferableList.getUpperMostLeftMostCorner());
		createNewFactorsAndContents();	
		createNewDiagramFactors();
	}
	
	public ORef getDiagramFactorWrappedRef(ORef oldWrappedRef) throws Exception
	{
		ORef foundRef = getOldToNewObjectRefMap().get(oldWrappedRef);
		if (foundRef == null)
		{
			if (Stress.is(oldWrappedRef) || Task.is(oldWrappedRef))
				return oldWrappedRef;
		}
		
		return foundRef;
	}
	
	public ORef getFactorLinkRef(ORef oldWrappedFactorLinkRef)
	{
		if(oldWrappedFactorLinkRef.isInvalid())
			return oldWrappedFactorLinkRef;
		
		return getOldToNewObjectRefMap().get(oldWrappedFactorLinkRef);
	}
	
	protected boolean canPasteTypeInDiagram(int type)
	{
		return true;
	}
	
	protected boolean shouldCreateObject(ORef ref)
	{
		return true;
	}
}
