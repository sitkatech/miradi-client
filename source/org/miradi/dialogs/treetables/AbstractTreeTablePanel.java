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
package org.miradi.dialogs.treetables;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Objective;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.TableSettings;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

abstract public class AbstractTreeTablePanel extends MultiTreeTablePanel
{
	public AbstractTreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] buttonActionClasses) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActionClasses);
	}
	
	public GenericTreeTableModel getTreeTableModel()
	{
		return getModel();
	}
	
	protected boolean isColumnExpandCollapseCommand(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_DATE_UNIT_LIST_DATA);
	}
	
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event)
	{
		if(wereAssignmentNodesAddedOrRemoved(event))
			return true;
		
		if(wereProgressReportsAddedOrRemoved(event))
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
		
		if(didAffectTableSettingsMapForBudgetColumns(event))
			return true;
		
		return false;
	}
	
	protected boolean wereAssignmentNodesAddedOrRemoved(CommandExecutedEvent event)
	{
		return false;
	}
	
	private boolean wereProgressReportsAddedOrRemoved(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTag(BaseObject.TAG_PROGRESS_REPORT_REFS);
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
		if(type == Task.getObjectType() && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		if(type == Strategy.getObjectType() && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		if(type == Indicator.getObjectType() && tag.equals(Indicator.TAG_METHOD_IDS))
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
		
		if(type == KeyEcologicalAttribute.getObjectType() && tag.equals(KeyEcologicalAttribute.TAG_INDICATOR_IDS))
			return true;
				
		return false;
	}
	
	private boolean isValidFactorTag(String relevancyTag)
	{
		if (relevancyTag.equals(Factor.TAG_INDICATOR_IDS))
				return true;
		
		if (relevancyTag.equals(Factor.TAG_OBJECTIVE_IDS))
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

		return false;
	}
	
	private boolean isTargetModeChange (CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE);
	}
	
	private boolean didAffectMeasurementInTree(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(Indicator.getObjectType(), Indicator.TAG_MEASUREMENT_REFS);
	}
	
	private boolean didAffectTableSettingsMapForBudgetColumns(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_TABLE_SETTINGS_MAP);
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
	
	abstract protected EditableObjectTableModel getMainModel();

	abstract protected TableWithColumnWidthAndSequenceSaver getMainTable();
	
	abstract protected void rebuildEntireTreeTable() throws Exception;
	
	private int disableSideTabSwitchingCount;
}
