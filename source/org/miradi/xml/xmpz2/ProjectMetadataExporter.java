/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2;

import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.xml.generic.XmlSchemaCreator;

public class ProjectMetadataExporter extends BaseObjectExporter
{
	public ProjectMetadataExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	public void writeBaseObjectDataSchemaElement(final BaseObject baseObject) throws Exception
	{
		BaseObjectSchema baseObjectSchema = baseObject.getSchema();
		writeFields(baseObject, baseObjectSchema);
		writeOptionalOverallProjectThreatRating();
		writeOptionalOverallProjectViabilityRating();
		writeExternalAppIds();
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		writeShareOutsideOrganizationElement();
	}
	
	private void writeShareOutsideOrganizationElement() throws Exception
	{
		String shareOutSideOfTnc = "0";
		if (getTncProjectData().canShareOutsideOfTnc())
			shareOutSideOfTnc = BooleanData.BOOLEAN_TRUE;
		
		getWriter().writeElement(PROJECT_SUMMARY + XmlSchemaCreator.PROJECT_SHARE_OUTSIDE_ORGANIZATION, shareOutSideOfTnc);
	}
	
	private void writeOptionalOverallProjectThreatRating() throws Exception
	{
		int rawOverallProjectThreatRatingCode = getProject().getProjectSummaryThreatRating();
		if (rawOverallProjectThreatRatingCode == 0)
			return;
		
		getWriter().writeStartElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING);
		getWriter().writeXmlText(Integer.toString(rawOverallProjectThreatRatingCode));
		getWriter().writeEndElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING);
	}
	
	private void writeOptionalOverallProjectViabilityRating() throws Exception
	{
		String code = Target.computeTNCViability(getProject());
		ChoiceItem choiceItem = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(code);
		
		getWriter().writeStartElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_RATING);
		getWriter().writeXmlText(choiceItem.getCode());
		getWriter().writeEndElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_RATING);
	}
	
	private void writeExternalAppIds() throws Exception
	{
		getWriter().writeStartElement(getWriter().appendParentNameToChildName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));

		String stringRefMapAsString = getProject().getMetadata().getData(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP);
		StringRefMap stringRefMap = new StringRefMap(stringRefMapAsString);
		Set<String> keys = stringRefMap.getKeys();
		for(String key: keys)
		{
			ORef xenodataRef = stringRefMap.getValue(key);
			if (xenodataRef.isInvalid())
			{
				EAM.logWarning("Invalid Xenodata ref found for key: " + key + " while exporting.");
				continue;
			}

			Xenodata xenodata = Xenodata.find(getProject(), xenodataRef);
			String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);
			getWriter().writeStartElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
			getWriter().writeElement(EXTERNAL_APP_ELEMENT_NAME, key);
			getWriter().writeElement(PROJECT_ID, projectId);
			getWriter().writeEndElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
		}
		
		getWriter().writeEndElement(getWriter().appendParentNameToChildName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));
	}

	private TncProjectData getTncProjectData()
	{
		final ORef ref = getWriter().getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		return TncProjectData.find(getWriter().getProject(), ref);
	}
}
