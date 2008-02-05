/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.conservationmeasures.eam.utils.ExportableTableInterface;

public interface MiradiTabContentsPanelInterface
{
	public String getTabName();
	public Icon getIcon();
	public Component getComponent();
	public boolean isImageAvailable();
	public BufferedImage getImage() throws Exception;
	public boolean isExportableTableAvailable();
	public ExportableTableInterface getExportableTable() throws Exception;
	public JComponent getPrintableComponent() throws Exception;
}
