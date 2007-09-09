/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
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
		//FIXME planning - finish this
		configurationComponents = new Hashtable<String, Component>();
		//ButtonGroup buttonGroup = new ButtonGroup();		
	}
	
	public void addRadioButtonToGroup(ButtonGroup buttonGroup, PlanningViewRadioButton radioButton)
	{
		buttonGroup.add(radioButton);
		configurationComponents.put(radioButton.getPropertyName(), radioButton);
	}
	
	public void addComboBoxToHashMap(PlanningViewComboBox comboBoxToAdd)
	{
		configurationComponents.put(comboBoxToAdd.getPropertyName(), comboBoxToAdd);
	}
	
	public JRadioButton findRadioButton(String property)
	{
		return (JRadioButton) findComponent(property);
	}
	
	public JComboBox findComboBox(String property)
	{
		return (JComboBox) findComponent(property);
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
		String selectedRadioName = getCurrentRadioChoice(viewData);
		JRadioButton radioButton = findRadioButton(selectedRadioName);
		radioButton.setSelected(true);
	}
	
	public String getCurrentRadioChoice(ViewData viewData)
	{
		String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		boolean shouldReturnDefault = selectedRadioName.trim().equals("");
		if (shouldReturnDefault)
			return PlanningView.STRATEGIC_PLAN_RADIO_CHOICE;

		return selectedRadioName;
	}
	
	private Project project;
	private Hashtable<String, Component> configurationComponents;
}
