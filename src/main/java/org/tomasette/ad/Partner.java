package org.tomasette.ad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Represents a Partner. A Partner has a bunch of Ad Campaigns. Only one
 * AdCampaign can be active at once. Uses an HashSet to hold the AdCampaigns.
 * This ensures uniqueness of AdCampaigns as well as preserving order in case
 * that is important.
 * 
 * @author ktomasette
 * 
 */
public class Partner {
	private final String partnerId;

	private final Set<AdCampaign> adCampaigns;

	public static final String DUPLICATE_ENTRY_ADD_FAIL = "Add AdCampaign failed: already has this campaign";
	public static final String ALREADY_HAS_ACTIVE_ADD_FAIL = "Add AdCampaign failed: already has an active campaign.";

	/**
	 * Constructor
	 * 
	 * @param partnerId
	 */
	public Partner(String partnerId) {
		this.partnerId = partnerId;
		adCampaigns = new HashSet<AdCampaign>();
	}

	/**
	 * partnerId getter
	 * 
	 * @return the partner's Id
	 */
	public String getPartnerId() {
		return partnerId;
	}

	/**
	 * Will fetch the AdCampaign that is active. If there are more than one
	 * active campaigns will randomly pick one
	 * 
	 * @return the active add campaign or null if there is none
	 */
	public AdCampaign fetchRandomActiveAdCampaign() {

		ArrayList<AdCampaign> activeAdCamps = new ArrayList<AdCampaign>();
		for (AdCampaign adCampaign : adCampaigns) {
			if (adCampaign.isActive())
				activeAdCamps.add(adCampaign);
		}
		if (activeAdCamps.isEmpty()) {
			return null;
		}
		return getRandomAdCampaign(activeAdCamps);

	}

	/**
	 * Randomly selects an Ad Campaign give an array of ad campaigns.
	 * 
	 * this is protected so that it can be mocked out for testing.
	 * 
	 * @param adCamps
	 * @return a randomly selected AdCampaign
	 */
	public AdCampaign getRandomAdCampaign(ArrayList<AdCampaign> adCamps) {
		Random r = new Random(System.currentTimeMillis());
		int slot = (int) Math.round(r.nextDouble() * (adCamps.size() - 1));
		return adCamps.get(slot);
	}

	/**
	 * Attempts to add a campaign to the partner. If the campaign already exists
	 * an exception is thrown. If the partner already has an active campaign an
	 * exception is thrown.
	 * 
	 * @param an
	 *            adCampaign to be added to the Partner
	 * @Throws Exception if failed to ad a Campaign
	 */
	public void addAdCampaign(AdCampaign adCampaign) throws Exception {

		if (adCampaign == null)
			throw new NullPointerException(
					"adCampaign is null so cannot be added.");

		boolean addOk = adCampaigns.add(adCampaign);

		// see if add worked
		if (!addOk) {
			throw new Exception(DUPLICATE_ENTRY_ADD_FAIL);
		}

	}

	/**
	 * Determines if the Partner has an active campaign. Returns true if there
	 * is one false if not.
	 * 
	 * @return true if has active campaigns, false if not
	 */
	public boolean hasActiveCampaigns() {

		for (AdCampaign adCampaign : adCampaigns) {
			if (adCampaign.isActive())
				return true;
		}
		return false;
	}

	/**
	 * Returns a Set of AdCampaigns belonging to this Partner
	 * 
	 * @return Set of AdCampaigns
	 * 
	 */
	public Set<AdCampaign> fetchAllAdCampaigns() {
		return adCampaigns;
	}

	/**
	 * Determines if two Partners are equal based on whether their partnerIds
	 * are equal
	 * 
	 * @param Partner
	 *            to compare to
	 * @return true if they are equal false if not
	 */
	public boolean equals(Partner p) {
		if (p == null)
			return false;
		return p.getPartnerId().equals(this.getPartnerId());
	}

}
