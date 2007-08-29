/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.umbrella.LegendPanel;

import com.jhlabs.awt.BasicGridLayout;

abstract public class PlanningViewLegendPanel extends LegendPanel implements ActionListener
{
	public PlanningViewLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), new BasicGridLayout(0, 1));
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();

		createLegendCheckBoxes();
		addAllComponents();
		updateCheckBoxesFromProjectSettings();
	}
	
	public void updateCheckBoxesFromProjectSettings()
	{
		CodeList hiddenTypes = getLegendSettings(getViewDataHiddenTypesTag());
		selectAllCheckBoxs();
		for (int i = 0; i < hiddenTypes.size(); ++i)
		{
			String hiddenType = hiddenTypes.get(i);
			JCheckBox checkBox = findCheckBox(hiddenType);
			checkBox.setSelected(false);
		}
	}

	private void addAllComponents()
	{
		setBorder(new EmptyBorder(5,5,5,5));
		add(createLegendButtonPanel(mainWindow.getActions()));	
		selectAllCheckBoxs();
		setMinimumSize(new Dimension(0,0));
	}

	public void actionPerformed(ActionEvent event)
	{	
		saveSettingsToProject(getViewDataHiddenTypesTag());
	}

	public CodeList getLegendSettings()
	{
		CodeList hiddenTypes = new CodeList();
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			JCheckBox checkBox = findCheckBox(keys[i]);
			if (checkBox.isSelected())
				continue;
			
			hiddenTypes.add(keys[i].toString());
		}

		return hiddenTypes;
	}
	
	abstract protected void createLegendCheckBoxes();	
	abstract protected JPanel createLegendButtonPanel(Actions actions);
	abstract protected String getViewDataHiddenTypesTag();
	
	MainWindow mainWindow;
	Project project;
	JCheckBox objectiveCheckBox;
}
