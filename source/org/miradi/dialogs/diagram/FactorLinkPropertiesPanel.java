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
package org.miradi.dialogs.diagram;

import javax.swing.JLabel;

import org.miradi.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.threatrating.properties.StressBasedThreatRatingPropertiesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.views.umbrella.ObjectPicker;

public class FactorLinkPropertiesPanel extends ObjectDataInputPanel
{
	public static FactorLinkPropertiesPanel createWithOnlyBidirectionalAndColorPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		return new FactorLinkPropertiesPanel(projectToUse, link);
	}
	
	public static FactorLinkPropertiesPanel createGroupBoxedTargetLinkPropertiesPanel(MainWindow mainWindow, ORef factorLinkRef, ObjectPicker objectPicker) throws Exception
	{
		return new FactorLinkPropertiesPanel(mainWindow, factorLinkRef, objectPicker);
	}
	
	public static FactorLinkPropertiesPanel createTargetLinkPropertiesPanel(MainWindow mainWindow, DiagramLink link, ObjectPicker objectPicker) throws Exception
	{
		return new FactorLinkPropertiesPanel(mainWindow, link, objectPicker);
	}
	
	private FactorLinkPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		super(projectToUse, ObjectType.FACTOR_LINK, link.getWrappedId());

		addField(createCheckBoxField(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
		addField(createChoiceField(DiagramLink.getObjectType(), DiagramLink.TAG_COLOR, new DiagramLinkColorQuestion()));
		
		setObjectRefsWithGroupBoxLinkAndChildrenRefs(link);
		updateFieldsFromProject();
	}

	private FactorLinkPropertiesPanel(MainWindow mainWindow, ORef factorLinkRef, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), factorLinkRef);
				
		addThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		
		setObjectRefs(objectPicker.getSelectedHierarchies()[0]);
		updateFieldsFromProject();
	}
	
	private FactorLinkPropertiesPanel(MainWindow mainWindow, DiagramLink link, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.FACTOR_LINK, link.getWrappedId());
		DiagramLinkColorSubPanel diagramLinkColorSubPanel = new DiagramLinkColorSubPanel(getProject(), DiagramLink.getObjectType());
		addSubPanel(diagramLinkColorSubPanel);
		add(diagramLinkColorSubPanel);
		add(new JLabel(""));
		
		addThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		
		setObjectRefs(objectPicker.getSelectedHierarchies()[0]);
		updateFieldsFromProject();
	}

	private void addThreatStressRatingPropertiesPanel(MainWindow mainWindow, ObjectPicker objectPicker) throws Exception
	{
		StressBasedThreatRatingPropertiesPanel threatStressRatingPropertiesPanel = new StressBasedThreatRatingPropertiesPanel(mainWindow, objectPicker);
		addSubPanel(threatStressRatingPropertiesPanel);
		add(threatStressRatingPropertiesPanel);
	}
	
	private void setObjectRefsWithGroupBoxLinkAndChildrenRefs(DiagramLink groupBoxLink)
	{
		ORefList selfOrChildrenRefs = groupBoxLink.getSelfOrChildren();
		ORefList groupBoxAndChildrenWrappedRefs = new ORefList();
		for (int refIndex = 0; refIndex < selfOrChildrenRefs.size(); ++refIndex)
		{
			DiagramLink childLink = DiagramLink.find(getProject(), selfOrChildrenRefs.get(refIndex));
			ORef factorLinkRef = childLink.getWrappedRef();
			groupBoxAndChildrenWrappedRefs.add(factorLinkRef);
		}
		
		ORef diagramLinkRef = groupBoxLink.getRef();
		groupBoxAndChildrenWrappedRefs.add(diagramLinkRef);
		setObjectRefs(groupBoxAndChildrenWrappedRefs);
	}

	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class;
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK))
			ensureSiblingsHaveEqualBidirectionality((CommandSetObjectData) event.getCommand());
	}

	private void ensureSiblingsHaveEqualBidirectionality(CommandSetObjectData command)
	{
		try
		{
			ORef factorLinkRef = command.getObjectORef();
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
			ORefList diagramLinkReferrerRefs = factorLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
			for (int index = 0; index < diagramLinkReferrerRefs.size(); ++index)
			{
				DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrerRefs.get(index));
				ensureSiblingsHaveEqualBidirectionality(diagramLink);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void ensureSiblingsHaveEqualBidirectionality(DiagramLink diagramLink) throws CommandFailedException
	{
		ORefList groupBoxReferrers = diagramLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int index = 0; index < groupBoxReferrers.size(); ++index)
		{
			DiagramLink groupBoxLink = DiagramLink.find(getProject(), groupBoxReferrers.get(index));
			ensureChildrenBidirectionality(groupBoxLink, diagramLink.getUnderlyingLink().getData(FactorLink.TAG_BIDIRECTIONAL_LINK));
		}
	}

	private void ensureChildrenBidirectionality(DiagramLink groupBoxLink, String bidirectionalMode) throws CommandFailedException
	{
		ORefList diagramLinkChildRefs = groupBoxLink.getGroupedDiagramLinkRefs();
		for (int index = 0; index < diagramLinkChildRefs.size(); ++index)
		{
			DiagramLink childLink = DiagramLink.find(getProject(), diagramLinkChildRefs.get(index));
			String childLinkBidirectionalMode = childLink.getUnderlyingLink().getData(FactorLink.TAG_BIDIRECTIONAL_LINK);
			if (!bidirectionalMode.equals(childLinkBidirectionalMode))
			{
				CommandSetObjectData setBidirectionality = new CommandSetObjectData(childLink.getWrappedRef(), FactorLink.TAG_BIDIRECTIONAL_LINK, bidirectionalMode);
				getProject().executeAsSideEffect(setBidirectionality);
			}
		}
	}
}
