package DBCE.HTML.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HashMap;

public class DuplicateDetector {
	
	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	/**
	 * Delete duplicate files.
	 * 
	 * @param input
	 *            input files path
	 */
	public void FileDuplicationDetector(String input) {

		HashMap<String, String> md5map = new HashMap<String, String>();
		Path outdir = FileSystems.getDefault().getPath(input);
		DirectoryStream<Path> outstream = null;
		try {
			outstream = Files.newDirectoryStream(outdir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int count = 1;
		for (Path path : outstream) {
			try {
				String digest = getMD5Checksum(input + "/" + path.getFileName().toFile());
				String md5 = md5map.get(digest);
				File f = new File(input + "/" + path.getFileName().toFile());
				if (md5 == null) {
					md5map.put(digest, path.getFileName().toString());
				} else {
					System.out.println(count + " [DELETE] : " + path.getFileName().toFile());
					f.delete();
					count++;
				}
			} catch (Exception e) {
				System.out.println("Maybe This path is directory..");
				//e.printStackTrace();
			}
		}
	}

}
