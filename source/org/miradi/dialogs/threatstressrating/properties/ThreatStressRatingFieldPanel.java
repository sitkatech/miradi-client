/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import org.miradi.diagram.factortypes.FactorType;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatStressRatingFieldPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingFieldPanel(Project projectToUse, ObjectPicker picker) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		ORefList[] selected = picker.getSelectedHierarchies();
		System.out.println(selected[0]);
		ORef linkRef = selected[0].getRefForType(FactorLink.getObjectType());
		FactorLink link = FactorLink.find(getProject(), linkRef);

		Factor fromFactor = Factor.findFactor(getProject(), link.getFromFactorRef());
		ObjectDataInputField fromNameField = createReadonlyTextField(fromFactor.getType(), Factor.TAG_LABEL);
		PanelTitleLabel fromLabel = new PanelTitleLabel(FactorType.getFactorTypeLabel(fromFactor), FactorType.getFactorIcon(fromFactor));
		addFieldWithCustomLabel(fromNameField, fromLabel);

		Factor toFactor = Factor.findFactor(getProject(), link.getToFactorRef());
		ObjectDataInputField toNameField = createReadonlyTextField(toFactor.getType(), Factor.TAG_LABEL);
		PanelTitleLabel toLabel = new PanelTitleLabel(FactorType.getFactorTypeLabel(toFactor), FactorType.getFactorIcon(toFactor));
		addFieldWithCustomLabel(toNameField, toLabel);

		addField(createCheckBoxField(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));

		
		addField(createMultilineField(FactorLink.getObjectType(), FactorLink.TAG_COMMENT));
		addField(createReadOnlyChoiceField(FactorLink.getObjectType(), FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, new ThreatRatingQuestion()));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return "ThreatStressRatingFieldPanel";
	}
}
