/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewColumsLegendPanel extends PlanningViewLegendPanel
{
	public PlanningViewColumsLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected String getViewDataHiddenTypesTag()
	{
		return ViewData.TAG_PLANNING_HIDDEN_COL_TYPES;
	}
	
	protected void createLegendCheckBoxes()
	{
		createCheckBox(Indicator.TAG_MEASUREMENT_SUMMARY);
		createCheckBox(Indicator.PSEUDO_TAG_METHODS); 
		createCheckBox(Indicator.PSEUDO_TAG_FACTOR); 
		createCheckBox(Indicator.TAG_PRIORITY);
		createCheckBox(Indicator.PSEUDO_TAG_STATUS_VALUE);
		createCheckBox(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		createCheckBox(Indicator.TAG_MEASUREMENT_DATE);
		createCheckBox(Task.PSEUDO_TAG_TASK_TOTAL);
	}
		
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(0,3));
		
		addTitleBar(jPanel, EAM.text("Columns"));
		addCheckBoxLine(jPanel, Indicator.TAG_MEASUREMENT_SUMMARY);
		addCheckBoxLine(jPanel, Indicator.PSEUDO_TAG_METHODS);
		addCheckBoxLine(jPanel, Indicator.PSEUDO_TAG_FACTOR);
		addCheckBoxLine(jPanel, Indicator.TAG_PRIORITY);	
		addCheckBoxLine(jPanel, Indicator.PSEUDO_TAG_STATUS_VALUE);
		addCheckBoxLine(jPanel, Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		addCheckBoxLine(jPanel, Indicator.TAG_MEASUREMENT_DATE);				
		addCheckBoxLine(jPanel, Task.PSEUDO_TAG_TASK_TOTAL);
		
		return jPanel;
	}
}
