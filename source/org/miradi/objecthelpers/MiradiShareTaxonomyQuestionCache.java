/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.objecthelpers;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.objects.MiradiShareTaxonomy;
import org.miradi.project.Project;
import org.miradi.questions.MiradiShareTaxonomyQuestion;

import java.util.HashMap;

public class MiradiShareTaxonomyQuestionCache implements CommandExecutedListener
{
	public MiradiShareTaxonomyQuestionCache(Project projectToUse)
	{
		project = projectToUse;
		clear();
	}
	
	public void clear()
	{
		clearAllCachedData();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if (commandInvalidatesCache(event))
			clear();
	}

	private boolean commandInvalidatesCache(CommandExecutedEvent event)
	{
		return false;
	}

	private void clearAllCachedData()
	{
		allMiradiShareTaxonomyQuestions = new HashMap<MiradiShareTaxonomyQuestionCacheKey, MiradiShareTaxonomyQuestion>();
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	private Project getProject()
	{
		return project;
	}

	public MiradiShareTaxonomyQuestion getMiradiShareTaxonomyQuestion(MiradiShareTaxonomy miradiShareTaxonomy, AbstractTaxonomyAssociation taxonomyAssociation) throws Exception
	{
		MiradiShareTaxonomyQuestionCacheKey cacheKey = new MiradiShareTaxonomyQuestionCacheKey(miradiShareTaxonomy, taxonomyAssociation);
		MiradiShareTaxonomyQuestion result = allMiradiShareTaxonomyQuestions.get(cacheKey);
		if(result == null)
		{
			result = new MiradiShareTaxonomyQuestion(miradiShareTaxonomy, taxonomyAssociation);
			allMiradiShareTaxonomyQuestions.put(cacheKey, result);
		}

		return result;
	}

	private class MiradiShareTaxonomyQuestionCacheKey
	{
		public MiradiShareTaxonomyQuestionCacheKey(MiradiShareTaxonomy miradiShareTaxonomyToUse, AbstractTaxonomyAssociation taxonomyAssociationToUse)
		{
			miradiShareTaxonomy = miradiShareTaxonomyToUse;
			taxonomyAssociation = taxonomyAssociationToUse;
		}

		@Override
		public String toString()
		{
			return miradiShareTaxonomy.getTaxonomyCode() + ":" + taxonomyAssociation.getTaxonomyCode();
		}

		@Override
		public boolean equals(Object rawOther)
		{
			if(!(rawOther instanceof MiradiShareTaxonomyQuestionCacheKey))
				return false;

			MiradiShareTaxonomyQuestionCacheKey other = (MiradiShareTaxonomyQuestionCacheKey)rawOther;
			if(!miradiShareTaxonomy.equals(other.miradiShareTaxonomy))
				return false;
			return (taxonomyAssociation.equals(other.taxonomyAssociation));
		}

		@Override
		public int hashCode()
		{
			return toString().hashCode();
		}

		private MiradiShareTaxonomy miradiShareTaxonomy;
		private AbstractTaxonomyAssociation taxonomyAssociation;
	}

	private Project project;
	private HashMap<MiradiShareTaxonomyQuestionCacheKey, MiradiShareTaxonomyQuestion> allMiradiShareTaxonomyQuestions;
}
