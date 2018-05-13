package io.github.oliviercailloux.y2018.j_voting;

import java.io.*;
//import org.apache.commons.io.FileUtils;
import java.util.*;
import org.slf4j.*;

import com.google.common.base.Preconditions;

/**
 * 
 * This class maps the voters to their strict preference (StrictPreference).
 *
 */
public class StrictProfile {
	private static Logger LOGGER = LoggerFactory.getLogger(StrictProfile.class.getName());
	private Map<Voter, StrictPreference> association;
	public int nextVoterId = 1;// id is the id of the next voter that will be created in the profile if the profile is created from a file.
	
	public StrictProfile(){
		association = new HashMap<>();
	}
	
	/**
	 * Adds a StrictPreference to a voter. If the voter is already in the map, it replaces the StrictPreference. 
	 * If the voter isn't in the map, it adds a new voter and associates the StrictPreference.
	 * @param voter
	 * @param preference
	 */
	public void addProfile(Voter voter, StrictPreference preference) {
		LOGGER.debug("addProfile\n");
		Preconditions.checkNotNull(voter);
		Preconditions.checkNotNull(preference);
		LOGGER.debug("parameters : voterId = {}, preferences = {}", voter.getId(), preference);
		association.put(voter, preference);
	}
	
	/**
	 * @param voter
	 * @return true if the profile contains a preference for the voter given.
	 */
	public boolean contains(Voter voter) {
		LOGGER.debug("contains:\n");
		LOGGER.debug("parameter : voter = {}\n", voter.getId());
		Preconditions.checkNotNull(voter);
		Iterable<Voter> voters = getAllVoters();
		Iterator<Voter> iterator = voters.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().equals(voter)) {
				LOGGER.debug("return true\n");
				return true;
			}
		}
		LOGGER.debug("return false\n");
		return false;
	}
	
	/**
	 * Writes a StrictProfile into a SOC file.
	 * @param profile a StrictProfile, fileName a String : name of the file that will be written in resources folder
	 * @throws IOException
	 */
	public void writeToSOC(String fileName) throws IOException {
		LOGGER.debug("writeToSOC:\n");
		Preconditions.checkNotNull(fileName);
		LOGGER.debug("parameter filename : {}\n", fileName);
		try(OutputStream output = new FileOutputStream(fileName)){
			output.write(this.toSOC().getBytes());
			output.close();
		}
	}
	
	/**
	 * 
	 * @param voter
	 * @return the strict preference of the voter. If the voter is not in the map, it throws an exception.
	 */
	public StrictPreference getPreference(Voter voter) {
		LOGGER.debug("getPreference\n");
		Preconditions.checkNotNull(voter);
		LOGGER.debug("parameter : voterId = {}\n",voter.getId());
		Set<Map.Entry<Voter,StrictPreference>> mapping = association.entrySet();
		Iterator<Map.Entry<Voter,StrictPreference>> iterator = mapping.iterator();
		while(iterator.hasNext()) {
			Map.Entry<Voter, StrictPreference> vote = iterator.next(); 
			if(vote.getKey().equals(voter)) {
				LOGGER.debug("return {}\n",vote.getValue());
				return vote.getValue();
			}
		}
		throw new NoSuchElementException("Voter " + voter + "is not in the map !");
	}
	
	/**
	 * 
	 * @return all the voters in the map.
	 */
	public Iterable<Voter> getAllVoters(){
		return association.keySet();
	}
	
	/**
	 * 
	 * @return the number of voters in the profile
	 */
	public int getNbVoters() {
		return association.size();
	}
	
	/**
	 * 
	 * @return the sum of the vote count 
	 */
	public int getSumVoteCount() {
		return association.size();
	}
	
	/**
	 * @return all the unique StrictPreference in the profile.
	 */
	public Set<StrictPreference> getUniquePreferences(){
		LOGGER.debug("getUniquePreferences\n");
		Set<StrictPreference> unique = new HashSet<>();
		boolean alreadyInList = false;
		for(StrictPreference pref : association.values()) {
			LOGGER.debug("next preference : {}\n", pref);
			for(StrictPreference uniquePref : unique) {
				if(pref.equals(uniquePref)) {
					LOGGER.debug("{} already in the list\n", pref);
					alreadyInList = true;
				}
			}
			if(!alreadyInList) {
				unique.add(pref);
			}
			alreadyInList=false;
		}
		return unique;
	}
	
	/**
	 * @return the number of different StrictPreference in the map.
	 */
	public int getNbUniquePreferences() {
		LOGGER.debug("getnbUniquePreference\n");
		return getUniquePreferences().size();
	}
	
	/**
	 * @return the number of different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public int getNbAlternativesComplete() {
		LOGGER.debug("getNbAlternativesComplete\n");
		if(!isComplete()) {
			throw new IllegalArgumentException("The profile is not complete.");
		}
		Iterator<StrictPreference> iterator = association.values().iterator();
		return iterator.next().size();
	}

	/**
	 * @return all the different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public List<Alternative> getAlternativesComplete(){
		LOGGER.debug("getAlternativesComplete\n");
		if(!isComplete()) {
			throw new IllegalArgumentException("The profile is not complete.");
		}
		Iterator<StrictPreference> iterator = association.values().iterator();
		return iterator.next().getPreferences();
	}
	/**
	 * 
	 * @return whether the profile is complete : all votes in the profile are about the same alternatives
	 */
	public boolean isComplete() {
		LOGGER.debug("isComplete\n");
		Iterator<StrictPreference> iterator = association.values().iterator();
		StrictPreference pref = iterator.next();
		LOGGER.debug("first preferences :{}\n", pref);
		while(iterator.hasNext()) {
			StrictPreference pref2=iterator.next();
			LOGGER.debug("next preferences : {}\n", pref2);
			if(!pref.hasSameAlternatives(pref2)) {
				LOGGER.debug("Not the same alternatives\n");
				return false;
			}
		}
		LOGGER.debug("Profile is complete.\n");
		return true;
	}

	/**
	 * 
	 * @param preferences
	 * @return how many voters voted for the StrictPreference.
	 */
	public int getNbVoterByPreference(StrictPreference preferences) {
		LOGGER.debug("getNbVoterByPreference\n");
		Preconditions.checkNotNull(preferences);
		LOGGER.debug("parameter preferences : {}\n", preferences);
		int nbVotes = 0;
		Iterator<Voter> votersIterator = getAllVoters().iterator();
		while(votersIterator.hasNext()) {
			Voter v = votersIterator.next();
			LOGGER.debug("Voter with id = {} voted for : {}\n", v.getId(), association.get(v));
			if(association.get(v).equals(preferences)) {
				nbVotes++;
			}
		}
		LOGGER.debug("number of votes for {} is {}\n", preferences, nbVotes);
		return nbVotes;
	}
	
	
	/**
	 * 
	 * @return a complete Strict Profile into a string in SOC format
	 */
	public String toSOC() {
		LOGGER.debug("toSOC\n");
		String soc = "";
		soc += getNbAlternativesComplete() + "\n";
		for(Alternative alter : getAlternativesComplete()) {
			soc += alter.getId() + "\n";
		}
		soc += getNbVoters() + ","+getSumVoteCount() + "," + getNbUniquePreferences() + "\n";
		for(StrictPreference pref : this.getUniquePreferences()) {
			soc += getNbVoterByPreference(pref);
			for(Alternative a : pref.getPreferences()) {
				soc = soc + "," + a;
			}
			soc = soc + "\n";
		}
		return soc;
	}
}
