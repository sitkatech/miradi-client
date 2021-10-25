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
package org.miradi.dialogs.planning.propertiesPanel;


import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.resultReport.ResultReportSubPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.MainWindow;
import org.miradi.schemas.BaseObjectSchema;

public abstract class ResultFactorPropertiesPanel extends ObjectDataInputPanelWithSections
{
    public ResultFactorPropertiesPanel(MainWindow mainWindow, BaseObjectSchema factorSchemaToUse) throws Exception
    {
        super(mainWindow.getProject(), factorSchemaToUse.getType());

        setLayout(new OneColumnGridLayout());

        addSubPanelWithTitledBorder(new ResultFactorSummaryCommentsPanel(getProject(), factorSchemaToUse));
        addSubPanelWithTitledBorder(new ResultReportSubPanel(getMainWindow()));

        updateFieldsFromProject();
    }
}
