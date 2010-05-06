package org.tomasette.ad;

import java.util.Collection;
import java.util.Set;

/**
 * This is an interface representing the contract for interacting with
 * Campaigns.
 * 
 * @author ktomasette
 * @see InMemoryAdCampaignManager
 * 
 */
public interface IAdCampaignManager {

	public final static String DUPLICATE_PARTNER_CREATE_FAIL = "Create Partner failed: Partner already exists.";
	public final static String PARTNER_DOES_NOT_EXIST_FETCH_FAIL = "Fetch active campaign failed: Partner does not exist.";
	public final static String PARTNER_DOES_NOT_EXIST_ADD_CAMP_FAIL = "Add Campaign failed: Partner does not exist.";
	public final static String NULL_PARTNER_ID_FAIL = "Partner Id must not be null";
	public final static String NULL_AD_CAMPAIGN_FAIL = "AdCampaign must not be null.";

	/**
	 * Creates a new partner and associates it to the manager.
	 * 
	 * @param partner
	 *            to be created
	 * @throws Exception
	 *             representing a failure to create a partner
	 */
	public void createPartner(String partner) throws Exception;

	/**
	 * Detectes if a partner exists in the manager
	 * 
	 * @param partnerId
	 *            to detect
	 * @return true if manager has this partner, false otherwise
	 */
	public boolean hasPartner(String partnerId);

	/**
	 * For a given partner will return the active campaign if there is one.
	 * Returns null if there are no active campaigns
	 * 
	 * @param partnerId
	 *            to check for an active campaign
	 * @return the active AdCampaign or null if there is none.
	 * @throws Exception
	 *             representing a failure to fetch an active AdCampaign
	 */
	public AdCampaign fetchRandomActiveAdCampaign(String partnerId)
			throws Exception;

	/**
	 * Inserts the given AdCampaign to the given partner.
	 * 
	 * @param partnerId
	 *            to add the campaign to
	 * @param adCampaign
	 *            to add to add to the partner
	 * @throws Exception
	 *             representing a failure to insert the adCampaign
	 */
	public void insertAdCampaign(String partnerId, AdCampaign adCampaign)
			throws Exception;

	/**
	 * Fetches all AdCampaigns for a give partner.
	 * 
	 * @param partnerId
	 * @return a Set of AdCampaigns
	 * @throws Exception
	 *             representing a failure to fetch a partner's add campaigns
	 */
	public Set<AdCampaign> fetchAllAdCampaigns(String partnerId)
			throws Exception;

	/**
	 * Fetches all Partners associated to the manager
	 * 
	 * @return Collection of Partners
	 */
	public Collection<Partner> fetchAllPartners();

}
