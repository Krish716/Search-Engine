package WordSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class SearchEngine {
	
	//Intialising the Regex pattern to remove the special characters 

	private final String splitstrings = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[’]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
	
	//Created a text file called "avoidwords" which contains some of the common words
	
	private final String avoidwordsFile = "avoidwords.txt";
	
	private Trie<ArrayList<Integer>> trie;
	
	// An array is used to store all the Urls called webPagesArray
	
	private String [] webPagesArray;
	
	public SearchEngine(String websiteName) {
		
		// Mapping the words to the urls in (w,L) format using Trie Data Structure
		
		this.trie = new Trie<ArrayList<Integer>>();
		
		// A Hash set is initialized to store the text information in the set interface    
		 
		HashSet<String> avoid_words = savePages(avoidwordsFile);
		
		HashSet<String> temp = savePages(websiteName);
		
		//webPageArrays is assigned the value of the HashSet which is converted to string type
		
		 this.webPagesArray = temp.toArray(new String[0]);
		
		temp = null;
		String txt;
		String word;
		String[] words;
		
		//Iterator is used to get the URL one by one
		
		Iterator<String> iterator = null;
		
		for (int index = 0; index < this.webPagesArray.length; ++index) {
			try {
				txt = webCrawl(this.webPagesArray[index]);
				txt = txt.toLowerCase();
				words = txt.split(splitstrings);
				
				// the pre-processing is done and the words are then converted to list and assigned to the hashset
				 
				temp = new HashSet<String>(Arrays.asList(words));
				temp.removeAll(avoid_words); 
				
				iterator = temp.iterator();
				while(iterator.hasNext()) {
					word = (String) iterator.next();
					//search for the valid input in the Trie if exists or else insert the given word
					ArrayList<Integer> ar = this.trie.search_word(word);
					if (ar == null) {
						this.trie.insert(word, new ArrayList<Integer>(Arrays.asList(index)));
					} else {
						ar.add(index);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		System.out.println("============================================================\nThe total no of values in Trie are " + this.trie.size);
	}
	
	
	 //to parse and read through a file hashset is used
	
	//savepages method is used to read and store the website content 
	
	private HashSet<String> savePages(String filename) {
		HashSet<String> hash = new HashSet<String>();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				hash.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("********no such file exists!!***********");
			
			
		}
		catch (IOException e){
			System.out.println("OOPS!!");
		}
		return hash;
	}
	
	private String webCrawl(String url) throws Exception {	
		Document doc = Jsoup.connect(url).get();
		//System.out.println(doc);
		String txt = doc.body().text();
		//System.out.println(text);
		return txt;
	}
	//the index is passed 
	//searching and the ranking is done  
	public String[] search (String[] indexTerm) {
	
		int[] leng = new int[this.webPagesArray.length];
		ArrayList<Integer> tmp = null;
		for (int i = 0; i < indexTerm.length; ++i) {
			tmp = this.trie.search_word(indexTerm[i].toLowerCase());
			if (tmp != null) {
				for (int k = 0; k < tmp.size(); k++) {
					leng[tmp.get(k)]++;
				}
			} else {
				System.out.println("The word <" + indexTerm[i] + "> is not in any file!" );
				
				return null;
			}
		}
		/*answers stores the indexes of the webPages*/ 
		ArrayList<String> webPages = new ArrayList<String>();
		for (int p = 0; p < leng.length; ++p) {
			if (leng[p] == indexTerm.length) {
				webPages.add(this.webPagesArray[p]);
			}
		}
		return webPages.toArray(new String[0]);
	}

}