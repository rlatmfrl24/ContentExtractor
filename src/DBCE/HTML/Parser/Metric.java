package DBCE.HTML.Parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Element;

public class Metric {
	
	double bodyCi;
	double bodyTi;
	double bodyLCi;
	double bodyLTi;
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

	public double calCompositeTextDensity(Element e, TNode et) {
		double Numerator;
		double Denominator;
		double result;
		
		
		if (et.getTi() == 0 || et.getNLCi() == 0
				|| et.getLTi() == 0) {
			System.out.println("[Alert] It could be Eroor..");
			return 0;
		} else {
			if(et.getLCi()!=0){
				Numerator = (et.getCi() / et.getLCi()) * (et.getTi() / et.getLTi());
				Denominator = (et.getCi() / et.getNLCi()) * et.getLCi();
			}else{
				Numerator = (et.getCi()) * (et.getTi() / et.getLTi());
				Denominator = (et.getCi() / et.getNLCi());
			}
			Numerator = (double) Math.log10(Numerator);
			Denominator = Denominator + ((bodyLCi / bodyCi) * et.getCi());
			Denominator = (double) (Denominator + Math.E);
			Denominator = (double) Math.log10(Math.log(Denominator));
			result = calTextDensity(e, et) * (Numerator / Denominator);
			
			if(calTextDensity(e, et)==0) return 0;
			else return result;
		}
	}
	
	public double calTextDensity(Element e, TNode et){
		//입력된 Element의 Text Density 계산
		return et.getCi()/et.getTi();
	}
	
	public double calDensitySum(Element e, HashMap<Element, TNode> tMap){
		double DenSum = 0;
		
		for(Element child : e.children()){
			DenSum += calCompositeTextDensity(child, tMap.get(child));
		}
		return DenSum;
	}

	public Element getMaxDensitySumTag(HashMap<Element, TNode> tMap, Element e){
		Element maxDS=e;
		double maxValue=tMap.get(e).getDS();
		double tmp;
		for(Element el : e.getAllElements()){
			tmp = tMap.get(el).getDS();
			if(tmp > maxValue){
				maxValue = tmp;
				maxDS = el;
			}
		}	
		return maxDS;
	}
	
	public double getThreshold(HashMap<Element, TNode> tMap, Element MDS){
		double threshold = Double.MAX_VALUE;
		Element finder=MDS;
		
		while(!finder.tagName().equals("html")){
			if(tMap.get(finder).getCTD() < threshold){
				threshold = tMap.get(finder).getCTD();
			}
			finder = finder.parent();
		}
		return threshold;
	}

	public void ExtractContent(HashMap<Element, TNode> tMap, Element e, double threshold){
		if(tMap.get(e).getCTD() >= threshold){	
			Element check=getMaxDensitySumTag(tMap, e);
			if(!Content.contains(check.ownText()) && !check.ownText().isEmpty() && !check.tagName().equals("body")) {
				Content += check.ownText()+"\n";
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


