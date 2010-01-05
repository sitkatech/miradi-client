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

import javax.swing.JPanel;

import org.miradi.actions.ActionDeleteProgressReport;
import org.miradi.dialogs.base.EditableObjectTableSubPanel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProgressReport;
import org.miradi.views.umbrella.ActionCreateProgressReport;
import org.miradi.views.umbrella.ObjectPicker;

public class ProgressReportSubPanel extends EditableObjectTableSubPanel
{
	public ProgressReportSubPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow.getProject(), objectPickerToUse, getObjectType());
	}
	
	@Override
	protected void createTable() throws Exception
	{
		progressReportTableModel = new ProgressReportTableModel(getMainWindow().getProject());
		progressReportTable = new ProgressReportTable(getMainWindow(), progressReportTableModel);
	}
	
	@Override
	protected JPanel createButtonBar()
	{
		OneRowPanel box = new OneRowPanel();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.setGaps(3);
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionCreateProgressReport.class), objectPicker));
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionDeleteProgressReport.class), progressReportTable));
		
		return box;
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
	protected String getTag()
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
