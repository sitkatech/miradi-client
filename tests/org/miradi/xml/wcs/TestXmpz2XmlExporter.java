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

package org.miradi.xml.wcs;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.objectImporters.BaseObjectImporter;
import org.miradi.xml.xmpz2.objectImporters.IndicatorImporter;
import org.miradi.xml.xmpz2.objectImporters.StrategyImporter;
import org.miradi.xml.xmpz2.objectImporters.TaskImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestXmpz2XmlExporter extends TestCaseWithProject
{
	public TestXmpz2XmlExporter(String name)
	{
		super(name);
	}
	
	public void testNonBlankEmptyExternalProjectData() throws Exception
	{
		verifyEmptyValues("", "");
		verifyEmptyValues("randomCode", "");
		verifyEmptyValues("", "randomProjectId");
		
		verifyNonEmptyValues("randomCode", "randomProjectId");
	}
	
	private void verifyNonEmptyValues(final String externalAppCope,	String xenoDataProjectId) throws Exception
	{
		create(externalAppCope, xenoDataProjectId);
		validateProject();
	}

	private void verifyEmptyValues(final String externalAppCope, String xenoDataProjectId)
	{
		try
		{
			create(externalAppCope, xenoDataProjectId);
			validateProject();
			fail("empty values should have caused xml to fail validation?");
		}
		catch (Exception expectedExceptionToIgnore)
		{
		}
	}
	
	private void create(final String externalAppCope, String xenoDataProjectId) throws Exception
	{
		final String xenodataAsString = getProject().createConproXenodata(externalAppCope, xenoDataProjectId);
		getProject().fillObjectUsingCommand(getProject().getMetadata().getRef(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, xenodataAsString);
	}
	
	public void testValidateEmptyProject() throws Exception
	{
		validateProject();
	}
	
	public void testThreeLanguageVocabularyElement() throws Exception
	{
		verifyLanguageCode("zun");
		verifyLanguageCode("plt");
		verifyLanguageCode("es");
	}

	private void verifyLanguageCode(final String languageCode) throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_PROJECT_LANGUAGE, languageCode);
		validateProject();
	}
	
	public void testElementsWithCalculatedCostsElement() throws Exception
	{
		getProject().createAndPopulateActivity();
		getProject().createAndPopulateIndicator(getProject().createStrategy());
		getProject().createAndPopulateStrategy();
		verifyCalculatedCostsElement();
	}

	public void verifyCalculatedCostsElement() throws Exception
	{
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(projectToImportInto, new NullProgressMeter());
		String exportedProjectXml = validateProject();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(exportedProjectXml);
		try
		{
			xmlImporter.importProject(stringInputputStream);
			verifyCalculatedCostsElement(xmlImporter, new TaskImporter(xmlImporter), 5);
			verifyCalculatedCostsElement(xmlImporter, new IndicatorImporter(xmlImporter), 3);
			verifyCalculatedCostsElement(xmlImporter, new StrategyImporter(xmlImporter), 3);
		}
		finally
		{
			stringInputputStream.close();	
		}
	}

	public void verifyCalculatedCostsElement(Xmpz2XmlImporter xmlImporter, BaseObjectImporter objectImporter, int expectedTaskCount) throws Exception
	{
		final String elementObjectName = objectImporter.getBaseObjectSchema().getXmpz2ElementName();
		final String containerElementName = Xmpz2XmlWriter.createPoolElementName(elementObjectName);
		final Node rootNode = xmlImporter.getRootNode();
		final NodeList baseObjectNodes = xmlImporter.getNodes(rootNode, new String[]{containerElementName, elementObjectName, });
		
		assertEquals("should have one task node?", expectedTaskCount, baseObjectNodes.getLength());
		Node baseObjectNode = baseObjectNodes.item(0);
		
		Node baseObjectCalculatedCostsNode = xmlImporter.getNode(baseObjectNode, elementObjectName + Xmpz2XmlConstants.TIME_PERIOD_COSTS);
		assertNotNull("should have object calcualted costs element?", baseObjectCalculatedCostsNode);
		
		Node calculatedCostsNode = xmlImporter.getNode(baseObjectCalculatedCostsNode, Xmpz2XmlConstants.TIME_PERIOD_COSTS);
		assertNotNull("should have calcualted costs element?", calculatedCostsNode);
		
		Node calculatedTotalBudgetCostNode = xmlImporter.getNode(calculatedCostsNode, XmpzXmlConstants.CALCULATED_TOTAL_BUDGET_COST);
		assertEquals("incorrect total budget value for object?", "112", xmlImporter.getSafeNodeContent(calculatedTotalBudgetCostNode));
	}
	
	public void testProjectWithHtmlInQuarantinedContent() throws Exception
	{
		getProject().appendToQuarantineFile("some <br/> random <b>bolded</b> text");
		validateProject();
	}
	
	public void testUserTextFieldWithHtml() throws Exception
	{
		String sampleText = "one <br/> and <b>some bold</b> <br/>" +
				"two <b>spanning lines<br/>" +
				"</b>" +
				"<a href=\"www.miradi.org\">link</a>" +
				"<i>some <u><strike>combining</strike></u></i>" +
				"<ul><li>test one</li><li><b>bold item</b></li></ul>";
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION, sampleText);
		validateProject();
	}
	
	public void testValidateFilledProject() throws Exception
	{
		// FIXME urgent: This needs to do a better job of populating everything
		// It should auto-populate any newly created field so they are immediately 
		// flagged as needing to be added to the schema.
		getProject().populateEverything();
		getProject().populateBaseObjectWithSampleData(getProject().getMetadata());
		DiagramFactor diagramFactor1 = getProject().createAndPopulateDiagramFactor();
		DiagramFactor diagramFactor2 = getProject().createAndPopulateDiagramFactor();
		getProject().tagDiagramFactor(diagramFactor2.getWrappedORef());
		getProject().createDiagramFactorLinkAndAddToDiagram(diagramFactor1, diagramFactor2);
		getProject().createResourceAssignment();
		validateProject();
	}
	
	private String validateProject() throws Exception
	{
		final UnicodeXmlWriter writer = UnicodeXmlWriter.create();
		new Xmpz2XmlExporter(getProject()).exportProject(writer);
		writer.close();
		String xml = writer.toString();
				
		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
		if (!new Xmpz2XmlValidator().isValid(inputStream))
		{
			throw new ValidationException(EAM.text("File to import does not validate."));
		}
		
		return xml;
	}
}
