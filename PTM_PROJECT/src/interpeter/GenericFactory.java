package interpeter;

import java.util.HashMap;
import java.util.Map;

public class GenericFactory<Product> {

	private interface Creator<Product> {
		public Product create();
	}

	Map<String, Creator<Product>> map;

	public GenericFactory() {
		map = new HashMap<>();
	}

	public void insertProduct(String key, Class<? extends Product> c) {
		Creator crt = new Creator<Product>() {

			@Override
			public Product create() {
				try {
					return c.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		map.put(key, crt);
	}

	public Product getNewProduct(String key) {
		Creator c = map.get(key);
		if (c != null)
			return (Product) c.create();
		return null;

	}
}
