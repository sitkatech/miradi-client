/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;
import org.conservationmeasures.eam.views.MiradiTabContentsPanelInterface;

abstract public class ObjectManagementPanel extends VerticalSplitPanel implements MiradiTabContentsPanelInterface
{
	public ObjectManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		this(splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
		project = projectToUse;
	}
	
	public ObjectManagementPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
		listComponent = tablePanelToUse;
		
		propertiesPanel = propertiesPanelToUse;
		listComponent.setPropertiesPanel(propertiesPanel);
	}
	
	public void dispose()
	{
		listComponent.dispose();
		listComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}

	public void addTablePanelButton(ObjectsAction action)
	{
		listComponent.addButton(action);
	}
	
	public Project getProject()
	{
		return project;
	}

	public BaseObject getObject()
	{
		if(listComponent == null)
			return null;
		return listComponent.getSelectedObject();
	}
	
	public Component getComponent()
	{
		return this;
	}

	public Icon getIcon()
	{
		return null;
	}

	public String getTabName()
	{
		return getPanelDescription();
	}
	
	public boolean isImageAvailable()
	{
		return false;
	}
	
	public BufferedImage getImage() throws Exception
	{
		return null;
	}
	
	
	private Project project;
	private ObjectCollectionPanel listComponent;
	public AbstractObjectDataInputPanel propertiesPanel;
}
