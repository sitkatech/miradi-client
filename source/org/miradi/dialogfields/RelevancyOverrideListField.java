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
package org.miradi.dialogfields;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.ids.BaseId;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.MiradiScrollPane;

public class RelevancyOverrideListField extends ObjectDataInputField implements ListSelectionListener
{
	public RelevancyOverrideListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		
		refListEditor = new RefListComponent(questionToUse, 1, this);
		refListScroller = new MiradiScrollPane(refListEditor);
	}
	
	public JComponent getComponent()
	{
		return refListScroller;
	}

	public String getText()
	{
		return refListEditor.getText();
	}

	public void setText(String codes)
	{	
		refListEditor.setText(codes);
	}
	
	public void updateEditableState()
	{
		refListEditor.setEnabled(isValidObject());
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		forceSave();
	}
	
	protected RefListComponent refListEditor;
	private MiradiScrollPane refListScroller;
}
