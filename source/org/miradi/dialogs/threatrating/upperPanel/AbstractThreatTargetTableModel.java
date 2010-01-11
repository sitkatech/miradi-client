/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.threatrating.upperPanel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.project.threatrating.StressBasedThreatRatingFramework;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.SortDirectionQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.ColumnTagProvider;

abstract public class AbstractThreatTargetTableModel extends AbstractTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public AbstractThreatTargetTableModel(Project projectToUse)
	{
		project = projectToUse;
		
		frameWork = new StressBasedThreatRatingFramework(getProject());
		resetTargetAndThreats();
	}
	
	private void resetTargetAndThreats()
	{
		threatRows =  getProject().getCausePool().getDirectThreats();
		targetColumns = getOnlyTargetsInConceptualModelDiagrams(getProject()).toArray(new Target[0]);
	}

	public static Vector<Target> getOnlyTargetsInConceptualModelDiagrams(Project projectToUse)
	{
		Vector<Target> targetsInConceptualModelDiagrams = new Vector();
		Target[] allTargets =  projectToUse.getTargetPool().getSortedTargets();
		for (int index = 0; index < allTargets.length; ++index)
		{
			ORefList diagramRefsContainingTarget = DiagramObject.getDiagramRefsContainingFactor(projectToUse, allTargets[index].getRef());
			ORef conceptualModelDiagramRef = diagramRefsContainingTarget.getRefForType(ConceptualModelDiagram.getObjectType());
			if (!conceptualModelDiagramRef.isInvalid())
				targetsInConceptualModelDiagrams.add(allTargets[index]);
		}
		
		return targetsInConceptualModelDiagrams;
	}
	
	protected static String convertIntToString(int calculatedValue)
	{
		if (calculatedValue == 0)
			return "";
		
		return Integer.toString(calculatedValue);
	}

	public static ChoiceItem convertThreatRatingCodeToChoiceItem(int rawThreatRatingCode)
	{
		String safeThreatRatingCodeAsString = convertIntToString(rawThreatRatingCode);
		return convertThreatRatingCodeToChoiceItem(safeThreatRatingCodeAsString);
	}

	public static ChoiceItem convertThreatRatingCodeToChoiceItem(String valueToConvert)
	{
		ChoiceItem foundChoiceItem = new ThreatRatingQuestion().findChoiceByCode(valueToConvert);
		if (foundChoiceItem == null)
			return new EmptyChoiceItem();
		
		return foundChoiceItem;
	}

	public Factor[] getThreatsSortedBy(int sortByTableColumn, String sortDirectionCode)
	{	
		Vector<Integer> rows = new Vector();
		for(int index = 0; index < getRowCount(); ++index)
		{
			rows.add(new Integer(index));
		}
		
		Collections.sort(rows, getComparator(sortByTableColumn));
		if (sortDirectionCode.equals(SortDirectionQuestion.REVERSED_SORT_ORDER_CODE))
			Collections.reverse(rows);
		
		Vector<Factor> newSortedThreatList = new Vector();
		for(int index = 0; index < rows.size(); ++index)
		{
			int nextExistingRowIndex = rows.get(index).intValue();
			newSortedThreatList.add(getDirectThreat(nextExistingRowIndex));
		}
		
		return newSortedThreatList.toArray(new Factor[0]);
	}
	
	public boolean isPopupSupportableCell(int row, int modelColumn)
	{
		return true;
	}
    	
	public Project getProject()
	{
		return project;
	}
	
	public boolean isActiveCell(int threatIndex, int targetIndex)
	{
		if(threatIndex < 0 || targetIndex < 0)
			return false;
		
		Factor threat = getThreat(threatIndex);
		Factor target = getTargets()[targetIndex];
		return getProject().areLinked(threat, target);
	}
	
	public void setThreats(Factor[] threats)
	{
		threatRows = threats;
		fireTableDataChanged();
	}
	
	protected Factor[] getTargets()
	{
		return targetColumns;
	}
	
	protected Factor[] getDirectThreats()
	{
		return threatRows;
	}
	
	public Factor getDirectThreat(int row)
	{
		return threatRows[row];
	}
	
	public Target getTarget(int modelColumn)
	{
		return targetColumns[modelColumn];
	}

	public int getTargetCount()
	{
		return getTargets().length;
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}
	
	public String getThreatName(int threatIndex)
	{
		return getThreat(threatIndex).getLabel();
	}
	
	public String getTargetName(int targetIndex)
	{
		return getTargets()[targetIndex].getLabel();
	}

	public ORef getThreatRef(int threatIndex)
	{
		return getThreat(threatIndex).getRef();
	}

	public String getColumnGroupCode(int column)
	{
		return targetColumns[column].getRef().toString();
	}

	public int getProportionShares(int row)
	{
		return 1;
	}

	public boolean areBudgetValuesAllocated(int row)
	{
		return false;
	}

	public ORef getLinkRef(Factor from, Factor to)
	{
		return getProject().getFactorLinkPool().getLinkedRef(from, to);
	}

	public Factor getThreat(int row)
	{
		return getDirectThreats()[row];
	}

	public ORef getTargetRef(int targetIndex)
	{
		return getTargets()[targetIndex].getRef();
	}
		
	public Comparator getComparator(int columnToSortOn)
	{
		return new TableModelStringComparator(this, columnToSortOn);
	}
	
	public abstract String getUniqueTableModelIdentifier();
	
	private Project project;
	protected Factor[] threatRows;
	protected Target[] targetColumns;
	protected StressBasedThreatRatingFramework frameWork;
}