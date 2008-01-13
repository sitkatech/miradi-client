/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class TargetViabilityIndicatorSubPanel extends ObjectDataInputPanel
{
	public TargetViabilityIndicatorSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		ObjectDataInputField shortLabelField = createStringField(ObjectType.INDICATOR, Indicator.TAG_SHORT_LABEL,STD_SHORT);
		ObjectDataInputField labelField = createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Indicator"), new IndicatorIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		
		addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_DETAIL));
		addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_COMMENT));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Indicator");
	}
}
