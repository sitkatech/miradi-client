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

import org.martus.swing.Utilities;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.dialogfields.FactorTagListEditor;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.questions.TaggedObjectSetQuestion;
import org.miradi.views.diagram.LocationDoer;

public class ManageFactorTagsFromMenuDoer extends LocationDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
			
		return getSingleSelectedFactorCell() != null;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		FactorCell selectedCell = getSingleSelectedFactorCell();
		FactorTagListEditor refListComponent = new FactorTagListEditor(getProject(), selectedCell.getUnderlyingObject(), new TaggedObjectSetQuestion(getProject()), 1);
		DisposablePanel editListPanel = new DisposablePanel();
		editListPanel.add(refListComponent);
		
		ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), editListPanel, EAM.text("Edit Dialog"));
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);			
	}
	
	private FactorCell getSingleSelectedFactorCell()
	{
		FactorCell[] selectedCells = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		if (selectedCells.length == 0)
			return null;
	
		return selectedCells[0];
	}
}
