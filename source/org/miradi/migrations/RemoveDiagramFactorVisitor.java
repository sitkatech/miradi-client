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

package org.miradi.migrations;

import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;

import java.util.HashMap;

public class RemoveDiagramFactorVisitor
{
    public RemoveDiagramFactorVisitor(RawProject rawProjectToUse, int typeToVisit)
    {
        rawProject = rawProjectToUse;
        type = typeToVisit;
    }

	public RawProject getRawProject() { return rawProject; }

	public int getTypeToVisit()
	{
		return type;
	}


	public MigrationResult visit() throws Exception
	{
        return removeDiagramFactors(getTypeToVisit());
	}

    private String safeGetTag(RawObject rawObject, String tag)
	{
		if (rawObject.hasValue(tag))
			return rawObject.getData(tag);

		return "";
	}

    private MigrationResult removeDiagramFactors(int wrappedFactorObjectType) throws Exception
    {
        MigrationResult migrationResult = MigrationResult.createUninitializedResult();

        ORefSet diagramFactorRefs = getDiagramFactors(wrappedFactorObjectType);

        if (diagramFactorRefs.hasData())
        {
            removeDiagramFactorRefs(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, diagramFactorRefs);
            removeDiagramFactorRefs(ObjectType.RESULTS_CHAIN_DIAGRAM, diagramFactorRefs);

            for (ORef diagramFactorRef : diagramFactorRefs)
            {
                getRawProject().deleteRawObject(diagramFactorRef);
            }

            HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
            tokenReplacementMap.put("%diagramFactorCount", Integer.toString(diagramFactorRefs.size()));
            tokenReplacementMap.put("%objectType", getUserFriendlyObjectName(wrappedFactorObjectType));
            final String dataLossMessage = EAM.substitute(EAM.text("%diagramFactorCount %objectType diagram factor(s) will be removed."), tokenReplacementMap);
            migrationResult.addDataLoss(dataLossMessage);
        }

        getRawProject().deletePoolWithData(wrappedFactorObjectType);

        return migrationResult;
    }

    private ORefSet getDiagramFactors(int wrappedFactorObjectType)
    {
        ORefSet diagramFactorRefs = new ORefSet();

        if (getRawProject().containsAnyObjectsOfType(ObjectType.DIAGRAM_FACTOR))
        {
            ORefList diagramFactorsToCheck = getRawProject().getAllRefsForType(ObjectType.DIAGRAM_FACTOR);
            for (ORef diagramFactorToCheck : diagramFactorsToCheck)
            {
                RawObject rawDiagramFactor = getRawProject().findObject(diagramFactorToCheck);
                if (rawDiagramFactor != null)
                {
                    String wrappedFactorRefAsString = safeGetTag(rawDiagramFactor, TAG_WRAPPED_REF);
                    ORef wrappedFactorRef = ORef.createFromString(wrappedFactorRefAsString);
                    if (wrappedFactorRef.getObjectType() == wrappedFactorObjectType)
                    {
                        diagramFactorRefs.add(diagramFactorToCheck);
                    }
                }
            }
        }

        return diagramFactorRefs;
    }

    private void removeDiagramFactorRefs(int diagramObjectType, ORefSet diagramFactorRefs) throws Exception
    {
        if (getRawProject().containsAnyObjectsOfType(diagramObjectType))
        {
            ORefList diagramRefsToCheck = getRawProject().getAllRefsForType(diagramObjectType);
            for (ORef diagramRefToCheck : diagramRefsToCheck)
            {
                RawObject rawDiagram = getRawProject().findObject(diagramRefToCheck);
                if (rawDiagram != null)
                {
                    removeDiagramFactorRefs(rawDiagram, diagramFactorRefs);
                }
            }
        }
    }

    private void removeDiagramFactorRefs(RawObject rawDiagram, ORefSet diagramFactorRefs) throws Exception
    {
        String diagramFactorIdsAsString = safeGetTag(rawDiagram, TAG_DIAGRAM_FACTOR_IDS);
        if (!diagramFactorIdsAsString.isEmpty())
        {
            IdList diagramFactorIdList = new IdList(ObjectType.DIAGRAM_FACTOR, diagramFactorIdsAsString);
            int diagramFactorCountBefore = diagramFactorIdList.size();
            diagramFactorIdList.subtract(diagramFactorRefs.toIdList(ObjectType.DIAGRAM_FACTOR));
            int diagramFactorCountAfter = diagramFactorIdList.size();
            if (diagramFactorCountBefore != diagramFactorCountAfter)
            {
                rawDiagram.setData(TAG_DIAGRAM_FACTOR_IDS, diagramFactorIdList.toString());
            }
        }
    }

    private String getUserFriendlyObjectName(int objectType) throws Exception
    {
        switch(objectType)
        {
            case ObjectType.ANALYTICAL_QUESTION:
                return EAM.text("Analytical Question");
            case ObjectType.ASSUMPTION:
                return EAM.text("Assumption");
        }

        throw new Exception("getUserFriendlyObjectName called for unexpected object type " + objectType);
    }

	private final RawProject rawProject;
	private final int type;

	// diagram / diagram factor fields
	public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
}
