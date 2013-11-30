package br.com.mvendas.utils;


public class StringUtil {
	
    public static String toRestData(String params[][][]) {
    	String parameters[][];
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < params.length; i++) {
			parameters = params[i];			
			sb.append(toRestData(parameters));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("]");
    	return sb.toString();
    }

    public static String toRestData(String params[][]) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (int i = 0; i < params.length; i++) {
			sb.append(Character.toString((char) 34));
			sb.append(params[i][0]);
			sb.append(Character.toString((char) 34));
			sb.append(":");
			if (colocaAspas(params[i][0]))	sb.append(Character.toString((char) 34));
			sb.append(params[i][1]);
			if (colocaAspas(params[i][0]))	sb.append(Character.toString((char) 34));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("}");    	
    	
    	return sb.toString();
    }
    
    public static boolean colocaAspas(String name) {
    	boolean ret = true;
		if(name.equals("select_fields")) {
			ret = false;
		} else if(name.equals("link_name_to_fields_array")) {
			ret = false;
		} else if(name.equals("max_results")) {
			ret = false;			
		} else if(name.equals("Favorites")) {
			ret = false;
		} else if(name.equals("name_value_list")) {
			ret = false;
		}    	
    	return ret;
    }

    public static String toArrayData(String params[]) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("[");
		for (int i = 0; i < params.length; i++) {
			sb.append(Character.toString((char) 34));
			sb.append(params[i]);
			sb.append(Character.toString((char) 34));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("]");    	
    	
    	return sb.toString();
    }	

}
