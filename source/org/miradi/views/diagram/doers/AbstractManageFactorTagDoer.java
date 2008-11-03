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

import javax.swing.Box;

import org.martus.swing.Utilities;
import org.miradi.actions.ActionStandaloneCreateTag;
import org.miradi.dialogfields.FactorTagListEditor;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Factor;
import org.miradi.questions.TaggedObjectSetQuestion;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractManageFactorTagDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
			
		return getSingleSelectedFactor() != null;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		Factor selectedFactor = getSingleSelectedFactor();
		FactorTagListEditor factorTagListEditor = new FactorTagListEditor(getProject(), selectedFactor, new TaggedObjectSetQuestion(getProject()));
		EditTagWithCreateTagButtonDialog dialog = new EditTagWithCreateTagButtonDialog(EAM.getMainWindow(), factorTagListEditor, EAM.text("Edit Dialog"));
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);			
	}
	
	class EditTagWithCreateTagButtonDialog extends ModalDialogWithClose
	{
		public EditTagWithCreateTagButtonDialog(MainWindow parent, DisposablePanel panel, String headingText)
		{
			super(parent, panel, headingText);
		}
		
		@Override
		public void addAdditionalButtons(Box buttonBar)
		{
			super.addAdditionalButtons(buttonBar);
			
			DisposablePanel wrappedEditPanel = (DisposablePanel) getWrappedPanel();
			createButton = wrappedEditPanel.createObjectsActionButton(getMainWindow().getActions().getObjectsAction(ActionStandaloneCreateTag.class), getPicker());
			buttonBar.add(createButton);
		}
		
		private ObjectsActionButton createButton; 
	}
	
	abstract protected Factor getSingleSelectedFactor();
}
