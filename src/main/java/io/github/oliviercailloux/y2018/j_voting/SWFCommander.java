package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SWFCommander{
    private static SocialWelfareFunction result ; 
	static Logger log = LoggerFactory.getLogger(SWFCommander.class.getName());
    
    /**
     * Asks the user to enter a StrictPreference 
     * @return the entered StrictPreference
     */
    public static StrictPreference askPreference(){
    	log.debug("askPreference\n");
        System.out.println("Enter a StrictPreference complete");
        Scanner scan=new Scanner(System.in);
        String vote = scan.nextLine();
        scan.close();
        String[] preference = vote.split(",");
        List<Alternative> list = new ArrayList<Alternative>();
        for(int i=0;i<preference.length;i++){
            list.add(new Alternative(Integer.parseInt(preference[i].trim())));
            log.debug("added alternative : {}\n",Integer.parseInt(preference[i].trim()));
        }
        log.debug("list of alternatives : {}\n",list);
        return new StrictPreference(list);
    }   
    
    public static void main(String[] args){
    	log.debug("main");
        StrictProfile prof = new StrictProfile();
        boolean keepGoing = true;
        int voterId = 1;
        //gerer les alternatives ? complete ?
        while(keepGoing){
        	log.debug("new voter id  : {}\n",voterId);
            Voter v = new Voter(voterId);
            prof.addProfile(v,askPreference());
            System.out.println(result.getSocietyStrictPreference(prof));
        }
    }
}