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
package org.miradi.dialogs.assumption;

import org.miradi.actions.ActionEditAssumptionIndicatorRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.AssumptionIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractAnalyticalQuestion;
import org.miradi.objects.Assumption;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.EvidenceConfidenceTypeQuestion;
import org.miradi.schemas.AssumptionSchema;
import org.miradi.utils.FillerLabel;

public class AssumptionDetailsPanel extends ObjectDataInputPanel
{
    public AssumptionDetailsPanel(Project projectToUse, MainWindow mainWindowToUse, AssumptionFactorVisibilityControlPanel assumptionVisibilityButtonPanel) throws Exception
    {
        super(projectToUse, AssumptionSchema.getObjectType());

		ObjectDataInputField shortLabelField = createShortStringField(ObjectType.ASSUMPTION, Assumption.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(ObjectType.ASSUMPTION, Assumption.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Assumption"), new AssumptionIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

        if (assumptionVisibilityButtonPanel != null)
        {
            add(new FillerLabel());
            addSubPanelField(assumptionVisibilityButtonPanel);
        }

        addField(createMultilineField(AssumptionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_TEXT));
		addCustomFields(mainWindowToUse.getActions());

        Actions actionsToUse = mainWindowToUse.getActions();
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(AssumptionSchema.getObjectType(), Assumption.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditAssumptionIndicatorRelevancyList.class), getPicker()));

        addField(createMultilineField(AssumptionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_COMMENTS));
        addField(createRadioButtonEditorField(AssumptionSchema.getObjectType(), BaseObject.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestion(AssumptionSchema.getObjectType())));
        addField(createMultilineField(AssumptionSchema.getObjectType(), BaseObject.TAG_EVIDENCE_NOTES));
        addField(createMultilineField(AssumptionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_IMPLICATIONS));
        addField(createMultilineField(AssumptionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_FUTURE_INFORMATION_NEEDS));

        addTaxonomyFields(AssumptionSchema.getObjectType());
    }

    protected void addCustomFields(Actions actionsToUse)
	{
	}

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Summary");
    }

    private PanelTitleLabel assumptionNameLabel;
}
