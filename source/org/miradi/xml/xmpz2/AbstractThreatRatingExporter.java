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

package org.miradi.xml.xmpz2;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.project.Project;

public abstract class AbstractThreatRatingExporter implements Xmpz2XmlConstants
{
    public AbstractThreatRatingExporter(Xmpz2XmlWriter writerToUse)
    {
        writer = writerToUse;
    }

    protected void exportThreatRatingData(ORef threatRef, ORef targetRef) throws Exception
    {
        AbstractThreatRatingData threatRatingData = findThreatRatingData(threatRef, targetRef);
        exportThreatRatingComments(threatRatingData);
        exportEvidenceFields(threatRatingData);
        exportIsNotApplicableField(threatRatingData);
    }

    protected void exportThreatRatingComments(AbstractThreatRatingData threatRatingData) throws Exception
    {
        if (threatRatingData != null)
            getWriter().writeElement(getParentElementName() + COMMENTS, threatRatingData.getComments());
    }

    protected void exportEvidenceFields(AbstractThreatRatingData threatRatingData) throws Exception
    {
        if (threatRatingData != null)
        {
            getWriter().writeOptionalCodeElement(getParentElementName(), EVIDENCE_CONFIDENCE, threatRatingData.getEvidenceConfidence().getCode());
            getWriter().writeElement(getParentElementName() + EVIDENCE_NOTES, threatRatingData.getEvidenceNotes());
        }
    }

    protected void exportIsNotApplicableField(AbstractThreatRatingData threatRatingData) throws Exception
    {
        boolean isThreatRatingNotApplicable = threatRatingData != null && threatRatingData.isThreatRatingNotApplicable();
        getWriter().writeBooleanElement(getParentElementName() + IS_NOT_APPLICABLE, isThreatRatingNotApplicable);
    }

    protected void exportId(String parentElementName, String idElementName, ORef ref) throws Exception
    {
        getWriter().writeStartElement(parentElementName + ID);

        getWriter().writeStartElement(idElementName + ID);
        getWriter().writeXmlText(ref.getObjectId().toString());
        getWriter().writeEndElement(idElementName + ID);

        getWriter().writeEndElement(parentElementName + ID);
    }

    protected void exportThreatId(ORef threatRef) throws Exception
    {
        exportId(getParentElementName() + THREAT, THREAT, threatRef);
    }

    protected void exportTargetId(ORef targetRef) throws Exception
    {
        exportId(getParentElementName() + TARGET, BIODIVERSITY_TARGET, targetRef);
    }

    protected abstract AbstractThreatRatingData findThreatRatingData(ORef threatRef, ORef targetRef);

    protected abstract String getParentElementName();

    protected Xmpz2XmlWriter getWriter()
    {
        return writer;
    }

    protected Project getProject()
    {
        return getWriter().getProject();
    }

    protected Xmpz2XmlWriter writer;
}
