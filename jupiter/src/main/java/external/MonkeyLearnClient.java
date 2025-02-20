package external;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.monkeylearn.ExtraParam;

import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnResponse;
import com.monkeylearn.MonkeyLearnException;


public class MonkeyLearnClient {
	
	private static final String API_KEY = "1363d4d8d759a4062d566ff48608f79fe9732c43";
	private static final String MODEL_ID = "ex_YCya9nrn";
	
    public static void main( String[] args ) throws MonkeyLearnException {
        String[] textList = {"Elon Musk has shared a photo of the spacesuit designed by SpaceX. This is the second image shared of the new design and the first to feature the spacesuit’s full-body look.", };
        
        List<List<String>> keywords = extractKeywords(textList);
        for (List<String> ws : keywords) {
        	for (String w : ws) {
        		System.out.println(w);
        	}
        	System.out.println();
        }
    }
    
    public static List<List<String>> extractKeywords (String[] text) {
    	if (text == null || text.length == 0) {
    		return new ArrayList<>(); 
    	}
    	
    	MonkeyLearn ml = new MonkeyLearn(API_KEY);
    	
    	ExtraParam[] extraParams = {new ExtraParam("max_keywords", "3")};
    	MonkeyLearnResponse res;
    	
    	try {
    		res = ml.extractors.extract(MODEL_ID, text, extraParams);
    		JSONArray resultArray = res.arrayResult;
			return getKeywords(resultArray);
    	} catch (MonkeyLearnException e) {
    		e.printStackTrace();
    	}
    	return new ArrayList<>(); 
    }
    
    private static List<List<String>> getKeywords(JSONArray mlResultArray) {
		List<List<String>> topKeywords = new ArrayList<>();
		// Iterate the result array and convert it to our format.
		for (int i = 0; i < mlResultArray.size(); ++i) {
			List<String> keywords = new ArrayList<>();
			JSONArray keywordsArray = (JSONArray) mlResultArray.get(i);
			for (int j = 0; j < keywordsArray.size(); ++j) {
				JSONObject keywordObject = (JSONObject) keywordsArray.get(j);
				// We just need the keyword, excluding other fields.
				String keyword = (String) keywordObject.get("keyword");
				keywords.add(keyword);

			}
			topKeywords.add(keywords);
		}
		return topKeywords;
	}

}