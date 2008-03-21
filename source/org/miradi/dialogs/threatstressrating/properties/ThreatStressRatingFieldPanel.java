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
package org.miradi.dialogs.threatstressrating.properties;

import javax.swing.JPanel;

import org.miradi.actions.ActionManageStresses;
import org.miradi.actions.Actions;
import org.miradi.diagram.factortypes.FactorType;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.ObjectsActionButton;

public class ThreatStressRatingFieldPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingFieldPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		fromLabel = new PanelTitleLabel();
		fromNameField = createReadonlyTextField(ObjectType.FAKE, Factor.TAG_LABEL);
		addFieldWithCustomLabel(fromNameField, fromLabel);

		toLabel = new PanelTitleLabel();
		toNameField = createReadonlyTextField(ObjectType.FAKE, Factor.TAG_LABEL);
		addFieldWithCustomLabel(toNameField, toLabel);

		addField(createCheckBoxField(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));

		
		addField(createMultilineField(FactorLink.getObjectType(), FactorLink.TAG_COMMENT));
		addField(createReadOnlyChoiceField(FactorLink.getObjectType(), FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, new ThreatRatingQuestion()));

		addLabel(EAM.text("Stresses"));
		add(createManageStressesComponent(actions));
		
		addBlankHorizontalLine();
		
		updateFieldsFromProject();
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		updateFieldLabels(orefsToUse);
		super.setObjectRefs(orefsToUse);
	}

	protected JPanel createManageStressesComponent(Actions actions)
	{
		OneRowPanel buttonPanel = new OneRowPanel();
		buttonPanel.setGaps(5);
		buttonPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());

		ObjectsActionButton manageStressesButton = createObjectsActionButton(actions.getObjectsAction(ActionManageStresses.class), getPicker());
		buttonPanel.add(manageStressesButton);
		buttonPanel.add(new PanelTitleLabel(EAM.text("(Create, manage, and rate the stresses for this target)")));
		return buttonPanel;
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
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	public String getPanelDescription()
	{
		return "ThreatStressRatingFieldPanel";
	}
	
	private PanelTitleLabel fromLabel;
	private PanelTitleLabel toLabel;
	ObjectDataInputField fromNameField;
	ObjectDataInputField toNameField;
}
