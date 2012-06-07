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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.xml.generic.SchemaWriter;

public class ThreatTargetThreatRatingSchemaWriter extends BaseObjectSchemaWriter
{
	public ThreatTargetThreatRatingSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse)
	{
		super(creatorToUse, null);
	}
	
	@Override
	public void writeFields(SchemaWriter writer) throws Exception
	{
		getCreator().writeSchemaElement(THREAT_RATING, TARGET + ID, BIODIVERSITY_TARGET + ID + ".element*");
		writer.println(" &");
		
		getCreator().writeSchemaElement(THREAT_RATING, THREAT_ID, THREAT_ID + ".element*");
		writer.println(" &");
		
		String vocabularyName = getCreator().getChoiceQuestionToSchemaElementNameMap().findVocabulary(new ThreatRatingQuestion());
		getCreator().writeSchemaElement(THREAT_RATING, THREAT_TARGET_RATING, vocabularyName);
		writer.println(" &");
		
		getCreator().writeSchemaElement(THREAT_RATING, COMMENTS, "formatted_text");
		writer.println(" &");
		
		getCreator().writeSchemaElement(THREAT, RATING + RATINGS, " SimpleThreatRating.element | StressBasedThreatRating.element* ");
	}
	
	@Override
	public String getXmpz2ElementName()
	{
		return THREAT_RATING;
	}
}
