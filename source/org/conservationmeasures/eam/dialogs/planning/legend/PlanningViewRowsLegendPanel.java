/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.ConceptualModelIcon;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ResultsChainIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.RowManager;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewRowsLegendPanel extends AbstractPlanningViewLegendPanel
{
	public PlanningViewRowsLegendPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		updateCheckBoxesFromProjectSettings();

	}

	public String getBorderTitle()
	{
		return EAM.text("Rows");
	}
	
	protected CodeList getMasterListToCreateCheckBoxesFrom()
	{
		return RowManager.getMasterRowList();
	}
	
	protected JComponent createLegendButtonPanel(Actions actions)
	{
		JPanel panel = new JPanel(new GridLayoutPlus(0, 3));
		
		addIconLineWithCheckBox(panel, ConceptualModelDiagram.getObjectType(), ConceptualModelDiagram.OBJECT_NAME, new ConceptualModelIcon());
		addIconLineWithCheckBox(panel, ResultsChainDiagram.getObjectType(), ResultsChainDiagram.OBJECT_NAME, new ResultsChainIcon());
		addIconLineWithCheckBox(panel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(panel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(panel, Strategy.getObjectType(), Strategy.OBJECT_NAME, new StrategyIcon());
		addButtonLineWithCheckBox(panel, Task.getObjectType(), Task.ACTIVITY_NAME, actions.get(ActionTreeCreateActivity.class));
		addIconLineWithCheckBox(panel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addButtonLineWithCheckBox(panel, Task.getObjectType(), Task.METHOD_NAME, actions.get(ActionTreeCreateMethod.class));
		addButtonLineWithCheckBox(panel, Task.getObjectType(), Task.OBJECT_NAME, actions.get(ActionTreeCreateTask.class));
		
		return panel;
	}
	
	protected String getConfigurationTypeTag()
	{
		return PlanningViewConfiguration.TAG_ROW_CONFIGURATION;
	}

	protected CodeList getVisibleTypes() throws Exception
	{
		return RowManager.getVisibleRowCodes(getViewData());
	}
}
