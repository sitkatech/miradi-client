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

package org.miradi.views.diagram.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ResultReportSchema;
import org.miradi.views.diagram.CreateAnnotationDoer;

public class CreateResultReportDoer extends CreateAnnotationDoer
{
    @Override
    public boolean isAvailable()
    {
        if(!super.isAvailable())
            return false;

        final ORef actualSelectedRef = getSelectedRef();
        return canHaveResultReports(actualSelectedRef);
    }

    private boolean canHaveResultReports(ORef selectedRef)
    {
        if (IntermediateResult.is(selectedRef))
            return true;

        if (ThreatReductionResult.is(selectedRef))
            return true;

        return BiophysicalResult.is(selectedRef);
    }

    @Override
    public BaseObject getSelectedParentFactor()
    {
        if (getPicker() == null)
            return null;

        ORefList[] selectedHierarchies = getPicker().getSelectedHierarchies();
        if (selectedHierarchies.length == 0)
            return null;

        ORefList selectionRefs = selectedHierarchies[0];
        return extractResultReportParentCandidate(getProject(), selectionRefs, getAnnotationType());
    }

    public static BaseObject extractResultReportParentCandidate(Project projectToUse, ORefList selectionRefs, int objectTypeToRemove)
    {
        removeFirstRefInPlace(selectionRefs, objectTypeToRemove);
        if (selectionRefs.isEmpty())
            return null;

        ORef ref = selectionRefs.getFirstElement();
        if (ref.isInvalid())
            return null;

        return BaseObject.find(projectToUse, ref);
    }

    private static void removeFirstRefInPlace(ORefList selectionRefs, int objectTypeToRemove)
    {
        ORef firstInstanceOfTypeToRemove = selectionRefs.getRefForType(objectTypeToRemove);
        selectionRefs.remove(firstInstanceOfTypeToRemove);
    }

    @Override
    public String getAnnotationListTag()
    {
        return BaseObject.TAG_RESULT_REPORT_REFS;
    }

    @Override
    public int getAnnotationType()
    {
        return ResultReportSchema.getObjectType();
    }
}