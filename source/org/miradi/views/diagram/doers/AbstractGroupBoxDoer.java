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
package org.miradi.views.diagram.doers;

import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.views.diagram.LocationDoer;

abstract public class AbstractGroupBoxDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
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
			updateGroupBoxChildrenUsingCommands();
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
	
	protected boolean isExactlyOneGroupBoxSelected()
	{
		return getSelectedGroupBoxDiagramFactors().size() == 1;		
	}
	
	protected DiagramFactor getSingleSelectedGroupBox()
	{
		final int FIRST_INDEX = 0;
		return getSelectedGroupBoxDiagramFactors().get(FIRST_INDEX);
	}
	
	protected Vector<DiagramFactor> getSelectedGroupBoxDiagramFactors()
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
		return getSelectedNonGroupBoxDiagramFactors().size() > 0;
	}
		
	protected ORefList getSelectedNonGroupBoxDiagramFactors()
	{
		FactorCell[] selected = getSelectedCells();
		ORefList nonGroupBoxDiagramFactorRefs = new ORefList();
		for (int i = 0; i < selected.length; ++i)
		{
			FactorCell factorCell = selected[i];
			int type = factorCell.getWrappedType();
			if (isAcceptableFactor(type))
				nonGroupBoxDiagramFactorRefs.add(factorCell.getDiagramFactorRef());		
		}
		
		return nonGroupBoxDiagramFactorRefs;
	}

	public static boolean isAcceptableFactor(int type)
	{
		return (type == Target.getObjectType() || 
				type == Cause.getObjectType() || 
				type == Strategy.getObjectType() ||
				type == IntermediateResult.getObjectType() || 
				type == ThreatReductionResult.getObjectType());
	}
	
	abstract protected void updateGroupBoxChildrenUsingCommands() throws Exception;
}
