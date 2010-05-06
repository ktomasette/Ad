package org.tomasette.ad;

/**
 * 
 * Represents an Ad.
 * 
 * @author ktomasette
 * 
 */
public class Ad {

	String adContent = new String();

	/**
	 * Constructor
	 * 
	 * @param adContent
	 */
	public Ad(String adContent) {
		this.adContent = adContent;
	}

	/**
	 * Ad Content getter
	 * 
	 * @return
	 */
	public String getAdContent() {
		return this.adContent;
	}

	/**
	 * AdContent setter
	 * 
	 * @param adContent
	 */
	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}

}
