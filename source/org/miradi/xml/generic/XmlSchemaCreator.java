/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.generic;

import org.miradi.main.Miradi;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.questions.FiscalYearStartQuestion;
import org.miradi.questions.ProtectedAreaCategoryQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.Translation;

public class XmlSchemaCreator
{
	public static void main(String[] args) throws Exception
	{
		new XmlSchemaCreator().printXmlRncSchema(new SchemaWriter(System.out));
	}

	public XmlSchemaCreator() throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();
	}

	public void printXmlRncSchema(SchemaWriter writer) throws Exception
	{
		ProjectSchemaElement rootElement = new ProjectSchemaElement();
		writer.println("namespace miradi = 'http://xml.miradi.org/schema/ConservationProject/1'");
		writer.defineAlias("start", rootElement.getProjectElementName() + ".element");
		rootElement.output(writer);
		
		writer.println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");
		writer.printlnIndented("vocabulary_iso_country_code = xsd:NMTOKEN { pattern = '[A-Z]{3}' }");
		defineVocabulary(writer, VOCABULARY_FISCAL_YEAR_START, new FiscalYearStartQuestion());
		defineVocabulary(writer, VOCABULARY_PROTECTED_AREA_CATEGORIES, new ProtectedAreaCategoryQuestion());
		defineVocabulary(writer, VOCABULARY_RESOURCE_TYPE, new ResourceTypeQuestion());
		defineVocabulary(writer, VOCABULARY_RESOURCE_ROLE_CODES, new ResourceRoleQuestion());
		defineVocabulary(writer, VOCABULARY_HIDDEN_TYPES, new DiagramLegendQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_LINK_COLOR, new DiagramLinkColorQuestion());
		
		defineIdElement(writer, "ConceptualModel");
		defineIdElement(writer, "ResultsChain");

		defineIdElement(writer, "DiagramFactor");
		defineIdElement(writer, "BiodiversityTarget");
		defineIdElement(writer, "HumanWelfareTarget");
		defineIdElement(writer, "Cause");
		defineIdElement(writer, "Strategy");
		defineIdElement(writer, "ThreatReductionResult");
		defineIdElement(writer, "IntermediateResult");
		defineIdElement(writer, "GroupBox");
		defineIdElement(writer, "TextBox");
		defineIdElement(writer, "ScopeBox");
		
		defineIdElement(writer, "Activity");
		defineIdElement(writer, "Stress");
		
		defineIdElement(writer, "DiagramLink");
		defineIdElement(writer, "FactorLink");
		 
		defineIdElement(writer, "Goal");
		defineIdElement(writer, "Objective");
		defineIdElement(writer, "Indicator");
		defineIdElement(writer, "KeyEcologicalAttribute");
		defineIdElement(writer, TAGGED_OBJECT_SET_ELEMENT_NAME);
		
		writer.defineAlias("WrappedByDiagramLinkId.element", "element WrappedByDiagramLinkId");
		writer.startBlock();
		writer.printlnIndented("FactorLinkId.element");
		writer.endBlock();
		
		writer.defineAlias("WrappedByDiagramFactorId.element", "element WrappedByDiagramFactorId");
		writer.startBlock();
		writer.printlnIndented("BiodiversityTargetId.element |");
		writer.printlnIndented("HumanWelfareTargetId.element |");
		writer.printlnIndented("CauseId.element |");
		writer.printlnIndented("StrategyId.element |");
		writer.printlnIndented("ThreatReductionResultId.element |");
		writer.printlnIndented("IntermediateResultId.element |");
		writer.printlnIndented("GroupBoxId.element |");
		writer.printlnIndented("TextBoxId.element |");
		writer.printlnIndented("ScopeBoxId.element |");
		writer.printlnIndented("ActivityId.element |");
		writer.printlnIndented("StressId.element ");
		writer.endBlock();
		
		writer.defineAlias("LinkableFactorId.element", "element WrappedByDiagramFactorId");
		writer.startBlock();
		writer.printlnIndented("BiodiversityTargetId.element |");
		writer.printlnIndented("HumanWelfareTargetId.element |");
		writer.printlnIndented("CauseId.element |");
		writer.printlnIndented("StrategyId.element |");
		writer.printlnIndented("ThreatReductionResultId.element |");
		writer.printlnIndented("IntermediateResultId.element |");
		writer.printlnIndented("GroupBoxId.element ");
		writer.endBlock();
		
		writer.defineAlias("GeospatialLocation.element", "element miradi:GeospatialLocation");
		writer.startBlock();
		writer.printlnIndented("element miradi:latitude { xsd:decimal } &");
		writer.printlnIndented("element miradi:longitude { xsd:decimal } ");
		writer.endBlock();
		
		writer.defineAlias("DiagramPoint.element", "element miradi:DiagramPoint");
		writer.startBlock();
		writer.printlnIndented("element miradi:x { xsd:integer } &");
		writer.printlnIndented("element miradi:y { xsd:integer } ");
		writer.endBlock();
		
		writer.defineAlias("DiagramSize.element", "element miradi:DiagramSize");
		writer.startBlock();
		writer.printlnIndented("element miradi:width { xsd:integer } &");
		writer.printlnIndented("element miradi:height { xsd:integer } ");
		writer.endBlock();
		
		defineVocabularyDefinedAlias(writer, VOCABULARY_FISCAL_YEAR_START, "FiscalYearStartMonth");
		defineVocabularyDefinedAlias(writer, VOCABULARY_PROTECTED_AREA_CATEGORIES, PROTECTED_AREA_CATEGORIES_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_RESOURCE_TYPE, RESOURCE_TYPE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_RESOURCE_ROLE_CODES, RESOURCE_ROLE_CODES_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_HIDDEN_TYPES, HIDDEN_TYPES_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE, DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE, DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR, DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR, DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_LINK_COLOR, DIAGRAM_LINK_COLOR_ELEMENT_NAME);
		
		writer.flush();
    }

	private void defineVocabularyDefinedAlias(SchemaWriter writer, String vocabularyFiscalYearStartElementName, String elementName)
	{
		writer.defineAlias(elementName + ".element", "element miradi:" + elementName + "");
		writer.startBlock();
		writer.printlnIndented("element miradi:code { " + vocabularyFiscalYearStartElementName + " }* ");
		writer.endBlock();
	}
	
	private void defineIdElement(SchemaWriter writer, String baseName)
	{
		writer.println(baseName + "Id.element = element " + baseName + "Id { xsd:integer }");
	}
	
	private void defineVocabulary(SchemaWriter writer, String vocabularyName, ChoiceQuestion question)
	{
		writer.print(vocabularyName + " = ");
		CodeList codes = question.getAllCodes();
		for(int index = 0; index < codes.size(); ++index)
		{
			writer.write("\"" + codes.get(index)+ "\"");
			if (index < codes.size() - 1)
				writer.print("|");
		}
		
		writer.println();
	}
	
	public static final String VOCABULARY_FISCAL_YEAR_START = "vocabulary_fiscal_year_start";
	public static final String VOCABULARY_PROTECTED_AREA_CATEGORIES = "vocabulary_protected_area_categories";
	public static final String VOCABULARY_RESOURCE_TYPE = "vocabulary_resource_type";
	public static final String VOCABULARY_RESOURCE_ROLE_CODES = "vocabulary_resource_role_codes";
	public static final String VOCABULARY_HIDDEN_TYPES = "vocabulary_hidden_types";
	public static final String VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE = "vocabulary_diagram_factor_font_size";
	public static final String VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE = "vocabulary_diagram_factor_font_style";
	public static final String VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR = "vocabulary_diagram_factor_background_color";
	public static final String VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR = "vocabulary_diagram_factor_foreground_color";
	public static final String VOCABULARY_DIAGRAM_LINK_COLOR = "vocabulary_diagram_link_color";
	
	public static final String PROTECTED_AREA_CATEGORIES_ELEMENT_NAME = ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES;
	public static final String RESOURCE_TYPE_ELEMENT_NAME = ProjectResource.TAG_RESOURCE_TYPE;
	public static final String RESOURCE_ROLE_CODES_ELEMENT_NAME = ProjectResource.TAG_ROLE_CODES;
	public static final String HIDDEN_TYPES_ELEMENT_NAME = DiagramObject.TAG_HIDDEN_TYPES;
	public static final String TAGGED_OBJECT_SET_ELEMENT_NAME = "TaggedObjectSet";
	public static final String DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME = DiagramFactor.TAG_FONT_SIZE;
	public static final String DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME = DiagramFactor.TAG_FONT_STYLE;
	public static final String DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME = DiagramFactor.TAG_BACKGROUND_COLOR;
	public static final String DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME = DiagramFactor.TAG_FOREGROUND_COLOR;
	
	public static final String DIAGRAM_LINK_COLOR_ELEMENT_NAME = DiagramLink.TAG_COLOR;
}
