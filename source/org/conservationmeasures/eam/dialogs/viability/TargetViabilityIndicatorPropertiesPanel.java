/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.BorderFactory;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;

public class TargetViabilityIndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public TargetViabilityIndicatorPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, getInvalidTargetRef());			
		setLayout(new OneColumnGridLayout());
		
		TargetViabilityIndicatorSubPanel indicatorSubPanel = new TargetViabilityIndicatorSubPanel(projectToUse, getInvalidTargetRef());
		indicatorSubPanel.setBorder(BorderFactory.createTitledBorder(indicatorSubPanel.getPanelDescription()));
		addSubPanel(indicatorSubPanel);
		add(indicatorSubPanel);

		IndicatorViabilityRatingsSubPanel viabilityRatingsSubPanel = new IndicatorViabilityRatingsSubPanel(projectToUse, getInvalidTargetRef());
		viabilityRatingsSubPanel.setBorder(BorderFactory.createTitledBorder(viabilityRatingsSubPanel.getPanelDescription()));
		addSubPanel(viabilityRatingsSubPanel);
		add(viabilityRatingsSubPanel);

		
		
		
		
		addField(createRatingChoiceField(ObjectType.INDICATOR,  new PriorityRatingQuestion(Indicator.TAG_PRIORITY)));
		addField(createChoiceField(ObjectType.INDICATOR,  new IndicatorStatusRatingQuestion(Indicator.TAG_STATUS)));
		
		addField(createMultilineField(Indicator.TAG_COMMENT));
		
		updateFieldsFromProject();
	}

	private static ORef getInvalidTargetRef()
	{
		return new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt()));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}
}
