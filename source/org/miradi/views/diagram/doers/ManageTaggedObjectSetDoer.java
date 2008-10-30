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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ObjectsDoer;

public class ManageTaggedObjectSetDoer extends ObjectsDoer implements ListSelectionListener
{
	@Override
	public boolean isAvailable()
	{
		//FIXME this class is under construction
		//if (!isInDiagram())
		//	return false;
		
		//EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		//return (selected.length > 0);
		
		return false;
		
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
//FIXME this class and method are still under construction
//		RefListComponent refListComponent = new RefListComponent(new TaggedObjectSetQuestion(getProject()), 1, this);
//		DisposablePanel editListPanel = new DisposablePanel();
//		editListPanel.add(refListComponent);
//		
//		ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), editListPanel, EAM.text("Edit Dialog"));
//		Utilities.centerDlg(dialog);
//		dialog.setVisible(true);			
	}

	public void valueChanged(ListSelectionEvent e)
	{
		
	}	
}
