package de.unihd.dbs.uima.annotator.heideltime.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * The Toolbox class contains methods with functionality that you would also
 * find outside the context of HeidelTime's specific skillset; i.e. they do
 * not require the CAS context, but are 'useful code snippets'. 
 *
 */
public class Toolbox {
	/**
	 * Find all the matches of a pattern in a charSequence and return the
	 * results as list.
	 * 
	 * @param pattern Pattern to be matched
	 * @param s String to be matched against
	 * @return Iterable List of MatchResults
	 */
	public static Iterable<MatchResult> findMatches(Pattern pattern, CharSequence s) {
		List<MatchResult> results = new ArrayList<MatchResult>();

		for (Matcher m = pattern.matcher(s); m.find();)
			results.add(m.toMatchResult());

		return results;
	}

	/**
	 * Sorts a given HashMap using a custom function
	 * @param m Map of items to sort
	 * @return sorted List of items
	 */
    public static List<Pattern> sortByValue(final HashMap<Pattern,String> m) {
        List<Pattern> keys = new ArrayList<Pattern>();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v1 instanceof Comparable) {
                    return ((Comparable) v1).compareTo(v2);
                } else {
                    return 0;
                }
            }
        });
        return keys;
    }
	
	/**
	 * Durations of a finer granularity are mapped to a coarser one if possible, e.g., "PT24H" -> "P1D".
	 * One may add several further corrections.
	 * @param value 
     * @return
     */
	public static String correctDurationValue(String value) {
		if (value.matches("PT[0-9]+H")){
			for (MatchResult mr : findMatches(Pattern.compile("PT([0-9]+)H"), value)){
				int hours = Integer.parseInt(mr.group(1));
				if ((hours % 24) == 0){
					int days = hours / 24;
					value = "P"+days+"D";
				}
			}
		} else if (value.matches("PT[0-9]+M")){
			for (MatchResult mr : findMatches(Pattern.compile("PT([0-9]+)M"), value)){
				int minutes = Integer.parseInt(mr.group(1));
				if ((minutes % 60) == 0){
					int hours = minutes / 60;
					value = "PT"+hours+"H";
				}
			}
		} else if (value.matches("P[0-9]+M")){
			for (MatchResult mr : findMatches(Pattern.compile("P([0-9]+)M"), value)){
				int months = Integer.parseInt(mr.group(1));
				if ((months % 12) == 0){
					int years = months / 12;
					value = "P"+years+"Y";
				}
			}
		}
		return value;
	}
}
