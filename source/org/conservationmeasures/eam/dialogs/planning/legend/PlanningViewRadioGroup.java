/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.planning.PlanningView;

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
