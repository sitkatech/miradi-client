/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

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
