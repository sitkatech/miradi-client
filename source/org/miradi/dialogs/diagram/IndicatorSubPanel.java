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
package org.miradi.dialogs.diagram;

import org.miradi.actions.ActionEditIndicatorStrategyActivityRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.viability.ViabilityTreeModel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.schemas.IndicatorSchema;

public class IndicatorSubPanel extends ObjectDataInputPanel
{
	public IndicatorSubPanel(Project projectToUse, ORef orefToUse, Actions actionsToUse) throws Exception
	{
		super(projectToUse, orefToUse);

		ObjectDataInputField shortLabelField = createStringField(IndicatorSchema.getObjectType(), Indicator.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createExpandableField(IndicatorSchema.getObjectType(), Indicator.TAG_LABEL);
		ObjectDataInputField unitField = createStringField(IndicatorSchema.getObjectType(), Indicator.TAG_UNIT, 10);
		addFieldsOnOneLine(EAM.text("Indicator"), IconManager.getIndicatorIcon(), new ObjectDataInputField[]{shortLabelField, labelField, unitField,});
		
		final int COLUMNS = 75;
		addField(createMultilineField(IndicatorSchema.getObjectType(), Indicator.TAG_DETAIL, COLUMNS));
		addTaxonomyFields(IndicatorSchema.getObjectType());
		addField(createMultilineField(IndicatorSchema.getObjectType(), Indicator.TAG_COMMENTS, COLUMNS));
		addField(createMultilineField(IndicatorSchema.getObjectType(), Indicator.TAG_EVIDENCE_NOTES, COLUMNS));

		addFieldWithEditButton(EAM.text("Strategies And Activities"), createReadOnlyObjectList(IndicatorSchema.getObjectType(), Indicator.PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditIndicatorStrategyActivityRelevancyList.class), getPicker()));

		updateFieldsFromProject();
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		if (tagToUse.equals(Target.TAG_VIABILITY_MODE))
			return true;

		if (tagToUse.equals(ViabilityTreeModel.VIRTUAL_TAG_STATUS))
			return true;

		if (tagToUse.equals(ViabilityTreeModel.VIRTUAL_TAG_FUTURE_STATUS))
			return true;

		if (tagToUse.equals(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE))
			return true;

		return super.doesSectionContainFieldWithTag(tagToUse);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}
}
