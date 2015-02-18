/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.progressReport;

import java.util.LinkedHashMap;

import org.miradi.actions.ActionDeleteProgressReport;
import org.miradi.dialogs.base.EditableObjectListTableSubPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProgressReport;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.views.umbrella.ActionCreateProgressReport;
import org.miradi.views.umbrella.ObjectPicker;

public class ProgressReportSubPanel extends EditableObjectListTableSubPanel
{
	public ProgressReportSubPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow.getProject(), getObjectType());
	}
	
	@Override
	protected void createTable() throws Exception
	{
		objectTableModel = new ProgressReportTableModel(getMainWindow().getProject());
		objectTable = new ProgressReportTable(getMainWindow(), objectTableModel);
	}
	
	@Override
	protected LinkedHashMap<Class, ObjectPicker> getButtonsActionsPickerMap()
	{
		LinkedHashMap<Class, ObjectPicker> buttonsMap = new LinkedHashMap<Class, ObjectPicker>();
		buttonsMap.put(ActionCreateProgressReport.class, getPicker());
		buttonsMap.put(ActionDeleteProgressReport.class, objectTable);
		
		return buttonsMap;
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report");
	}
	
	@Override
	protected int getEditableObjectType()
	{
		return getObjectType();
	}
	
	@Override
	protected String getTagForRefListFieldBeingEdited()
	{
		return BaseObject.TAG_PROGRESS_REPORT_REFS;
	}
	
	@Override
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		if (tagToUse.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return true;
		
		return super.doesSectionContainFieldWithTag(tagToUse);
	}
	
	private static int getObjectType()
	{
		return ProgressReportSchema.getObjectType();
	}
	
	@Override
	protected boolean shouldRefreshModel(CommandExecutedEvent event)
	{
		if (super.shouldRefreshModel(event))
			return true;
		
		return event.isSetDataCommandWithThisTag(ProgressReport.TAG_PROGRESS_DATE);
	}
}
