package io.github.oliviercailloux.y2018.j_voting;

import java.io.File;
import java.util.*;

/**
 * 
 * This class maps the voters to their strict preference (StrictPreference).
 *
 */
public class StrictProfile {
	private Map<Voter,StrictPreference> association;
	
	public StrictProfile(){
		association=new HashMap<>();
	}
	
	/**
	 * Adds a StrictPreference to a voter. If the voter is already in the map, it replaces the StrictPreference. If the voter isn't in the map, it adds a new voter and associates the StrictPreference.
	 * @param voter
	 * @param preference
	 */
	public void addProfile(Voter voter,StrictPreference preference) {
		Objects.requireNonNull(voter);
		Objects.requireNonNull(preference);
		association.put(voter,preference);
	}
	
	/**
	 * 
	 * @param voter
	 * @return the strict preference of the voter. If the voter is not in the map, it throws an exception.
	 */
	public StrictPreference getPreference(Voter voter) {
		if(!association.containsKey(voter)) {
			throw new NoSuchElementException("This voter is not in the map?");
		}
		return association.get(voter);
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
	 * 
	 * @return all the unique StrictPreference in the profile.
	 */
	public Iterable<StrictPreference> getUniquePreferences(){
		Iterable<StrictPreference> unique=new LinkedHashSet<StrictPreference>();
		Iterator<StrictPreference> i1=association.values().iterator();
		boolean alreadyInList=false;
		while(i1.hasNext()) {
			StrictPreference pref=i1.next();
			Iterator<StrictPreference> i2=unique.iterator();
			while(i2.hasNext()) {
				if(pref.equals(i2.next())) {
					alreadyInList=true;
					break;
				}
			}
			if(!alreadyInList) {
				((LinkedHashSet<StrictPreference>) unique).add(pref);
			}
		}
		return unique;
	}
	
	/**
	 * 
	 * @return the number of different StrictPreference in the map.
	 */
	public int getNbUniquePreferences() {
		Iterator<StrictPreference> iterator=getUniquePreferences().iterator();
		int size=0;
		while(iterator.hasNext()) {
			size++;
		}
		return size;
	}
	
	/**
	 * 
	 * @return the number of different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public int getNbAlternativesComplete() {
		if(!isComplete()) {
			throw new IllegalArgumentException("The profile is not complete.");
		}
		Iterator<StrictPreference> iterator=association.values().iterator();
		return iterator.next().size();
	}

	/**
	 * 
	 * @return all the different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public Iterable<Alternative> getAlternativesComplete(){
		if(!isComplete()) {
			throw new IllegalArgumentException("The profile is not complete.");
		}
		Iterator<StrictPreference> iterator=association.values().iterator();
		return iterator.next().getPreferences();
	}
	/**
	 * 
	 * @return whether the profile is complete : all votes in the profile are about the same alternatives
	 */
	public boolean isComplete() {
		Iterator<StrictPreference> iterator=association.values().iterator();
		StrictPreference pref=iterator.next();
		while(iterator.hasNext()) {
			if(!pref.hasSameAlternatives(iterator.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param preferences
	 * @return how many voters voted for the StrictPreference.
	 */
	public int getNbVoterByPreference(StrictPreference preferences) {
		Objects.requireNonNull(preferences);
		int nbVotes=0;
		Iterator<Voter> votersIterator=getAllVoters().iterator();
		while(votersIterator.hasNext()) {
			if(association.get(votersIterator.next()).equals(preferences)) {
				nbVotes++;
			}
		}
		return nbVotes;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @return a complete Strict Profile into a string in SOC format
	 */
	public String toSOC() {
		String soc="";
		soc+=getNbAlternativesComplete()+"\n";
		Iterator<Alternative> i1=getAlternativesComplete().iterator();
		while(i1.hasNext()) {
			soc+=i1.next().getId()+"\n";
		}
		soc+=getNbVoters()+", "+getSumVoteCount()+", "+getNbUniquePreferences()+"\n";
		Iterator<StrictPreference> i2=getUniquePreferences().iterator();
		while(i2.hasNext()) {
			StrictPreference pref=i2.next();
			soc+=getNbVoterByPreference(pref)+pref.toString()+"\n";
		}
		return soc;
	}
	
	public static StrictProfile fromSOCorSOI(File file) {
		
	}
}
