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
package org.miradi.dialogfields;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.IgnoreCaseStringComparator;

import java.util.Collections;
import java.util.Vector;

public class TaggedObjectSetFactorListField extends ObjectReadonlyObjectListField
{
    public TaggedObjectSetFactorListField(MainWindow mainWindowToUse, DiagramObject diagramObjectToUse, ORef refToUse, String tagToUse, String uniqueIdentifier)
    {
        super(mainWindowToUse, refToUse, tagToUse, uniqueIdentifier);

        diagramObject = diagramObjectToUse;
    }

    @Override
    protected Vector<String> createSortedBaseObjectFullNameList(ORefList refList)
    {
        ORefList taggedDiagramFactorRefs = getDiagramObject().getAllTaggedDiagramFactorRefs(getORef());

        Vector<String> names = new Vector<String>();
        for (int index = 0; index < taggedDiagramFactorRefs.size(); ++index)
        {
            ORef ref = taggedDiagramFactorRefs.get(index);
            if (ref.isInvalid())
                continue;

            BaseObject object = getProject().findObject(ref);
            if(object == null)
            {
                EAM.logError("Ignored a missing object while in TaggedObjectSetFactorListField.createSortedBaseObjectFullNameList(). Ref = " + ref);
            }
            else
            {
                names.add(object.getFullName());
            }
        }

        Collections.sort(names, new IgnoreCaseStringComparator());

        return names;
    }

    private DiagramObject getDiagramObject()
    {
        return diagramObject;
    }

    private DiagramObject diagramObject;
}