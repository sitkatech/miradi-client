/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramStrategyCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.ViewData;

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

	protected void doExtraSetup(ORef factorRef) throws CommandFailedException
	{
		CommandSetObjectData setStatusCommand = new CommandSetObjectData(factorRef, Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
		getProject().executeCommand(setStatusCommand);
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramStrategyCell.class, true);
	}
	
}
