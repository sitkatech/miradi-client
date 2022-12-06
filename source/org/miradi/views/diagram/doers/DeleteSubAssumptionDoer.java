/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.*;
import org.miradi.utils.CommandVector;
import org.miradi.views.ObjectsDoer;

import java.text.ParseException;
import java.util.Vector;

public class DeleteSubAssumptionDoer extends ObjectsDoer
{
    @Override
    public boolean isAvailable()
    {
        if (getObjects() == null)
            return false;

        if ((getObjects().length != 1))
            return false;

        if (!(getSelectedObjectType() == ObjectType.SUB_ASSUMPTION))
            return false;

        return true;
    }

    @Override
    protected void doIt() throws Exception
    {
        if(!isAvailable())
            return;

        SubAssumption selectedSubAssumption = (SubAssumption)getObjects()[0];
        deleteSubAssumptionWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedSubAssumption);
    }

    public static void deleteSubAssumptionWithUserConfirmation(Project project, ORefList selectionHierarchy, SubAssumption selectedAssumption) throws CommandFailedException
    {
        Vector<String> dialogText = new Vector<String>();
        dialogText.add(EAM.text("Are you sure you want to delete this Subassumption?"));
        boolean containsMoreThanOneParent = selectionHierarchy.getOverlappingRefs(selectedAssumption.findAllObjectsThatReferToUs()).size() > 1;
        if (containsMoreThanOneParent)
            dialogText.add(EAM.text("This item is shared, so will be deleted from multiple places."));

        String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
        if(!EAM.confirmDialog(EAM.text("Title|Delete"), dialogText.toArray(new String[0]), buttons))
            return;

        deleteSubAssumption(project, selectionHierarchy, selectedAssumption);
    }

    private static void deleteSubAssumption(Project project, ORefList selectionHierarchy, SubAssumption selectedSubAssumption) throws CommandFailedException
    {
        project.executeCommand(new CommandBeginTransaction());
        try
        {
            CommandVector commandsToDeleteAssumption = createDeleteCommands(project, selectionHierarchy, selectedSubAssumption);
            executeDeleteCommands(project, commandsToDeleteAssumption);
        }
        catch(Exception e)
        {
            EAM.logException(e);
            throw new CommandFailedException(e);
        }
        finally
        {
            project.executeCommand(new CommandEndTransaction());
        }
    }

	private static void executeDeleteCommands(Project project, CommandVector commands) throws ParseException, CommandFailedException
	{
		project.executeCommands(commands);
	}

    private static CommandVector createDeleteCommands(Project project, ORefList selectionHierarchy, SubAssumption subAssumption) throws Exception
    {
        CommandVector commandsToDeleteSubAssumption = new CommandVector();
        commandsToDeleteSubAssumption.addAll(buildDeleteDiagramFactors(project, selectionHierarchy, subAssumption));
        commandsToDeleteSubAssumption.addAll(buildRemoveCommandsForSubAssumptionIds(project, selectionHierarchy, subAssumption));
        commandsToDeleteSubAssumption.addAll(subAssumption.createCommandsToDeleteChildrenAndObject());

        return commandsToDeleteSubAssumption;
    }

    private static CommandVector buildRemoveCommandsForSubAssumptionIds(Project project, ORefList selectionHierarchy, SubAssumption subAssumption) throws Exception
    {
        return buildRemoveCommands(project, AssumptionSchema.getObjectType(), selectionHierarchy, Assumption.TAG_SUB_ASSUMPTION_IDS, subAssumption);
    }

    private static CommandVector buildDeleteDiagramFactors(Project project, ORefList selectionHierarchy, SubAssumption subAssumption) throws Exception
    {
        CommandVector commands = new CommandVector();

        ORef assumptionRef = selectionHierarchy.getRefForType(AssumptionSchema.getObjectType());
        ORefList subAssumptionDiagramFactorReferrerRefs = subAssumption.findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());

        for (int index = 0; index < subAssumptionDiagramFactorReferrerRefs.size(); ++index)
        {
            ORef subAssumptionDiagramFactorRef = subAssumptionDiagramFactorReferrerRefs.get(index);
            DiagramFactor subAssumptionDiagramFactor = DiagramFactor.find(project, subAssumptionDiagramFactorRef);

            ORefList diagramObjectsWithSubAssumptions = new ORefList();
            diagramObjectsWithSubAssumptions.addAll(subAssumptionDiagramFactor.findObjectsThatReferToUs(ConceptualModelDiagramSchema.getObjectType()));
            diagramObjectsWithSubAssumptions.addAll(subAssumptionDiagramFactor.findObjectsThatReferToUs(ResultsChainDiagramSchema.getObjectType()));

            for (int diagramObjectIndex = 0; diagramObjectIndex < diagramObjectsWithSubAssumptions.size(); ++diagramObjectIndex)
            {
                DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectsWithSubAssumptions.get(diagramObjectIndex));
                ORefList assumptionRefs = getDiagramObjectAssumptions(project, diagramObject);
                if (assumptionRefs.contains(assumptionRef))
                {
                    commands.add(CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, subAssumptionDiagramFactorRef.getObjectId()));
                    commands.addAll(subAssumptionDiagramFactor.createCommandsToDeleteChildrenAndObject());
                }
            }
        }

        return commands;
    }

    private static ORefList getDiagramObjectAssumptions(Project project, DiagramObject diagramObject)
    {
        ORefList assumptionRefs = new ORefList();
        ORefList diagramFactorRefs =  diagramObject.getAllDiagramFactorRefs();
        for (int index = 0; index < diagramFactorRefs.size(); ++index)
        {
            DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
            if (Assumption.is(diagramFactor.getWrappedType()))
                assumptionRefs.add(diagramFactor.getWrappedORef());
        }

        return assumptionRefs;
    }

    private static CommandVector buildRemoveCommands(Project project, int parentType, ORefList selectionHierarchy, String tag, SubAssumption subAssumption) throws Exception
    {
        CommandVector removeCommands = new CommandVector();
        ORefList referrerRefs = subAssumption.findObjectsThatReferToUs(parentType);
        for (int i = 0; i < referrerRefs.size(); ++i)
        {
            BaseObject referrer = project.findObject(referrerRefs.get(i));
            if (selectionHierarchy.contains(referrer.getRef()))
                removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrer, tag, subAssumption.getId()));
        }

        return removeCommands;
    }
}
