/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.SimpleThreatFormula;
import org.conservationmeasures.eam.utils.Utility;

public class TargetSummaryRowTableModel extends MainThreatTableModel
{
	public TargetSummaryRowTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		threatFormula = new SimpleThreatFormula();
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
		String valueToConvert = getCalculatedTargetSummaryRatingValue(column);
		return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);
	}
	
	private String getCalculatedTargetSummaryRatingValue(int column)
	{
		try
		{
			int calculatedValue = calculateTargetSummaryRatingValue(column);
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}

	private int calculateTargetSummaryRatingValue(int column) throws Exception
	{
		Vector<Integer> calculatedTargetSummaryRatingValues = new Vector();
		for (int i = 0; i < directThreatRows.length; ++i)
		{
			Factor directThreat = directThreatRows[i];
			Target target = targets[column];
			if (!getProject().areLinked(directThreat.getRef(), target.getRef()))
				continue;
			
			FactorLink factorLink = FactorLink.find(getProject(), getLinkRef(directThreat.getRef(), target.getRef()));
			calculatedTargetSummaryRatingValues.add(factorLink.calculateThreatRatingBundleValue());
		}
		
		return threatFormula.getSummaryOfBundlesWithTwoPrimeRule(Utility.convertToIntArray(calculatedTargetSummaryRatingValues));
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return targets[column];
	}
	
	private SimpleThreatFormula threatFormula;
}
