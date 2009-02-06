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

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class TestThreatRatingBundle extends EAMTestCase
{
	public TestThreatRatingBundle(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		FactorId threatId = new FactorId(5);
		FactorId targetId = new FactorId(9);
		BaseId defaultValueId = new BaseId(29);
		
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		assertEquals("can't get threat id?", threatId, bundle.getThreatId());
		assertEquals("can't get target it?", targetId, bundle.getTargetId());
		
		BaseId criterionId = new BaseId(4);
		assertEquals("non-existant value isn't INVALID?", defaultValueId, bundle.getValueId(criterionId));
		
		BaseId valueId = new BaseId(12);
		bundle.setValueId(criterionId, valueId);
		assertEquals("didn't remember value?", valueId, bundle.getValueId(criterionId));
	}
	
	public void testPullDataFrom() throws Exception
	{
		FactorId threatId = new FactorId(5);
		FactorId targetId = new FactorId(9);
		BaseId defaultValueId = new BaseId(29);
		FactorId threatId2 = new FactorId(7);
		FactorId targetId2 = new FactorId(8);
		BaseId defaultValueId2 = new BaseId(59);
		
		BaseId criterionId = new BaseId(34);
		BaseId valueId = new BaseId(10);

		ThreatRatingBundle originalBundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		originalBundle.setValueId(criterionId, valueId);
		
		ThreatRatingBundle copiedBundle = new ThreatRatingBundle(threatId2, targetId2, defaultValueId2);
		copiedBundle.pullDataFrom(originalBundle);
		
		verifyBundlesEqual(criterionId, originalBundle, copiedBundle);
		
		ThreatRatingBundle copyConstructorBundle = new ThreatRatingBundle(originalBundle);
		verifyBundlesEqual(criterionId, originalBundle, copyConstructorBundle);
	}

	private void verifyBundlesEqual(BaseId criterionId, ThreatRatingBundle originalBundle, ThreatRatingBundle copiedBundle)
	{
		assertEquals("threatId wrong?", originalBundle.getThreatId(), copiedBundle.getThreatId());
		assertEquals("targetId wrong?", originalBundle.getTargetId(), copiedBundle.getTargetId());
		assertEquals("defaultValueId wrong?", originalBundle.getDefaultValueId(), copiedBundle.getDefaultValueId());
		assertEquals("valueId wrong?", originalBundle.getValueId(criterionId), copiedBundle.getValueId(criterionId));
	}
	
	public void testJson() throws Exception
	{
		FactorId threatId = new FactorId(5);
		FactorId targetId = new FactorId(9);
		BaseId defaultValueId = new BaseId(29);
		BaseId criterion1 = new BaseId(32);
		BaseId rating1 = new BaseId(1);
		BaseId criterion2 = new BaseId(57);
		BaseId rating2 = new BaseId(2);
		
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		bundle.setValueId(criterion1, rating1);
		bundle.setValueId(criterion2, rating2);
		EnhancedJsonObject json = bundle.toJson();
		ThreatRatingBundle loaded = new ThreatRatingBundle(json);
		assertEquals("Didn't load threat id?", bundle.getThreatId(), loaded.getThreatId());
		assertEquals("Didn't load target id?", bundle.getTargetId(), loaded.getTargetId());
		assertEquals("Didn't load default?", bundle.getValueId(new BaseId(-999)), loaded.getValueId(new BaseId(-999)));
		assertEquals("Didn't load rating 1?", bundle.getValueId(criterion1), loaded.getValueId(criterion1));
		assertEquals("Didn't load rating 2?", bundle.getValueId(criterion2), loaded.getValueId(criterion2));
	}
}
