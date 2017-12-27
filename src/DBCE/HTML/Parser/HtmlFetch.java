package DBCE.HTML.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlFetch {
	public static String getHttpHTML(String urlToRead, String charset) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			while ((line = rd.readLine()) != null) {
				result += line + "\n";
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
		
	public static Document killNoise(Document doc){
		Document res_doc = doc;
		//res_doc.select("li").remove();
		String[] noise_tags = {"button", "script", "comment", "style", "iframe", "footer", "label", "li", "em" ,"figcaption", "figure", "img", "option"};
		for(String s : noise_tags){
			res_doc.select(s).remove();
		}
		
		for(Element e : res_doc.getAllElements()){
			if(!e.tagName().equals("html") && !e.tagName().equals("body")){
				if(e.attr("class").toLowerCase().contains("icon")
				|| e.attr("class").toLowerCase().contains("footer")
				|| e.attr("class").toLowerCase().contains("header")
				|| e.attr("class").toLowerCase().contains("btn")
				|| e.attr("class").toLowerCase().contains("button")
				|| e.attr("class").toLowerCase().contains("img")
				|| e.attr("class").toLowerCase().contains("date")
				|| e.attr("class").toLowerCase().contains("copyright")
				|| e.attr("class").toLowerCase().contains("caption")
				|| e.attr("class").toLowerCase().contains("player")
				|| e.attr("class").toLowerCase().contains("photo")
				|| e.attr("class").toLowerCase().contains("option")
				) {
					e.remove();
				}
			}
		}		
		
		return res_doc;
	}	

}
