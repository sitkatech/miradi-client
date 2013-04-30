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
package org.miradi.dialogs.stress;

import javax.swing.JDialog;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class StressPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public static StressPropertiesPanel createWithVisibilityPanel(MainWindow mainWindow) throws Exception
	{
		StressFactorVisibilityControlPanel visibilityPanel = new StressFactorVisibilityControlPanel(mainWindow);
		return new StressPropertiesPanel(null, mainWindow, visibilityPanel);
	}

	public static StressPropertiesPanel createWithoutVisibilityPanel(JDialog parent, MainWindow mainWindow) throws Exception
	{
		return new StressPropertiesPanel(parent, mainWindow, null);
	}

	private StressPropertiesPanel(JDialog parent, MainWindow mainWindow, AbstractObjectDataInputPanel visibilityPanel) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.STRESS);
	
		setLayout(new OneColumnGridLayout());
		addSubPanelWithTitledBorder(new StressDetailsSubPanel(parent, getProject()));
		if(visibilityPanel != null)
			addSubPanelWithTitledBorder(visibilityPanel);
		addSubPanelWithTitledBorder(new StressCommentsSubPanel(getProject()));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Properties");
	}
}
