/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.base.EditableObjectListTableSubPanel;
import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

abstract public class AbstractProgressReportSubPanel extends EditableObjectListTableSubPanel
{
	public AbstractProgressReportSubPanel(MainWindow mainWindow, int objectType) throws Exception
	{
		super(mainWindow.getProject(), objectType);
	}
	
	@Override
	protected void createTable() throws Exception
	{
		objectTableModel = createTableModel(getMainWindow().getProject());
		objectTable = createTable(getMainWindow(), objectTableModel);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report");
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		if (tagToUse.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return true;
		
		if (tagToUse.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS))
			return true;

		return super.doesSectionContainFieldWithTag(tagToUse);
	}
	
	abstract protected EditableObjectRefsTableModel createTableModel(Project project);
	abstract protected AbstractProgressReportTable createTable(MainWindow mainWindow, EditableObjectRefsTableModel model) throws Exception;
}
