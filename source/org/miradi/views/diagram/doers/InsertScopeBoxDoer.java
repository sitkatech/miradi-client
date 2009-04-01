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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.DiagramScopeBoxCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.ScopeBox;
import org.miradi.views.diagram.InsertFactorDoer;

public class InsertScopeBoxDoer extends InsertFactorDoer
{
	@Override
	public void forceVisibleInLayerManager()
	{
		getCurrentLayerManager().setVisibility(DiagramScopeBoxCell.class, true);
	}

	@Override
	public String getInitialText()
	{
		return EAM.text("Label|New Scope Box");
	}

	@Override
	public int getTypeToInsert()
	{
		return ScopeBox.getObjectType();
	}
	
	@Override
	protected void doExtraSetup(DiagramFactor newlyInsertedDiagramFactor, FactorCell[] selectedFactorCells) throws Exception
	{
		super.doExtraSetup(newlyInsertedDiagramFactor, selectedFactorCells);
		if (isFirstScopeBoxCreated())
		{
			ORef scopeBoxRef = newlyInsertedDiagramFactor.getWrappedORef();
			CommandSetObjectData setLabel = new CommandSetObjectData(scopeBoxRef, ScopeBox.TAG_LABEL, getScopeLabel());
			getProject().executeCommand(setLabel);
			
			String details = getProject().getMetadata().getProjectScope();
			CommandSetObjectData setDetails = new CommandSetObjectData(scopeBoxRef, ScopeBox.TAG_TEXT, details);
			getProject().executeCommand(setDetails);
		}
	}

	private String getScopeLabel()
	{
		String label = getProject().getMetadata().getShortProjectScope();
		if (label.length() == 0)
			return "";
		
		return EAM.text("Project Scope") + " :" + label;
	}

	private boolean isFirstScopeBoxCreated()
	{
		return getProject().getScopeBoxPool().size() == 1;
	}
}
