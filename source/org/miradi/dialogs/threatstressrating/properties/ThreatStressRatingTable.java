/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.dialogs.threatstressrating.ThreatStressRatingTableModel;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(threatStressRatingTableModel);
		rebuildColumnEditorsAndRenderers();
		//TODO shouldn't set row height to constant value
		setRowHeight(25);
		setForcedPreferredScrollableViewportWidth(PREFERRED_VIEWPORT_WIDTH);
		setForcedPreferredScrollableViewportHeight(PREFERRED_VIEWPORT_HEIGHT);
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
	public static final int PREFERRED_VIEWPORT_WIDTH = 500;
	public static final int PREFERRED_VIEWPORT_HEIGHT = 100;
}
