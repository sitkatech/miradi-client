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

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.project.threatrating.ThreatRatingFramework;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.SortableTableModel;

abstract public class AbstractThreatTargetTableModel extends SortableTableModel
{
	public AbstractThreatTargetTableModel(Project projectToUse)
	{
		project = projectToUse;
		
		resetTargetAndThreats();
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}
	
	private void resetTargetAndThreats()
	{
		threatRows =  getProject().getCausePool().getDirectThreats();
		targetColumns = getOnlyTargetsInConceptualModelDiagrams(getProject()).toArray(new Target[0]);
	}

	public static Set<Target> getOnlyTargetsInConceptualModelDiagrams(Project projectToUse)
	{
		// TODO: Seems like it would be more efficient to loop through CM's, getting targets
		HashSet<Target> targetsInConceptualModelDiagrams = new HashSet<Target>();
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
	
	@Override
	public void setSortedRowIndexes(Vector<Integer> sortedRowIndexes)
	{
		Vector<Factor> newSortedThreatList = new Vector<Factor>();
		for(int index = 0; index < sortedRowIndexes.size(); ++index)
		{
			int nextExistingRowIndex = sortedRowIndexes.get(index).intValue();
			newSortedThreatList.add(getDirectThreat(nextExistingRowIndex));
		}
		
		threatRows = newSortedThreatList.toArray(new Factor[0]);
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
		throw new RuntimeException("getProportionShares has not been implemented by AbstractThreatTargetTableModel");
	}

	public boolean areBudgetValuesAllocated(int row)
	{
		throw new RuntimeException("areBudgetValuesAllocated has not been implemented by AbstractThreatTargetTableModel");
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
	
	protected ChoiceQuestion getThreatRatingQuestion()
	{
		return getProject().getQuestion(ThreatRatingQuestion.class);
	}
	
	public ORefList getObjectHiearchy(int row, int column)
	{
		throw new RuntimeException("Method is currently unused and has no implementation");
	}
	
	public ThreatRatingFramework getFramework()
	{
		return getProject().getThreatRatingFramework();
	}
	
	private Project project;
	protected Factor[] threatRows;
	protected Target[] targetColumns;
}