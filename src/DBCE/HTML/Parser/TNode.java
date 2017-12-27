package DBCE.HTML.Parser;

import java.util.ArrayList;
import org.jsoup.nodes.Element;

public class TNode {

	private double CTD=0;
	private double DS=0;
	private double Ci=0;
	private double Ti=0;
	private double LCi=0;
	private double LTi=0;
	private double NLCi=0;
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
	
	public double getCTD() {
		return CTD;
	}
	public void setCTD(double cTD) {
		CTD = cTD;
	}
	public double getDS() {
		return DS;
	}
	public void setDS(double dS) {
		DS = dS;
	}
	public double getCi() {
		return Ci;
	}
	public void setCi(double ci) {
		Ci = ci;
	}
	public double getTi() {
		return Ti;
	}
	public void setTi(double ti) {
		Ti = ti;
	}
	public double getLCi() {
		return LCi;
	}
	public void setLCi(double lCi) {
		LCi = lCi;
	}
	public double getLTi() {
		return LTi;
	}
	public void setLTi(double lTi) {
		LTi = lTi;
	}
	public double getNLCi() {
		return NLCi;
	}
	public void setNLCi(double NlCi) {
		NLCi = NlCi;
	}
	
}