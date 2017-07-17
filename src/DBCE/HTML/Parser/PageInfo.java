package DBCE.HTML.Parser;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageInfo {

	private String title;
	private String content;
	private String url;
	private int content_length;
	private int content_words;
	private String content_keyword="";
	private String charset="";
	private Document doc;
	private Date date;
	private HashMap<String, Integer> word_map = new HashMap<String, Integer>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getContent_length() {
		return content_length;
	}
	public void setContent_length(int content_length) {
		this.content_length = content_length;
	}
	public int getContent_words() {
		return content_words;
	}
	public void setContent_words(int content_words) {
		this.content_words = content_words;
	}
	public String getContent_keyword() {
		return content_keyword;
	}
	public void setContent_keyword(String content_keyword) {
		this.content_keyword = content_keyword;
	}
	
	public void AnalyzeContent(){
		int change_value;
		setContent_length(content.length());
		
		String spc[] = content.split(" ");
		for(String sp : spc){
			if(word_map.containsKey(sp)){
				change_value = word_map.get(sp).intValue()+1;
				word_map.remove(sp);
				word_map.put(sp, change_value);
			}else {
				word_map.put(sp, 1);
			}
		}
		setContent_words(word_map.size());
		
		for(Entry<String, Integer> e : word_map.entrySet()){
			if(e.getKey().length() > 6 && e.getValue() > 4){
				setContent_keyword(getContent_keyword() +" #"+ e.getKey());
			}
		}
	}
	public Document getHTML() {
		return doc;
	}
	public Document setHTML(String path) {
		File f = new File(path);
		Document doc=null;
		try {
			doc = Jsoup.parse(f, "UTF-8");
			charset = doc.select("meta").attr("charset");
			if (charset.isEmpty() || charset.contains("/")) {
				charset = doc.select("meta[http-equiv=\"Content-Type\"]").attr("content");
				charset = charset.substring(charset.indexOf("=") + 1, charset.length());
			}
			if (charset.isEmpty() || charset.contains("/")) {
				System.out.println("Charset::Default(utf8)");
				charset = "utf8";
			}
			charset = charset.trim();
			doc = Jsoup.parse(f, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.doc = doc;
		return doc;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
