package org.tomasette.ad;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of IAdCampaignManager that stores all data in memory.
 * Methods are synchronized to protect against concurrency issues.
 * 
 * 
 * 
 * @author ktomasette
 * 
 */
public class InMemoryAdCampaignManager implements IAdCampaignManager {

	private final Map<String, Partner> partners = new HashMap<String, Partner>();

	@Override
	synchronized public void createPartner(String partnerId) throws Exception {
		if (partnerId == null)
			throw new NullPointerException(
					IAdCampaignManager.NULL_PARTNER_ID_FAIL);
		if (partners.containsKey(partnerId)) {
			throw new Exception(DUPLICATE_PARTNER_CREATE_FAIL);
		}
		Partner p = new Partner(partnerId);
		partners.put(partnerId, p);

	}

	@Override
	synchronized public boolean hasPartner(String partnerId) {
		return partners.containsKey(partnerId);
	}

	/**
	 * returns a partnerId if it exists. This is for testing only
	 * 
	 * @param partnerId
	 * @return Partner object
	 */
	synchronized Partner fetchPartner(String partnerId) {
		return partners.get(partnerId);
	}

	@Override
	synchronized public AdCampaign fetchRandomActiveAdCampaign(String partnerId)
			throws Exception {
		if (partners.containsKey(partnerId)) {
			return partners.get(partnerId).fetchRandomActiveAdCampaign();

		} else {
			throw new Exception(PARTNER_DOES_NOT_EXIST_FETCH_FAIL);
		}
	}

	@Override
	synchronized public void insertAdCampaign(String partnerId,
			AdCampaign adCampaign) throws Exception {

		if (partnerId == null)
			throw new NullPointerException(
					IAdCampaignManager.NULL_PARTNER_ID_FAIL);
		if (adCampaign == null)
			throw new NullPointerException(
					IAdCampaignManager.NULL_AD_CAMPAIGN_FAIL);
		if (partners.containsKey(partnerId)) {
			partners.get(partnerId).addAdCampaign(adCampaign);
		} else {
			throw new Exception(PARTNER_DOES_NOT_EXIST_ADD_CAMP_FAIL);
		}

	}

	@Override
	synchronized public Set<AdCampaign> fetchAllAdCampaigns(String partnerId)
			throws Exception {
		if (partners.containsKey(partnerId)) {
			return partners.get(partnerId).fetchAllAdCampaigns();
		} else {
			throw new Exception(PARTNER_DOES_NOT_EXIST_ADD_CAMP_FAIL);
		}

	}

	@Override
	synchronized public Collection<Partner> fetchAllPartners() {
		return partners.values();

	}

}
