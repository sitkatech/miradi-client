/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.objecthelpers.ObjectDeepCopier;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;


public class TransferableMiradiList extends TransferableEamList
{
	public TransferableMiradiList(Project projectToUse)
	{
		super(projectToUse);
	}

	private void clear()
	{
		factorDeepCopies = new Vector();
		diagramFactorDeepCopies = new Vector();
	}
	
	public void storeData(Object[] cells)
	{
		clear();
		ObjectDeepCopier deepCopier = new ObjectDeepCopier(project);
		for (int i = 0; i < cells.length; i++) 
		{
			EAMGraphCell cell = (EAMGraphCell)cells[i];
			if (cell.isFactor())
			{
				FactorCell factorCell = ((FactorCell)cell);
				Factor factor = factorCell.getUnderlyingObject();
				Vector factorJsonStrings = deepCopier.createDeepCopy(factor);
				factorDeepCopies.addAll(factorJsonStrings);

				DiagramFactor diagramFactor = factorCell.getDiagramFactor();
				Vector diagramFactorJsonStrings = deepCopier.createDeepCopy(diagramFactor);
				diagramFactorDeepCopies.addAll(diagramFactorJsonStrings);
			}

			if (cell.isFactorLink())
			{
				//FXIME copy/paste code for link has to be done
			}
		}
	}
	
	//FIXME this is to switch between falvors while in transition 
	public boolean isEAMFlavorSupported()
	{
		return false;
	}
	
	public Vector getDiagramFactorDeepCopies()
	{
		return diagramFactorDeepCopies;
	}

	public Vector getFactorDeepCopies()
	{
		return factorDeepCopies;
	}
	
	Vector factorDeepCopies;
	Vector diagramFactorDeepCopies;
}
