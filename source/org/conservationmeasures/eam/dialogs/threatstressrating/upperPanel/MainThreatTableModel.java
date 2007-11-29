/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

abstract public class MainThreatTableModel extends AbstractTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public MainThreatTableModel(Project projectToUse)
	{
		project = projectToUse;
		directThreatRows =  getProject().getCausePool().getDirectThreats();
		targets = getProject().getTargetPool().getTargets();
	}
	
	public Factor getDirectThreat(int row)
	{
		return directThreatRows[row];
	}
	
	public int getRowCount()
	{
		return directThreatRows.length;
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
			return null;
		
		return Integer.toString(calculatedValue);
	}
	
	protected ChoiceItem convertToChoiceItem(String fieldTag, String valueToConvert)
	{
		return new StatusQuestion(fieldTag).findChoiceByCode(valueToConvert);
	}
	
	private Project project;
	protected Factor[] directThreatRows;
	protected Target[] targets;
}
