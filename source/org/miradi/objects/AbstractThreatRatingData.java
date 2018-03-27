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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.schemas.BaseObjectSchema;

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

    public static String createKey(ORef threatRef, ORef targetRef)
    {
        return threatRef.toString() + targetRef.toString();
    }

    protected abstract String getThreatRatingCommentsMapTag();

    private CodeToUserStringMap getThreatRatingCommentsMap()
    {
        String commentsTag = getThreatRatingCommentsMapTag();
        return getCodeToUserStringMapData(commentsTag);
    }

    public String findComment(ORef threatRef, ORef targetRef)
    {
        String threatTargetRefsAsKey = createKey(threatRef, targetRef);
        CodeToUserStringMap commentsMap = getThreatRatingCommentsMap();
        return commentsMap.getUserString(threatTargetRefsAsKey);
    }

    public void updateComment(ORef threatRef, ORef targetRef, String comments) throws Exception
    {
        CodeToUserStringMap commentsMap = getThreatRatingCommentsMap();
        String threatTargetKey = createKey(threatRef, targetRef);
        commentsMap.putUserString(threatTargetKey, comments);
        String commentsTag = getThreatRatingCommentsMapTag();
        setData(commentsTag, commentsMap.toJsonString());
    }

    public CommandSetObjectData createCommandToUpdateComment(ORef threatRef, ORef targetRef, String comments)
    {
        CodeToUserStringMap commentsMap = getThreatRatingCommentsMap();
        String threatTargetKey = createKey(threatRef, targetRef);
        commentsMap.putUserString(threatTargetKey, comments);
        return new CommandSetObjectData(getRef(), getThreatRatingCommentsMapTag(), commentsMap.toJsonString());
    }

    public static final String TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP = "SimpleThreatRatingCommentsMap";
    public static final String TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP = "StressBasedThreatRatingCommentsMap";
}
