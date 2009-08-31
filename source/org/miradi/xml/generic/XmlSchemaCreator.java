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

	private void printXmlRncSchema(SchemaWriter writer) throws Exception
	{
		ProjectSchemaElement rootElement = new ProjectSchemaElement();
		writer.defineAlias("start", rootElement.getProjectElementName() + ".element");
		rootElement.output(writer);
		
		writer.println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");
		writer.printlnIndented("vocabulary_iso_country_code = xsd:NMTOKEN { pattern = '[A-Z]{3}'' }");
		
		writer.println("DiagramFactorId = { xsd:integer }");
		writer.println("BiodiversityTargetId = { xsd:integer }");
		writer.println("HumanWelfareTargetId = { xsd:integer }");
		writer.println("CauseFactorId = { xsd:integer }");
		writer.println("StrategyFactorId = { xsd:integer }");
		writer.println("ThreatReductionResultFactorId = { xsd:integer }");
		writer.println("IntermediateResultFactorId = { xsd:integer }");
		writer.println("GroupBoxId = { xsd:integer }");
		writer.println("TextBoxId = { xsd:integer }");
		writer.println("ScopeBoxId = { xsd:integer }");
		
		writer.println("ActivityId = { xsd:integer }");
		writer.println("StressId = { xsd:integer }");
		
		writer.println("DiagramLinkId = { xsd:integer }");
		 
		        writer.println("GoalId = { xsd:integer }");
		writer.println("ObjectiveId = { xsd:integer }");
		writer.println("IndicatorId = { xsd:integer }");
		writer.println("KeyEcologicalAttributeId = { xsd:integer }");
		
		writer.defineAlias("WrappedByDiagramFactorId.element", "element WrappedByDiagramFactorId");
		writer.startBlock();
		writer.printlnIndented("BiodiversityTargetFactorId |");
		writer.printlnIndented("HumanWelfareTargetFactorId |");
		writer.printlnIndented("CauseFactorId |");
		writer.printlnIndented("StrategyFactorId |");
		writer.printlnIndented("ThreatReductionResultFactorId |");
		writer.printlnIndented("IntermediateResultFactorId |");
		writer.printlnIndented("GroupBoxId |");
		writer.printlnIndented("TextBoxId |");
		writer.printlnIndented("ScopeBoxId |");
		writer.printlnIndented("ActivityId |");
		writer.printlnIndented("StressId |");
		writer.endBlock();
		
		writer.defineAlias("LinkableFactorId.element", "element WrappedByDiagramFactorId");
		writer.startBlock();
		writer.printlnIndented("BiodiversityTargetFactorId |");
		writer.printlnIndented("HumanWelfareTargetFactorId |");
		writer.printlnIndented("CauseFactorId |");
		writer.printlnIndented("StrategyFactorId |");
		writer.printlnIndented("ThreatReductionResultFactorId |");
		writer.printlnIndented("IntermediateResultFactorId |");
		writer.printlnIndented("GroupBoxId |");
		writer.endBlock();
		
		writer.defineAlias("GeospatialLocation.element", "element cp:GeospatialLocation");
		writer.startBlock();
		writer.printlnIndented("element cp:latitude { xsd:decimal } &");
		writer.printlnIndented("element cp:longitude { xsd:decimal } &");
		writer.endBlock();
		
		writer.defineAlias("DiagramPoint.element", "element cp:DiagramPoint");
		writer.startBlock();
		writer.printlnIndented("element cp:x { xsd:integer } &");
		writer.printlnIndented("element cp:y { xsd:integer } &");
		writer.endBlock();
		
		writer.defineAlias("DiagramSize.element", "element cp:DiagramSize");
		writer.startBlock();
		writer.printlnIndented("element cp:width { xsd:integer } &");
		writer.printlnIndented("element cp:height { xsd:integer } &");
		writer.endBlock();
		
		
		writer.flush();
    }
}
