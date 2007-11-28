/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.TNCThreatFormula;
import org.conservationmeasures.eam.utils.Utility;

public class ThreatSummaryColumnTableModel extends MainThreatTableModel
{
	public ThreatSummaryColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		threatFormula = new TNCThreatFormula();
	}

	public String getColumnName(int column)
	{
		return "Summary Threat Rating";
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int row, int column)
	{
		return getCalculatedThreatRatingBundleValue(row);
	}
	
	private String getCalculatedThreatRatingBundleValue(int row)
	{
		try
		{
			return Integer.toString(calculateThreatRatingBundleValue(row));
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}
	
	private int calculateThreatRatingBundleValue(int row) throws Exception
	{
		Vector<Integer> calculatedThreatRatingBundleValues = new Vector();
		for (int i = 0; i < targets.length; ++i)
		{
			Target target = targets[i];
			Factor directThreat = directThreatRows[row];
			if (!getProject().isLinked(directThreat.getFactorId(), target.getFactorId()))
				continue;
			
			FactorLink factorLink = FactorLink.findFactorLink(getProject(), getLinkRef(directThreat.getRef(), target.getRef()));
			calculatedThreatRatingBundleValues.add(factorLink.calculateThreatRatingBundleValue());
		}
		
		return threatFormula.getSummaryOfBundles(Utility.convertToIntArray(calculatedThreatRatingBundleValues));
	}

	private TNCThreatFormula threatFormula;
}
