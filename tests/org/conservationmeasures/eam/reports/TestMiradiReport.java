/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.martus.util.TestCaseEnhanced;

public class TestMiradiReport  extends TestCaseEnhanced
{
	public TestMiradiReport(String name)
	{
		super(name);
	}
	
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting("ttttttt");
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testHasLinkage() throws Exception
	{
		project.setMetadata(ProjectMetadata.TAG_PROJECT_NAME, "this name");
		project.setMetadata(ProjectMetadata.TAG_PROJECT_SCOPE, "this scope");
		project.setMetadata(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, "this short scope");

		CodeList codeList = new CodeList();
		codeList.add(ResourceRoleQuestion.TeamMemberRoleCode);
		
		createTeamResource(codeList);
		createTeamResource(codeList);
		createTeamResource(codeList);
		
		//TODO: harded paths are here during initl developement and will be replaced 
		new MiradiReport(project).getPDFReport(
				EAM.getResourcePath(MiradiReport.class, "MiradisReport.jasper"),
				"C:/JasperReports/MardisReport.pdf");
	}


	private void createTeamResource(CodeList codeList) throws Exception
	{
		BaseId baseId = project.createObject(ProjectResource.getObjectType());
		project.setObjectData(ProjectResource.getObjectType(), baseId, ProjectResource.TAG_ROLE_CODES, codeList.toString());
		project.setObjectData(ProjectResource.getObjectType(), baseId, ProjectResource.TAG_NAME, "resource");
	}
	
	Project project;
}
