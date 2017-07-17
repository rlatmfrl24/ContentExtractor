package DBCE.HTML.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.nodes.Document;

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
		res_doc.select("script").remove();
		res_doc.select("comment").remove();
		res_doc.select("style").remove();
		res_doc.select("iframe").remove();
		res_doc.select("footer").remove();
		
		return res_doc;
	}	
}
