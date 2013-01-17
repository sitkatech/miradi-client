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

public class StressBasedThreatRatingSchemaWriter extends BaseObjectSchemaWriter
{
	public StressBasedThreatRatingSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse)
	{
		super(creatorToUse);
	}
	
	@Override
	public Vector<String> createFieldSchemas() throws Exception
	{
		Vector<String> schemaElements = new Vector<String>();
		
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createSchemaElement(STRESS_BASED_THREAT_RATING + THREAT_ID, (THREAT_ID + ".element")));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createSchemaElement(STRESS_BASED_THREAT_RATING + TARGET + ID, (BIODIVERSITY_TARGET + ID + ".element")));		
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createSchemaElement(STRESS_BASED_THREAT_RATING + THREAT_STRESS_RATING, THREAT_STRESS_RATING + ".element*"));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(STRESS_BASED_THREAT_RATING + COMMENTS, "formatted_text"));
		
		String vocabularyName = getXmpz2XmlSchemaCreator().getChoiceQuestionToSchemaElementNameMap().findVocabulary(new ThreatRatingQuestion());
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(STRESS_BASED_THREAT_RATING + CALCULATED_THREAT_TARGET_RATING, vocabularyName));
		
		return schemaElements;
	}
	
	@Override
	public String getXmpz2ElementName()
	{
		return STRESS_BASED_THREAT_RATING;
	}
}
