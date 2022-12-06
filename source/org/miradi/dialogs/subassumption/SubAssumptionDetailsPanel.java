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
package org.miradi.dialogs.subassumption;

import org.miradi.actions.ActionEditSubAssumptionIndicatorRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.SubAssumptionIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractAssumption;
import org.miradi.objects.SubAssumption;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.EvidenceConfidenceTypeQuestion;
import org.miradi.schemas.SubAssumptionSchema;
import org.miradi.utils.FillerLabel;

public class SubAssumptionDetailsPanel extends ObjectDataInputPanel
{
    public SubAssumptionDetailsPanel(Project projectToUse, MainWindow mainWindowToUse, SubAssumptionFactorVisibilityControlPanel subAssumptionVisibilityButtonPanel) throws Exception
    {
        super(projectToUse, SubAssumptionSchema.getObjectType());

		ObjectDataInputField shortLabelField = createShortStringField(ObjectType.SUB_ASSUMPTION, SubAssumption.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(ObjectType.SUB_ASSUMPTION, SubAssumption.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Subassumption"), new SubAssumptionIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

        if (subAssumptionVisibilityButtonPanel != null)
        {
            add(new FillerLabel());
            addSubPanelField(subAssumptionVisibilityButtonPanel);
        }

        addField(createMultilineField(SubAssumptionSchema.getObjectType(), AbstractAssumption.TAG_TEXT));
		addCustomFields(mainWindowToUse.getActions());

        Actions actionsToUse = mainWindowToUse.getActions();
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(SubAssumptionSchema.getObjectType(), SubAssumption.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditSubAssumptionIndicatorRelevancyList.class), getPicker()));

        addField(createMultilineField(SubAssumptionSchema.getObjectType(), AbstractAssumption.TAG_COMMENTS));
        addField(createRadioButtonEditorField(SubAssumptionSchema.getObjectType(), BaseObject.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestion(SubAssumptionSchema.getObjectType())));
        addField(createMultilineField(SubAssumptionSchema.getObjectType(), BaseObject.TAG_EVIDENCE_NOTES));
        addField(createMultilineField(SubAssumptionSchema.getObjectType(), AbstractAssumption.TAG_IMPLICATIONS));
        addField(createMultilineField(SubAssumptionSchema.getObjectType(), AbstractAssumption.TAG_FUTURE_INFORMATION_NEEDS));

        addTaxonomyFields(SubAssumptionSchema.getObjectType());
    }

    protected void addCustomFields(Actions actionsToUse)
	{
	}

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Summary");
    }

    private PanelTitleLabel subAssumptionNameLabel;
}
