package org.tomasette.ad;

import junit.framework.TestCase;

import org.junit.Test;

public class PartnerTest extends TestCase {

	private Ad a;
	private Ad b;
	private AdCampaign activeCamp;
	private AdCampaign expiredCamp;
	private Partner partner;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		a = new Ad("hello kitty");
		b = new Ad("goodbye kitty");
		activeCamp = new AdCampaign(50000000, a);
		expiredCamp = new AdCampaign(-1, b);

		partner = new Partner("12");

	}

	@Test
	public void testHasActiveAdCampaigns() throws Exception {

		partner.addAdCampaign(activeCamp);
		assertTrue(partner.hasActiveCampaigns());

	}

	@Test
	public void testNotHasActiveAdCampaigns() throws Exception {

		partner.addAdCampaign(expiredCamp);
		assertFalse(partner.hasActiveCampaigns());

	}

	@Test
	public void testDoubleAddAdCampaigns() throws Exception {

		partner.addAdCampaign(expiredCamp);
		try {
			partner.addAdCampaign(expiredCamp);
		} catch (Exception e) {
			assertTrue(e.getMessage().equals(Partner.DUPLICATE_ENTRY_ADD_FAIL));
			return;
		}
		fail();
	}

	@Test
	public void testAlreadyActiveAddAdCampaigns() throws Exception {

		partner.addAdCampaign(activeCamp);
		AdCampaign anotherActiveCamp = new AdCampaign(50000000, new Ad(
				"another ad"));

		try {
			partner.addAdCampaign(anotherActiveCamp);
		} catch (Exception e) {
			fail("add active should have worked.");
		}
		assertEquals(2, partner.fetchAllAdCampaigns().size());

	}

	@Test
	public void testAlreadyActiveWithOthersAddAdCampaigns() throws Exception {

		partner.addAdCampaign(expiredCamp);
		partner.addAdCampaign(activeCamp);
		AdCampaign anotherActiveCamp = new AdCampaign(50000000, new Ad(
				"another ad"));

		try {
			partner.addAdCampaign(anotherActiveCamp);
		} catch (Exception e) {
			fail("Insert another active should have worked");

		}
		assertEquals(3, partner.fetchAllAdCampaigns().size());

	}

	@Test
	public void testFetchWhenNoActive() throws Exception {
		partner.addAdCampaign(expiredCamp);
		AdCampaign actual = partner.fetchRandomActiveAdCampaign();

		assertTrue(actual == null);
	}

	@Test
	public void testFetchWhenActive() throws Exception {
		partner.addAdCampaign(expiredCamp);
		partner.addAdCampaign(activeCamp);
		AdCampaign actual = partner.fetchRandomActiveAdCampaign();

		assertTrue(actual.equals(activeCamp));
	}

	@Test
	public void testFetchWhenMultipleActive() throws Exception {

		partner.addAdCampaign(expiredCamp);
		partner.addAdCampaign(activeCamp);

		AdCampaign anotherActiveCamp = new AdCampaign(50000000, new Ad(
				"another ad2"));
		partner.addAdCampaign(anotherActiveCamp);

		AdCampaign actual = partner.fetchRandomActiveAdCampaign();

		assertFalse(actual.equals(expiredCamp));
		assertTrue(actual.equals(activeCamp)
				|| actual.equals(anotherActiveCamp));
	}
}
