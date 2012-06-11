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

import java.util.Vector;

import org.miradi.questions.ThreatRatingQuestion;

public class ThreatTargetThreatRatingSchemaWriter extends BaseObjectSchemaWriter
{
	public ThreatTargetThreatRatingSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse)
	{
		super(creatorToUse, null);
	}
	
	@Override
	public Vector<String> createFieldSchemas() throws Exception
	{
		Vector<String> schemaElements = new Vector<String>();
		schemaElements.add(getCreator().writeSchemaElement(THREAT_RATING, TARGET + ID, BIODIVERSITY_TARGET + ID + ".element*"));
		schemaElements.add(getCreator().writeSchemaElement(THREAT_RATING, THREAT_ID, THREAT_ID + ".element*"));
		String vocabularyName = getCreator().getChoiceQuestionToSchemaElementNameMap().findVocabulary(new ThreatRatingQuestion());
		schemaElements.add(getCreator().writeSchemaElement(THREAT_RATING, THREAT_TARGET_RATING, vocabularyName));
		schemaElements.add(getCreator().writeSchemaElement(THREAT_RATING, COMMENTS, "formatted_text"));
		schemaElements.add(getCreator().writeSchemaElement(THREAT, RATING + RATINGS, " SimpleThreatRating.element | StressBasedThreatRating.element* "));
				
		return schemaElements;
	}
	
	@Override
	public String getXmpz2ElementName()
	{
		return THREAT_RATING;
	}
}
