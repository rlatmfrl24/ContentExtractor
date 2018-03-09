package DBCE.HTML.Parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import DBCE.Filter.DuplicateDetector;
import DBCE.Filter.NoiseFilter;
import DBCE.Filter.RepeatLineMap;
import DBCE.Utility.DateUtil;

public class ExtractMain extends Thread {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		Document doc = Jsoup.parse(HtmlFetch.getHttpHTML(url));
		if (args.length < 3) {
			System.err.println(
					"[Usage] java -jar Extractor.jar lang_code /input /output");
			System.exit(-1);
		}
		
		String lang = args[0];
		int sub_number = 0;
		HashMap<String, String> path_Map = new HashMap<>();
		ArrayList<String> input_path_list = new ArrayList<>();
		
		for(int i=1; i<args.length-1; i++){
			File ipfolder = new File(args[i]);
			for(String str : ipfolder.list()){
				input_path_list.add(args[i]+str);
				path_Map.put(args[i]+str, str);
			}
		}
		String output_path = args[args.length-1];	//TEXT Output Path
		
		long star_time = System.currentTimeMillis();

		File ofp = new File(output_path);
		if(!ofp.exists()){
			ofp.mkdirs();
		}else{
			subDirList(output_path);
		}
		
		try{
			for(int i=0; i<input_path_list.size(); i++){
				
				PageInfo pi = MakePageInformation(lang, input_path_list.get(i), true);
				pi.AnalyzeContent();

				String outpath = output_path+"/"+path_Map.get(input_path_list.get(i));
				//System.out.println(outpath);
				outpath = outpath.substring(0, outpath.lastIndexOf("."))+".txt";
				File of = new File(outpath);
				while(of.exists()){
					of = new File(outpath.replaceAll(".txt", "_"+sub_number+".txt"));
					sub_number++;
				}
				sub_number=0;
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(of), StandardCharsets.UTF_8));
				bw.write(pi.getContent()+"\n");
				bw.flush();
				bw.close();
				System.out.println(input_path_list.get(i));
			}
			
			System.out.println("Detect Duplicate files..");
			DuplicateDetector dd = new DuplicateDetector();
			dd.FileDuplicationDetector(output_path);
			
			System.out.println("Check repeated line..");
			RepeatLineMap rlm = new RepeatLineMap();
			rlm.refineByMap(output_path, output_path+"/refine/");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		long end_time = System.currentTimeMillis();
		System.out.println("Elapsed Time:"+(end_time-star_time)+"ms");
		
		//TestUtil.showF1(output_path+"/", input_path.replaceAll("original", "gold"));
		
	}
	
	public static PageInfo MakePageInformation(String lang, String data_path, boolean NoiseFiltering) {
		
		PageInfo pi = new PageInfo();
		//System.out.println(input_path_list.get(i));
		Document doc = pi.setHTML(data_path);
		pi.setTitle(doc.title());
		pi.setUrl(doc.select("link[rel=\"canonical\"]").attr("href"));
		DateUtil du = new DateUtil(pi);
		Date pdate = du.getPageDate();
		if(pdate!=null) pi.setDate(pdate);
		pi.setContent(performExtraction(doc));

		if(NoiseFiltering) {
			if(lang.equals("ko_kr")){
				NoiseFilter nc = new NoiseFilter();
				pi.setContent(nc.refineNoise(pi.getContent(), lang, true, true, true, true));
				//System.out.println("Apply Noise Filter["+lang+"]");
			}
		}

		return pi;
	}
	
	public static String performExtraction(Document doc){
		HashMap<Element, TNode> bodyMap = new HashMap<Element, TNode>();
		doc = HtmlFetch.killNoise(doc);
		Element bodyElement = doc.body();
		double Threshold;
		
		Metric m = new Metric(bodyElement);
		
		for(Element e : bodyElement.getAllElements()){
			TNode et = new TNode(e);
			et.setCTD(m.calCompositeTextDensity(e, et));
			bodyMap.put(e, et);
		}
		
		for(Entry<Element, TNode> ten : bodyMap.entrySet()){
			ten.getValue().setDS(m.calDensitySum(ten.getKey(), bodyMap));
		}
		
		Element Max_DensitySum_Tag = m.getMaxDensitySumTag(bodyMap, bodyElement);
		
		Threshold = m.getThreshold(bodyMap, Max_DensitySum_Tag);		
		
		//각 Element별로 Score 출력
/*		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("F:/finderror.txt")));
			for (Entry<Element, TNode> e : bodyMap.entrySet()) {
				bw.write(e.getKey() + "\n");
				bw.write("#Score:" +e.getValue().getTi()/e.getValue().getLTi()+"\n");
				bw.write("#Score::" + e.getValue().getCTD() + ", " + e.getValue().getCi() + ", " + e.getValue().getTi()+ ", " + e.getValue().getLCi() + ", " + e.getValue().getLTi() + ", " + e.getValue().getNLCi()+ "\n");
				bw.write("=====================================\n");
			}

			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		m.ExtractContent(bodyMap, bodyElement, Threshold);
		
		return m.getContent();
	}
	
	public static void subDirList(String source){
		File dir = new File(source); 
		File[] fileList = dir.listFiles();
		
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					subDirList(file.getCanonicalPath().toString()); 
					file.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
