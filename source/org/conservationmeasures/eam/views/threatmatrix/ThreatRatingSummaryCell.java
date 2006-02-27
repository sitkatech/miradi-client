package org.conservationmeasures.eam.views.threatmatrix;

import java.util.HashSet;

import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
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

	
	private ThreatRatingSummaryCell(ThreatMatrixTableModel modelToUse, int threatIndexToUse, int targetIndexToUse)
	{
		model = modelToUse;
		threatIndex = threatIndexToUse;
		targetIndex = targetIndexToUse;
		
		label = new UiLabel();
		add(label);
	}
	
	public void dataHasChanged()
	{
		ThreatRatingFramework framework = model.getProject().getThreatRatingFramework();
		
		HashSet bundleGroup = new HashSet();
		
		if(threatIndex >= 0)
		{
			for(int tempTargetIndex = 0; tempTargetIndex < model.getTargetCount(); ++tempTargetIndex)
			{
				if(model.isActiveCell(threatIndex, tempTargetIndex))
				{
					bundleGroup.add(framework.getBundle(model.getThreatId(threatIndex), model.getTargetId(tempTargetIndex)));
				}
			}
		}
		else
		{
			for(int tempThreatIndex = 0; tempThreatIndex < model.getThreatCount(); ++tempThreatIndex)
			{
				if(model.isActiveCell(tempThreatIndex, targetIndex))
				{
					bundleGroup.add(framework.getBundle(model.getThreatId(tempThreatIndex), model.getTargetId(targetIndex)));
				}
			}
		}
			
		ThreatRatingBundle[] bundleArray = (ThreatRatingBundle[])bundleGroup.toArray(new ThreatRatingBundle[0]);
		ThreatRatingValueOption result = framework.getSummaryOfBundles(bundleArray);
		label.setText(result.getLabel());
		setBackground(result.getColor());
		
	}
	
	ThreatMatrixTableModel model;
	int threatIndex;
	int targetIndex;
	UiLabel label;
}
