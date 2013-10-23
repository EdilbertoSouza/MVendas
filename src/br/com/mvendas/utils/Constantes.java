package br.com.mvendas.utils;

import java.io.File;

import android.os.Environment;

public class Constantes {
	
	/**
	 * Nova linha de acordo com o SO
	 */
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	/**
	 * Diretorios
	 */
	public static final File DIR_SDCARD = Environment.getExternalStorageDirectory();
	public static final File DIR_MVENDAS = new File(DIR_SDCARD, "mvendas/");
	public static final File DIR_IMAGENS = new File(DIR_MVENDAS, "imagens/");
	
}
