/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.conservationmeasures.eam.dialogs.base.AbstractObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.base.DisposablePanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.resources.ResourcesHandler;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.views.MiradiTabContentsPanelInterface;
import org.conservationmeasures.eam.wizard.MiradiHtmlViewer;
import org.martus.swing.HyperlinkHandler;

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
	
	public Component getTabContentsComponent()
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
	
	private AbstractObjectDataInputPanel dataPanel;

}
