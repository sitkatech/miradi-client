/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

abstract public class AbstractGroupBoxDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (!isDiagramView())
		return false;
	
		if (!isAtLeastOneFactorSelected())
			return false;
		
		return true;
	}

	protected FactorCell[] getSelectedCells()
	{
		return getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getCommandsToUpdateGroupBoxChildren();
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
	
	protected boolean isAtLeastOneGroupBoxSelected()
	{
		return extractSelectedGroupBoxes().size() == 1;		
	}
	
	protected DiagramFactor getGroupBox()
	{
		final int FIRST_INDEX = 0;
		return extractSelectedGroupBoxes().get(FIRST_INDEX);
	}
	
	protected Vector<DiagramFactor> extractSelectedGroupBoxes()
	{
		FactorCell[] selected = getSelectedCells();
		Vector<DiagramFactor> groupBoxDiagramFactors = new Vector();
		for (int i = 0; i < selected.length; ++i)
		{
			if (!selected[i].isFactor())
				continue;
			
			FactorCell factorCell = selected[i];
			if (factorCell.getWrappedType() == GroupBox.getObjectType())
				groupBoxDiagramFactors.add(factorCell.getDiagramFactor());
		}
		
		return groupBoxDiagramFactors;
	}
	
	protected boolean isAtLeastOneFactorSelected()
	{
		return extractNonGroupBoxDiagramFactors().size() > 0;
	}
		
	protected ORefList extractNonGroupBoxDiagramFactors()
	{
		FactorCell[] selected = getSelectedCells();
		ORefList nonGroupBoxDiagramFactorRefs = new ORefList();
		for (int i = 0; i < selected.length; ++i)
		{
			FactorCell factorCell = selected[i];
			int type = factorCell.getWrappedType();
			if (type == Target.getObjectType() || type == Cause.getObjectType() || type == Strategy.getObjectType())
				nonGroupBoxDiagramFactorRefs.add(factorCell.getDiagramFactorRef());		
		}
		
		return nonGroupBoxDiagramFactorRefs;
	}
	
	abstract protected void getCommandsToUpdateGroupBoxChildren() throws Exception;
}
