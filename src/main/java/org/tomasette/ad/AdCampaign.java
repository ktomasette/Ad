package org.tomasette.ad;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Represents an Ad Campaign. Links attrubutes about a campaign {duration,
 * creation date} to the actual Ad
 * 
 * @author ktomasette
 * 
 */
public class AdCampaign {

	private final static Logger logger = Logger.getLogger(AdCampaign.class);

	private final long duration;
	private final Date creationDate;
	private final Ad ad;

	/**
	 * Constructor
	 * 
	 * @param inDuration
	 * @param inAd
	 */
	public AdCampaign(long inDuration, Ad inAd) {
		this.duration = inDuration;
		this.creationDate = new Date();
		this.ad = inAd;
	}

	/**
	 * Duration getter
	 * 
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Ad getter
	 * 
	 * @return the Ad
	 */
	public Ad getAd() {
		return ad;
	}

	/**
	 * Creation Date getter
	 * 
	 * @return the creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Calculates whether the AdCampaign is active. if creationDate + Duration
	 * is in the future then the AdCampaign is active
	 * 
	 * @return true if active false if not
	 */
	public boolean isActive() {

		long now = System.currentTimeMillis();
		if (this.creationDate.getTime() + duration >= now) {
			logger.debug("Ad Campaign is active.  Will be active for "
					+ (this.creationDate.getTime() + duration - now) + "ms.");
			return true;
		} else {
			return false;
		}
	}
}
