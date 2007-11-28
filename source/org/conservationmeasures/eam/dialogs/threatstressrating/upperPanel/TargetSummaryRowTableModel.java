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

public class TargetSummaryRowTableModel extends MainThreatTableModel
{
	public TargetSummaryRowTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		tNCThreatFormula = new TNCThreatFormula();
	}

	public String getColumnTag(int column)
	{
		return "";
	}

	public int getColumnCount()
	{
		return targets.length;
	}

	public Object getValueAt(int row, int column)
	{
		return getCalculatedThreatRatingBundleValue(column);
	}
	
	private String getCalculatedThreatRatingBundleValue(int column)
	{
		try
		{
			return Integer.toString(calculateThreatRatingBundleValue(column));
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}
	
	private int calculateThreatRatingBundleValue(int column) throws Exception
	{
		Vector<Integer> calculatedThreatRatingBundleValues = new Vector();
		for (int i = 0; i < directThreatRows.length; ++i)
		{
			Factor directThreat = directThreatRows[i];
			Target target = targets[column];
			if (!getProject().isLinked(directThreat.getFactorId(), target.getFactorId()))
				continue;
			
			FactorLink factorLink = FactorLink.findFactorLink(getProject(), getLinkRef(directThreat.getRef(), target.getRef()));
			calculatedThreatRatingBundleValues.add(factorLink.calculateThreatRatingBundleValue());
		}
		
		return tNCThreatFormula.getSummaryOfBundles(Utility.convertToIntArray(calculatedThreatRatingBundleValues));
	}
	
	private TNCThreatFormula tNCThreatFormula;
}
