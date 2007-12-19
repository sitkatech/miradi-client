/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

abstract public class AbstractGroupBoxDoer extends LocationDoer
{
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getCommandsToUpdateGroupBoxChildren();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	
	}
	
	protected DiagramFactor getGroupBox(EAMGraphCell[] selected)
	{
		final int FIRST_INDEX = 0;
		return extractSelectedGroupBoxes(selected).get(FIRST_INDEX);
	}
	
	protected Vector<DiagramFactor>extractSelectedGroupBoxes(EAMGraphCell[] selected)
	{
		Vector<DiagramFactor> groupBoxDiagramFactors = new Vector();
		for (int i = 0; i < selected.length; ++i)
		{
			if (!selected[i].isFactor())
				continue;
			
			FactorCell factorCell = (FactorCell) selected[i];
			if (factorCell.getWrappedType() == GroupBox.getObjectType())
				groupBoxDiagramFactors.add(factorCell.getDiagramFactor());
		}
		
		return groupBoxDiagramFactors;
	}
	
	protected boolean containsAtleastOneFactor(EAMGraphCell[] selected)
	{
		return extractNonGroupBoxDiagramFactors(selected).size() > 0;
	}
		
	protected ORefList extractNonGroupBoxDiagramFactors(EAMGraphCell[] selected)
	{
		ORefList nonGroupBoxDiagramFactorRefs = new ORefList();
		for (int i = 0; i < selected.length; ++i)
		{
			if (!selected[i].isFactor())
				continue;
			
			FactorCell factorCell = (FactorCell) selected[i];
			int type = factorCell.getWrappedType();
			if (type == Target.getObjectType() || type == Cause.getObjectType() || type == Strategy.getObjectType())
				nonGroupBoxDiagramFactorRefs.add(factorCell.getDiagramFactorRef());		
		}
		
		return nonGroupBoxDiagramFactorRefs;
	}
	
	abstract protected void getCommandsToUpdateGroupBoxChildren() throws Exception;
}
