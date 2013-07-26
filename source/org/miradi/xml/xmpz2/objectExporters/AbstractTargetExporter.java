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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.BaseObjectWithThreatRatingExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

abstract public class AbstractTargetExporter extends BaseObjectWithThreatRatingExporter
{
	public AbstractTargetExporter(final Xmpz2XmlWriter writerToUse, final int objectTypeToUse)
	{
		super(writerToUse, objectTypeToUse);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject, final BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		AbstractTarget abstractTarget = (AbstractTarget) baseObject;
		writeNonOptionalCodeElement(AbstractTarget.TAG_VIABILITY_MODE, ViabilityModeQuestion.class, abstractTarget.getViabilityMode());
		getWriter().writeOptionalCodeElement(getContainerElementName(), TARGET_CALCULATED_STATUS_ELEMENT_NAME, abstractTarget.getTargetViability());
	}

	private void writeNonOptionalCodeElement(final String elementName, final Class questionClass, final String code) throws Exception
	{
		final ChoiceQuestion choiceQuestion = StaticQuestionManager.getQuestion(questionClass);
		getWriter().writeNonOptionalCodeElement(getTargetElementName(), elementName, choiceQuestion, code);
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(AbstractTarget.TAG_VIABILITY_MODE))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}

	@Override
	public ChoiceItem getSimpleModeThreatRating(Factor factor) throws Exception
	{
		return getProject().getSimpleThreatRatingFramework().getTargetThreatRatingValue(factor.getRef());
	}
	
	@Override
	protected String getContainerElementName()
	{
		return getTargetElementName();
	}
	
	abstract protected String getTargetElementName();
}
