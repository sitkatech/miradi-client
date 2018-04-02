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
package org.miradi.dialogs.threatrating.properties;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.project.Project;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.TargetSchema;

public abstract class AbstractThreatRatingPropertiesPanel extends ObjectDataInputPanel
{
    public AbstractThreatRatingPropertiesPanel(Project projectToUse, int objectTypeToUse, int threatthreatRatingDataObjectTypeToUse) throws Exception
    {
        super(projectToUse, objectTypeToUse);
        threatRatingDataObjectType = threatthreatRatingDataObjectTypeToUse;
    }

    private ORef getTargetRef()
    {
        return getSelectedRefs().getRefForType(TargetSchema.getObjectType());
    }

    private ORef getThreatRef()
    {
        return getSelectedRefs().getRefForType(CauseSchema.getObjectType());
    }

    private void setEditorEnabledStatus()
    {
        ORef threatRef = getThreatRef();
        ORef targetRef = getTargetRef();

        AbstractThreatRatingData threatRatingData = null;

        if (threatRef.isValid() && targetRef.isValid())
            threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), threatRef, targetRef, threatRatingDataObjectType);

        boolean enableControls = threatRatingData == null || !threatRatingData.isThreatRatingNotApplicable();
        setEditorEnabled(enableControls);
    }

    protected abstract void setEditorEnabled(boolean isEditable);

    protected abstract void setEditorObjectRefs(ORef[] hierarchyToSelectedRef);

    @Override
    public void commandExecuted(CommandExecutedEvent event)
    {
        super.commandExecuted(event);

        if(event.isSetDataCommandWithThisTypeAndTag(threatRatingDataObjectType, AbstractThreatRatingData.TAG_IS_THREAT_RATING_NOT_APPLICABLE))
            setObjectRefs(getSelectedRefs());
    }

    @Override
    public void setObjectRefs(ORef[] hierarchyToSelectedRef)
    {
        super.setObjectRefs(hierarchyToSelectedRef);
        setEditorObjectRefs(hierarchyToSelectedRef);
        setEditorEnabledStatus();
    }

    int threatRatingDataObjectType;
}
