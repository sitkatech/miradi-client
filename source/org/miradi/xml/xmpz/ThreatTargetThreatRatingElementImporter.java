/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.WcsXmlConstants;

public class ThreatTargetThreatRatingElementImporter extends AbstractXmpzObjectImporter
{
	public ThreatTargetThreatRatingElementImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.THREAT_RATING);
	}

	@Override
	public void importElement() throws Exception
	{
		
	}
	
//	@Override
//	public void exportXml() throws Exception
//	{
//		getWcsXmlExporter().writeStartPoolElement(THREAT_RATING);
//		if (getProject().isStressBaseMode())
//			exportStressBasedThreatRating();
//		else
//			exportSimpleThreatRating();
//		
//		getWcsXmlExporter().writeEndPoolElement(THREAT_RATING);
//	}
//
//	private void exportSimpleThreatRating() throws Exception
//	{
//		Vector<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
//		ThreatTargetChainObject chain = new ThreatTargetChainObject(getProject());
//		for(Target target : targets)
//		{
//			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
//			for(ORef threatRef : upstreamThreats)
//			{
//				getWcsXmlExporter().writeStartElement(THREAT_RATING);
//				
//				ORef targetRef = target.getRef();
//				exportTargetId(targetRef);
//				exportThreatId(threatRef);
//				exportComment(threatRef, targetRef);				
//				exportSimpleBaseThreatRatingDetails(threatRef, targetRef);
//				
//				getWcsXmlExporter().writeEndElement(THREAT_RATING);
//			}		
//		}
//	}
//
//	private void exportSimpleBaseThreatRatingDetails(ORef threatRef, ORef targetRef) throws Exception
//	{
//		getWcsXmlExporter().writeStartElement(THREAT_RATING + RATINGS);
//		getWcsXmlExporter().writeStartElement(SIMPLE_BASED_THREAT_RATING);
//		
//		ThreatRatingBundle bundle = getSimpleThreatRatingFramework().getBundle(threatRef, targetRef);
//		
//		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + SCOPE, getSimpleThreatRatingFramework().getScopeChoiceItem(bundle).getCode());
//		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + SEVERITY, getSimpleThreatRatingFramework().getSeverityChoiceItem(bundle).getCode());
//		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + IRREVERSIBILITY, getSimpleThreatRatingFramework().getIrreversibilityChoiceItem(bundle).getCode());
//		
//		getWcsXmlExporter().writeEndElement(SIMPLE_BASED_THREAT_RATING);
//		getWcsXmlExporter().writeEndElement(THREAT_RATING + RATINGS);
//	}
//
//	private void exportStressBasedThreatRating() throws Exception
//	{
//		Vector<Cause> threats =  getProject().getCausePool().getDirectThreatsAsVector();
//		Vector<Target> targets = AbstractThreatTargetTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
//		for(Target target : targets)
//		{
//			if (target.getStressRefs().hasRefs())
//				exportStressBasedThreatRatingDetailsRow(target, threats);
//		}
//	}
//	
//	private void exportStressBasedThreatRatingDetailsRow(Target target, Vector<Cause> threats) throws Exception
//	{
//		ORefList stressRefs = target.getStressRefs();
//		for (int index = 0; index < stressRefs.size(); ++index)
//		{
//			Stress stress = Stress.find(getProject(), stressRefs.get(index));
//			for(Cause threat : threats)
//			{
//				getWcsXmlExporter().writeStartElement(THREAT_RATING);
//				ORef targetRef = target.getRef();
//				ORef threatRef = threat.getRef();
//				exportTargetId(targetRef);
//				exportThreatId(threatRef);
//				exportComment(threatRef, targetRef);
//				exportStressBasedThreatRatingDetails(target, stress, threat);
//				getWcsXmlExporter().writeEndElement(THREAT_RATING);
//			}
//		}
//	}
//
//	private void exportStressBasedThreatRatingDetails(Target target, Stress stress, Cause threat) throws Exception
//	{
//		getWcsXmlExporter().writeStartElement(THREAT_RATING + RATINGS);
//		getWcsXmlExporter().writeStartElement(STRESS_BASED_THREAT_RATING);
//		
//		exportId(STRESS_BASED_THREAT_RATING + STRESS, STRESS, stress.getRef());
//		ChoiceItem irreversibility = ThreatStressRatingDetailsTableExporter.getIrreversibility(getProject(), target.getRef(), threat.getRef(), stress);
//		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), STRESS_BASED_THREAT_RATING + IRREVERSIBILITY, irreversibility.getCode());
//		
//		ChoiceItem contribution = ThreatStressRatingDetailsTableExporter.getContribution(getProject(), target.getRef(), threat.getRef(), stress);
//		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), STRESS_BASED_THREAT_RATING + CONTRIBUTION, contribution.getCode());
//		
//		exportStressBasedStressRating(stress.getCalculatedStressRating());
//		exportStressBasedThreatStressRating(target.getRef(), threat.getRef());
//		
//		getWcsXmlExporter().writeEndElement(STRESS_BASED_THREAT_RATING);
//		getWcsXmlExporter().writeEndElement(THREAT_RATING + RATINGS);
//	}
//	
//	private void exportStressBasedThreatStressRating(ORef targetRef, ORef threatRef) throws Exception
//	{
//		ThreatTargetVirtualLinkHelper virtualLink = new ThreatTargetVirtualLinkHelper(getProject());
//		int rawThreatStressRating = virtualLink.calculateStressBasedThreatRating(threatRef, targetRef);
//		String safeThreatRatingCode = ThreatRatingFramework.getSafeThreatRatingCode(rawThreatStressRating);
//		ChoiceQuestion question = getProject().getQuestion(ThreatStressRatingChoiceQuestion.class);
//		exportStressBasedThreatRatingCode(THREAT_STRESS_RATING, question.findChoiceByCode(safeThreatRatingCode));
//	}
//
//	private void exportStressBasedStressRating(String stressRating) throws Exception
//	{
//		ChoiceQuestion question = getProject().getQuestion(StressRatingChoiceQuestion.class);
//		ChoiceItem stressRatingChoiceItem = question.findChoiceByCode(stressRating);
//		exportStressBasedThreatRatingCode(STRESS_RATING, stressRatingChoiceItem);
//	}
//	
//	private void exportStressBasedThreatRatingCode(String elementName, ChoiceItem rating) throws Exception
//	{
//		getWcsXmlExporter().writeStartElement(STRESS_BASED_THREAT_RATING + elementName);
//		getWcsXmlExporter().writeXmlEncodedData(getWcsXmlExporter().getWriter(), rating.getCode());
//		getWcsXmlExporter().writeEndElement(STRESS_BASED_THREAT_RATING + elementName);
//	}
//
//	private void exportComment(ORef threatRef, ORef targetRef) throws Exception
//	{
//		ThreatRatingCommentsData threatRatingCommentsData = getProject().getSingletonThreatRatingCommentsData();
//		String threatRatingComment = threatRatingCommentsData.findComment(threatRef, targetRef);
//		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), THREAT_RATING + COMMENTS, threatRatingComment);
//	}
//	
//	private void exportThreatId(ORef threatRef) throws Exception
//	{
//		exportId(THREAT_RATING + THREAT, THREAT, threatRef);
//	}
//
//	private void exportTargetId(ORef targetRef) throws Exception
//	{
//		exportId(THREAT_RATING + TARGET, BIODIVERSITY_TARGET, targetRef);
//	}
//
//	private void exportId(String parentElementName, String idElementName, ORef ref) throws Exception
//	{
//		getWcsXmlExporter().writeStartElement(parentElementName + ID);
//		
//		getWcsXmlExporter().writeStartElement(idElementName + ID);
//		getWcsXmlExporter().writeXmlEncodedData(getWcsXmlExporter().getWriter(), ref.getObjectId().toString());
//		getWcsXmlExporter().writeEndElement(idElementName + ID);
//		
//		getWcsXmlExporter().writeEndElement(parentElementName + ID);
//	}
//	
//	private SimpleThreatRatingFramework getSimpleThreatRatingFramework()
//	{
//		return getProject().getSimpleThreatRatingFramework();
//	}
}
