/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

public class GroupBoxInsertFactorDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (!isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if (!containsOnlyOneGroupBox(selected))
			return false;
		
		if (!containsAtleastOneFactor(selected))
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;		
		
		try
		{
			Command[] commands = getCommandsToAppendGroupBoxChildren();
			getProject().executeCommandsAsTransaction(commands);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private Command[] getCommandsToAppendGroupBoxChildren() throws ParseException
	{
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		ORefList nonGroupBoxDiagramFactorRefs = extractNonGroupBoxDiagramFactors(selected);
		DiagramFactor groupBoxDiagramFactor = getGroupBox(selected);
		ORefList groupBoxChildrenRefs = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		Vector<Command> commandsToAppend = new Vector();
		for (int i = 0; i < nonGroupBoxDiagramFactorRefs.size(); ++i)
		{
			ORef diagramfactorRef = nonGroupBoxDiagramFactorRefs.get(i);
			if (!groupBoxChildrenRefs.contains(diagramfactorRef))
				commandsToAppend.add(CommandSetObjectData.createAppendORefCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDERN_REFS, diagramfactorRef));
		}
		
		return commandsToAppend.toArray(new Command[0]);
	}
	
	private boolean containsOnlyOneGroupBox(EAMGraphCell[] selected)
	{
		return extractSelectedGroupBoxes(selected).size() == 1;		
	}
	
	private boolean containsAtleastOneFactor(EAMGraphCell[] selected)
	{
		return extractNonGroupBoxDiagramFactors(selected).size() > 0;
	}
		
	private DiagramFactor getGroupBox(EAMGraphCell[] selected)
	{
		final int FIRST_INDEX = 0;
		return extractSelectedGroupBoxes(selected).get(FIRST_INDEX);
	}
	
	private Vector<DiagramFactor>extractSelectedGroupBoxes(EAMGraphCell[] selected)
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
	
	private ORefList extractNonGroupBoxDiagramFactors(EAMGraphCell[] selected)
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
}
