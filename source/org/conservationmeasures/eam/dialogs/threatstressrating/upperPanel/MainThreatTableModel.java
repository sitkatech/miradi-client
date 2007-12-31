/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.awt.Color;

import org.conservationmeasures.eam.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.StressBasedThreatRatingFramework;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ThreatRatingQuestion;
import org.conservationmeasures.eam.utils.ColumnTagProvider;
import org.conservationmeasures.eam.views.threatmatrix.AbstractDirectThreatTargetTableModel;

abstract public class MainThreatTableModel extends AbstractDirectThreatTargetTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public MainThreatTableModel(Project projectToUse)
	{
		project = projectToUse;
		resetTargetAndThreats();
		emptyChoiceItem = new ChoiceItem("Not Specified", "", Color.WHITE);
		frameWork = new StressBasedThreatRatingFramework(getProject());
	}

	private void resetTargetAndThreats()
	{
		threatRows =  getProject().getCausePool().getDirectThreats();
		targetColumns = getProject().getTargetPool().getTargets();
	}
	
	public Factor getDirectThreat(int row)
	{
		return threatRows[row];
	}
	
	public int getRowCount()
	{
		return threatRows.length;
	}

	public Project getProject()
	{
		return project;
	}

	public ORef getLinkRef(Factor from, Factor to)
	{
		return getProject().getFactorLinkPool().getLinkedRef(from, to);
	}
	
	protected String convertIntToString(int calculatedValue)
	{
		if (calculatedValue == 0)
			return "";
		
		return Integer.toString(calculatedValue);
	}
	
	protected ChoiceItem convertToChoiceItem(String fieldTag, String valueToConvert)
	{
		ChoiceItem foundChoiceItem = new ThreatRatingQuestion(fieldTag).findChoiceByCode(valueToConvert);
		if (foundChoiceItem == null)
			return emptyChoiceItem;
		
		return foundChoiceItem;
	}
	
	private ChoiceItem emptyChoiceItem;
	private Project project;
	protected Factor[] threatRows;
	protected Target[] targetColumns;
	protected StressBasedThreatRatingFramework frameWork;
}
