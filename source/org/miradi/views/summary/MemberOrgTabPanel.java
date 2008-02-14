/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.martus.swing.HyperlinkHandler;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.resources.ResourcesHandler;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.wizard.MiradiHtmlViewer;

public class MemberOrgTabPanel extends DisposablePanel implements MiradiTabContentsPanelInterface
{
	public MemberOrgTabPanel(MainWindow mainWindowToUse, String htmlResourceName, AbstractObjectDataInputPanel dataPanelToUse) throws Exception
	{
		dataPanel = dataPanelToUse;
		
		HyperlinkHandler handler = mainWindowToUse.getHyperlinkHandler();
		MiradiHtmlViewer logoPanel = new MiradiHtmlViewer(mainWindowToUse, handler);

		String html = EAM.loadResourceFile(ResourcesHandler.class, htmlResourceName);
		logoPanel.setText(html);
		
		add(logoPanel, BorderLayout.BEFORE_FIRST_LINE);
		add(dataPanel, BorderLayout.CENTER);
	}
	
	public void dispose()
	{
		super.dispose();
		dataPanel.dispose();
	}
	
	public DisposablePanel getTabContentsComponent()
	{
		return this;
	}

	public ExportableTableInterface getExportableTable() throws Exception
	{
		return dataPanel.getExportableTable();
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
	
	private AbstractObjectDataInputPanel dataPanel;

}
