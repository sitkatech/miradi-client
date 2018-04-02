/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.xmpz2schema;

import java.util.Vector;

import org.miradi.questions.ThreatRatingQuestion;

public class SimpleThreatRatingSchemaWriter extends BaseObjectSchemaWriter
{
	public SimpleThreatRatingSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse)
	{
		super(creatorToUse);
	}
	
	@Override
	public Vector<String> createFieldSchemas() throws Exception
	{
		Vector<String> schemaElements = new Vector<String>();
		
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createSchemaElement(SIMPLE_BASED_THREAT_RATING + TARGET + ID, (BIODIVERSITY_TARGET + ID + ".element")));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createSchemaElement(SIMPLE_BASED_THREAT_RATING + THREAT_ID, (THREAT_ID + ".element")));
		String vocabularyName = getXmpz2XmlSchemaCreator().getChoiceQuestionToSchemaElementNameMap().findVocabulary(new ThreatRatingQuestion());
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + SIMPLE_THREAT_TARGET_CALCULATED_RATING, vocabularyName));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + SCOPE, VOCABULARY_SCOPE_RATING));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + SEVERITY, VOCABULARY_SEVERITY_RATING));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + IRREVERSIBILITY, VOCABULARY_IRREVERSIBILITY_CODE));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + COMMENTS, "formatted_text"));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + EVIDENCE_CONFIDENCE, VOCABULARY_EVIDENCE_CONFIDENCE_PROJECT));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + EVIDENCE_NOTES, "formatted_text"));
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(SIMPLE_BASED_THREAT_RATING + IS_NOT_APPLICABLE, "xsd:boolean"));

		return schemaElements;
	}
	
	@Override
	public String getXmpz2ElementName()
	{
		return SIMPLE_BASED_THREAT_RATING;
	}
}
