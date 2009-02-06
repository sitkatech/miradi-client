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

import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;

abstract public class AbstractThreatTargetTableModel extends AbstractTableModel
{
	public AbstractThreatTargetTableModel(Project projectToUse)
	{
		project = projectToUse;
		
		resetTargetAndThreats();
	}
	
	public void resetTargetAndThreats()
	{
		threatRows =  getProject().getCausePool().getDirectThreats();
		targetColumns = getOnlyTargetsInConceptualModelDiagrams().toArray(new Target[0]);
	}

	private Vector<Target> getOnlyTargetsInConceptualModelDiagrams()
	{
		Vector<Target> targetsInConceptualModelDiagrams = new Vector();
		Target[] allTargets =  getProject().getTargetPool().getTargets();
		for (int index = 0; index < allTargets.length; ++index)
		{
			ORefList diagramRefsContainingTarget = DiagramObject.getDiagramRefsContainingFactor(getProject(), allTargets[index].getRef());
			ORef conceptualModelDiagramRef = diagramRefsContainingTarget.getRefForType(ConceptualModelDiagram.getObjectType());
			if (!conceptualModelDiagramRef.isInvalid())
				targetsInConceptualModelDiagrams.add(allTargets[index]);
		}
		
		return targetsInConceptualModelDiagrams;
	}
	
	public Factor[] getThreatsSortedBy(int sortByTableColumn)
	{	
		Vector<Integer> rows = new Vector();
		for(int index = 0; index < getRowCount(); ++index)
		{
			rows.add(new Integer(index));
		}
		
		Vector unsortedRows = (Vector)rows.clone();
		Collections.sort(rows, getComparator(sortByTableColumn));
		
		if (rows.equals(unsortedRows))
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
		
		Factor threat = getDirectThreats()[threatIndex];
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
		return getDirectThreats()[threatIndex].getLabel();
	}
	
	public String getTargetName(int targetIndex)
	{
		return getTargets()[targetIndex].getLabel();
	}

	public FactorId getThreatId(int threatIndex)
	{
		return getDirectThreats()[threatIndex].getFactorId();
	}

	public FactorId getTargetId(int targetIndex)
	{
		return getTargets()[targetIndex].getFactorId();
	}
		
	public Comparator getComparator(int columnToSortOn)
	{
		return new TableModelStringComparator(this, columnToSortOn);
	}
	
	private Project project;
	protected Factor[] threatRows;
	protected Target[] targetColumns;	
}