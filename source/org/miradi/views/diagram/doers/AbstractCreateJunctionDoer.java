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
package org.miradi.views.diagram.doers;

import java.util.Vector;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.views.diagram.LocationDoer;

abstract public class AbstractCreateJunctionDoer extends LocationDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return isAtleastOneSelectedFactor();
	}

	protected Vector<DiagramLink> getDiagramLinks(DiagramFactor diagramFactor, int direction)
	{
		Vector<DiagramLink> diagramLinks = new Vector<DiagramLink>();
		ORefList diagramLinkReferrerRefs = diagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int refIndex = 0; refIndex < diagramLinkReferrerRefs.size(); ++refIndex)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrerRefs.get(refIndex));
			ORef diagramFactorRef = diagramLink.getDiagramFactorRef(direction);
			if (diagramFactor.getRef().equals(diagramFactorRef))
				diagramLinks.add(diagramLink);
		}
		
		return diagramLinks;
	}

	private boolean isAtleastOneSelectedFactor()
	{
		return getSelectedDiagramFactors().size() > 0;
	}

	protected Vector<DiagramFactor> getSelectedDiagramFactors()
	{
		Vector<DiagramFactor> diagramFactors = new Vector();
		EAMGraphCell[] cells = getSelectedCells();
		for (int index = 0; index < cells.length; ++index)
		{
			if (cells[index].isFactor())
			{
				FactorCell factorCell = ((FactorCell)cells[index]);
				diagramFactors.add(factorCell.getDiagramFactor());
			}
		}
		
		return diagramFactors;
	}
	
	protected EAMGraphCell[] getSelectedCells()
	{
		return getDiagramView().getDiagramComponent().getSelectedAndRelatedCells();
	}
}
