package DBCE.HTML.Parser;

import java.util.HashMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebClassify {

	public WebClassify() {

	}

	public WebClassify(String input_path, String[] fList) {

		for (int i = 0; i < fList.length; i++) {
			PageInfo pi = new PageInfo();
			Document doc = pi.setHTML(input_path + fList[i]);
			System.out.println(fList[i]);
			getCScore(doc);
		}
	}

	public void getCScore(Element e) {
		HashMap<String, Integer> tagMap = new HashMap<>();

		for (Element e1 : e.getAllElements()) {
			if (tagMap.containsKey(e1.tagName())) {
				tagMap.put(e1.tagName(), tagMap.get(e1.tagName()) + 1);
			} else {
				tagMap.put(e1.tagName(), 1);
			}
		}
		//System.out.println(tagMap.get("a"));
		System.out.println(tagMap);
		
		
	}

}
