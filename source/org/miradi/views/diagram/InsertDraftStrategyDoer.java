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
import org.miradi.diagram.cells.DiagramStrategyCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Strategy;
import org.miradi.objects.ViewData;

public class InsertDraftStrategyDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
		
			if(ViewData.MODE_STRATEGY_BRAINSTORM.equals(currentViewMode))
				return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		return false;
	}
	
	public int getTypeToInsert()
	{
		return ObjectType.STRATEGY;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Draft Strategy");
	}

	protected void doExtraSetup(DiagramFactor diagramFactor, FactorCell[] selectedFactorCells) throws Exception
	{
		CommandSetObjectData setStatusCommand = new CommandSetObjectData(diagramFactor.getWrappedORef(), Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
		getProject().executeCommand(setStatusCommand);
	}

	public void forceVisibleInLayerManager()
	{
		getCurrentLayerManager().setVisibility(DiagramStrategyCell.class, true);
	}
	
}
