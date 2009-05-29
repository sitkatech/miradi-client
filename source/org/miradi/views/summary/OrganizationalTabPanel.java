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

package org.miradi.views.summary;

import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.Translation;
import org.miradi.views.MiradiTabContentsPanelInterface;

import com.jhlabs.awt.Alignment;

class OrganizationalTabPanel extends DisposablePanelWithDescription implements MiradiTabContentsPanelInterface
{
	OrganizationalTabPanel(MainWindow mainWindowToUse, ObjectManagementPanel panelToWrap) throws Exception
	{
		wrappedPanel = panelToWrap;
		
		OneColumnGridLayout layout = new OneColumnGridLayout();
		layout.setFill(Alignment.FILL_BOTH);
		layout.setRowWeight(0, 0);
		layout.setRowWeight(1, 100);
		setLayout(layout);
		
		String html = Translation.getHtmlContent("OtherOrgOverview.html");
		FlexibleWidthHtmlViewer htmlViewer = new FlexibleWidthHtmlViewer(mainWindowToUse, html);
		
		// NOTE: For some reason, without a border, the htmlViewer is not visible
		htmlViewer.setBorder(BorderFactory.createLineBorder(AppPreferences.getDataPanelBackgroundColor()));
		
		add(htmlViewer);
		add(wrappedPanel);
	}

	public void becomeActive()
	{
		wrappedPanel.becomeActive();
	}

	public void becomeInactive()
	{
		wrappedPanel.becomeInactive();
	}

	public void exportRtf(RtfWriter writer) throws Exception
	{
		wrappedPanel.exportRtf(writer);
	}

	public Icon getIcon()
	{
		return wrappedPanel.getIcon();
	}

	public BufferedImage getImage() throws Exception
	{
		return wrappedPanel.getImage();
	}

	public Class getJumpActionClass()
	{
		return wrappedPanel.getJumpActionClass();
	}

	public JComponent getPrintableComponent() throws Exception
	{
		return wrappedPanel.getPrintableComponent();
	}

	public DisposablePanelWithDescription getTabContentsComponent()
	{
		return this;
	}

	public String getTabName()
	{
		return wrappedPanel.getTabName();
	}

	public AbstractTableExporter getTableExporter() throws Exception
	{
		return wrappedPanel.getTableExporter();
	}

	public boolean isExportableTableAvailable()
	{
		return wrappedPanel.isExportableTableAvailable();
	}

	public boolean isImageAvailable()
	{
		return wrappedPanel.isImageAvailable();
	}

	public boolean isPrintable()
	{
		return wrappedPanel.isPrintable();
	}

	public boolean isRtfExportable()
	{
		return wrappedPanel.isRtfExportable();
	}
	
	@Override
	public String getPanelDescription()
	{
		return wrappedPanel.getPanelDescription();
	}

	private ObjectManagementPanel wrappedPanel;

}