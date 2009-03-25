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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;

public class InsertDirectThreatDoer extends InsertFactorDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
				
		return !getDiagramView().isResultsChainTab();
	}
	
	public int getTypeToInsert()
	{
		return ObjectType.CAUSE;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Factor");
	}

	public void forceVisibleInLayerManager()
	{
		getCurrentLayerManager().setContributingFactorsVisible(true);
		getCurrentLayerManager().setDirectThreatsVisible(true);
	}
	
	protected void doExtraSetup(DiagramFactor newlyInsertedDiagramFactor, FactorCell[] selectedFactorCells) throws Exception
	{
		CommandSetObjectData enableThreat = new CommandSetObjectData(newlyInsertedDiagramFactor.getWrappedORef(), Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
		getProject().executeCommand(enableThreat);
	}
}

