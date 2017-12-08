/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.stress;

import javax.swing.JDialog;

import org.miradi.actions.ActionManageFactorTags;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.FactorSummaryCorePanel;
import org.miradi.icons.StressIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.questions.ScopeStressBasedThreatRatingQuestion;
import org.miradi.questions.SeverityStressBasedThreatRatingQuestion;
import org.miradi.questions.StressRatingChoiceQuestion;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.StressSchema;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.ObjectsActionButton;

public class StressDetailsSubPanel extends ObjectDataInputPanel
{
	public StressDetailsSubPanel(JDialog parentDialogToUse, Project projectToUse, AbstractObjectDataInputPanel visibilityPanel) throws Exception
	{
		super(projectToUse, ObjectType.STRESS);
	
		parentDialog = parentDialogToUse;
		
		ObjectDataInputField shortLabelField = createShortStringField(Stress.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Stress.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Stress"), new StressIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		if(visibilityPanel != null)
		{
			add(new FillerLabel());
			addSubPanelWithTitledBorder(visibilityPanel);
		}
		
		addField(createMultilineField(Stress.TAG_DETAIL));
		if (projectToUse.isStressBaseMode())
			addRatingsFields();

		ObjectsActionButton chooseTagForFactorButton = createObjectsActionButton(getMainWindow().getActions().getObjectsAction(ActionManageFactorTags.class), getPicker());
		ObjectDataInputField readOnlyTaggedObjects = createReadOnlyObjectList(DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS);
		addFieldWithEditButton(FactorSummaryCorePanel.getTagsLabel(), readOnlyTaggedObjects, chooseTagForFactorButton);

		addTaxonomyFields(StressSchema.getObjectType());
		
		updateFieldsFromProject();
	}

	private void addRatingsFields() throws Exception
	{
		ObjectDataInputField scopeField = createPopupQuestionEditor(parentDialog, StressSchema.getObjectType(), Stress.TAG_SCOPE, ScopeStressBasedThreatRatingQuestion.class);
		ObjectDataInputField severityField = createPopupQuestionEditor(parentDialog, StressSchema.getObjectType(), Stress.TAG_SEVERITY, SeverityStressBasedThreatRatingQuestion.class);		
		addFieldsOnOneLine(EAM.text("Ratings"), new ObjectDataInputField[]{scopeField, severityField});
		
		addField(createReadOnlyChoiceField(StressSchema.getObjectType(), Stress.PSEUDO_STRESS_RATING, new StressRatingChoiceQuestion()));
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Details");
	}
	
	private JDialog parentDialog;
}
