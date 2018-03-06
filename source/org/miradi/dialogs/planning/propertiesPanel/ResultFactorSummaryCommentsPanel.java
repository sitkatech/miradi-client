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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.diagram.FactorSummaryCommentsPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;

import javax.swing.*;

public class ResultFactorSummaryCommentsPanel extends FactorSummaryCommentsPanel
{
    public ResultFactorSummaryCommentsPanel(Project project, BaseObjectSchema factorSchemaToUse) throws Exception
    {
        super(project, factorSchemaToUse);
    }

    @Override
    protected void addFactorCoreFields(BaseObjectSchema factorSchema) throws Exception
    {
        ObjectDataInputField shortLabelField = createShortStringField(Factor.TAG_SHORT_LABEL);
        ObjectDataInputField labelField = createExpandableField(Factor.TAG_LABEL);

        String factorLabel = EAM.fieldLabel(factorSchema.getType(), factorSchema.getObjectName());
        Icon factorIcon = IconManager.getImage(factorSchema.getType());

        addFieldsOnOneLine(factorLabel, factorIcon, new ObjectDataInputField[]{shortLabelField, labelField,});
        addField(createMultilineField(Factor.TAG_TEXT));
    }

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Summary");
    }
}
