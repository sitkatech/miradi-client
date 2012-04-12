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

import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatPerRowTableModel;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.generic.XmlSchemaCreator;

abstract public class AbstractTargetExporter extends BaseObjectExporter
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
		exportThreatRatingThreatTargetRating(abstractTarget);
		getWriter().writeNonOptionalCodeElement(getTargetElementName(), XmlSchemaCreator.TARGET_STATUS_ELEMENT_NAME, getProject().getQuestion(StatusQuestion.class), abstractTarget.getTargetViability());
		getWriter().writeNonOptionalCodeElement(getTargetElementName(), AbstractTarget.TAG_VIABILITY_MODE, new ViabilityModeQuestion(), abstractTarget.getViabilityMode());
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(AbstractTarget.TAG_TARGET_STATUS))
			return true;
		
		if (tag.equals(AbstractTarget.TAG_VIABILITY_MODE))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	private void exportThreatRatingThreatTargetRating(AbstractTarget abstractTarget) throws Exception
	{
		if (getProject().isStressBaseMode())
			exportStressBasedThreatRatingThreatTargetRating(abstractTarget);
		else
			exportSimpleThreatRatingThreatTargetRating(abstractTarget);
	}
	
	private void exportStressBasedThreatRatingThreatTargetRating(AbstractTarget target) throws Exception
	{
		int rawTargetRatingValue = getProject().getStressBasedThreatRatingFramework().get2PrimeSummaryRatingValue(target);
		ChoiceItem targetThreatRating = AbstractThreatPerRowTableModel.convertThreatRatingCodeToChoiceItem(rawTargetRatingValue);
		getWriter().writeNonOptionalCodeElement(getTargetElementName(), TARGET_THREAT_RATING, new ThreatRatingQuestion(), targetThreatRating.getCode());
	}

	private void exportSimpleThreatRatingThreatTargetRating(AbstractTarget abstractTarget) throws Exception
	{
		ChoiceItem threatTargetRating = getProject().getSimpleThreatRatingFramework().getTargetThreatRatingValue(abstractTarget.getRef());
		getWriter().writeNonOptionalCodeElement(getTargetElementName(), TARGET_THREAT_RATING, new ThreatRatingQuestion(), threatTargetRating.getCode());
	}
	
	abstract protected String getTargetElementName();
}
