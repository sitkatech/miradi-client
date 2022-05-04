/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.treetables;

import com.jhlabs.awt.GridLayoutPlus;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.schemas.*;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

import javax.swing.*;
import java.awt.*;

abstract public class AbstractTreeTablePanel extends MultiTreeTablePanel
{
	public AbstractTreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] buttonActionClasses) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActionClasses);
	}
	
	@Override
	public void dispose()
	{
		getMainTable().dispose();
		
		super.dispose();
	}

	protected void createTreeAndTablePanel() throws Exception
	{
		// NOTE: Replace treeScrollPane that super constructor added
		removeAll();

		GridLayoutPlus buttonLayout = createButtonLayout();
		GridLayoutPlus headerLayout = new GridLayoutPlus(buttonLayout.getRows(), 3, 1, 1);
		JPanel headerPanel = new MiradiPanel(headerLayout);
		add(headerPanel, BorderLayout.BEFORE_FIRST_LINE);

		JPanel diagramFilterPanel = new MiradiPanel(new OneRowGridLayout());
		addFilterPanel(diagramFilterPanel);
		headerPanel.add(diagramFilterPanel);

		headerPanel.add(getButtonBox());

		GridLayoutPlus statusLayout = this.createFilterStatusLayout();
		JPanel filterStatusPanel = new MiradiPanel(statusLayout);
		addFilterStatusPanel(filterStatusPanel);
		headerPanel.add(filterStatusPanel);

		JPanel leftPanel = new MiradiPanel(new BorderLayout());
		addAboveTreeHeaderPanel(leftPanel);
		leftPanel.add(getTreeTableScrollPane(), BorderLayout.CENTER);
		
		JPanel rightPanel = new MiradiPanel(new BorderLayout());
		addAboveColumnBar(rightPanel);
		rightPanel.add(getMainTableScrollPane(), BorderLayout.CENTER);
		
		add(leftPanel, BorderLayout.BEFORE_LINE_BEGINS);
		add(rightPanel, BorderLayout.CENTER);
	}

	protected GridLayoutPlus createFilterStatusLayout()
	{
		return new GridLayoutPlus();
	};

	protected void addFilterPanel(JPanel filterPanel) throws Exception
	{
	}

	protected void addFilterStatusPanel(JPanel filterStatusPanel)
	{
	}

	protected void addAboveTreeHeaderPanel(JPanel leftPanel)
	{
	}

	protected void addAboveColumnBar(JPanel rightPanel)
	{
	}
	
	@Override
	protected void handleCommandEventWhileInactive(CommandExecutedEvent event)
	{
		try
		{
			if(doesCommandForceRebuild(event) || isColumnExpandCollapseCommand(event))
			{
				needsFullRebuild = true;
				EAM.logDebug("Queuing a rebuild for " + getClass().getSimpleName());
			}
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		try
		{
			if(needsFullRebuild)
				rebuildEntireTreeAndTable();
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	@Override
	public void handleCommandEventImmediately(CommandExecutedEvent event)
	{
		try
		{		
			if (isColumnExpandCollapseCommand(event))
			{
				rebuildEntireTreeAndTable();
			}

			if (doesCommandForceRebuild(event))
			{
				rebuildEntireTreeAndTable();
			}
			else if(doesAffectTableRowHeight(event))
			{
				getTree().updateAutomaticRowHeights();
				getMainTable().updateAutomaticRowHeights();
			}
			else if(event.isSetDataCommand())
			{
				validate();
			}
		
			
			if(isTreeExpansionCommand(event))
			{
				restoreTreeExpansionState();
			}
			
			repaintToGrowIfTreeIsTaller();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
	}
	
	protected void rebuildEntireTreeAndTable() throws Exception
	{
		if(!isActive())
		{
			needsFullRebuild = true;
			return;
		}
		
		needsFullRebuild = false;
		
		disableSectionSwitchDuringFullRebuild();
		try
		{
			rebuildEntireTreeTable();
		}
		finally
		{
			enableSectionSwitch();
		}
	}
	
	protected boolean isColumnExpandCollapseCommand(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(TableSettingsSchema.getObjectType(), TableSettings.TAG_DATE_UNIT_LIST_DATA);
	}
	
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event) throws Exception
	{
		if(wereAssignmentNodesAddedOrRemoved(event))
			return true;

		if(wereProgressReportsAddedOrRemoved(event))
			return true;

		if(wereResultReportsAddedOrRemoved(event))
			return true;

		if(wereKeasAddedOrRemoved(event))
			return true;
		
		if(didAffectTaskInTree(event))
			return true;
		
		if(didAffectIndicatorInTree(event))
			return true;
		
		if(didAffectRelevancyInTree(event))
			return true;
		
		if(isTargetModeChange(event))
			return true;
		
		if(didAffectMeasurementInTree(event))
			return true;
		
		if(didAffectFutureStatus(event))
			return true;
		
		if(didAffectTableSettingsMapForBudgetColumns(event))
			return true;
		
		if(event.isSetDataCommandWithThisTypeAndTag(TableSettingsSchema.getObjectType(), TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE))
			return true;
		
		if (isCustomConfigurationCommand(event))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_TARGET_NODE_POSITION))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewDataSchema.getObjectType(), ViewData.TAG_ACTION_TREE_CONFIGURATION_CHOICE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewDataSchema.getObjectType(), ViewData.TAG_MONITORING_TREE_CONFIGURATION_CHOICE))
			return true;
		
		if (event.isSetDataCommandWithThisTag(BaseObject.TAG_ASSIGNED_LEADER_RESOURCE))
			return true;

		return false;
	}
	
	protected boolean wereAssignmentNodesAddedOrRemoved(CommandExecutedEvent event) throws Exception
	{
		return false;
	}
	
	private boolean wereProgressReportsAddedOrRemoved(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTag(BaseObject.TAG_PROGRESS_REPORT_REFS);
	}
	
	private boolean wereResultReportsAddedOrRemoved(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTag(BaseObject.TAG_RESULT_REPORT_REFS);
	}

	private boolean wereKeasAddedOrRemoved(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(HumanWelfareTargetSchema.getObjectType(), HumanWelfareTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS))
			return true;
		
		return event.isSetDataCommandWithThisTypeAndTag(TargetSchema.getObjectType(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}

	//TODO this should use that getTasksTag (or something like that) method
	//from email :Please put a todo in isTaskMove that it should use that 
	//getTasksTag method (or whatever it's called) that I mentioned the 
	//other day. I know that one is my code not yours.
	private boolean didAffectTaskInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int type = setCommand.getObjectType();
		String tag = setCommand.getFieldTag();
		if(type == TaskSchema.getObjectType() && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		if(type == TaskSchema.getObjectType() && tag.equals(Task.TAG_IS_MONITORING_ACTIVITY))
			return true;
		if(type == StrategySchema.getObjectType() && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;

		return false;
	}
	
	private boolean didAffectIndicatorInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int type = setCommand.getObjectType();
		String tag = setCommand.getFieldTag();
		if(Factor.isFactor(type))
			return isValidFactorTag(tag);

		if(type == IndicatorSchema.getObjectType() && tag.equals(Indicator.TAG_METHOD_IDS))
			return true;

		if(type == KeyEcologicalAttributeSchema.getObjectType() && tag.equals(KeyEcologicalAttribute.TAG_INDICATOR_IDS))
			return true;
				
		return false;
	}
	
	private boolean isValidFactorTag(String relevancyTag)
	{
		if (relevancyTag.equals(Factor.TAG_INDICATOR_IDS))
				return true;
		
		if (relevancyTag.equals(Factor.TAG_OBJECTIVE_IDS))
			return true;
		
		if (relevancyTag.equals(AbstractTarget.TAG_GOAL_IDS))
			return true;
		
		return false;
	}

	private boolean didAffectRelevancyInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		ORef ref = setCommand.getObjectORef();
		String tag = setCommand.getFieldTag();

		if(Objective.is(ref))
		{
			if(tag.equals(Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
				return true;
			
			if(tag.equals(Objective.TAG_RELEVANT_INDICATOR_SET))
				return true;
		}

		if(Indicator.is(ref))
		{
			if(tag.equals(Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
				return true;
		}

		return false;
	}
	
	private boolean isTargetModeChange (CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(TargetSchema.getObjectType(), Target.TAG_VIABILITY_MODE);
	}
	
	private boolean didAffectMeasurementInTree(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(IndicatorSchema.getObjectType(), Indicator.TAG_MEASUREMENT_REFS))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(MeasurementSchema.getObjectType(), Measurement.TAG_DATE))
			return true;
		
		return false;
	}
	
	private boolean didAffectFutureStatus(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(FutureStatusSchema.getObjectType(), FutureStatusSchema.TAG_FUTURE_STATUS_DATE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(IndicatorSchema.getObjectType(), Indicator.TAG_FUTURE_STATUS_REFS))
			return true;
		
		return false;
	}

	private boolean didAffectTableSettingsMapForBudgetColumns(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(TableSettingsSchema.getObjectType(), TableSettings.TAG_TABLE_SETTINGS_MAP);
	}
	
	private boolean isCustomConfigurationCommand(CommandExecutedEvent event)
	{
		if(event.isSetDataCommandWithThisTypeAndTag(ViewDataSchema.getObjectType(), ViewData.TAG_TREE_CONFIGURATION_REF))
			return true;
		
		if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION))
			return true;

		if(event.isSetDataCommandWithThisTypeAndTag(TableSettingsSchema.getObjectType(), TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER))
			return true;

		if(event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION))
			return true;
		
		if(event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION))
			return true;
		
		if(event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION))
			return true;
		
		if(event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER))
			return true;

		if(event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER))
			return true;
				
		return false;
	}
	
	protected boolean doesAffectTableRowHeight(CommandExecutedEvent event)
	{
		if (!event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		ORef affectedObjectRef = setCommand.getObjectORef();
		
		if(isAffectedRefFoundInMainTableModel(affectedObjectRef))
			return true;
		
		if(ResourceAssignment.is(affectedObjectRef))
			return true;
		
		if(ExpenseAssignment.is(affectedObjectRef))
			return true;
		
		return false;
	}

	private boolean isAffectedRefFoundInMainTableModel(ORef affectedObjectRef)
	{
		for (int row = 0; row < getMainModel().getRowCount(); ++row)
		{
			BaseObject baseObjectForRow = getMainModel().getBaseObjectForRow(row);
			if (baseObjectForRow != null && baseObjectForRow.getRef().equals(affectedObjectRef))
				return true;
		}
		
		return false;
	}
	
	protected boolean isSideTabSwitchingDisabled()
	{
		return (disableSideTabSwitchingCount > 0);
	}
	
	protected void disableSectionSwitchDuringFullRebuild()
	{
		++disableSideTabSwitchingCount;
	}
	
	protected void enableSectionSwitch()
	{
		if(disableSideTabSwitchingCount == 0)
		{
			EAM.logError("PlanningTreeTablePanel.enableSelectionSwitch called too many times");
			EAM.logStackTrace();
			return;
		}
		
		--disableSideTabSwitchingCount;
	}
	
	public ScrollPaneWithHideableScrollBar getMainTableScrollPane()
	{
		return mainTableScrollPane;
	}
	
	abstract protected EditableObjectTableModel getMainModel();

	abstract protected TableWithColumnWidthAndSequenceSaver getMainTable();
	
	abstract protected void rebuildEntireTreeTable() throws Exception;
	
	private int disableSideTabSwitchingCount;
	private boolean needsFullRebuild;
	protected ScrollPaneWithHideableScrollBar mainTableScrollPane;
}
