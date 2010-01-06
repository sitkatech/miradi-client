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

package org.miradi.dialogs.progressReport;

import java.util.HashMap;

import org.miradi.actions.ActionDeleteProgressReport;
import org.miradi.dialogs.base.EditableObjectListTableSubPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProgressReport;
import org.miradi.views.umbrella.ActionCreateProgressReport;
import org.miradi.views.umbrella.ObjectPicker;

public class ProgressReportSubPanel extends EditableObjectListTableSubPanel
{
	public ProgressReportSubPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow.getProject(), objectPickerToUse, getObjectType());
	}
	
	@Override
	protected void createTable() throws Exception
	{
		objectTableModel = new ProgressReportTableModel(getMainWindow().getProject());
		objectTable = new ProgressReportTable(getMainWindow(), objectTableModel);
	}
	
	protected HashMap<Class, ObjectPicker> getButtonsActionsPickerMap()
	{
		HashMap<Class, ObjectPicker> buttonsMap = new HashMap<Class, ObjectPicker>();
		buttonsMap.put(ActionCreateProgressReport.class, objectPicker);
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
		return ProgressReport.getObjectType();
	}
}
