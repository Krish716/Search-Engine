package WordSearch;

import java.io.IOException;
import java.util.Scanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//Main class for search operation.
public class InputTest {	
	public static void main(String[] args) throws IOException {
		
		// retrieving urls from websites.txt for user input word to pass through search engine.
		 
		System.out.println("Trie is creating for flipkart.com");

		SearchEngine searchEngine = new SearchEngine("websites.txt");
		System.out.println("===================================================================");
		System.out.println("Search in flipkart.com");

		//Enter a word you want to search
		String input = new Scanner(System.in).next();

		try {
			while (!input.equals("esc") && !input.equals(null)) {
				String[] indexOfArray = input.split("[[,]*|[ ]*]+");
				String[] webpagesList = searchEngine.search(indexOfArray);
				try {
					if (webpagesList == null) 
						System.out.println("Enter a keyword:");

					Map<String, Integer> unsortedURLsLinks = null;
					unsortedURLsLinks = new HashMap<>();
					
					//inserting links to unsortedURLsLinks which contains the input word
					for (String url : webpagesList) {
						unsortedURLsLinks.put(url, WordCount.getWordCount(url, input));
					}
					
					// Displaying the links with account of priority
					LinkedHashMap<String, Integer> prioritySortedMap = 
							new LinkedHashMap<>();
					unsortedURLsLinks.entrySet().stream().sorted
			        (Map.Entry.comparingByValue(Comparator.reverseOrder()))
			                .forEachOrdered(x -> prioritySortedMap.put
			                		(x.getKey(), x.getValue()));
			        System.out.println("Priority: \t Search Result:");
			        for (Map.Entry<String, Integer> entry : prioritySortedMap.entrySet())
			            System.out.println(entry.getValue()+"\t \t"+entry.getKey());
				}catch (NullPointerException e) {
					System.out.println("sorry");
				}

				System.out.println("\nSearch again (search multiple words separated "
						+ "by comma");
				System.out.println(" or enter\"esc\" to end:");
				input = new Scanner(System.in).next();
			}
		} catch (NullPointerException e) {
			System.out.println("No value is passed");
		}
	}
}