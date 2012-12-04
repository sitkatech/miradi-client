package org.miradi.questions;


public class MajorPlusCurrentLanguagesQuestion extends AbstractLanguagesQuestion
{
	@Override
	protected boolean shouldIncludeLanguage(String threeLetterCode, String oldThreeLetterCode, String twoLetterCode)
	{
		if(oldThreeLetterCode.length() > 0)
			return true;
		
		return false;
	}
}
