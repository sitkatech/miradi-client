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

package org.miradi.dialogs.output;

import org.miradi.dialogs.base.EditableObjectListTableSubPanel;
import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.OutputSchema;

abstract public class AbstractOutputSubPanel extends EditableObjectListTableSubPanel
{
    public AbstractOutputSubPanel(MainWindow mainWindow) throws Exception
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
        return EAM.text("Title|Outputs");
    }

    private EditableObjectRefsTableModel createTableModel(Project project)
    {
        return new OutputTableModel(project);
    }

    protected OutputTable createTable(MainWindow mainWindow, EditableObjectRefsTableModel model) throws Exception
    {
        return new OutputTable(mainWindow, model);
    }

    @Override
    protected int getEditableObjectType()
    {
        return getObjectType();
    }

    @Override
    protected String getTagForRefListFieldBeingEdited()
    {
        return BaseObject.TAG_OUTPUT_REFS;
    }

    private static int getObjectType()
    {
        return OutputSchema.getObjectType();
    }
}

