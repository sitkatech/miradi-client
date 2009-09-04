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
package org.miradi.objects;

import java.io.StringReader;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectPlaceTypeQuestion;
import org.miradi.utils.DelimitedFileLoader;
import org.miradi.utils.EnhancedJsonObject;

public class TncProjectData extends BaseObject
{
	public TncProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public TncProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.TNC_PROJECT_DATA;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_CLASSIFICATIONS_AS_MULTILINE_TEXT))
			return parseClassifications();
		
		return super.getPseudoData(fieldTag);
	}

	private String parseClassifications()
	{
		try
		{
			StringReader reader = new StringReader(getClassifications());
			Vector<Vector<String>> rawClassifications = new DelimitedFileLoader().getDelimitedContents(reader);
			final String NEW_LINE = "\n";
			String appendedClassications = "";
			for(int index = 0; index < rawClassifications.size(); ++index)
			{
				if (index > 0)
					appendedClassications += NEW_LINE;
				
				Vector<String> row = rawClassifications.get(index);
				appendedClassications += row.get(2);
				appendedClassications += ":";
				appendedClassications += row.get(1);
			}
			
			return appendedClassications;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error");
		}
	}
	
	public String getClassifications()
	{
		return classifications.get();
	}
	
	public static TncProjectData find(ObjectManager objectManager, ORef tncProjectDataRef)
	{
		return (TncProjectData) objectManager.findObject(tncProjectDataRef);
	}
	
	public static TncProjectData find(Project project, ORef tncProjectDataRef)
	{
		return find(project.getObjectManager(), tncProjectDataRef);
	}
		
	void clear()
	{
		super.clear();
		
		projectSharingCode = new StringData(TAG_PROJECT_SHARING_CODE);
		projectTypes = new CodeListData(TAG_PROJECT_PLACE_TYPES, getProject().getQuestion(TncProjectPlaceTypeQuestion.class));
		organizationalPriorities = new CodeListData(TAG_ORGANIZATIONAL_PRIORITIES, getProject().getQuestion(TncOrganizationalPrioritiesQuestion.class));
		parentChild = new StringData(TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		classifications = new StringData(TAG_CLASSIFICATIONS);
		
		pseudoClassifications = new PseudoStringData(PSEUDO_TAG_CLASSIFICATIONS_AS_MULTILINE_TEXT);
		
		addField(projectSharingCode);
		addField(projectTypes);
		addField(parentChild);
		addField(classifications);
		
		addField(pseudoClassifications);
	}
	
	public static final String OBJECT_NAME = "TncProjectData";

	public final static String TAG_PROJECT_SHARING_CODE = "ProjectSharingCode";
	public final static String TAG_PROJECT_PLACE_TYPES = "ProjectPlaceTypes";
	public final static String TAG_ORGANIZATIONAL_PRIORITIES = "OrganizationalPriorities";
	public final static String TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT = "ConProParentChildProjectText";
	public final static String TAG_CLASSIFICATIONS = "Classifications";
	
	public final static String PSEUDO_TAG_CLASSIFICATIONS_AS_MULTILINE_TEXT = "PsuedoClassificationsAsMultilineText";
	
	public StringData projectSharingCode;
	public CodeListData projectTypes;
	public CodeListData organizationalPriorities;
	public StringData parentChild;
	public StringData classifications;
	
	public PseudoStringData pseudoClassifications;
}
