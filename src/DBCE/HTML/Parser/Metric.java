package DBCE.HTML.Parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Element;

public class Metric {
	
	float bodyCi;
	float bodyTi;
	float bodyLCi;
	float bodyLTi;
	private String Content="";
	ArrayList<String> LinkCharList = new ArrayList<String>();
	int ContentTagCount=0;
	int NoiseTagCount=0;

	public Metric(){
		
	}
	public Metric(Element body){
		
		bodyCi = body.text().length();
		bodyTi = body.getAllElements().size();
		bodyLCi = body.select("a").text().length();
		bodyLTi = body.select("a").size();
		
		if(bodyTi <= 0) bodyTi=1;
		if(bodyLTi <= 0) bodyLTi=1;

	}
	
	public String getContent(){
		return this.Content;
	}

	public float calCompositeTextDensity(Element e, TNode et) {
		float Numerator;
		float Denominator;
		float result;
		
		
		if (et.getTi() == 0 || et.getNLCi() == 0
				|| et.getLTi() == 0) {
			System.out.println("[Alert] It could be Eroor..");
			return 0;
		} else {

			if(et.getLCi()!=0){
				Numerator = (et.getCi() / et.getLCi()) * (et.getTi() / et.getLTi());
			}else{
				Numerator = (et.getCi()) * (et.getTi() / et.getLTi());
			}
			Numerator = (float) Math.log10(Numerator);
			Denominator = (et.getCi() / et.getNLCi()) * et.getLCi();
			Denominator = Denominator + ((bodyLCi / bodyCi) * et.getCi());
			Denominator = (float) (Denominator + Math.E);
			Denominator = (float) Math.log10(Math.log(Denominator));
			
			result = calTextDensity(e, et) * (Numerator / Denominator);

			if(calTextDensity(e, et)==0) return 0;
			else return result;
		}
	}
	
	public float calTextDensity(Element e, TNode et){
		//입력된 Element의 Text Density 계산
		return et.getCi()/et.getTi();
	}
	
	public float calDensitySum(Element e, HashMap<Element, TNode> tMap){
		float DenSum = 0;
		
		for(Element child : e.children()){
			DenSum += calCompositeTextDensity(child, tMap.get(child));
		}
		return DenSum;
	}

	public Element getMaxDensitySumTag(HashMap<Element, TNode> tMap, Element e){
		Element maxDS=e;
		float maxValue=tMap.get(e).getDS();
		float tmp;
		for(Element el : e.getAllElements()){
			tmp = tMap.get(el).getDS();
			if(tmp > maxValue){
				maxValue = tmp;
				maxDS = el;
			}
		}	
		return maxDS;
	}
	
	public float getThreshold(HashMap<Element, TNode> tMap, Element MDS){
		float threshold = Float.MAX_VALUE;
		Element finder=MDS;
		
		while(!finder.tagName().equals("html")){
			if(tMap.get(finder).getCTD() < threshold){
				threshold = tMap.get(finder).getCTD();
			}
			finder = finder.parent();
		}
		return threshold;
	}

	public void ExtractContent(HashMap<Element, TNode> tMap, Element e, float threshold){
		if(tMap.get(e).getCTD() >= threshold){		
			Element check=getMaxDensitySumTag(tMap, e);
			if(!Content.contains(check.text()) && !check.ownText().isEmpty()) {
				Content += check.text()+"\n";
				ContentTagCount++;
			}else{
				NoiseTagCount++;
			}
			for(Element el : e.children()){
				ExtractContent(tMap, el, threshold);
			}
			
		}
	}
}


