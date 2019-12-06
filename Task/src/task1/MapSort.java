package task1;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapSort {
	
	private Map<String, HashSet<String>> dic;
	
	protected MapSort(Map<String, HashSet<String>> dic) {
		this.dic = sorting(dic);
	}
	
	//map의 value인 HashSet의 사이즈를 기준으로 내림차순으로 정렬하고, 사이즈 같으면 key를 기준으로 오름차순으로 정렬
	protected Map<String, HashSet<String>> sorting(Map<String, HashSet<String>> dic){
		List<Map.Entry<String, HashSet<String>>> list = new LinkedList<>(dic.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<String, HashSet<String>>>() {

			@Override
			public int compare(Map.Entry<String, HashSet<String>> o1, Map.Entry<String, HashSet<String>> o2) {
				int comp = (o1.getValue().size() - o2.getValue().size()) * -1;
				return comp == 0 ? o1.getKey().compareTo(o2.getKey()) : comp;
			}
			
		});
		
		Map<String, HashSet<String>> sorted = new LinkedHashMap<>();
		for(Iterator<Map.Entry<String, HashSet<String>>> iter = list.iterator(); iter.hasNext();) {
			Map.Entry<String, HashSet<String>> entry = iter.next();
			sorted.put(entry.getKey(), entry.getValue());
		}
		
		return sorted;
	}

	protected Map<String, HashSet<String>> getDic() {
		return dic;
	}
	
}
