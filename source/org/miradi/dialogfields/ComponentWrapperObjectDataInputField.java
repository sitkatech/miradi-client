/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class ComponentWrapperObjectDataInputField extends ObjectDataInputField implements ListSelectionListener
{
	public ComponentWrapperObjectDataInputField(Project projectToUse, ORef refToUse, String tagToUse, SavebleComponent componentToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		component = componentToUse;
		component.addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent e)
	{
		forceSave();
	}

	@Override
	public JComponent getComponent()
	{
		return component;
	}

	@Override
	public String getText()
	{
		return component.getText();
	}

	@Override
	public void setText(String newValue)
	{
		component.setText(newValue);
	}
	
	@Override
	public void updateEditableState()
	{
		super.updateEditableState();
		
		component.setEnabled(isValidObject());			
	}
	
	private SavebleComponent component;
}
