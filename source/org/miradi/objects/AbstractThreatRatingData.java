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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.BaseObjectSchema;

import java.util.function.Function;

public abstract class AbstractThreatRatingData extends BaseObject
{
    public AbstractThreatRatingData(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schemaToUse)
    {
        super(objectManager, idToUse, schemaToUse);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return NO_OWNERS;
    }

    public ORef getThreatRef()
    {
        return getRefData(TAG_THREAT_REF);
    }

    public ORef getTargetRef()
    {
        return getRefData(TAG_TARGET_REF);
    }

    public String getComments()
    {
        return getStringData(TAG_COMMENTS);
    }

    public ChoiceItem getEvidenceConfidence()
    {
        ChoiceData data = (ChoiceData)getField(TAG_EVIDENCE_CONFIDENCE);
        ChoiceQuestion question = data.getChoiceQuestion();
        String code = data.get();
        return question.findChoiceByCode(code);
    }

    public String getEvidenceNotes()
    {
        return getStringData(TAG_EVIDENCE_NOTES);
    }

    public static AbstractThreatRatingData findOrCreateThreatRatingData(Project project, ORef threatRef, ORef targetRef) throws Exception
    {
        int objectType = getThreatRatingDataObjectType(project);
        return AbstractThreatRatingData.findOrCreateThreatRatingData(project, threatRef, targetRef, objectType);
    }

    public static AbstractThreatRatingData findOrCreateThreatRatingData(Project project, ORef threatRef, ORef targetRef, int objectType) throws Exception
    {
        AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(project, threatRef, targetRef, objectType);
        if (threatRatingData == null)
        {
            ORef threatRatingDataRef = project.createObject(objectType);
            threatRatingData = (AbstractThreatRatingData) project.findObject(threatRatingDataRef);
            threatRatingData.setData(TAG_THREAT_REF, threatRef.toJson().toString());
            threatRatingData.setData(TAG_TARGET_REF, targetRef.toJson().toString());
        }

        return threatRatingData;
    }

    public static AbstractThreatRatingData findThreatRatingData(Project project, ORef threatRef, ORef targetRef)
    {
        int objectType = getThreatRatingDataObjectType(project);
        return AbstractThreatRatingData.findThreatRatingData(project, threatRef, targetRef, objectType);
    }

    public static AbstractThreatRatingData findThreatRatingData(Project project, ORef threatRef, ORef targetRef, int objectType)
    {
        AbstractThreatRatingData foundThreatRatingData = null;

        for (ORef threatDataRef : project.getAllRefsForType(objectType))
        {
            AbstractThreatRatingData threatRatingData = (AbstractThreatRatingData) project.findObject(threatDataRef);
            if (threatRatingData.getThreatRef().equals(threatRef) && threatRatingData.getTargetRef().equals(targetRef))
            {
                foundThreatRatingData = threatRatingData;
                break;
            }

        }

        return foundThreatRatingData;
    }

    public static ORefList findThreatRatingDataRefsForThreat(Project project, ORef threatRef)
    {
        return AbstractThreatRatingData.findThreatRatingDataRefs(project, threatRef, AbstractThreatRatingData::getThreatRef);
    }

    public static ORefList findThreatRatingDataRefsForTarget(Project project, ORef targetRef)
    {
        return AbstractThreatRatingData.findThreatRatingDataRefs(project, targetRef, AbstractThreatRatingData::getTargetRef);
    }

    private static ORefList findThreatRatingDataRefs(Project project, ORef objectRef, Function<AbstractThreatRatingData, ORef> getRefFn)
    {
        ORefList threatRatingDataRefs = new ORefList();

        for (ORef threatDataRef : project.getAllRefsForType(ObjectType.THREAT_SIMPLE_RATING_DATA))
        {
            AbstractThreatRatingData threatRatingData = (AbstractThreatRatingData) project.findObject(threatDataRef);
            if (getRefFn.apply(threatRatingData).equals(objectRef))
                threatRatingDataRefs.add(threatRatingData.getRef());
        }

        for (ORef threatDataRef : project.getAllRefsForType(ObjectType.THREAT_STRESS_RATING_DATA))
        {
            AbstractThreatRatingData threatRatingData = (AbstractThreatRatingData) project.findObject(threatDataRef);
            if (getRefFn.apply(threatRatingData).equals(objectRef))
                threatRatingDataRefs.add(threatRatingData.getRef());
        }

        return threatRatingDataRefs;
    }

    private static int getThreatRatingDataObjectType(Project project)
    {
        return project.isSimpleThreatRatingMode() ? ObjectType.THREAT_SIMPLE_RATING_DATA : ObjectType.THREAT_STRESS_RATING_DATA;
    }

    public static final String TAG_THREAT_REF = "ThreatRef";
    public static final String TAG_TARGET_REF = "TargetRef";
    public static final String TAG_COMMENTS = "Comments";
    public static final String TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP = "SimpleThreatRatingCommentsMap";
    public static final String TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP = "StressBasedThreatRatingCommentsMap";
}
