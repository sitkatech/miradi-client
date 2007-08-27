/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.umbrella.LegendPanel;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewLegendPanel extends LegendPanel implements ActionListener
{
	public PlanningViewLegendPanel(Project projectToUse)
	{
		super(projectToUse, new BasicGridLayout(0, 1));
		project = projectToUse;
		objectiveCheckBox = new JCheckBox("objective");
		objectiveCheckBox.addActionListener(this);
		add(objectiveCheckBox);
	}

	public void actionPerformed(ActionEvent event)
	{	
		JCheckBox checkBox = (JCheckBox)event.getSource();
		String property = (String) checkBox.getClientProperty(LAYER);
		updateProjectLegendSettings(property, ViewData.TAG_PLANNING_HIDDEN_TYPES);
	}
	
	public CodeList getLegendSettings()
	{
		//FIXME planning - add all the types here
		CodeList hiddenTypes = new CodeList();
		hiddenTypes.add(Integer.toString(Objective.getObjectType()));
		
		return hiddenTypes;
	}
	
	Project project;
	JCheckBox objectiveCheckBox;
}
