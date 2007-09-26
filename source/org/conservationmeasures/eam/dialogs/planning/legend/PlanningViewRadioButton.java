/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewCustomizationQuestion;

//FIXME planning - this class should extend UiRadioButton but it cant since UiRadioButton has no constructor that takes in 0 args
abstract public class PlanningViewRadioButton extends JRadioButton implements ActionListener
{
	public PlanningViewRadioButton(Project projectToUse)
	{	
		super();
		project = projectToUse;
		setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			saveRadioSelection();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error Occurred While Trying to Save Settings"));
		}	
	}

	private void saveRadioSelection() throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			saveCurrentRadioSelection();
			saveCustomizationComboSelection();
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private void saveCurrentRadioSelection() throws Exception
	{
		save(ViewData.TAG_PLANNING_STYLE_CHOICE, getPropertyName());
	}

	private void save(String tag, String newValue) throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		String existingValue = viewData.getData(tag);
		if (existingValue.equals(newValue))
			return;

		CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
		project.executeCommand(setComboItem);
	}
	
	private void saveCustomizationComboSelection() throws Exception
	{
		save(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, getCustomizationComboRefToSelect().toString());
	}

	private ORef getCustomizationComboRefToSelect() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		ORef customRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		if (!customRef.isInvalid())
			return customRef;
		
		PlanningViewCustomizationQuestion question = new PlanningViewCustomizationQuestion(getProject());
		ChoiceItem[] choices = question.getChoices();
		if (choices.length == 0)
			return ORef.INVALID;
		
		return ORef.createFromString(choices[0].getCode());
	}

	protected Project getProject()
	{
		return project;
	}
		
	abstract public String getPropertyName();
	
	private	Project project;
}
