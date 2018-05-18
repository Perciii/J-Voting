package io.github.oliviercailloux.y2018.j_voting;

import java.io.*;
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
		LOGGER.debug("addProfile");
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
		LOGGER.debug("contains:");
		LOGGER.debug("parameter : voter = {}", voter.getId());
		Preconditions.checkNotNull(voter);
		Iterable<Voter> voters = getAllVoters();
		Iterator<Voter> iterator = voters.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().equals(voter)) {
				LOGGER.debug("return true");
				return true;
			}
		}
		LOGGER.debug("return false");
		return false;
	}
	
	/**
	 * Writes a StrictProfile into a SOC file.
	 * @param profile a StrictProfile, fileName a String : name of the file that will be written in resources folder
	 * @throws IOException
	 */
	/**
	 * 
	 * @param output
	 * @throws IOException
	 * writes the strict profile in the given output in the SOC format.
	 */
	public void writeToSOC(OutputStream output) throws IOException {
		LOGGER.debug("writeToSOC:");
		Preconditions.checkNotNull(output);
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(output))){
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
			writer.append(soc);
			writer.close();
		}
	}
	
	/**
	 * @param voter
	 * @return the strict preference of the voter. If the voter is not in the map, it throws an exception.
	 */
	public StrictPreference getPreference(Voter voter) {
		LOGGER.debug("getPreference");
		Preconditions.checkNotNull(voter);
		LOGGER.debug("parameter : voterId = {}", voter.getId());
		
		Set<Map.Entry<Voter, StrictPreference>> mapping = association.entrySet();
		Iterator<Map.Entry<Voter, StrictPreference>> iterator = mapping.iterator();
		while(iterator.hasNext()) {
			Map.Entry<Voter, StrictPreference> vote = iterator.next(); 
			if(vote.getKey().equals(voter)) {
				LOGGER.debug("return {}", vote.getValue());
				return vote.getValue();
			}
		}
		throw new NoSuchElementException("Voter " + voter + "is not in the map !");
	}
	
	/**
	 * @return all the voters in the map.
	 */
	public Iterable<Voter> getAllVoters(){
		
		Set<Voter> keys = association.keySet();
		Set<Voter> finalKeys = new HashSet<>();
		boolean first = true;
		int size = keys.size();
		Voter minV = new Voter(0);

		while(finalKeys.size() != size){
			
			for (Voter v : keys){
				if (first){
					minV = v;
					first = false;
				}
				else{
					if (v.getId()<minV.getId()){
						minV = v ;
					}
				}
			}
			
			finalKeys.add(minV);
			keys.remove(minV);
			first = true;
		}
		
		return finalKeys;
	}
	
	/**
	 * @return the number of voters in the profile
	 */
	public int getNbVoters() {
		return association.size();
	}
	
	/**
	 * @return the sum of the vote count 
	 */
	public int getSumVoteCount() {
		return association.size();
	}
	
	/**
	 * @return all the unique StrictPreference in the profile.
	 */
	public Set<StrictPreference> getUniquePreferences(){
		LOGGER.debug("getUniquePreferences");
		Set<StrictPreference> unique = new HashSet<>();
		for(StrictPreference pref : association.values()) {
			LOGGER.debug("next preference : {}", pref);
			unique.add(pref);
		}
		return unique;
	}
	
	/**
	 * @return the number of different StrictPreference in the map.
	 */
	public int getNbUniquePreferences() {
		LOGGER.debug("getnbUniquePreference");
		return getUniquePreferences().size();
	}
	
	/**
	 * @return the number of different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public int getNbAlternativesComplete() {
		LOGGER.debug("getNbAlternativesComplete");
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
		LOGGER.debug("getAlternativesComplete");
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
		LOGGER.debug("isComplete");
		Iterator<StrictPreference> iterator = association.values().iterator();
		StrictPreference pref = iterator.next();
		LOGGER.debug("first preferences :{}", pref);
		
		while(iterator.hasNext()) {
			StrictPreference pref2=iterator.next();
			LOGGER.debug("next preferences : {}", pref2);
			if(!pref.hasSameAlternatives(pref2)) {
				LOGGER.debug("Not the same alternatives");
				return false;
			}
		}
		LOGGER.debug("Profile is complete.");
		return true;
	}

	/**
	 * @param preferences
	 * @return how many voters voted for the StrictPreference.
	 */
	public int getNbVoterByPreference(StrictPreference preferences) {
		LOGGER.debug("getNbVoterByPreference");
		Preconditions.checkNotNull(preferences);
		LOGGER.debug("parameter preferences : {}", preferences);
		int nbVotes = 0;
		Iterator<Voter> votersIterator = getAllVoters().iterator();
		while(votersIterator.hasNext()) {
			Voter v = votersIterator.next();
			LOGGER.debug("Voter with id = {} voted for : {}", v.getId(), association.get(v));
			if(association.get(v).equals(preferences)) {
				nbVotes++;
			}
		}
		LOGGER.debug("number of votes for {} is {}", preferences, nbVotes);
		return nbVotes;
	}
	
	
	/**
	 * @return a complete Strict Profile into a string in SOC format
	 */
	public String toSOC() {
		LOGGER.debug("toSOC");
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
