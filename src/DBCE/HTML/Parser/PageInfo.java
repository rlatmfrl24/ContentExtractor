package DBCE.HTML.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mozilla.universalchardet.UniversalDetector;

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
	private String[] NoiseRegexSet = {
			"^[0-9]((:|\\)|\\.)|[0-9]+(:|\\)|\\.))",
			"<[^>]*>",
			"\\{[^>]*\\}",
			"^\\{\".*",
			"�",
			"<.*\b[^>]*>(.*?)</.*>"
			//"^[0-9]+.*[0-9]",
	};
	
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
		//Remove Noise
		String process="";
		for(String line : content.split("\n")){
			String curline = line;
			for(String nr : NoiseRegexSet){
				curline = curline.replaceAll(nr, "");
			}
			process += curline+"\n";
		}
		this.content = process.trim();
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
		String charset = DetectCharset(path);
		Document doc=null;
		try{
			doc = Jsoup.parse(f, charset);
		}catch(Exception e){
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
	public static String DetectCharset(String input){
		String encoding = "";
		try {
			byte[] buf = new byte[4096];
			java.io.FileInputStream fis = new java.io.FileInputStream(input);
			UniversalDetector detector = new UniversalDetector(null);
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();
			encoding = detector.getDetectedCharset();
			if (encoding != null) {
				System.out.println("Detected encoding = " + encoding);
			} else {
				System.out.println("No encoding detected.");
				encoding = "UTF-8";
			}
			detector.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encoding;
	}
}
