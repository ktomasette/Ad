package org.tomasette.servlet;

import java.util.Collection;
import java.util.LinkedList;

import org.easymock.EasyMock;
import org.tomasette.ad.Ad;
import org.tomasette.ad.AdCampaign;
import org.tomasette.ad.IAdCampaignManager;
import org.tomasette.ad.Partner;

import com.google.gson.Gson;
import com.mockrunner.servlet.BasicServletTestCaseAdapter;

public class AdServletTest extends BasicServletTestCaseAdapter {

	IAdCampaignManager mockManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createServlet(AdServlet.class);
		mockManager = EasyMock.createMock(IAdCampaignManager.class);

	}

	public void testGetAll() throws Exception {
		// setup mock
		Collection<Partner> partners = new LinkedList<Partner>();

		Partner partner = new Partner("12");
		partner.addAdCampaign(new AdCampaign(500000, new Ad("temp ad1")));
		partner.addAdCampaign(new AdCampaign(500000, new Ad("temp ad2")));
		partner.addAdCampaign(new AdCampaign(500000, new Ad("temp ad3")));
		partners.add(partner);

		partner = new Partner("13");
		partner.addAdCampaign(new AdCampaign(500000, new Ad("temp ad4")));
		partner.addAdCampaign(new AdCampaign(500000, new Ad("temp ad5")));
		partner.addAdCampaign(new AdCampaign(500000, new Ad("temp ad6")));
		partners.add(partner);

		EasyMock.expect(mockManager.fetchAllPartners()).andReturn(partners);
		EasyMock.replay(mockManager);
		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("all", "");
		doGet();

		String output = getOutput();

		EasyMock.verify(mockManager);

		Gson gson = new Gson();
		String expected = gson.toJson(partners);

		assertEquals(expected, output);

	}

	public void testGet() throws Exception {
		// setup mock
		AdCampaign adCamp = new AdCampaign(500000, new Ad("temp ad"));
		EasyMock.expect(
				mockManager.fetchRandomActiveAdCampaign(EasyMock
						.isA(String.class))).andReturn(adCamp);
		EasyMock.replay(mockManager);
		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		doGet();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals("callback({\"ad_content\":\"temp ad\"});", output);
	}

	public void testGetErrorNoPartner() throws Exception {
		// setup mock

		EasyMock.expect(
				mockManager.fetchRandomActiveAdCampaign(EasyMock
						.isA(String.class))).andThrow(
				new Exception("Expected Exception"));
		EasyMock.replay(mockManager);
		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		doGet();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals(
				"callback({\"error\":\"No active campaign for partner.\"});",
				output);
	}

	public void testGetError() throws Exception {
		// setup mock
		EasyMock.expect(
				mockManager.fetchRandomActiveAdCampaign(EasyMock
						.isA(String.class))).andReturn(null);
		EasyMock.replay(mockManager);
		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		doGet();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals(
				"callback({\"error\":\"No active campaign for partner.\"});",
				output);

	}

	public void testPost() throws Exception {
		// setup mock
		EasyMock.expect(mockManager.hasPartner(EasyMock.isA(String.class)))
				.andReturn(true);

		mockManager.insertAdCampaign(EasyMock.isA(String.class), EasyMock
				.isA(AdCampaign.class));
		EasyMock.expectLastCall();
		EasyMock.replay(mockManager);

		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		addRequestParameter("duration", "5000");
		addRequestParameter("ad_content", "hello kitty!");
		doPost();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals("{\"result\":\"success\"}", output);
	}

	public void testPostNoPartner() throws Exception {
		// setup mock
		EasyMock.expect(mockManager.hasPartner(EasyMock.isA(String.class)))
				.andReturn(false);

		mockManager.createPartner(EasyMock.isA(String.class));
		EasyMock.expectLastCall();

		mockManager.insertAdCampaign(EasyMock.isA(String.class), EasyMock
				.isA(AdCampaign.class));
		EasyMock.expectLastCall();

		EasyMock.replay(mockManager);

		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		addRequestParameter("duration", "5000");
		addRequestParameter("ad_content", "hello kitty!");
		doPost();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals("{\"result\":\"success\"}", output);
	}

	public void testPostCreatePartnerFailure() throws Exception {
		// setup mock

		EasyMock.expect(mockManager.hasPartner(EasyMock.isA(String.class)))
				.andReturn(false);

		mockManager.createPartner(EasyMock.isA(String.class));
		EasyMock.expectLastCall().andThrow(new Exception("Expected exception"));

		EasyMock.replay(mockManager);

		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		addRequestParameter("duration", "5000");
		addRequestParameter("ad_content", "hello kitty!");
		doPost();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals("{\"result\":\"failure creating partner.\"}", output);
	}

	public void testPostInsertAdCampaignFailure() throws Exception {
		// setup mock

		EasyMock.expect(mockManager.hasPartner(EasyMock.isA(String.class)))
				.andReturn(false);

		mockManager.createPartner(EasyMock.isA(String.class));
		EasyMock.expectLastCall();

		mockManager.insertAdCampaign(EasyMock.isA(String.class), EasyMock
				.isA(AdCampaign.class));
		EasyMock.expectLastCall().andThrow(new Exception("Expected exception"));

		EasyMock.replay(mockManager);

		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		addRequestParameter("duration", "5000");
		addRequestParameter("ad_content", "hello kitty!");
		doPost();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals("{\"result\":\"general failure\"}", output);
	}

	public void testPostInsertBadInput() throws Exception {
		// setup mock

		EasyMock.replay(mockManager);

		// inject mock
		((AdServlet) getServlet()).setAdManager(mockManager);

		addRequestParameter("partner_id", "12");
		addRequestParameter("duration", "");
		addRequestParameter("ad_content", "hello kitty!");
		doPost();

		String output = getOutput();

		EasyMock.verify(mockManager);

		assertEquals("{\"result\":\"Invalid input.\"}", output);
	}
}
