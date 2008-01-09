/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.util.HashSet;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.FactorDeleteHelper;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

public class DeleteGroupBoxDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (hasSelectedGroupBoxes())
			return true;
		
		return false;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			FactorDeleteHelper deleteHelper = new FactorDeleteHelper(getDiagramView().getDiagramModel());
			HashSet<FactorCell> groupBoxCells = getDiagramView().getDiagramComponent().getOnlySelectedGroupBoxCells();
			for(FactorCell groupBoxCell : groupBoxCells)
			{
				deleteHelper.deleteFactor(groupBoxCell.getDiagramFactor());	
			}
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}

	}
	
	private boolean hasSelectedGroupBoxes()
	{
		try
		{
			HashSet<FactorCell> groupBoxCells = getDiagramView().getDiagramComponent().getOnlySelectedGroupBoxCells();
			return (groupBoxCells.size() > 0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
}
