/*
 * ArchiveShowObj.java
 * VERSION 1.3
 * 
 * Copyright 2011 Andrew Pearson and Sanders DeNardi.
 * 
 * This file is part of Vibe Vault.
 * 
 * Vibe Vault is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */

package com.code.android.vibevault;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class ArchiveShowObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wholeTitle = "";
	private URL showURL = null;
	private String identifier = "";
	private String date = "";
	private double rating = 0.0;
	private String source = "";
	private String showArtist = "";
	private String showTitle = "";
	private boolean vbrShow = false;
	private boolean lbrShow = false;
	
	
	/** Create an object which represents a show returned from an archive.org search.
	 * 
	 * @param tit Show's title.
	 * @param id Show's "identifier" (unique part of its URL).
	 * @param dat Show's date.
	 * @param rat Show's rating.
	 * @param format Show's format list.
	 * @parm src Show's source.
	 */
	public ArchiveShowObj(String tit, String id, String dat, double rat, String format, String src) {
		wholeTitle = tit;
		String artistAndShowTitle[] = tit.split(" Live at ");
		if(artistAndShowTitle.length < 2){
			artistAndShowTitle = tit.split(" Live @ ");
		}
		if(artistAndShowTitle.length < 2){
			artistAndShowTitle = tit.split(" Live ");
		}
		showArtist = artistAndShowTitle[0].replaceAll(" - ", "").replaceAll("-","");
		if(artistAndShowTitle.length >= 2){
			showTitle = artistAndShowTitle[1];
		}
		identifier = id;
		date = dat;
		rating = rat;
		source = src;
		this.parseFormatList(format);
		try{
			showURL = new URL("http://www.archive.org/details/" + identifier);
		} catch(MalformedURLException e){
			// url is null in this case!
		}
	}
	
	public String getShowSource(){
		return source;
	}
	
	public String getShowArtist(){
		return showArtist;
	}
	
	public String getShowTitle(){
		return showTitle;
	}
	
	// Constructor called from DB version > 5
	public ArchiveShowObj(String ident, String title, String artist, String src, String hasVBR, String hasLBR){
		wholeTitle = artist + " Live at " + title;
		identifier = ident;
		showTitle = title;
		showArtist = artist;
		source = src;
		vbrShow = Boolean.valueOf(hasVBR);
		lbrShow = Boolean.valueOf(hasLBR);
		try{
			showURL = new URL("http://www.archive.org/details/" + identifier);
		} catch(MalformedURLException e){
			// url is null in this case!
		}
	}
	
	// Constructor called from DB version <= 5
	public ArchiveShowObj(String id, String tit, String hasVBR, String hasLBR){
		wholeTitle = tit;
		String artistAndShowTitle[] = tit.split(" Live at ");
		if(artistAndShowTitle.length < 2){
			artistAndShowTitle = tit.split(" Live @ ");
		}
		if(artistAndShowTitle.length < 2){
			artistAndShowTitle = tit.split(" Live ");
		}
		showArtist = artistAndShowTitle[0].replaceAll(" - ", "").replaceAll("-","");
		if(artistAndShowTitle.length >= 2){
			showTitle = artistAndShowTitle[1];
		}
		identifier = id;
		if(hasVBR.equals("1")){
			vbrShow = true;
		}
		if(hasLBR.equals("1")){
			lbrShow = true;
		}
		try{
			showURL = new URL("http://www.archive.org/details/" + identifier);
		} catch(MalformedURLException e){
			// url is null in this case!
		}
		
	}
	
	private void parseFormatList(String formatList){
		if(formatList.contains("64Kbps MP3")){
			lbrShow = true;
		}
		if(formatList.contains("64Kbps M3U")){
			lbrShow = true;
		}
		if(formatList.contains("VBR MP3")){
			vbrShow = true;
		}
		if(formatList.contains("VBR M3U")){
			vbrShow = true;
		}
	}
	
	public boolean hasVBR(){
		return vbrShow;
	}
	
	public boolean hasLBR(){
		return lbrShow;
	}
	
	public double getRating(){
		return rating;
	}
	
	public String getSource(){
		return source;
	}
	
	public String getDate(){
		return date;
	}

	@Override
	public String toString() {
		return String.format(wholeTitle);
	}

	public String getArtistAndTitle(){
		return wholeTitle;
	}
	
	public String getIdentifier(){
		return identifier;
	}
	
	public String getLinkPrefix(){
		return "http://www.archive.org/download/" + identifier + "/" + identifier;
	}
	
	// CALLER MUST CHECK FOR NULL RETURN VALUE!
	// \/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
	public URL getShowURL() {
		return showURL;
	}

}