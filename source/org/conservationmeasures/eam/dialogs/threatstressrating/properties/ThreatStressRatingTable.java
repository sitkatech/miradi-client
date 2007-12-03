/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTable;
import org.conservationmeasures.eam.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel.TargetThreatLinkTable;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(threatStressRatingTableModel);
		rebuildColumnEditorsAndRenderers();
		//TODO shouldn't set row height to constant value
		setRowHeight(25);
		setForcedPreferredScrollableViewportWidth(TargetThreatLinkTable.PREFERRED_VIEWPORT_WIDTH);
		setForcedPreferredScrollableViewportHeight(TargetThreatLinkTable.PREFERRED_VIEWPORT_HEIGHT);
	}
	
	public ThreatStressRatingTableModel getThreatStressRatingTableModel()
	{
		return (ThreatStressRatingTableModel) getModel();
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		ThreatStressRatingTableModel threatStressRatingTableModel = getThreatStressRatingTableModel();
		for (int tableColumn = 0; tableColumn < threatStressRatingTableModel.getColumnCount(); ++tableColumn)
		{
			if (threatStressRatingTableModel.isStressRatingColumn(tableColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createStressRatingQuestion(tableColumn), tableColumn);
			
			if (threatStressRatingTableModel.isContributionColumn(tableColumn))
				createComboQuestionColumn(threatStressRatingTableModel.createContributionQuestion(tableColumn), tableColumn);
			
			if (threatStressRatingTableModel.isIrreversibilityColumn(tableColumn))
				createComboQuestionColumn(threatStressRatingTableModel.createIrreversibilityQuestion(tableColumn), tableColumn);
			
			if (threatStressRatingTableModel.isThreatRatingColumn(tableColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createThreatStressRatingQuestion(tableColumn), tableColumn);
		}
	}

	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
}
