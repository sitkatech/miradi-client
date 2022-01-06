/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

public class DeleteAssumptionDoer extends ObjectsDoer
{
    @Override
    public boolean isAvailable()
    {
        if (getObjects() == null)
            return false;

        if ((getObjects().length != 1))
            return false;

        if (!(getSelectedObjectType() == ObjectType.ASSUMPTION))
            return false;

        return true;
    }

    @Override
    protected void doIt() throws Exception
    {
        if(!isAvailable())
            return;

        Assumption selectedAssumption = (Assumption)getObjects()[0];
        deleteAssumptionWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedAssumption);
    }

    public static void deleteAssumptionWithUserConfirmation(Project project, ORefList selectionHierarchy, Assumption selectedAssumption) throws CommandFailedException
    {
        Vector<String> dialogText = new Vector<String>();
        dialogText.add(EAM.text("Are you sure you want to delete this Assumption?"));
        boolean containsMoreThanOneParent = selectionHierarchy.getOverlappingRefs(selectedAssumption.findAllObjectsThatReferToUs()).size() > 1;
        if (containsMoreThanOneParent)
            dialogText.add(EAM.text("This item is shared, so will be deleted from multiple places."));

        String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
        if(!EAM.confirmDialog(EAM.text("Title|Delete"), dialogText.toArray(new String[0]), buttons))
            return;

        deleteAssumption(project, selectionHierarchy, selectedAssumption);
    }

    private static void deleteAssumption(Project project, ORefList selectionHierarchy, Assumption selectedAssumption) throws CommandFailedException
    {
        project.executeCommand(new CommandBeginTransaction());
        try
        {
            CommandVector commandsToDeleteAssumption = createDeleteCommands(project, selectionHierarchy, selectedAssumption);
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

    private static CommandVector createDeleteCommands(Project project, ORefList selectionHierarchy, Assumption assumption) throws Exception
    {
        CommandVector commandsToDeleteAssumption = new CommandVector();
        commandsToDeleteAssumption.addAll(buildDeleteDiagramFactors(project, selectionHierarchy, assumption));
        commandsToDeleteAssumption.addAll(buildRemoveCommandsForAssumptionIds(project, selectionHierarchy, assumption));
        commandsToDeleteAssumption.addAll(assumption.createCommandsToDeleteChildrenAndObject());

        return commandsToDeleteAssumption;
    }

    private static CommandVector buildRemoveCommandsForAssumptionIds(Project project, ORefList selectionHierarchy, Assumption assumption) throws Exception
    {
        return buildRemoveCommands(project, AnalyticalQuestionSchema.getObjectType(), selectionHierarchy, AnalyticalQuestion.TAG_ASSUMPTION_IDS, assumption);
    }

    private static CommandVector buildDeleteDiagramFactors(Project project, ORefList selectionHierarchy, Assumption assumption) throws Exception
    {
        CommandVector commands = new CommandVector();

        ORef analyticalQuestionRef = selectionHierarchy.getRefForType(AnalyticalQuestionSchema.getObjectType());
        ORefList assumptionDiagramFactorReferrerRefs = assumption.findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());

        for (int index = 0; index < assumptionDiagramFactorReferrerRefs.size(); ++index)
        {
            ORef assumptionDiagramFactorRef = assumptionDiagramFactorReferrerRefs.get(index);
            DiagramFactor assumptionDiagramFactor = DiagramFactor.find(project, assumptionDiagramFactorRef);

            ORefList diagramObjectsWithAssumptions = new ORefList();
            diagramObjectsWithAssumptions.addAll(assumptionDiagramFactor.findObjectsThatReferToUs(ConceptualModelDiagramSchema.getObjectType()));
            diagramObjectsWithAssumptions.addAll(assumptionDiagramFactor.findObjectsThatReferToUs(ResultsChainDiagramSchema.getObjectType()));

            for (int diagramObjectIndex = 0; diagramObjectIndex < diagramObjectsWithAssumptions.size(); ++diagramObjectIndex)
            {
                DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectsWithAssumptions.get(diagramObjectIndex));
                ORefList analyticalQuestionRefs = getDiagramObjectAnalyticalQuestions(project, diagramObject);
                if (analyticalQuestionRefs.contains(analyticalQuestionRef))
                {
                    commands.add(CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, assumptionDiagramFactorRef.getObjectId()));
                    commands.addAll(assumptionDiagramFactor.createCommandsToDeleteChildrenAndObject());
                }
            }
        }

        return commands;
    }

    private static ORefList getDiagramObjectAnalyticalQuestions(Project project, DiagramObject diagramObject)
    {
        ORefList analyticalQuestionRefs = new ORefList();
        ORefList diagramFactorRefs =  diagramObject.getAllDiagramFactorRefs();
        for (int index = 0; index < diagramFactorRefs.size(); ++index)
        {
            DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
            if (AnalyticalQuestion.is(diagramFactor.getWrappedType()))
                analyticalQuestionRefs.add(diagramFactor.getWrappedORef());
        }

        return analyticalQuestionRefs;
    }

    private static CommandVector buildRemoveCommands(Project project, int parentType, ORefList selectionHierarchy, String tag, Assumption assumption) throws Exception
    {
        CommandVector removeCommands = new CommandVector();
        ORefList referrerRefs = assumption.findObjectsThatReferToUs(parentType);
        for (int i = 0; i < referrerRefs.size(); ++i)
        {
            BaseObject referrer = project.findObject(referrerRefs.get(i));
            if (selectionHierarchy.contains(referrer.getRef()))
                removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrer, tag, assumption.getId()));
        }

        return removeCommands;
    }
}
