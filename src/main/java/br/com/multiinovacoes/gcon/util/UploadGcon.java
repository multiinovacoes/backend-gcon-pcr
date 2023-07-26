package br.com.multiinovacoes.gcon.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.tomcat.util.codec.binary.Base64;


public final class UploadGcon {
	
	private UploadGcon() {}
	
	public static void upload(String base64, String urlAnexo)throws IOException {
		try {
			byte[] anexo2 = Base64.decodeBase64(base64.split(",")[1]);
			OutputStream out = new FileOutputStream(
					"D:\\gcon_arquivos\\arquivos\\"+urlAnexo); 
			out.write(anexo2);
			out.close();
		}catch (Exception e) {
			e.getMessage();
		}
			
	}


}
