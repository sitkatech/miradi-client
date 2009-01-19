/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.martus.swing.HyperlinkHandler;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.main.MainWindow;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.Translation;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.wizard.MiradiHtmlViewer;

public class MemberOrgTabPanel extends DisposablePanelWithDescription implements MiradiTabContentsPanelInterface
{
	public MemberOrgTabPanel(MainWindow mainWindowToUse, String htmlResourceName, AbstractObjectDataInputPanel dataPanelToUse) throws Exception
	{
		dataPanel = dataPanelToUse;
		
		HyperlinkHandler handler = mainWindowToUse.getHyperlinkHandler();
		MiradiHtmlViewer logoPanel = new MiradiHtmlViewer(mainWindowToUse, handler);

		String html = Translation.getHtmlContent(htmlResourceName);
		logoPanel.setText(html);
		
		add(logoPanel, BorderLayout.BEFORE_FIRST_LINE);
		add(dataPanel, BorderLayout.CENTER);
	}
	
	public void dispose()
	{
		super.dispose();
		dataPanel.dispose();
	}
	
	public DisposablePanelWithDescription getTabContentsComponent()
	{
		return this;
	}

	public AbstractTableExporter getTableExporter() throws Exception
	{
		return dataPanel.getTableExporter();
	}

	public Icon getIcon()
	{
		return dataPanel.getIcon();
	}

	public BufferedImage getImage() throws Exception
	{
		return dataPanel.getImage();
	}

	public String getTabName()
	{
		return dataPanel.getTabName();
	}

	public boolean isExportableTableAvailable()
	{
		return dataPanel.isExportableTableAvailable();
	}

	public boolean isImageAvailable()
	{
		return dataPanel.isImageAvailable();
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		return dataPanel.getPrintableComponent();
	}
	
	public boolean isPrintable()
	{
		return dataPanel.isPrintable();
	}

	public boolean isRtfExportable()
	{
		return dataPanel.isRtfExportable();
	}
	
	public void exportRtf(RtfWriter writer) throws Exception
	{
		dataPanel.exportRtf(writer);
	}
	
	@Override
	public String getPanelDescription()
	{
		return dataPanel.getPanelDescription();
	}

	private AbstractObjectDataInputPanel dataPanel;

}
