package DBCE.Evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Evaulation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		showF1("F:/eVal/", "F:/dataset/arstechnica/gold/");
	}
	public static void showF1(String output_path, String gold_path) {
		File opath = new File(output_path);
		File gpath = new File(gold_path);
		String output_text = "";
		String gold_text = "";
		String[] oList = opath.list();
		String[] gList = gpath.list();
		double f1sum = 0;
		int i;

		try {
			for (i = 0; i < oList.length; i++) {
				if (oList[i].equals(gList[i])) {
					double f1 = 0;
					File ofile = new File(output_path + oList[i]);
					BufferedReader obr = new BufferedReader(new FileReader(ofile));
					String tmp;
					while ((tmp = obr.readLine()) != null) {
						output_text += tmp;
					}
					obr.close();
					File gfile = new File(gold_path + gList[i]);
					BufferedReader gbr = new BufferedReader(new FileReader(gfile));
					while ((tmp = gbr.readLine()) != null) {
						gold_text += tmp;
					}
					gbr.close();
					f1 = SørensenDiceMain.similarity(output_text, gold_text) * 100;
					
					/*if(f1 < 10){
						System.out.println(oList[i]+" : "+output_text);
						System.out.println("=======================");
						System.out.println(gList[i]+" : "+gold_text);
						System.exit(-1);
					}
					*/System.out.println(oList[i] + "::" + f1);
					f1sum += f1;
					output_text = "";
					gold_text = "";
				}
			}
			System.out.println(f1sum / i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
