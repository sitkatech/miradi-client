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

import org.martus.swing.UiTextArea;
import org.miradi.ids.BaseId;
import org.miradi.project.Project;
import org.miradi.utils.MiradiScrollPane;

public class ObjectScrollingMultilineInputField extends ObjectMultilineInputField
{
	public ObjectScrollingMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 4, columnsToUse);
	}

	public JComponent getComponent()
	{
		if(scrollPane == null)
			scrollPane = new MiradiScrollPane(getTextComponent());
		return scrollPane;
	}

	@Override
	UiTextArea getTextComponent()
	{
		return (UiTextArea)super.getComponent();
	}
	
	@Override
	void addFocusListener()
	{
		super.addFocusListener();
		getTextComponent().addFocusListener(this);
	}

	private MiradiScrollPane scrollPane;

}
