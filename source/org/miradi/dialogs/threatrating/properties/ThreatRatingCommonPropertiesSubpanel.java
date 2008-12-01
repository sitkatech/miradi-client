/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogs.threatrating.properties;

import org.miradi.actions.Actions;
import org.miradi.diagram.factortypes.FactorType;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.questions.ThreatRatingQuestion;

public class ThreatRatingCommonPropertiesSubpanel extends ObjectDataInputPanel
{
	public ThreatRatingCommonPropertiesSubpanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		fromLabel = new PanelTitleLabel();
		fromNameField = createReadonlyTextField(ObjectType.FAKE, Factor.TAG_LABEL);
		addFieldWithCustomLabel(fromNameField, fromLabel);

		toLabel = new PanelTitleLabel();
		toNameField = createReadonlyTextField(ObjectType.FAKE, Factor.TAG_LABEL);
		addFieldWithCustomLabel(toNameField, toLabel);

		addField(createCheckBoxField(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));

		commentsField = createMultilineField(FactorLink.getObjectType(), getCommentTagForMode());
		addField(commentsField);
		addField(createReadOnlyChoiceField(FactorLink.getObjectType(), FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, new ThreatRatingQuestion()));

		addBlankHorizontalLine();
		
		updateFieldsFromProject();
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		updateFieldLabels(orefsToUse);
		super.setObjectRefs(orefsToUse);
	}

	private void updateFieldLabels(ORef[] orefsToUse)
	{
		ORefList refs = new ORefList(orefsToUse);
		
		ORef linkRef = refs.getRefForType(FactorLink.getObjectType());
		if(linkRef.isInvalid())
		{
			fromLabel.setText("");
			fromLabel.setIcon(null);
			fromNameField.setObjectType(ObjectType.FAKE);
			toLabel.setText("");
			toLabel.setIcon(null);
			toNameField.setObjectType(ObjectType.FAKE);
			return;
		}
		
		try
		{
			FactorLink link = FactorLink.find(getProject(), linkRef);

			Factor fromFactor = Factor.findFactor(getProject(), link.getFromFactorRef());
			fromLabel.setText(FactorType.getFactorTypeLabel(fromFactor));
			fromLabel.setIcon(FactorType.getFactorIcon(fromFactor));
			fromNameField.setObjectType(fromFactor.getType());

			Factor toFactor = Factor.findFactor(getProject(), link.getToFactorRef());
			toLabel.setText(FactorType.getFactorTypeLabel(toFactor));
			toLabel.setIcon(FactorType.getFactorIcon(toFactor));
			toNameField.setObjectType(toFactor.getType());
			commentsField.setTag(getCommentTagForMode());	
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private String getCommentTagForMode()
	{
		if (getProject().isStressBaseMode())
			return FactorLink.TAG_COMMENT;
		
		return FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT;
	}

	public String getPanelDescription()
	{
		return "ThreatRatingCommonPropertiesSubpanel";
	}
	
	private PanelTitleLabel fromLabel;
	private PanelTitleLabel toLabel;
	private ObjectDataInputField fromNameField;
	private ObjectDataInputField toNameField;
	private ObjectDataInputField commentsField;
}
