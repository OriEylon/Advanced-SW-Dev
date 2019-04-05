package server_side;

import java.util.HashMap;

public class FileCacheManager implements CacheManager<String,String> {

	HashMap<String, String> disc;
	
	
	public FileCacheManager() {
		disc=new HashMap<>();
	}

	@Override
	public Boolean Check (String in) {
		if(disc.isEmpty())
			return false;
		return disc.containsKey(in);
	}

	@Override
	public String Extract(String in) {
		return disc.get(in);
	}

	@Override
	public void Save(String in,String out) {
		disc.put(in,out);
		
	}

}
