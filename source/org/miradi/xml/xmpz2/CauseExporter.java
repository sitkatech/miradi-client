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

package org.miradi.xml.xmpz2;

import org.miradi.objecthelpers.CodeToCodeMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.questions.*;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.CauseSchema;

public class CauseExporter extends BaseObjectWithThreatRatingExporter
{
	public CauseExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, CauseSchema.getObjectType());
	}

	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);

		final Cause cause = (Cause) baseObject;

		CodeToCodeMap causeStandardClassificationCodes = getStandardClassifications(cause);
		if (causeStandardClassificationCodes.size() > 0)
			getWriter().writeCauseStandardClassifications(causeStandardClassificationCodes);
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE))
			return true;

		if (tag.equals(Cause.TAG_STANDARD_CLASSIFICATION_V20_CODE))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}

	@Override
	public ChoiceItem getSimpleModeThreatRating(Factor factor) throws Exception
	{
		return getProject().getSimpleThreatRatingFramework().getThreatThreatRatingValue(factor.getRef());
	}
	
	@Override
	protected String getContainerElementName()
	{
		return CauseSchema.OBJECT_NAME;
	}

	private CodeToCodeMap getStandardClassifications(Cause cause)
	{
		CodeToCodeMap causeStandardClassificationCodes = new CodeToCodeMap();

		String standardClassificationCodeV11 = cause.getTaxonomyCode(ThreatClassificationQuestionV11.STANDARD_CLASSIFICATION_CODELIST_KEY);
		if (!standardClassificationCodeV11.isEmpty())
			causeStandardClassificationCodes.putCode(ThreatClassificationQuestionV11.STANDARD_CLASSIFICATION_CODELIST_KEY, standardClassificationCodeV11);

		String standardClassificationCodeV20 = cause.getTaxonomyCode(ThreatClassificationQuestionV20.STANDARD_CLASSIFICATION_CODELIST_KEY);
		if (!standardClassificationCodeV20.isEmpty())
			causeStandardClassificationCodes.putCode(ThreatClassificationQuestionV20.STANDARD_CLASSIFICATION_CODELIST_KEY, standardClassificationCodeV20);

		return causeStandardClassificationCodes;
	}
}
