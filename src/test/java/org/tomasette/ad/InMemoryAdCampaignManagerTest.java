package org.tomasette.ad;

import junit.framework.TestCase;

import org.junit.Test;

public class InMemoryAdCampaignManagerTest extends TestCase {
	private Ad a;
	private Ad b;
	private AdCampaign activeCamp;
	private AdCampaign expiredCamp;
	private Partner partnerA;
	private Partner partnerB;
	private InMemoryAdCampaignManager cMan;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		a = new Ad("hello kitty");
		b = new Ad("goodbye kitty");
		activeCamp = new AdCampaign(50000000, a);
		expiredCamp = new AdCampaign(-1, b);

		partnerA = new Partner("12");
		partnerB = new Partner("13");
		cMan = new InMemoryAdCampaignManager();

	}

	@Test
	public void testCreatePartner() throws Exception {
		cMan.createPartner("12");
		cMan.createPartner("13");
		assertTrue(cMan.hasPartner("12"));
		assertFalse(cMan.hasPartner("14"));
		assertTrue(cMan.fetchPartner("12").equals(partnerA));
		assertTrue(cMan.fetchPartner("13").equals(partnerB));

	}

	@Test
	public void testCreateDuplicatePartner() throws Exception {
		cMan.createPartner("12");
		cMan.createPartner("13");

		try {
			cMan.createPartner("12");
		} catch (Exception e) {
			assertTrue(e.getMessage().equals(
					InMemoryAdCampaignManager.DUPLICATE_PARTNER_CREATE_FAIL));
			assertTrue(cMan.fetchPartner("12").equals(partnerA));
			assertTrue(cMan.fetchPartner("13").equals(partnerB));
			return;
		}
		fail();
	}

	@Test
	public void testCreateNullPartner() throws Exception {

		try {
			cMan.createPartner(null);
		} catch (NullPointerException e) {
			assertTrue(e.getMessage().equals(
					InMemoryAdCampaignManager.NULL_PARTNER_ID_FAIL));

			return;
		}
		fail("Expected NullPointerException");
	}

	@Test
	public void testInsertAndAddAdCampaign() throws Exception {
		cMan.createPartner("12");
		cMan.createPartner("13");
		cMan.insertAdCampaign("12", expiredCamp);
		cMan.insertAdCampaign("12", activeCamp);
		cMan.insertAdCampaign("13", expiredCamp);
		assertTrue(cMan.fetchRandomActiveAdCampaign("12").equals(activeCamp));

		try {
			cMan.fetchRandomActiveAdCampaign("14");
		} catch (Exception e) {
			assertTrue(e
					.getMessage()
					.equals(
							InMemoryAdCampaignManager.PARTNER_DOES_NOT_EXIST_FETCH_FAIL));
		}

		try {

			cMan.insertAdCampaign("14", activeCamp);

		} catch (Exception e) {
			assertTrue(e
					.getMessage()
					.equals(
							InMemoryAdCampaignManager.PARTNER_DOES_NOT_EXIST_ADD_CAMP_FAIL));

		}

		try {

			cMan.insertAdCampaign(null, activeCamp);

		} catch (NullPointerException e) {
			assertTrue(e.getMessage().equals(
					InMemoryAdCampaignManager.NULL_PARTNER_ID_FAIL));

		}

		try {

			cMan.insertAdCampaign("14", null);

		} catch (Exception e) {
			assertTrue(e.getMessage().equals(
					InMemoryAdCampaignManager.NULL_AD_CAMPAIGN_FAIL));

		}
		assertEquals(2, cMan.fetchAllAdCampaigns("12").size());

	}
}
