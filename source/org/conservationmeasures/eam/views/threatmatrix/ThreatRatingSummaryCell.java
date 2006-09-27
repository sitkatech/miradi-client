/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiLabel;

public class ThreatRatingSummaryCell extends JPanel
{
	public static ThreatRatingSummaryCell createThreatSummary(ThreatMatrixTableModel model, int threatIndex)
	{
		return new ThreatRatingSummaryCell(model, threatIndex, -1);
	}

	public static ThreatRatingSummaryCell createTargetSummary(ThreatMatrixTableModel model, int targetIndex)
	{
		return new ThreatRatingSummaryCell(model, -1, targetIndex);
	}

	public static ThreatRatingSummaryCell createGrandTotal(ThreatMatrixTableModel model)
	{
		return new ThreatRatingSummaryCell(model, -1, -1);
	}
	
	private ThreatRatingSummaryCell(ThreatMatrixTableModel modelToUse, int threatIndexToUse, int targetIndexToUse)
	{
		model = modelToUse;
		threatIndex = threatIndexToUse;
		targetIndex = targetIndexToUse;
		
		label = new UiLabel();
		add(label);
	}
	
	public void dataHasChanged() throws Exception
	{
		ThreatRatingValueOption result = null;

		ThreatRatingFramework framework = model.getProject().getThreatRatingFramework();
		if(threatIndex >= 0)
		{
			result = framework.getThreatThreatRatingValue(model.getThreatId(threatIndex));
		}
		else if(targetIndex >= 0)
		{
			result = framework.getTargetThreatRatingValue(model.getTargetId(targetIndex));
		}
		else
		{
			result = framework.getOverallProjectRating();
		}
			
		label.setText(result.getLabel());
		setBackground(result.getColor());
	}
	
	ThreatMatrixTableModel model;
	int threatIndex;
	int targetIndex;
	UiLabel label;
}
