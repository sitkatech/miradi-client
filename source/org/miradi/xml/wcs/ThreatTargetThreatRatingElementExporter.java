/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

package org.miradi.xml.wcs;

import java.util.Vector;

import org.miradi.diagram.ThreatTargetChainObject;
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Cause;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.ThreatStressRatingDetailsTableExporter;

public class ThreatTargetThreatRatingElementExporter extends AbstractContainerExporter
{
	public ThreatTargetThreatRatingElementExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse);
	}

	@Override
	public void exportObjectPool() throws Exception
	{
		getWcsXmlExporter().writeStartPoolElement(THREAT_RATING);
		if (getProject().isStressBaseMode())
			exportStressBasedThreatRating();
		else
			exportSimpleThreatRating();
		
		getWcsXmlExporter().writeEndPoolElement(THREAT_RATING);
	}

	private void exportSimpleThreatRating() throws Exception
	{
		Vector<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainObject chain = new ThreatTargetChainObject(getProject());
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			for(ORef threatRef : upstreamThreats)
			{
				getWcsXmlExporter().writeStartElement(THREAT_RATING);
				
				exportTargetId(target.getRef());
				exportThreatId(threatRef);
				exportComment(threatRef, target.getRef());				
				exportSimpleBaseThreatRatingDetails(threatRef, target.getRef());
				
				getWcsXmlExporter().writeEndElement(THREAT_RATING);
			}		
		}
	}

	private void exportSimpleBaseThreatRatingDetails(ORef threatRef, ORef targetRef) throws Exception
	{
		getWcsXmlExporter().writeStartElement(THREAT_RATING + RATINGS);
		getWcsXmlExporter().writeStartElement(SIMPLE_BASED_THREAT_RATING);
		
		SimpleThreatRatingFramework simpleThreatRatingFramework = getProject().getSimpleThreatRatingFramework();
		ThreatRatingBundle bundle = simpleThreatRatingFramework.getBundle(threatRef, targetRef);
		
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + SCOPE, simpleThreatRatingFramework.getScopeChoiceItem(bundle).getCode());
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + SEVERITY, simpleThreatRatingFramework.getSeverityChoiceItem(bundle).getCode());
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + IRREVERSIBILITY, simpleThreatRatingFramework.getIrreversibilityChoiceItem(bundle).getCode());
		
		getWcsXmlExporter().writeEndElement(SIMPLE_BASED_THREAT_RATING);
		getWcsXmlExporter().writeEndElement(THREAT_RATING + RATINGS);
	}

	private void exportStressBasedThreatRating() throws Exception
	{
		Vector<Cause> threats =  getProject().getCausePool().getDirectThreatsAsVector();
		Vector<Target> targets = AbstractThreatTargetTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		for(Target target : targets)
		{
			if (target.getStressRefs().hasData())
				exportStressBasedThreatRatingDetailsRow(target, threats);
		}
	}
	
	private void exportStressBasedThreatRatingDetailsRow(Target target, Vector<Cause> threats) throws Exception
	{
		getWcsXmlExporter().writeStartElement(THREAT_RATING);
		
		ORefList stressRefs = target.getStressRefs();
		for (int index = 0; index < stressRefs.size(); ++index)
		{
			Stress stress = Stress.find(getProject(), stressRefs.get(index));
			for(Cause threat : threats)
			{			 
				exportTargetId(target.getRef());
				exportThreatId(threat.getRef());
				exportComment(threat.getRef(), target.getRef());
				exportStressBasedThreatRatingDetails(target, stress, threat);
			}
		}
		
		getWcsXmlExporter().writeEndElement(THREAT_RATING);
	}
	
	private void exportStressBasedThreatRatingDetails(Target target, Stress stress, Cause threat) throws Exception
	{
		getWcsXmlExporter().writeStartElement(THREAT_RATING + RATINGS);
		getWcsXmlExporter().writeStartElement(STRESS_BASED_THREAT_RATING);
		
		exportId(STRESS_BASED_THREAT_RATING + STRESS, STRESS, stress.getRef());
		ChoiceItem irreversibility = ThreatStressRatingDetailsTableExporter.getIrreversibility(getProject(), target.getRef(), threat.getRef(), stress);
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), STRESS_BASED_THREAT_RATING + IRREVERSIBILITY, irreversibility.getCode());
		
		ChoiceItem contribution = ThreatStressRatingDetailsTableExporter.getContribution(getProject(), target.getRef(), threat.getRef(), stress);
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), STRESS_BASED_THREAT_RATING + CONTRIBUTION, contribution.getCode());
		
		getWcsXmlExporter().writeEndElement(STRESS_BASED_THREAT_RATING);
		getWcsXmlExporter().writeEndElement(THREAT_RATING + RATINGS);
	}

	private void exportComment(ORef threatRef, ORef targetRef) throws Exception
	{
		ThreatRatingCommentsData threatRatingCommentsData = getProject().getSingletonThreatRatingCommentsData();
		String threatRatingComment = threatRatingCommentsData.findComment(threatRef, targetRef);
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), THREAT_RATING + COMMENTS, threatRatingComment);
	}
	
	private void exportThreatId(ORef threatRef) throws Exception
	{
		exportId(THREAT_RATING + THREAT, THREAT, threatRef);
	}

	private void exportTargetId(ORef targetRef) throws Exception
	{
		exportId(THREAT_RATING + TARGET, BIODIVERSITY_TARGET, targetRef);
	}

	private void exportId(String parentElementName, String idElementName, ORef ref) throws Exception
	{
		getWcsXmlExporter().writeStartElement(parentElementName + ID);
		
		getWcsXmlExporter().writeStartElement(idElementName + ID);
		getWcsXmlExporter().writeXmlEncodedData(getWcsXmlExporter().getWriter(), ref.getObjectId().toString());
		getWcsXmlExporter().writeEndElement(idElementName + ID);
		
		getWcsXmlExporter().writeEndElement(parentElementName + ID);
	}
}
