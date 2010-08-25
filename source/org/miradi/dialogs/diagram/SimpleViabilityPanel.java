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
package org.miradi.dialogs.diagram;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.Border;

import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.indicator.SimpleViabilityFieldsPanel;
import org.miradi.dialogs.indicator.SimpleViabilityManagementPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;

public class SimpleViabilityPanel extends ModelessDialogPanel
{
	public SimpleViabilityPanel(MainWindow mainWindow, ORef factorRef) throws Exception
	{
		simpleViabilityManagementPanel = new SimpleViabilityManagementPanel(mainWindow, factorRef);
		
		simpleViabilityPropertiesPanel = new SimpleViabilityFieldsPanel(mainWindow.getProject(), factorRef);
		Border border = BorderFactory.createTitledBorder(simpleViabilityPropertiesPanel.getPanelDescription());
		simpleViabilityPropertiesPanel.setBorder(border);
		
		add(simpleViabilityPropertiesPanel, BorderLayout.BEFORE_FIRST_LINE);
		add(simpleViabilityManagementPanel, BorderLayout.CENTER);	
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		simpleViabilityManagementPanel.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		super.becomeInactive();
		
		simpleViabilityManagementPanel.becomeInactive();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		simpleViabilityManagementPanel.dispose();
		simpleViabilityPropertiesPanel.dispose();
	}
	
	@Override
	public String getPanelDescription()
	{
		return simpleViabilityManagementPanel.getPanelDescription();
	}
	
	public Icon getIcon()
	{
		return simpleViabilityManagementPanel.getIcon();
	}
	
	@Override
	public BaseObject getObject()
	{
		return simpleViabilityManagementPanel.getObject();
	}
	
	public void updateSplitterLocation()
	{
		simpleViabilityManagementPanel.updateSplitterLocation();
	}
		
	private SimpleViabilityManagementPanel simpleViabilityManagementPanel;
	private SimpleViabilityFieldsPanel simpleViabilityPropertiesPanel;
}
