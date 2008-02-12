/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import java.awt.Color;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.project.StressBasedThreatRatingFramework;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.views.threatmatrix.AbstractThreatTargetTableModel;

abstract public class MainThreatTableModel extends AbstractThreatTargetTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public MainThreatTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		emptyChoiceItem = new ChoiceItem("Not Specified", "", Color.WHITE);
		frameWork = new StressBasedThreatRatingFramework(getProject());
	}

	public int getRowCount()
	{
		return threatRows.length;
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
		ChoiceItem foundChoiceItem = new ThreatRatingQuestion().findChoiceByCode(valueToConvert);
		if (foundChoiceItem == null)
			return emptyChoiceItem;
		
		return foundChoiceItem;
	}
	
	private ChoiceItem emptyChoiceItem;
	protected StressBasedThreatRatingFramework frameWork;
}
