/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.IndicatorIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;

public class IndicatorSubPanel extends ObjectDataInputPanel
{
	public IndicatorSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);

		ObjectDataInputField shortLabelField = createStringField(Indicator.getObjectType(), Indicator.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createExpandableField(Indicator.getObjectType(), Indicator.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Indicator"), new IndicatorIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_DETAIL));
		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_COMMENT));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Indicator");
	}
}
