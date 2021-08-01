package WebCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	//create a list for URLs
	public static List<URL> crawl(URL start, int limit) {
		List<URL> urlsList = new ArrayList<URL>(limit);
		urlsList.add(start);

		//We are making a copy of urls called urlsTemp for faster extraction
      
		Set<URL> urlsTemp = new HashSet<URL>(urlsList);
		int i = 0;
		while (urlsList.size() < limit && i < urlsList.size()) {
			URL presenttUrl = urlsList.get(i);
			for (URL url : extract(presenttUrl)) {
				if (urlsTemp.add(url)) {
					urlsList.add(url);
					if (urlsList.size() == limit)
						break;
				}
           }
           i++;
       }
       return urlsList;
	}

	//main function for the crawler
	public static void main(String[] args) {
		try {
			URL flipkart = new URL("https://www.flipkart.com/");
			int limit = 1000;

			List<URL> discovered = WebCrawler.crawl(flipkart, limit);
			System.out.println("=======================================/nResults:=================================/n ");
			int i = 1;
			Iterator<URL> iterator = discovered.iterator();
			while (iterator.hasNext() && i <= 1000) {
				System.out.println(iterator.next());
				i++;
			}
		}
		catch (MalformedURLException e) {
			System.err.println("The URL to start crawling with is invalid.");
		}
	}
    
   	//Extracting and printing all the links embedded in the web page in the same order 
    
	private static LinkedHashSet<URL> extract(URL url) {
		LinkedHashSet<URL> all_links = new LinkedHashSet<URL>();
		Pattern pat = Pattern.compile("href=\"((http://|https://|www).*?)\"", 
				Pattern.DOTALL);
		Matcher mat = pat.matcher(fetch(url));

		while (mat.find()) {
			String linkStr = normalize(mat.group(1));
			try {
				URL link = new URL(linkStr);
				all_links.add(link);
			}
			catch (MalformedURLException e) {
				System.err.println("Page at " + url + " has a link to "
						+ "invalid URL : " + linkStr + ".");
			}
		}
		return all_links;
	}

	//fetching and returning the content from the url
	
	private static String fetch(URL url) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
			String input_Line;
			while ((input_Line = input.readLine()) != null)
				stringBuilder.append(input_Line);
			input.close();
		}
		catch (IOException e) {
			System.err.println("OOPS!! Error occured on the " + url);
		}
		return stringBuilder.toString();
	}
	//Transformation of URL to URL object by normalizing
	
	private static String normalize(String urlStr) {
		if (!urlStr.startsWith("http"))
			urlStr = "http://" + urlStr;
		if (urlStr.endsWith("/")) 
			urlStr = urlStr.substring(0, urlStr.length() - 1);
		if (urlStr.contains("#")) 
			urlStr = urlStr.substring(0, urlStr.indexOf("#"));
		return urlStr;
	}
}