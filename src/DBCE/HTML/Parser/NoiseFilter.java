package DBCE.HTML.Parser;

import java.util.ArrayList;

public class NoiseFilter {

	private static final int MinWordsLine = 10;
	private static final int MinWordsSentence = 5;
	String[] specialRegex = {
			"\\d{3}\\-\\d{4}\\-\\d{4}",
			"\\d{2,3}\\-\\d{3,4}\\-\\d{4}",
			"01(?:0|1[6-9])\\-(?:\\d{3}|\\d{4})\\-\\d{4}",
			"[A-Za-z0-9+_.-]+@(.+)",
			"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}",
			"([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}",
			"\\d{6}\\-[1-4]\\d{6}",
			"<!--(.|\n|\r)*-->",
			"([^\\s]+(?=\\.(jpg|gif|png))\\.\\2)/",
	};
	
	public String refineNoise(String content, String lang,  boolean Minwords, boolean FooterWords, boolean NoiseWord, boolean SpecialRegex){
		ArrayList<String> lines = new ArrayList<>();
		content = content.replaceAll("\\p{Z}", " ");
		for(String s : content.split("(\n|\r)")){
			lines.add(s);
		}
		
		if(FooterWords){
			boolean isFooter=false;
			FooterWord fw = new FooterWord();
			ArrayList<String> removeList = new ArrayList<>();

			switch(lang){
			case "ko_kr":
				for(int i=0; i<lines.size(); i++){
					if(!isFooter){
						for(String s : fw.ko_kr){
							if(lines.get(i).contains(s)){
								isFooter=true;
								removeList.add(lines.get(i));
							}
						}
					}else{
						removeList.add(lines.get(i));
					}
				}
				break;
			}
			for(int i=0; i<removeList.size(); i++){
				lines.remove(removeList.get(i));
			}
		}
		
		if(NoiseWord){
			ArrayList<String> removeList = new ArrayList<>();
			NoiseWord nw = new NoiseWord();
			
			switch(lang){
			case "ko_kr":
				for(int i=0; i<lines.size(); i++){
					for(String s : nw.ko_kr){
						if(lines.get(i).contains(s)){
							removeList.add(lines.get(i));
						}
					}
				}
				break;
			}
			for(int i=0; i<removeList.size(); i++){
				lines.remove(removeList.get(i));
			}

		}
		
		if(SpecialRegex){
			ArrayList<String> replaceList = new ArrayList<>();

			for (int i = 0; i < lines.size(); i++) {
				String temp = lines.get(i);
				for (String s : specialRegex) {
					temp = temp.replaceAll(s, "");
				}
				replaceList.add(temp);
			}
			lines = replaceList;
		}
		
		if(Minwords){
			ArrayList<String> removeList = new ArrayList<>();
			if(lines.size() > MinWordsLine){
				for(int i=0; i<MinWordsLine; i++){
					if(lines.get(i).split(" ").length < MinWordsSentence && !removeList.contains(lines.get(i))){
						removeList.add(lines.get(i));
					}
					if(lines.get(lines.size()-(i+1)).split(" ").length < MinWordsSentence && !removeList.contains(lines.get(lines.size()-(i+1)))){
						removeList.add(lines.get(lines.size()-(i+1)));
					}
				}
			}else{
				//Check All Line
				for(int i=0; i<lines.size(); i++){
					if(lines.get(i).split(" ").length < MinWordsSentence && !removeList.contains(lines.get(i))){
						removeList.add(lines.get(i));
					}
				}
			}
			for(int i=0; i<removeList.size(); i++){
				lines.remove(removeList.get(i));
			}
		}
		
	
		String res="";
		for(int i=0; i<lines.size(); i++){
			res += lines.get(i)+"\n";
		}
		return res;
	}
}

class FooterWord{
	String[] ko_kr = { "등록번호 :", "등록번호:", "사업자등록번호 :", "사업자등록번호:", "상호 :", "개인정보보호책임자 :", "자까지 쓰실 수 있습니다",
			"Copyright(c)", "Copyrights", "Copyright", "All Rights Reserved", "정보에 대해 만족하십니까", "댓글이 있습니다",
			"개인정보수집 및 이용안내", "앱 다운로드 URL을 전송", "로그인 후 글", "댓글을 남겨주", "([0-9])", "(0/1000자)", "자동등록방지용 코드", "잠시만 기다려주세요",
			"메일보내기", "오시는 길", "이 양식을 사용",/* "ⓒ", "©", */ };
}

class NoiseWord{
	String[] ko_kr = { "입력시간", "게시됨", "업데이트됨" };
}
