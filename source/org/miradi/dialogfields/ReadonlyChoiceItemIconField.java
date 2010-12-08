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

import javax.swing.Icon;
import javax.swing.JComponent;

import org.martus.swing.UiLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class ReadonlyChoiceItemIconField extends ObjectDataInputField
{
	public ReadonlyChoiceItemIconField(Project projectToUse, ORef refToUse,	String tagToUse, Icon statusIconToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		statusIcon = statusIconToUse;
		component = new PanelTitleLabel();
		component.setIcon(statusIcon);
	}
	
	@Override
	public void setText(String code)
	{
	}

	@Override
	public JComponent getComponent()
	{
		return component;
	}

	@Override
	public String getText()
	{
		return "";
	}

	private Icon statusIcon;
	private UiLabel component;
}
