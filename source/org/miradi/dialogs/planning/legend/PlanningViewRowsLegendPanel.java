/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.legend;

import javax.swing.JComponent;

import org.miradi.actions.ActionTreeCreateActivityIconOnly;
import org.miradi.actions.ActionTreeCreateMethodIconOnly;
import org.miradi.actions.ActionTreeCreateTaskIconOnly;
import org.miradi.actions.Actions;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.icons.AssignmentIcon;
import org.miradi.icons.ConceptualModelIcon;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.IndicatorIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.MeasurementIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Assignment;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.RowManager;

public class PlanningViewRowsLegendPanel extends AbstractPlanningViewLegendPanel
{
	public PlanningViewRowsLegendPanel(MainWindow mainWindowToUse, PlanningTreeTable treeAsObjectPicker) throws Exception
	{
		super(mainWindowToUse, treeAsObjectPicker);
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
		TwoColumnPanel panel = new TwoColumnPanel();
		panel.disableFill();
		panel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		addIconLineWithCheckBox(panel, ResultsChainDiagram.getObjectType(), ResultsChainDiagram.OBJECT_NAME, new ResultsChainIcon());
		addIconLineWithCheckBox(panel, ConceptualModelDiagram.getObjectType(), ConceptualModelDiagram.OBJECT_NAME, new ConceptualModelIcon());

		addSeparator(panel);
		
		addIconLineWithCheckBox(panel, Target.getObjectType(), Target.OBJECT_NAME, new TargetIcon());
		addIconLineWithCheckBox(panel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(panel, Cause.getObjectType(), Cause.OBJECT_NAME_THREAT, new DirectThreatIcon());
		addIconLineWithCheckBox(panel, Cause.getObjectType(), Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, new ContributingFactorIcon());
		addIconLineWithCheckBox(panel, ThreatReductionResult.getObjectType(), ThreatReductionResult.OBJECT_NAME, new ThreatReductionResultIcon());
		addIconLineWithCheckBox(panel, IntermediateResult.getObjectType(), IntermediateResult.OBJECT_NAME, new IntermediateResultIcon());
		addIconLineWithCheckBox(panel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(panel, Strategy.getObjectType(), Strategy.OBJECT_NAME, new StrategyIcon());
		addPickerButtonLineWithCheckBox(panel, Task.getObjectType(), Task.ACTIVITY_NAME, actions.getObjectsAction(ActionTreeCreateActivityIconOnly.class), picker);
		addIconLineWithCheckBox(panel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addPickerButtonLineWithCheckBox(panel, Task.getObjectType(), Task.METHOD_NAME, actions.getObjectsAction(ActionTreeCreateMethodIconOnly.class), picker);
		addPickerButtonLineWithCheckBox(panel, Task.getObjectType(), Task.OBJECT_NAME, actions.getObjectsAction(ActionTreeCreateTaskIconOnly.class), picker);
		addIconLineWithCheckBox(panel, Measurement.getObjectType(), Measurement.OBJECT_NAME, new MeasurementIcon());
		addIconLineWithCheckBox(panel, Assignment.getObjectType(), Assignment.OBJECT_NAME, new AssignmentIcon());
		
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
