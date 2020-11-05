package br.com.paulofranca.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static String decodeParam(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static List<Integer> decodeIntList(String str) {
		String[] vetorStr = str.split(",");
		List<Integer> list = new ArrayList<>();
		
		for (int i = 0; i < vetorStr.length; i++) {
			list.add(Integer.parseInt(vetorStr[i]));
		}
		
		return list;
		//return Arrays.asList(str.split(",")).stream().map(i -> Integer.parseInt(i)).collect(Collectors.toList());
	}
}
