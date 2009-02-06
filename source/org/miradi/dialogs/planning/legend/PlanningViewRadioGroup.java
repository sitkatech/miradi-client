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
package org.miradi.dialogs.planning.legend;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.planning.PlanningView;

public class PlanningViewRadioGroup
{
	public PlanningViewRadioGroup(Project projectToUse)
	{
		project = projectToUse;
		rebuildGroup();
	}
	
	public void rebuildGroup()
	{
		configurationComponents = new Hashtable<String, Component>();
		buttonGroup = new ButtonGroup();		
	}
	
	public void addRadioButtonToGroup(PlanningViewRadioButton radioButton)
	{
		buttonGroup.add(radioButton);
		configurationComponents.put(radioButton.getPropertyName(), radioButton);
	}
	
	public JRadioButton findRadioButton(String property)
	{
		if (property.equals(""))
			return (JRadioButton) findComponent(PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
		
		return (JRadioButton) findComponent(property);
	}
	
	private Component findComponent(String property)
	{
		return configurationComponents.get(property);
	}
	
	public void updateRadioSelection(String selectedProperty)
	{
		findRadioButton(selectedProperty).setSelected(true);
	}
	
	public void selectAppropriateRadioButton() throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		JRadioButton radioButton = findRadioButton(selectedRadioName);
		if (radioButton == null)
			return;
		
		radioButton.setSelected(true);
	}
	
	private Project project;
	private Hashtable<String, Component> configurationComponents;
	private ButtonGroup buttonGroup;
}
