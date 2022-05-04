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

import org.miradi.actions.ActionEditOutputGoalRelevancyList;
import org.miradi.actions.ActionEditOutputIndicatorRelevancyList;
import org.miradi.actions.ActionEditOutputObjectiveRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Factor;
import org.miradi.objects.Output;
import org.miradi.project.Project;
import org.miradi.schemas.OutputSchema;

public class OutputDetailsPanel extends ObjectDataInputPanel
{
    public OutputDetailsPanel(Project projectToUse, MainWindow mainWindowToUse) throws Exception
    {
		super(projectToUse, OutputSchema.getObjectType());

        ObjectDataInputField shortLabelField = createStringField(OutputSchema.getObjectType(), Factor.TAG_SHORT_LABEL, 10);
        ObjectDataInputField labelField = createExpandableField(OutputSchema.getObjectType(), Factor.TAG_LABEL);
        addFieldsOnOneLine(EAM.text("Output"), IconManager.getOutputIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

        addField(createMultilineField(OutputSchema.getObjectType(), Factor.TAG_TEXT));

        Actions actionsToUse = mainWindowToUse.getActions();
		addFieldWithEditButton(EAM.text("Objectives"), createReadOnlyObjectList(OutputSchema.getObjectType(), Output.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditOutputObjectiveRelevancyList.class), getPicker()));
		addFieldWithEditButton(EAM.text("Goals"), createReadOnlyObjectList(OutputSchema.getObjectType(), Output.PSEUDO_TAG_RELEVANT_GOAL_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditOutputGoalRelevancyList.class), getPicker()));
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(OutputSchema.getObjectType(), Output.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditOutputIndicatorRelevancyList.class), getPicker()));

        addTaxonomyFields(OutputSchema.getObjectType());

        addField(createMultilineField(OutputSchema.getObjectType(), Factor.TAG_COMMENTS));
		addField(createDateChooserField(Output.TAG_DUE_DATE));
        addField(createStringField(OutputSchema.getObjectType(), Output.TAG_URL));
    }

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Summary");
    }
}
