/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.project;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TaggedObjectSet;

import java.util.HashMap;
import java.util.Vector;

public class DiagramFactorTaggedObjectSetHelper
{
    public DiagramFactorTaggedObjectSetHelper(Project projectToUse)
    {
        project = projectToUse;
    }

    public Vector<CommandSetObjectData> createCommandsToTagDiagramFactor(DiagramFactor diagramFactor, TaggedObjectSet taggedObjectSet) throws Exception
    {
        Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();

        CommandSetObjectData commandToTagDiagramFactor = CommandSetObjectData.createAppendORefCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, taggedObjectSet.getRef());
        commands.add(commandToTagDiagramFactor);

        return commands;
    }

    public Vector<CommandSetObjectData> createCommandsToUntagDiagramFactor(DiagramFactor diagramFactor, TaggedObjectSet taggedObjectSet) throws Exception
    {
        Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();

        CommandSetObjectData commandToUntagDiagramFactor = CommandSetObjectData.createRemoveORefCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, taggedObjectSet.getRef());
        commands.add(commandToUntagDiagramFactor);

        Vector<CommandSetObjectData> commandsToRemoveSelectedTaggedObjectSetFromDiagramObjects = createCommandsToRemoveSelectedTaggedObjectSetFromDiagramObjects(diagramFactor, taggedObjectSet);
        commands.addAll(commandsToRemoveSelectedTaggedObjectSetFromDiagramObjects);

        return commands;
    }

    public Vector<CommandSetObjectData> createCommandsToUntagDiagramFactors(DiagramFactor[] diagramFactors, TaggedObjectSet taggedObjectSet) throws Exception
    {
        Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();

        for (DiagramFactor diagramFactor : diagramFactors)
        {
            CommandSetObjectData commandToUntagDiagramFactor = CommandSetObjectData.createRemoveORefCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, taggedObjectSet.getRef());
            commands.add(commandToUntagDiagramFactor);
        }

        Vector<CommandSetObjectData> commandsToRemoveSelectedTaggedObjectSetFromDiagramObjects = createCommandsToRemoveSelectedTaggedObjectSetsFromDiagramObjects(diagramFactors, new ORefSet(taggedObjectSet));
        commands.addAll(commandsToRemoveSelectedTaggedObjectSetFromDiagramObjects);

        return commands;
    }

    public Vector<CommandSetObjectData> createCommandsToRemoveSelectedTaggedObjectSetsFromDiagramObjects(DiagramFactor[] diagramFactors, ORefSet taggedObjectRefSet) throws Exception
    {
        Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();

        ORefList diagramFactorRefs = new ORefList();
        for (DiagramFactor diagramFactor : diagramFactors)
        {
            diagramFactorRefs.add(diagramFactor.getRef());
        }

        HashMap<DiagramObject, ORefList> diagramObjectTaggedObjectSetRefList = new HashMap<DiagramObject, ORefList>();

        for (ORef taggedObjectSetRef : taggedObjectRefSet)
        {
            TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);

            for (DiagramFactor diagramFactor : diagramFactors)
            {
                for (ORef diagramObjectRef : diagramFactor.findDiagramsThatReferToUs())
                {
                    DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRef);
                    if (shouldRemoveTaggedObjectSetFromDiagramSelectedTaggedObjectSetRefs(diagramObject, diagramFactorRefs, taggedObjectSet))
                    {
                        if (!diagramObjectTaggedObjectSetRefList.containsKey(diagramObject))
                            diagramObjectTaggedObjectSetRefList.put(diagramObject, new ORefList());

                        ORefList currentTaggedObjectSetRefList = diagramObjectTaggedObjectSetRefList.get(diagramObject);
                        if (!currentTaggedObjectSetRefList.contains(taggedObjectSet.getRef()))
                        {
                            currentTaggedObjectSetRefList.add(taggedObjectSet.getRef());
                            diagramObjectTaggedObjectSetRefList.put(diagramObject, currentTaggedObjectSetRefList);
                        }
                    }
                }
            }
        }

        for (DiagramObject diagramObject: diagramObjectTaggedObjectSetRefList.keySet())
        {
            CommandSetObjectData removeTagsFromDiagramObject =  CommandSetObjectData.createRemoveORefListCommand(diagramObject, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, diagramObjectTaggedObjectSetRefList.get(diagramObject));
            commands.add(removeTagsFromDiagramObject);
        }

        return commands;
    }

    private boolean shouldRemoveTaggedObjectSetFromDiagramSelectedTaggedObjectSetRefs(DiagramObject diagramObject, ORefList diagramFactorRefsToIgnore, TaggedObjectSet taggedObjectSet)
    {
        if (!diagramObject.getSelectedTaggedObjectSetRefs().contains(taggedObjectSet.getRef()))
            return false;

        for (DiagramFactor diagramFactorToCheck : diagramObject.getAllDiagramFactors())
        {
            if (!diagramFactorRefsToIgnore.contains(diagramFactorToCheck.getRef()))
                if (diagramFactorToCheck.getTaggedObjectSetRefs().contains(taggedObjectSet.getRef()))
                    return false;
        }

        return true;
    }

    private Vector<CommandSetObjectData> createCommandsToRemoveSelectedTaggedObjectSetFromDiagramObjects(DiagramFactor diagramFactor, TaggedObjectSet taggedObjectSet) throws Exception
    {
        return createCommandsToRemoveSelectedTaggedObjectSetsFromDiagramObjects(new DiagramFactor[] {diagramFactor}, new ORefSet(taggedObjectSet.getRef()));
    }

    private Project getProject()
    {
        return project;
    }

    private Project project;
}
