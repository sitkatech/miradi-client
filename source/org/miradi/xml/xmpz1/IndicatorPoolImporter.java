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

package org.miradi.xml.xmpz1;

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.wcs.Xmpz1XmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IndicatorPoolImporter extends AbstractBaseObjectPoolImporter
{
	public IndicatorPoolImporter(Xmpz1XmlImporter importerToUse)
	{
		super(importerToUse, Xmpz1XmlConstants.INDICATOR, IndicatorSchema.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		super.importFields(node, destinationRef);

		importField(node, destinationRef, Indicator.TAG_SHORT_LABEL);
		importField(node, destinationRef, Indicator.TAG_DETAIL);
		importField(node, destinationRef, Indicator.TAG_COMMENTS);
		importCodeField(node, destinationRef, Indicator.TAG_PRIORITY, new PriorityRatingQuestion());
		importFutureStatusData(node, destinationRef);
		importProgressReportRefs(node, destinationRef);
		importExpenseAssignmentRefs(node, destinationRef);
		importResourceAssignmentIds(node, destinationRef);
		importIds(node, destinationRef, Indicator.TAG_METHOD_IDS, TaskSchema.getObjectType(), Xmpz1XmlConstants.METHOD);
		importRefs(node, Xmpz1XmlConstants.MEASUREMENT_IDS, destinationRef, Indicator.TAG_MEASUREMENT_REFS, MeasurementSchema.getObjectType(), Xmpz1XmlConstants.MEASUREMENT);
		importThresholds(node, destinationRef);
		importCodeField(node, destinationRef, Indicator.TAG_RATING_SOURCE, getProject().getQuestion(RatingSourceQuestion.class));
		importField(node, destinationRef, Indicator.TAG_VIABILITY_RATINGS_COMMENTS);
	}

	private void importFutureStatusData(Node node, ORef destinationRef) throws Exception
	{
		ORef newFutureStatusRef = getProject().createObject(FutureStatusSchema.getObjectType(), getProject().getNormalIdAssigner().takeNextId());
		getProject().setObjectData(destinationRef, Indicator.TAG_FUTURE_STATUS_REFS, new ORefList(newFutureStatusRef).toString());
		
		getImporter().importField(node, "IndicatorFutureStatusDate", newFutureStatusRef, FutureStatusSchema.TAG_FUTURE_STATUS_DATE);
		getImporter().importField(node, "IndicatorFutureStatusSummary", newFutureStatusRef, FutureStatusSchema.TAG_FUTURE_STATUS_SUMMARY);
		getImporter().importField(node, "IndicatorFutureStatusComments", newFutureStatusRef, FutureStatusSchema.TAG_FUTURE_STATUS_COMMENTS);
		getImporter().importField(node, "IndicatorFutureStatusDetails", newFutureStatusRef, FutureStatusSchema.TAG_FUTURE_STATUS_DETAIL);
		
		final StatusQuestion question = new StatusQuestion();
		String importedReadableCode = getImporter().getPathData(node, new String[]{"IndicatorFutureStatusRating", });
		String trimmedImportedReadbleCode = importedReadableCode.trim();
		String internalCode = question.convertToInternalCode(trimmedImportedReadbleCode);		
		getImporter().setData(newFutureStatusRef, FutureStatusSchema.TAG_FUTURE_STATUS_RATING, internalCode);
	}

	private void importThresholds(Node indicatorNode, ORef destinationRef) throws Exception
	{
		NodeList thresholdNodes = getImporter().getNodes(indicatorNode, new String[]{getPoolName() + THRESHOLDS, THRESHOLD});
		CodeToUserStringMap thresholdValues = new CodeToUserStringMap();
		CodeToUserStringMap thresholdDetails = new CodeToUserStringMap();
		for (int index = 0; index < thresholdNodes.getLength(); ++index)
		{
			Node thrsholdNode = thresholdNodes.item(index);
			Node statusCodeNode = getImporter().getNode(thrsholdNode, STATUS_CODE);
			if (statusCodeNode != null)
			{
				String statusCode = statusCodeNode.getTextContent();
				Node thresholdValueNode = getImporter().getNode(thrsholdNode, THRESHOLD_VALUE);
				thresholdValues.putUserString(statusCode, getImporter().getSafeNodeContent(thresholdValueNode));
				
				Node thresholdDetailsNode = getImporter().getNode(thrsholdNode, THRESHOLD_DETAILS);
				thresholdDetails.putUserString(statusCode, getImporter().getSafeNodeContent(thresholdDetailsNode));
			}			
		}
		
		getImporter().setData(destinationRef, Indicator.TAG_THRESHOLDS_MAP, thresholdValues.toJsonString());
		getImporter().setData(destinationRef, Indicator.TAG_THRESHOLD_DETAILS_MAP, thresholdDetails.toJsonString());
	}
}
