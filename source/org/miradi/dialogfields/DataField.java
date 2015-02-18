/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.miradi.main.EAM;
import org.miradi.project.Project;

abstract public class DataField extends SavableField
{
	public DataField(Project projectToUse)
	{
		project = projectToUse;
	}
	
	protected void addFocusListener()
	{
		getComponent().addFocusListener(this);
	}
	
	public void setVisible(boolean isVisible)
	{
		getComponent().setVisible(isVisible);
	}

	public void updateEditableState()
	{
		updateEditableState(shouldBeEditable());
	}

	protected boolean shouldBeEditable()
	{
		return false;
	}

	protected void updateEditableState(boolean isEditable)
	{
		getComponent().setEnabled(isEditable);
		Color fg = EAM.READONLY_FOREGROUND_COLOR;
		Color bg = EAM.READONLY_BACKGROUND_COLOR;
		if(isEditable)
		{
			fg = EAM.EDITABLE_FOREGROUND_COLOR;
			bg = EAM.EDITABLE_BACKGROUND_COLOR;
		}

		getComponent().setForeground(fg);
		if(shouldSetBackground())
			getComponent().setBackground(bg);
	}
	
	protected boolean shouldSetBackground()
	{
		return true;
	}

	void setDefaultFieldBorder()
	{
		getComponent().setBorder(createLineBorderWithMargin());
	}

	public static CompoundBorder createLineBorderWithMargin()
	{
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border margin = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		CompoundBorder coumpoundBorder = BorderFactory.createCompoundBorder(lineBorder, margin);
		
		return coumpoundBorder;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	abstract public JComponent getComponent();

	private Project project;
}
