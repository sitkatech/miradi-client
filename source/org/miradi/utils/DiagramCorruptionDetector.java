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
package org.miradi.utils;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.ConceptualModelDiagramPool;
import org.miradi.objectpools.ResultsChainDiagramPool;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class DiagramCorruptionDetector
{
	public static Vector<String> getCorruptedDiagrams(Project project)
	{
		Vector<String> errorMessages = new Vector();
		ORefList diagramObjectRefs = project.getAllDiagramObjectRefs();
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectRefs.get(index));
			errorMessages.addAll(getDiagramCorruptedErrorMessages(project, diagramObject));
		}
		
		return errorMessages;
	}

	private static Vector<String> getDiagramCorruptedErrorMessages(Project project, DiagramObject diagramObject)
	{
		Vector<String> errorMessages = new Vector();
		errorMessages.addAll(getCorruptedDiagramFactorErrorMessages(project, diagramObject));
		errorMessages.addAll(getCorruptedDiagramLinksErrorMessages(project, diagramObject));
		
		return errorMessages;
	}

	protected static Vector<String> getCorruptedDiagramLinksErrorMessages(Project project, DiagramObject diagramObject)
	{
		String diagramName = diagramObject.toString();
		Vector<String> errorMessages = new Vector();
		ORefList diagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRefs.get(index));
			if (diagramLink ==null)
			{	
				EAM.logVerbose("Found null diagramLink ref = " + diagramLinkRefs.get(index));
				errorMessages.add(EAM.substitute(EAM.text("Found null diagramLink.  Diagram = %s"), diagramName));
				continue;
			}

			DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
			DiagramFactor toDiagramFactor = diagramLink.getToDiagramFactor();
			if (fromDiagramFactor == null || toDiagramFactor == null)
			{
				EAM.logVerbose("Found null from or to for diagram link ref = " + diagramLink.getRef() + " .  from = " + fromDiagramFactor + " to = " + toDiagramFactor);
				errorMessages.add(EAM.substitute(EAM.text("Found null from or to for diagram link.  Diagram = %s"), diagramName));
				continue;
			}
			
			if (fromDiagramFactor.getWrappedFactor() == null || toDiagramFactor.getWrappedFactor() == null)
			{
				EAM.logVerbose("Found null from wrapped factor or to wrapped factor from diagram link ref = " + diagramLink.getRef() + " from wrapped ref = " +fromDiagramFactor.getWrappedORef() +  " to wrapped ref = " + toDiagramFactor.getWrappedORef() );
				errorMessages.add(EAM.substitute(EAM.text("Found null from wrapped factor or to wrapped factor from diagram link.  Diagram = %s"), diagramName));
				continue;
			}
		}
		
		return errorMessages;
	}

	protected static Vector<DiagramLink> getCorruptedGroupBoxDiagramLinks(Project project, DiagramObject diagramObject)
	{
		Vector<DiagramLink> errorMessages = new Vector();
		ORefList diagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRefs.get(index));
			FactorLink factorLink = FactorLink.find(project, diagramLink.getWrappedRef());
			if (factorLink == null && !diagramLink.isGroupBoxLink())
			{
				errorMessages.add(diagramLink);
				continue;
			}
		}
		
		return errorMessages;
	}


	protected static Vector<String> getCorruptedDiagramFactorErrorMessages(Project project, DiagramObject diagramObject)
	{
		Vector<String> errorMessages = new Vector();
		String diagramName = diagramObject.toString();
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
			if (diagramFactor == null)
			{
				String errorMesssage = "Found null diagram factor. Ref = " + diagramFactorRefs.get(index);
				EAM.logVerbose(errorMesssage);
				errorMessages.add(EAM.substitute(EAM.text("Found null diagram factor.  Diagram = %s"), diagramName));
				continue;
			}
			
			final Factor factor = diagramFactor.getWrappedFactor();
			if (factor == null)
			{
				EAM.logVerbose("Found null wrapped factor.  Ref = " + diagramFactor.getWrappedORef());
				errorMessages.add(EAM.substitute(EAM.text("Found null underlying factor  .Diagram = %s"), diagramName));
				continue;
			}
			
			if (Task.is(factor) && !factor.isActivity())
			{
				EAM.logVerbose("Found non activity factor that is a task.  Diagram factor ref = " + diagramFactor.getRef());
				
				String message = EAM.text("Found non activity factor that is a task. label = %FactorName.  Diagram = %DiagramName");
				message = EAM.substitute(message, "%FactorName", factor.getLabel());
				message = EAM.substitute(message, "%DiagramName", diagramName);
				
				errorMessages.add(message);
				continue;
			}
		}
		
		return errorMessages;
	}

	public static void warnUserAboutGroupBoxLinkCorruption(MainWindow mainWindow)
	{
		StringBuffer body = new StringBuffer();
		
		Project project = mainWindow.getProject();
		ConceptualModelDiagramPool cmPool = project.getConceptualModelDiagramPool();
		String cmErrors = getGroupBoxLinkCorruptedErrorText(project, "Conceptual Model page", cmPool.getSortedRefList());
		if(cmErrors.length() > 0)
			body.append(cmErrors);
		
		ResultsChainDiagramPool rcPool = project.getResultsChainDiagramPool();
		String rcErrors = getGroupBoxLinkCorruptedErrorText(project, "Results Chain", rcPool.getSortedRefList());
		if(rcErrors.length() > 0)
			body.append(rcErrors);
		
		if(body.length() > 0)
		{
			String title = EAM.text("Title|Found Corrupted Links");
			new HtmlViewPanel(mainWindow, title, body.toString()).showAsOkDialog();
		}
		
	}

	private static String getGroupBoxLinkCorruptedErrorText(Project project, String diagramType, ORefList diagramRefs)
	{
		StringBuffer body = new StringBuffer();
		for(int i = 0; i < diagramRefs.size(); ++i)
		{
			StringBuffer pageErrors = new StringBuffer();
			DiagramObject cmPage = DiagramObject.findDiagramObject(project, diagramRefs.get(i));
			Vector<DiagramLink> corruptedDiagramLinks = getCorruptedGroupBoxDiagramLinks(project, cmPage);
			for(DiagramLink link : corruptedDiagramLinks)
			{
				pageErrors.append("<li>");
				pageErrors.append(buildGroupBoxLinkErrorMessage(link));
				pageErrors.append("</li>");
			}
			
			if(pageErrors.length() > 0)
			{
				String header = buildGroupBoxLinkErrorHeader(diagramType, cmPage);
				body.append(header);
				body.append("<ul>");
				body.append(pageErrors);
				body.append("</ul>");
			}
		}
		
		if(body.length() > 0)
			body.append("\n<hr></hr>\n");
	
		return body.toString();
	}

	private static String buildGroupBoxLinkErrorHeader(String diagramType, DiagramObject cmPage)
	{
		String header = EAM.text("Corrupted link(s) have been found on %DiagramType: '%DiagramName'.<br>" +
				"For each of the following, please delete the link and re-create it:<br>");
		header = EAM.substitute(header, "%DiagramType", diagramType);
		header = EAM.substitute(header, "%DiagramName", cmPage.getLabel());
		return header;
	}

	private static String buildGroupBoxLinkErrorMessage(DiagramLink diagramLink)
	{
		String fromName = diagramLink.getFromDiagramFactor().getWrappedFactor().toString();
		String toName = diagramLink.getToDiagramFactor().getWrappedFactor().toString();
		String message = EAM.text("'%FromName' -> '%ToName'<br>");
		message = EAM.substitute(message, "%FromName", fromName);
		message = EAM.substitute(message, "%ToName", toName);
		return message.toString();
	}
}
