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
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

abstract public class ObjectDataInputPanelWithSections extends AbstractObjectDataInputPanelWithActivation
{
	public ObjectDataInputPanelWithSections(Project projectToUse, int objectTypeToUse)
	{
		this(projectToUse, new ORef(objectTypeToUse, BaseId.INVALID));
	}
	
	
	public ObjectDataInputPanelWithSections(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
		
		objectType = refToUse.getObjectType();
		tabPanel = new PanelTabbedPane();
		tabPanel.setTabPlacement(tabPanel.LEFT);
		add(tabPanel, BorderLayout.CENTER);
		setBackground(getMainWindow().getAppPreferences().getDarkPanelBackgroundColor());
	}
	
	@Override
	public void selectSectionForTag(String tag)
	{
		int indexToSelect = findSectionWithTag(tag);
		if (indexToSelect >= 0)
			tabPanel.setSelectedIndex(indexToSelect);
	}
	
	private int findSectionWithTag(String tag)
	{
		for (int index = 0; index < tabPanel.getTabCount(); ++index)
		{
			AbstractObjectDataInputPanel panel = (AbstractObjectDataInputPanel) tabPanel.getComponentAt(index);
			if (tag.equals(panel.getPanelDescription()))
				return index;
			
			if (panel.doesSectionContainFieldWithTag(tag))
				return index;
		}
		
		return -1;
	}

	public void createSingleSection(String title)
	{
		singleSection = new SimpleObjectDataInputPanel(getProject(), objectType, title);
		singleSection.setObjectRefs(getSelectedRefs());
		addSubPanelWithTitledBorder(singleSection);
	}
	
	@Override
	public void addSubPanelWithoutTitledBorder(AbstractObjectDataInputPanel subPanel)
	{
		String title = subPanel.getPanelDescription();
		tabPanel.addTab(title, subPanel);
		addSubPanel(subPanel);
	}
	
	@Override
	public void addSubPanelWithTitledBorder(AbstractObjectDataInputPanel subPanel)
	{
		addSubPanelWithoutTitledBorder(subPanel);
	}

	public void addFieldWithEditButton(PanelTitleLabel label, ObjectDataInputField field, ObjectsActionButton button)
	{
		singleSection.addFieldWithEditButton(label, field, button);
	}

	public void addFieldWithEditButton(String translatedLabel, ObjectDataInputField field, ObjectsActionButton button)
	{
		singleSection.addFieldWithEditButton(translatedLabel, field, button);
	}

	public void addFieldsOnOneLine(String text, Icon icon, ObjectDataInputField[] objectDataInputFields)
	{
		singleSection.addFieldsOnOneLine(text, icon, objectDataInputFields);
	}
	
	public void addFieldsOnOneLine(String text, ObjectDataInputField[] objectDataInputFields)
	{
		singleSection.addFieldsOnOneLine(text, objectDataInputFields);
	}

	public void addFieldsOnOneLine(PanelTitleLabel labelComponent, Object[] componentsOrFields)
	{
		singleSection.addFieldsOnOneLine(labelComponent, componentsOrFields);
	}
	
	@Override
	public ObjectDataInputField addField(ObjectDataInputField field)
	{
		return singleSection.addField(field);
	}
 
	@Override
	public Component add(Component component)
	{
		if(singleSection == null)
			throw new RuntimeException("Cannot addFieldComponent directly to ODIPWithSections without singleSection");
		return singleSection.add(component);
	}
	
	protected void setTabEnabled(String panelDescription, boolean shouldEnable)
	{
		int tabAt = findTabByDescription(panelDescription);
		if(tabAt < 0)
			return;
		
		tabPanel.setEnabledAt(tabAt, shouldEnable);
		if(tabPanel.getSelectedIndex() == tabAt)
			tabPanel.setSelectedIndex(-1);
	}

	private int findTabByDescription(String panelDescription)
	{
		for(int tab = 0; tab < tabPanel.getTabCount(); ++tab)
			if(tabPanel.getTitleAt(tab).equals(panelDescription))
				return tab;
		
		return -1;
	}

	class SimpleObjectDataInputPanel extends ObjectDataInputPanel
	{
		public SimpleObjectDataInputPanel(Project projectToUse, int objectTypeToUse, String titleToUse)
		{
			super(projectToUse, objectTypeToUse, BaseId.INVALID);
			title = titleToUse;
		}
		
		@Override
		public String getPanelDescription()
		{
			return title;
		}

		private String title;
	}
	
	private ObjectDataInputPanel singleSection;
	private PanelTabbedPane tabPanel;
	private int objectType;
}
