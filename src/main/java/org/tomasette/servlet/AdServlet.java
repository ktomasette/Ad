package org.tomasette.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tomasette.ad.Ad;
import org.tomasette.ad.AdCampaign;
import org.tomasette.ad.IAdCampaignManager;
import org.tomasette.ad.InMemoryAdCampaignManager;
import org.tomasette.ad.Partner;
import org.tomasette.model.AdGetError;
import org.tomasette.model.AdGetResult;
import org.tomasette.model.AdPostResult;

import com.google.gson.Gson;

/**
 * Servlet implementation class AdServlet
 */
public class AdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AdServlet.class
			.getName());

	private IAdCampaignManager adManager;

	/**
	 * Default constructor.
	 */
	public AdServlet() {

	}

	/**
	 * Getter method that defaults to an InMemoryAdCampaignManger. Allows
	 * testing to inject a mock.
	 * 
	 * @return
	 */
	public IAdCampaignManager getAdManager() {
		if (adManager == null)
			this.adManager = new InMemoryAdCampaignManager();

		return this.adManager;
	}

	/**
	 * setter for testing mock injection only
	 * 
	 * @param adManager
	 */
	void setAdManager(IAdCampaignManager adManager) {
		this.adManager = adManager;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		log.debug("doGet");

		String partnerId = request.getParameter("partner_id");
		boolean getAll = request.getParameter("all") == null ? false : true;

		log.debug("Partner Id: " + partnerId + " All =" + getAll);

		PrintWriter out = response.getWriter();

		if (getAll) {
			processGetAll(out);
		} else {
			processGetActive(out, partnerId);
		}

		out.close();
	}

	private void processGetAll(PrintWriter out) {
		Collection<Partner> partners = getAdManager().fetchAllPartners();

		if (partners == null) {
			String jsonpError = generateJsonGetError("No campaigns for partner.");
			out.print(jsonpError);

			return;
		}
		Gson gson = new Gson();
		synchronized (partners) {

			out.print(gson.toJson(partners));
		}

	}

	private void processGetActive(PrintWriter out, String partnerId) {
		AdCampaign adCamp = null;
		try {
			adCamp = getAdManager().fetchRandomActiveAdCampaign(partnerId);
		} catch (Exception e) {
			log.debug(e.getMessage());

			String jsonpError = generateJsonpGetError("No active campaign for partner.");
			out.print(jsonpError);

			return;
		}

		if (adCamp != null) {

			synchronized (adCamp) {
				String jsonpResult = generateJsonpGetResult(adCamp.getAd()
						.getAdContent());
				out.print(jsonpResult);
			}
		} else {

			String jsonpError = generateJsonpGetError("No active campaign for partner.");
			out.print(jsonpError);

		}
	}

	private String generateJsonpGetResult(String adContent) {

		Gson gson = new Gson();
		AdGetResult result = new AdGetResult();
		result.setAdContent(adContent);
		return wrapJsonp(gson.toJson(result));

	}

	private String generateJsonGetError(String message) {
		AdGetError error = new AdGetError();
		error.setError(message);
		Gson gson = new Gson();
		return gson.toJson(error);
	}

	private String generateJsonpGetError(String message) {

		return wrapJsonp(generateJsonGetError(message));
	}

	private String generateJsonPostResult(String result) {
		AdPostResult resObj = new AdPostResult();
		resObj.setResult(result);
		Gson gson = new Gson();
		return gson.toJson(resObj);

	}

	private String wrapJsonp(String json) {
		return "callback(" + json + ");";
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doPost");

		// retrieve Post data
		String partnerId = request.getParameter("partner_id");
		String duration = request.getParameter("duration");
		String adContent = request.getParameter("ad_content");

		log.debug("Partner: " + partnerId + " - Duration: " + duration
				+ " - Content: " + adContent);

		PrintWriter out = response.getWriter();

		boolean validateOk = validate(partnerId, duration, adContent);
		if (!validateOk) {
			log.debug("Input Validation Failed");
			String json = generateJsonPostResult("Invalid input.");
			out.print(json);
			out.close();
			return;
		}

		if (!getAdManager().hasPartner(partnerId)) {
			try {
				getAdManager().createPartner(partnerId);
			} catch (Exception e) {

				log.debug(e.getMessage());
				String json = generateJsonPostResult("failure creating partner.");
				out.print(json);
				out.close();
				return;

			}

		}

		long dur = Long.parseLong(duration);
		AdCampaign adCampaign = new AdCampaign(dur, new Ad(adContent));
		try {
			getAdManager().insertAdCampaign(partnerId, adCampaign);

			String json = generateJsonPostResult("success");
			out.print(json);

		} catch (Exception e) {

			log.debug(e.getMessage());
			String json = null;
			if (e.getMessage().equals(Partner.ALREADY_HAS_ACTIVE_ADD_FAIL)
					|| e.getMessage().equals(Partner.DUPLICATE_ENTRY_ADD_FAIL)) {
				json = generateJsonPostResult(e.getMessage());
			} else {
				json = generateJsonPostResult("general failure");
			}
			out.print(json);

		}

		out.close();

	}

	private boolean validate(String partnerId, String duration, String adContent) {
		if (partnerId == null || duration == null || adContent == null)
			return false;
		if (StringUtils.isBlank(duration) || !StringUtils.isNumeric(duration))
			return false;
		return true;
	}
}
