/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

public class GroupBoxInsertFactorDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (!isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if (!containsOnlyOneGroupBox(selected))
			return false;
		
		if (!containsAtleastOneFactor(selected))
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	}

	private boolean containsOnlyOneGroupBox(EAMGraphCell[] selected)
	{
		ORefList groupBoxRefs = new ORefList();
		for (int i = 0; i < selected.length; ++i)
		{
			if (!selected[i].isFactor())
				continue;
			
			FactorCell factorCell = (FactorCell) selected[i];
			if (factorCell.getWrappedType() != GroupBox.getObjectType())
				groupBoxRefs.add(factorCell.getWrappedORef());
		}
		
		return groupBoxRefs.size() == 1;
	}
	
	private boolean containsAtleastOneFactor(EAMGraphCell[] selected)
	{
		for (int i = 0; i < selected.length; ++i)
		{
			if (!selected[i].isFactor())
				continue;
			
			FactorCell factorCell = (FactorCell) selected[i];
			int type = factorCell.getWrappedType();
			if (type == Target.getObjectType() || type == Cause.getObjectType() || type == Strategy.getObjectType())
				return true;		
		}
		
		return false;

	}	
}
