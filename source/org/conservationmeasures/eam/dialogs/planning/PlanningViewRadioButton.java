/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

//FIXME planning - this class should extend UiRadioButton but it cant since UiRadioButton has no constructor that takes in 0 args
abstract public class PlanningViewRadioButton extends JRadioButton implements ActionListener
{
	public PlanningViewRadioButton(Project projectToUse, RowColumnProvider rowColumnProviderToUse)
	{	
		super();
		project = projectToUse;
		rowColumnProvider = rowColumnProviderToUse;
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
			saveCurrentColumnList();
			saveCurrentRowList();
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private void saveCurrentRowList() throws Exception
	{
		String rowListAsString = getListToSave(PlanningView.getMasterRowList(), rowColumnProvider.getRowList());
		save(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, rowListAsString);
	}

	private void saveCurrentColumnList() throws Exception
	{
		String columnListAsString = getListToSave(PlanningView.getMasterColumnList(), rowColumnProvider.getColumnList());
		save(ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, columnListAsString);
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

	private String getListToSave(CodeList masterList, CodeList listToSubtract) throws Exception
	{
		masterList.subtract(listToSubtract);
		return masterList.toString();
	}
	
	protected Project getProject()
	{
		return project;
	}
		
	abstract public String getPropertyName();
	
	private	Project project;
	private RowColumnProvider rowColumnProvider;
}
