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

import java.awt.Component;
import java.util.Vector;

import org.martus.swing.Utilities;
import org.miradi.actions.ActionCreateNamedTaggedObjectSet;
import org.miradi.dialogfields.FactorTagListEditor;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Factor;
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
		FactorTagListEditor factorTagListEditor = new FactorTagListEditor(getProject(), selectedFactor);
		EditTagWithCreateTagButtonDialog dialog = new EditTagWithCreateTagButtonDialog(getMainWindow(), factorTagListEditor, EAM.text("Edit Dialog"));
		Utilities.centerDlg(dialog);
		dialog.setSize(250, 300);
		dialog.setVisible(true);			
	}
	
	class EditTagWithCreateTagButtonDialog extends ModelessDialogWithClose
	{
		public EditTagWithCreateTagButtonDialog(MainWindow parent, DisposablePanel panel, String headingText)
		{
			super(parent, panel, headingText);
			setScrollableMainPanel(panel);
		}
		
		
		@Override
		protected Vector<Component> getButtonBarComponents()
		{
			DisposablePanel wrappedEditPanel = (DisposablePanel) getWrappedPanel();
			createButton = wrappedEditPanel.createObjectsActionButton(getMainWindow().getActions().getObjectsAction(ActionCreateNamedTaggedObjectSet.class), getPicker());

			Vector<Component> components = new Vector<Component>();
			components.add(createButton);
			components.addAll(super.getButtonBarComponents());
			return components;
		}
		
		private ObjectsActionButton createButton; 
	}
	
	abstract protected Factor getSingleSelectedFactor();
}
