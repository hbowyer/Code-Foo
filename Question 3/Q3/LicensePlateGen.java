/**
 * Generates output(composition of license plate and excess plates) specified by input(population).
 * Also has helper functions for handling data generated by this class.
 * An instance of this class is not necessary.
 * @author Harry Bowyer
 *
 */
public final class LicensePlateGen {
	private LicensePlateGen() {}
	
	/**
	 * Generates an alpha-numeric licence plate for a given population with the fewest excess plates.
	 * @param sPop Population
	 * @return Returns a string in the format: "00,11eEE" where 00 is the number of letters, 
	 * 	11 is the number of numbers, and EE is excess plates
	 * Null is returned if there is a problem with the string.
	 */
	static public String calc(String sPop) {
		/* Two things to note:
		 * 	1) Thinking of numbers and letters as containers of 10 and 26 objects respectively, at the 
		 * 		number 130, 5 letters and 13 numbers could equally go in with no excess. Therefore, for 
		 * 		each and every EXTRA multiple of 130 (260+), there is easily 5 letters and 130 can be 
		 * 		subtracted from further calculations.
		 *	2) The last combination to try, if using alpha to numeric, would be all numerics. 
		 *		If all of the numbers is over the population given, this algorithm is done.
		 *		Ex. Population of 25: 3 numbers would yield 30, and there is no reason to do 4 of anything.
		 */
		long letters, numbers = 0L, pop;
		
		if(sPop.length() > 18)
			return null;
		try {
			pop = Long.parseLong(sPop.trim());
		} catch (Exception NumberFormatException) {
			return null;
		}
		
		letters = ((pop/130) - 1) < 0 ? 0 : ((pop/130) - 1);
		pop -= 130 * letters;
		letters *= 5;	//5 letters per 130
		
		long bestLetterCount=0, bestNumberCount=0, bestExcessCount=999, 
				currentLetterCount=1, currentNumberCount=0,  currentExcessCount=0; 
		boolean bDone = false;
		while(!bDone) {
			currentExcessCount = (currentLetterCount * 26 + currentNumberCount * 10) - pop;
			if(currentExcessCount < bestExcessCount && currentExcessCount >= 0) { //note: letters are saved first, so curE<bestE instead of <=
				bestExcessCount = currentExcessCount;
				bestNumberCount = currentNumberCount;
				bestLetterCount = currentLetterCount;
			}
			if(currentLetterCount == 0 && currentExcessCount > 0)
				bDone = true;
			if(currentLetterCount > 0) {
				currentLetterCount--;
				currentNumberCount++;
			}
			else {
				currentLetterCount = currentNumberCount + 1;
				currentNumberCount = 0;
			}
		}
		letters += bestLetterCount;
		numbers += bestNumberCount;
		
		return String.valueOf(letters) + "," + String.valueOf(numbers) + "e" + String.valueOf(bestExcessCount);
	}
	
	/**
	 * Decodes the string, returning the number of letters for the plates.
	 * @param inString The encoded string "AA,00eEE"
	 * @return Returns the number of letters for the plates
	 */
	static public String letters(String inString) {
		return inString.split(",")[0];
	}
	
	/**
	 * Decodes the string, returning the number of numbers for the plates.
	 * @param inString The encoded string "AA,00eEE"
	 * @return Returns the number of numbers for the plates
	 */
	static public String numbers(String inString) {
		inString = inString.split("e")[0];
		return inString.split(",")[1];
	}
	
	/**
	 * Decodes the string, returning the number of excess plates.
	 * @param inString The encoded string "AA,00eEE"
	 * @return Returns the number of excess plates
	 */
	static public String excess(String inString) {
		return inString.split("e")[1];
	}
}
