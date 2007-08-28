/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
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
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;
import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewLegendPanel extends LegendPanel implements ActionListener
{
	public PlanningViewLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), new BasicGridLayout(0, 1));
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();

		createLegendCheckBoxes();
		addAllComponents();
		updateLegendPanel();
	}
	
	public void updateLegendPanel()
	{
		CodeList hiddenTypes = getLegendSettings(ViewData.TAG_PLANNING_HIDDEN_TYPES);
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
		createCheckBox(Indicator.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME);
		createCheckBox(Task.OBJECT_NAME);
	}

	private void addAllComponents()
	{
		setBorder(new EmptyBorder(5,5,5,5));
		UiLabel title = new PanelTitleLabel(EAM.text("Planning Legend"));
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		add(title);
		
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
		JCheckBox checkBox = (JCheckBox)event.getSource();
		String property = (String) checkBox.getClientProperty(LAYER);
		updateProjectLegendSettings(property, ViewData.TAG_PLANNING_HIDDEN_TYPES);
	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(0,3));
		
		addIconLineWithCheckBox(jPanel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jPanel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jPanel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithCheckBox(jPanel, Strategy.getObjectType(), Strategy.OBJECT_NAME, new StrategyIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.OBJECT_NAME, new TaskIcon());
		
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
