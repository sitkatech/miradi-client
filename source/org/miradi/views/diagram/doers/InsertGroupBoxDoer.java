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

import java.text.ParseException;
import java.util.HashSet;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnexpectedNonSideEffectException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.GroupBox;
import org.miradi.views.diagram.InsertFactorDoer;
import org.miradi.views.diagram.LinkCreator;

public class InsertGroupBoxDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (containsDifferentType(getSelectedFactorCells()))
			return false;
		
		if (getDiagramView().isStategyBrainstormMode())
			return false;
		
		return true;
	}
	
	protected void doExtraSetup(DiagramFactor groupBoxDiagramFactor, FactorCell[] selectedFactorCells) throws Exception
	{
		super.doExtraSetup(groupBoxDiagramFactor, selectedFactorCells);
		if (!containsAllAcceptableDiagramFactors(selectedFactorCells))
			return;
		
		ORefList selectedDiagramFactorRefs = extractDiagramFactorRefs(selectedFactorCells);
		if (GroupBoxAddDiagramFactorDoer.hasOwnedSelectedDiagramFactors(getProject(), selectedDiagramFactorRefs))
			return;
		
		addChildrenToGroup(groupBoxDiagramFactor, selectedDiagramFactorRefs);
	}
	
	@Override
	protected void doPostTransactionWork(DiagramObject diagramObject, DiagramFactor newDiagramFactor) throws CommandFailedException
	{
		super.doPostTransactionWork(diagramObject, newDiagramFactor);
		
		// NOTE: Set up a new transaction, so the group link creation is independently undoable
		getProject().executeBeginTransaction();
		try
		{
			LinkCreator linkCreator = new LinkCreator(getProject());
			linkCreator.createAllPossibleGroupLinks(diagramObject, newDiagramFactor);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeEndTransaction();
		}

	}

	private void addChildrenToGroup(DiagramFactor groupBoxDiagramFactor,
			ORefList selectedDiagramFactorRefs) throws ParseException,
			UnexpectedNonSideEffectException, CommandFailedException
	{
		CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, selectedDiagramFactorRefs);
		getProject().executeCommand(appendCommand);
	}
	
	private ORefList extractDiagramFactorRefs(FactorCell[] factorCells)
	{
		ORefList diagramFactorRefs = new ORefList();
		for (int i = 0; i < factorCells.length; ++i)
		{
			diagramFactorRefs.add(factorCells[i].getDiagramFactorRef());
		}
		
		return diagramFactorRefs;
	}
	
	private boolean containsAllAcceptableDiagramFactors(FactorCell[] selectedFactorCells)
	{
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			if (!AbstractGroupBoxDoer.isAcceptableFactor(selectedFactorCells[i].getWrappedType()))
					return false;
		}
		
		return true;
	}
	
	public static boolean containsDifferentType(FactorCell[] selectedFactorCells)
	{
		HashSet<Integer> differentTypes = new HashSet();
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			int wrappedType = selectedFactorCells[i].getWrappedType();
			if (!GroupBox.is(wrappedType))
				differentTypes.add(wrappedType);
		}
		
		return differentTypes.size() > 1;
	}
	
	public void forceVisibleInLayerManager()
	{
		getMainWindow().getCurrentDiagramComponent().getLayerManager().setVisibility(GroupBox.OBJECT_NAME, true);
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Group Box");
	}

	public int getTypeToInsert()
	{
		return GroupBox.getObjectType();
	}
}
