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

package org.miradi.dialogs.resultReport;

import org.miradi.actions.ActionDeleteResultReport;
import org.miradi.dialogs.base.EditableObjectListTableSubPanel;
import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.ResultReportSchema;
import org.miradi.actions.ActionCreateResultReport;
import org.miradi.views.umbrella.ObjectPicker;

import java.util.LinkedHashMap;

public class ResultReportSubPanel extends EditableObjectListTableSubPanel
{
    public ResultReportSubPanel(MainWindow mainWindow) throws Exception
    {
        super(mainWindow.getProject(), getObjectType());
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
        return EAM.text("Title|Result Report");
    }

    @Override
    protected boolean doesSectionContainFieldWithTag(String tagToUse)
    {
        if (tagToUse.equals(BaseObject.PSEUDO_TAG_LATEST_RESULT_REPORT_CODE))
            return true;

        if (tagToUse.equals(BaseObject.PSEUDO_TAG_LATEST_RESULT_REPORT_DETAILS))
            return true;

        return super.doesSectionContainFieldWithTag(tagToUse);
    }

    @Override
    protected LinkedHashMap<Class, ObjectPicker> getButtonsActionsPickerMap()
    {
        LinkedHashMap<Class, ObjectPicker> buttonsMap = new LinkedHashMap<Class, ObjectPicker>();
        buttonsMap.put(ActionCreateResultReport.class, getPicker());
        buttonsMap.put(ActionDeleteResultReport.class, objectTable);

        return buttonsMap;
    }

    private EditableObjectRefsTableModel createTableModel(Project project)
    {
        return new ResultReportTableModel(project);
    }

    protected ResultReportTable createTable(MainWindow mainWindow, EditableObjectRefsTableModel model) throws Exception
    {
        return new ResultReportTable(mainWindow, model);
    }

    @Override
    protected int getEditableObjectType()
    {
        return getObjectType();
    }

    @Override
    protected String getTagForRefListFieldBeingEdited()
    {
        return BaseObject.TAG_RESULT_REPORT_REFS;
    }

    private static int getObjectType()
    {
        return ResultReportSchema.getObjectType();
    }
}

