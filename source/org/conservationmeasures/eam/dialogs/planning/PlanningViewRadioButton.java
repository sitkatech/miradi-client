/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

//FIXME planning - this class should extend UiRadioButton but it cant since UiRadioButton has no constructor that takes in 0 args
abstract public class PlanningViewRadioButton extends JRadioButton implements ActionListener
{
	public PlanningViewRadioButton(Project projectToUse)
	{	
		super();
		project = projectToUse;
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		saveRadioSelection();
	}

	private void saveRadioSelection()
	{
		try
		{
			//FIXME planning - uncomment and finish
			//ViewData viewData = project.getCurrentViewData();
			//String savedChoice = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		}
		catch (Exception e) 
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error Occurred While Trying to Save Settings"));
		}
	}
	
	abstract public String[] getRowList();
	abstract public String[] getColumnList();
	abstract public String getPropertyName();
	
	//TODO planning - make private
	Project project;
}
