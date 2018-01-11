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
package org.miradi.questions;

import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;

public class DiagramObjectTaggedObjectSetQuestion extends ObjectQuestion
{
    public DiagramObjectTaggedObjectSetQuestion(Project projectToUse, DiagramObject diagramObjectToUse)
    {
        super(getAllTaggedObjectSet(projectToUse, diagramObjectToUse));

        project = projectToUse;
        diagramObject = diagramObjectToUse;
    }

    private static TaggedObjectSet[] getAllTaggedObjectSet(Project project, DiagramObject diagramObject)
    {
        ORefSet taggedObjectSetRefSet = new ORefSet();
        Vector<TaggedObjectSet> allTaggedObjectSets = new Vector<TaggedObjectSet>();

        for (DiagramFactor diagramFactor : diagramObject.getAllDiagramFactors())
        {
            ORefList taggedObjectRefs = diagramFactor.getTaggedObjectSetRefs();
            taggedObjectSetRefSet.addAllRefs(taggedObjectRefs);
        }

        for (ORef taggedObjectSetRef : taggedObjectSetRefSet)
        {
            TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(project, taggedObjectSetRef);
            allTaggedObjectSets.add(taggedObjectSet);
        }

        return allTaggedObjectSets.toArray(new TaggedObjectSet[0]);
    }

    @Override
    public void reloadQuestion()
    {
        setObjects(getAllTaggedObjectSet(project, diagramObject));
    }

    private Project project;
    private DiagramObject diagramObject;
}
