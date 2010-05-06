package org.tomasette.ad;

import junit.framework.TestCase;

import org.junit.Test;

public class AdCampaignTest extends TestCase {

	@Test
	public void testIsActive() throws Exception {

		Ad expiredAd = new Ad("this is really old");
		Ad currentAd = new Ad("this is fresh and new");
		AdCampaign expiredAdCampaign = new AdCampaign(-1, expiredAd);

		AdCampaign activeAdCampaign = new AdCampaign(50000000, currentAd);

		assertFalse(expiredAdCampaign.isActive());
		assertTrue(activeAdCampaign.isActive());

	}

}
