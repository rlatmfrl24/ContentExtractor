package DBCE.HTML.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class DateUtil {

	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
		{
			put("^\\d{8}$", "yyyyMMdd");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
			put("^\\d{12}$", "yyyyMMddHHmm");
			put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
			put("^\\d{14}$", "yyyyMMddHHmmss");
			put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
		}
	};
	private Date pageDate;
	
	public DateUtil(){
	}
	
	public DateUtil(PageInfo pi){
		pageDate = parse(FindDateFromURL(pi.getUrl()));
		if(pageDate==null) pageDate=parse(FindDateFromMeta(pi.getHTML()));
		if(pageDate==null) pageDate=parse(FindDateFromHTML(pi.getHTML()));
	}
	
	public static String determineDateFormat(String dateString) {
		for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
			if (dateString.toLowerCase().matches(regexp)) {
				return DATE_FORMAT_REGEXPS.get(regexp);
			}
		}
		return null; // Unknown format.
	}

	public static Date parse(String dateString, String dateFormat)  {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		//simpleDateFormat.setLenient(false); // Don't automatically convert
											// invalid date.
		try {
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Date parse(String dateString) {
		String dateFormat = determineDateFormat(dateString);
		if (dateFormat == null) {
			return null;
		}else{
			return parse(dateString, dateFormat);
		}
	}
	
	public String FindDateFromURL(String url) {
		String dateString="";
		Matcher mat = Pattern.compile("([\\./\\-_]{0,1}(19|20)\\d{2})[\\./\\-_]{0,1}(([0-3]{0,1}[0-9][\\./\\-_])|(\\w{3,5}[\\./\\-_]))([0-3]{0,1}[0-9][\\./\\-]{0,1})?").matcher(url.toLowerCase());
		if (mat.find()) {
			dateString = mat.group(0);
			dateString = dateString.replaceAll("[\\/\\_\\.\\-\\\\]", "");
		}
		return dateString.trim();
	}

	public String FindDateFromMeta(Document doc) {
		String dateString="";
		
		if(!doc.select("meta[name=\"pubdate\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"pubdate\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"publishdate\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"publishdate\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"timestamp\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"timestamp\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"DC.date.issued\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"DC.date.issued\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[property=\"article:published_time\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[property=\"article:published_time\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"Date\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"Date\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[property=\"bt:pubdate\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[property=\"bt:pubdate\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"sailthru.date\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"sailthru.date\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"article.published\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"article.published\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"published-date\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"published-date\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"article.created\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"article.created\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"article_date_original\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"article_date_original\"]").first().attr("content");
			return dateString.trim();
		}

		if(!doc.select("meta[name=\"cXenseParse:recs:publishtime\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"cXenseParse:recs:publishtime\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[name=\"date_published\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[name=\"date_published\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[itemprop=\"datePublished\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[itemprop=\"datePublished\"]").first().attr("content");
			return dateString.trim();
		}
		
		if(!doc.select("meta[property=\"og:image\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[property=\"og:image\"]").first().attr("content");
			dateString = FindDateFromURL(dateString);
			if(!dateString.isEmpty()) return dateString.trim();
		}
		
		if(!doc.select("meta[itemprop=\"image\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[itemprop=\"image\"]").first().attr("content");
			dateString = FindDateFromURL(dateString);
			if(!dateString.isEmpty()) return dateString.trim();
		}
		
		if(!doc.select("meta[http-equiv=\"date\"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("meta[http-equiv=\"date\"]").first().attr("content");
			return dateString.trim();
		}
		
		return dateString.trim();
	}

	public String FindDateFromHTML(Document doc) {
		String dateString="";
		
		if(!doc.select("time").attr("datetime").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("time").attr("datetime");
			return dateString.trim();
		}
		
		if(!doc.select("span[itemprop=\"dataPublished\"]").isEmpty() && dateString.isEmpty()){
			dateString=doc.select("span[itemprop=\"dataPublished\"]").attr("content");
			if(dateString.isEmpty()){
				dateString=doc.select("span[itemprop=\"dataPublished\"]").first().text();
			}
			if(!dateString.isEmpty()) return dateString.trim();
		}
		
	String checkTag[] = {"pubdate", "timestamp", "article_date", "articledate", "date", "posted"};
	for(String str : checkTag){
		if(!doc.select("span[class="+str+"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("span[class="+str+"]").first().text();
			return dateString.trim();
		}
	}
	for(String str : checkTag){
		if(!doc.select("p[class="+str+"]").isEmpty() && dateString.isEmpty()){
			System.out.println(doc.select("p[class="+str+"]").first().text());
			dateString = doc.select("p[class="+str+"]").first().text();
			return dateString.trim();
		}
	}
	for(String str : checkTag){
		if(!doc.select("div[class="+str+"]").isEmpty() && dateString.isEmpty()){
			dateString = doc.select("div[class="+str+"]").first().text();
			return dateString.trim();
		}
	}
		
	return dateString.trim();
	}
	
	public Date getPageDate(){
		return pageDate;
	}
}
