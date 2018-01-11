/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import org.miradi.ActionDeleteExtendedProgressReport;
import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.ExtendedProgressReportSchema;
import org.miradi.views.umbrella.ActionCreateExtendedProgressReport;
import org.miradi.views.umbrella.ObjectPicker;

import java.util.LinkedHashMap;

public class ExtendedProgressReportSubPanel extends AbstractProgressReportSubPanel
{
	public ExtendedProgressReportSubPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow, getObjectType());
	}
	
	@Override
	protected LinkedHashMap<Class, ObjectPicker> getButtonsActionsPickerMap()
	{
		LinkedHashMap<Class, ObjectPicker> buttonsMap = new LinkedHashMap<Class, ObjectPicker>();
		buttonsMap.put(ActionCreateExtendedProgressReport.class, getPicker());
		buttonsMap.put(ActionDeleteExtendedProgressReport.class, objectTable);
		
		return buttonsMap;
	}

	@Override
	protected EditableObjectRefsTableModel createTableModel(Project project)
	{
		return new ExtendedProgressReportTableModel(project);
	}

	@Override
	protected AbstractProgressReportTable createTable(MainWindow mainWindow, EditableObjectRefsTableModel model) throws Exception
	{
		return new ExtendedProgressReportTable(mainWindow, model);
	}

	@Override
	protected int getEditableObjectType()
	{
		return getObjectType();
	}
	
	@Override
	protected String getTagForRefListFieldBeingEdited()
	{
		return BaseObject.TAG_EXTENDED_PROGRESS_REPORT_REFS;
	}
	
	private static int getObjectType()
	{
		return ExtendedProgressReportSchema.getObjectType();
	}
}
