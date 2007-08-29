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
import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.MethodIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TaskIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.umbrella.LegendPanel;

import com.jhlabs.awt.BasicGridLayout;
import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewRowsLegendPanel extends LegendPanel implements ActionListener
{
	public PlanningViewRowsLegendPanel(MainWindow mainWindowToUse)
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
		CodeList hiddenTypes = getLegendSettings(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES);
		selectAllCheckBoxs();
		for (int i = 0; i < hiddenTypes.size(); ++i)
		{
			String hiddenType = hiddenTypes.get(i);
			JCheckBox checkBox = findCheckBox(hiddenType);
			checkBox.setSelected(false);
		}
	}
	
	private void createLegendCheckBoxes()
	{
		createCheckBox(Goal.OBJECT_NAME);
		createCheckBox(Objective.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME);
		createCheckBox(Task.ACTIVITY_NAME);
		createCheckBox(Indicator.OBJECT_NAME);
		createCheckBox(Task.METHOD_NAME);
		createCheckBox(Task.OBJECT_NAME);
		
		createCheckBox(Indicator.TAG_MEASUREMENT_SUMMARY);
		createCheckBox(Indicator.PSEUDO_TAG_METHODS); 
		createCheckBox(Indicator.PSEUDO_TAG_FACTOR); 
		createCheckBox(Indicator.TAG_PRIORITY);
		createCheckBox(Indicator.PSEUDO_TAG_STATUS_VALUE);
		createCheckBox(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		createCheckBox(Indicator.TAG_MEASUREMENT_DATE);
		createCheckBox(Task.PSEUDO_TAG_TASK_TOTAL);
	}

	private void addAllComponents()
	{
		setBorder(new EmptyBorder(5,5,5,5));
		add(createTitleBar(EAM.text("Control Bar")));
		add(createLegendButtonPanel(mainWindow.getActions()));	
		selectAllCheckBoxs();
		setMinimumSize(new Dimension(0,0));
	}

	private void selectAllCheckBoxs()
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			String property = ((JCheckBox)checkBoxes.get(keys[i])).getClientProperty(LAYER).toString();
			JCheckBox checkBox = findCheckBox(property);
			checkBox.setSelected(true);
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{	
		saveSettingsToProject(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES);
	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(0,3));
		
		addTitleBar(jPanel, EAM.text("Rows"));
		addIconLineWithCheckBox(jPanel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jPanel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jPanel, Strategy.getObjectType(), Strategy.OBJECT_NAME, new StrategyIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.ACTIVITY_NAME, new ActivityIcon());
		addIconLineWithCheckBox(jPanel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.METHOD_NAME, new MethodIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.OBJECT_NAME, new TaskIcon());
		
		//FIXME planning - add rest of checkboxes
		addTitleBar(jPanel, EAM.text("Columns"));
		addCheckBoxLine(jPanel, Indicator.TAG_MEASUREMENT_SUMMARY);
		
		return jPanel;
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

	MainWindow mainWindow;
	Project project;
	JCheckBox objectiveCheckBox;
}
