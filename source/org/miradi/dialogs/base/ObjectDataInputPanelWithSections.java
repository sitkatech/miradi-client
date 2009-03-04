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
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

abstract public class ObjectDataInputPanelWithSections extends AbstractObjectDataInputPanel
{
	public ObjectDataInputPanelWithSections(Project projectToUse, int objectTypeToUse)
	{
		super(projectToUse, objectTypeToUse, BaseId.INVALID);
		objectType = objectTypeToUse;
		
		tabPanel = new PanelTabbedPane();
		tabPanel.setTabPlacement(tabPanel.LEFT);
		add(tabPanel, BorderLayout.CENTER);
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
	
	public void createSingleSection(String title)
	{
		singleSection = new SimpleObjectDataInputPanel(getProject(), objectType, title);
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

	public void addFieldWithEditButton(PanelTitleLabel label,
			ObjectDataInputField field,
			ObjectsActionButton button)
	{
		singleSection.addFieldWithEditButton(label, field, button);
	}

	public void addFieldWithEditButton(String translatedLabel,
			ObjectDataInputField field,
			ObjectsActionButton button)
	{
		singleSection.addFieldWithEditButton(translatedLabel, field, button);
	}

	public void addFieldsOnOneLine(String text, Icon icon,
			ObjectDataInputField[] objectDataInputFields)
	{
		singleSection.addFieldsOnOneLine(text, icon, objectDataInputFields);
	}
	
	public void addFieldsOnOneLine(String text, 
			ObjectDataInputField[] objectDataInputFields)
	{
		singleSection.addFieldsOnOneLine(text, objectDataInputFields);
	}

	public void addFieldsOnOneLine(PanelTitleLabel labelComponent, 
			Object[] componentsOrFields)
	{
		singleSection.addFieldsOnOneLine(labelComponent, componentsOrFields);
	}


	@Override
	public Component add(Component component)
	{
		if(singleSection == null)
			throw new RuntimeException("Cannot addFieldComponent directly to ODIPWithSections without singleSection");
		return singleSection.add(component);
	}
	
	private ObjectDataInputPanel singleSection;
	private PanelTabbedPane tabPanel;
	private int objectType;
}
