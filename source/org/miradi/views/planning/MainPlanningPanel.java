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
/**
 * 
 */
package org.miradi.views.planning;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.MiradiTabContentsPanelInterface;

class MainPlanningPanel extends DisposablePanelWithDescription implements MiradiTabContentsPanelInterface
{
	public MainPlanningPanel(MiradiScrollPane controlBarScrollPane, PlanningTreeManagementPanel planningManagementPanelToUse)
	{
		super(new BorderLayout());
		planningManagementPanel = planningManagementPanelToUse;
		
		add(controlBarScrollPane, BorderLayout.BEFORE_LINE_BEGINS);
		add(planningManagementPanel, BorderLayout.CENTER);
	}
	
	public String getTabName()
	{
		return planningManagementPanel.getTabName();
	}

	public Icon getIcon()
	{
		return planningManagementPanel.getIcon();
	}

	public DisposablePanelWithDescription getTabContentsComponent()
	{
		return this;
	}

	public boolean isImageAvailable()
	{
		return planningManagementPanel.isImageAvailable();
	}

	public BufferedImage getImage() throws Exception
	{
		return planningManagementPanel.getImage();
	}

	public AbstractTableExporter getTableExporter() throws Exception
	{
		return planningManagementPanel.getTableExporter();
	}

	public boolean isExportableTableAvailable()
	{
		return planningManagementPanel.isExportableTableAvailable();
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		return planningManagementPanel.getPrintableComponent();
	}
	
	public boolean isPrintable()
	{
		return true;
	}

	public boolean isRtfExportable()
	{
		return planningManagementPanel.isRtfExportable();
	}		
	
	public void exportRtf(RtfWriter writer) throws Exception
	{
		planningManagementPanel.exportRtf(writer);
	}
	
	public void becomeActive()
	{
		planningManagementPanel.becomeActive();
	}
	
	public void becomeInactive()
	{
		planningManagementPanel.becomeInactive();
	}

	@Override
	public String getPanelDescription()
	{
		return planningManagementPanel.getPanelDescription();
	}
	
	private PlanningTreeManagementPanel planningManagementPanel;
}