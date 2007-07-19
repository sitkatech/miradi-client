/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.ObjectDeepCopier;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class Copy extends ViewDoer
{
	public Copy()
	{
		super();
	}
	
	public Copy(Project project)
	{
		setProject(project);
	}
	
	public boolean isAvailable()
	{
		if (! getProject().isOpen())
			return false;

		if (! isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{ 
		copySelectedItemsToEAMClipboard();
		getProject().getDiagramClipboard().incrementPasteCount();
		copySelectedItemsToMiradiClipboard();
	}

	private void copySelectedItemsToMiradiClipboard()
	{
		//FIXME temp swith beween transitions of two flavors
		if (TransferableEamList.IS_EAM_FLAVOR)
			return;

		EAMGraphCell[] selectedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;

		Vector factors = new Vector();
		for (int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell graphCell = selectedCells[i];
			if (graphCell.isFactor())
				factors.addAll(getJsonAsStringDeepCopy((FactorCell)graphCell));
		}
		
		String[] factorsAsJsonStrings = (String[]) factors.toArray(new String[0]);
		TransferableMiradiList eamList = new TransferableMiradiList(getProject().getFilename(), factorsAsJsonStrings);		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(eamList, getMainWindow());
	}

	private Vector getJsonAsStringDeepCopy(FactorCell cell)
	{
		Factor factor = (Factor) getProject().findObject(cell.getWrappedORef());
		ObjectDeepCopier deepCopier = new ObjectDeepCopier(getProject());
		
		return deepCopier.createDeepCopy(factor);
	}

	public void copySelectedItemsToEAMClipboard()
	{
		//FIXME temp swith beween transitions of two flavors
		if (! TransferableEamList.IS_EAM_FLAVOR)
			return;
		
		EAMGraphCell[] selectedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;
		TransferableEamList eamList = new TransferableEamList(getProject().getFilename(), selectedCells);
		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(eamList, getMainWindow());
	}
}
