/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;


import org.conservationmeasures.eam.actions.ActionEditStrategyProgressReports;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.questions.StrategyTaxonomyQuestion;
import org.conservationmeasures.eam.utils.ObjectsActionButton;

public class StrategyPropertiesPanel extends ObjectDataInputPanel
{
	public StrategyPropertiesPanel(MainWindow mainWindow)
	{
		super(mainWindow.getProject(), Strategy.getObjectType(), BaseId.INVALID);
		
		ObjectDataInputField shortLabelField = createStringField(Strategy.getObjectType(), Strategy.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createStringField(Strategy.getObjectType(), Strategy.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Strategy"), new StrategyIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createChoiceField(Strategy.getObjectType(), Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion()));
		
		ObjectDataInputField impactField = createRatingChoiceField(Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion(Strategy.TAG_IMPACT_RATING));
		ObjectDataInputField feasibilityField = createRatingChoiceField(Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion(Strategy.TAG_FEASIBILITY_RATING));
		ObjectDataInputField prioritySummaryField = createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, new StrategyRatingSummaryQuestion(Strategy.PSEUDO_TAG_RATING_SUMMARY));

		addFieldsOnOneLine(EAM.text("Priority"), new ObjectDataInputField[] {impactField, feasibilityField, prioritySummaryField});
		
		ObjectsActionButton editProgressReportButton = createObjectsActionButton(mainWindow.getActions().getObjectsAction(ActionEditStrategyProgressReports.class), getPicker());
		ObjectDataInputField readOnlyProgressReportsList = createReadOnlyObjectList(Strategy.getObjectType(), Strategy.TAG_PROGRESS_REPORT_REFS);
		addFieldWithEditButton(EAM.text("Progress"), readOnlyProgressReportsList, editProgressReportButton);

		BudgetOverrideSubPanel budgetSubPanel = new BudgetOverrideSubPanel(getProject(), new ORef(Strategy.getObjectType(), BaseId.INVALID));
		addSubPanel(budgetSubPanel);

		addLabel(EAM.text("Budget"));
		addFieldComponent(budgetSubPanel);
		
		addField(createMultilineField(Strategy.TAG_COMMENT));
		
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_TARGETS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_GOALS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_OBJECTIVES));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}

}
