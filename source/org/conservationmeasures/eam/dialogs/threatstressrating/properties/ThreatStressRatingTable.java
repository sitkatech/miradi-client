/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTable;
import org.conservationmeasures.eam.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(threatStressRatingTableModel);
		rebuildColumnEditorsAndRenderers();
		//TODO 
		setRowHeight(20);
		setForcedPreferredScrollableViewportHeight(100);
		setForcedPreferredScrollableViewportWidth(400);
	}
	
	public ThreatStressRatingTableModel getThreatStressRatingTableModel()
	{
		return (ThreatStressRatingTableModel) getModel();
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		for (int i = 0; i < getThreatStressRatingTableModel().getColumnCount(); ++i)
		{
			createContributionColumn(i);
			creatIrreversibilityColumn(i);
		}
	}

	private void createContributionColumn(int column)
	{
		if (!getThreatStressRatingTableModel().isContributionColumn(column))
			return;
		
		StatusQuestion contributionQuestion = new StatusQuestion(ThreatStressRating.TAG_CONTRIBUTION);
		createComboColumn(contributionQuestion.getChoices(), column);
	}
	
	private void creatIrreversibilityColumn(int column)
	{
		if (!getThreatStressRatingTableModel().isIrreversibilityColumn(column))
			return;
		
		StatusQuestion irreversibilityQuestion = new StatusQuestion(ThreatStressRating.TAG_IRREVERSIBILITY);
		createComboColumn(irreversibilityQuestion.getChoices(), column);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
}
