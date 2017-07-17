package DBCE.HTML.Parser;

import java.util.ArrayList;
import org.jsoup.nodes.Element;

public class TNode {

	private float CTD=0;
	private float DS=0;
	private float Ci=0;
	private float Ti=0;
	private float LCi=0;
	private float LTi=0;
	private float NLCi=0;
	ArrayList<String> LinkCharList = new ArrayList<String>();
	
	public TNode(){
		
	}
	
	public TNode(Element e){
		Ci = e.text().length();
		Ti = e.getAllElements().size();
		LCi = e.select("a").text().length();
		LTi = e.select("a").size();
		NLCi = Ci - LCi;
		
		if(Ti <= 0) Ti=1;
		if(LTi <= 0) LTi=1;
		if(NLCi <= 0) NLCi=1;

	}
	
	public float getCTD() {
		return CTD;
	}
	public void setCTD(float cTD) {
		CTD = cTD;
	}
	public float getDS() {
		return DS;
	}
	public void setDS(float dS) {
		DS = dS;
	}
	public float getCi() {
		return Ci;
	}
	public void setCi(float ci) {
		Ci = ci;
	}
	public float getTi() {
		return Ti;
	}
	public void setTi(float ti) {
		Ti = ti;
	}
	public float getLCi() {
		return LCi;
	}
	public void setLCi(float lCi) {
		LCi = lCi;
	}
	public float getLTi() {
		return LTi;
	}
	public void setLTi(float lTi) {
		LTi = lTi;
	}
	public float getNLCi() {
		return NLCi;
	}
	public void setNLCi(float NlCi) {
		NLCi = NlCi;
	}
	
}