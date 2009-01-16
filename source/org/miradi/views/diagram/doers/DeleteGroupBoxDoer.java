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

import java.util.HashSet;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.views.diagram.LinkDeletor;
import org.miradi.views.diagram.LocationDoer;

public class DeleteGroupBoxDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
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
			LinkDeletor linkDeletor = new LinkDeletor(getProject());
			FactorDeleteHelper deleteHelper = FactorDeleteHelper.createFactorDeleteHelper(getDiagramView().getCurrentDiagramComponent());
			HashSet<FactorCell> groupBoxCells = getDiagramView().getCurrentDiagramComponent().getOnlySelectedGroupBoxCells();
			for(FactorCell groupBoxCell : groupBoxCells)
			{
				removeAllChildrenFromGroupBox(groupBoxCell);
				ORefList referringGroupBoxDiagramLinks = groupBoxCell.getDiagramFactor().findObjectsThatReferToUs(DiagramLink.getObjectType());
				linkDeletor.deleteDiagramLinks(referringGroupBoxDiagramLinks);
				deleteHelper.deleteFactorAndDiagramFactor(groupBoxCell.getDiagramFactor());	
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
	
	private void removeAllChildrenFromGroupBox(FactorCell groupBoxCell) throws Exception
	{
		CommandSetObjectData clearChildrenList = new CommandSetObjectData(groupBoxCell.getDiagramFactorRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, new ORefList().toString());
		getProject().executeCommand(clearChildrenList);
	}

	private boolean hasSelectedGroupBoxes()
	{
		try
		{
			HashSet<FactorCell> groupBoxCells = getDiagramView().getCurrentDiagramComponent().getOnlySelectedGroupBoxCells();
			return (groupBoxCells.size() > 0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
}
