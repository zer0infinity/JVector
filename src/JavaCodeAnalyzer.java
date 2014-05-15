

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Count Files with "java" postfix and its lines.
 * (ignore lines with package, import, class, "{", "}", "(whitespace)")
 * (ignore folder test, package)
 * 
 * @author david
 *
 */
public class JavaCodeAnalyzer {
	
	private static final String DIRECTORY = "."; // "." current directory
	private int counterLine = 0, counterFiles = 0, counterDir = 0, counterDup = 0, counterImage = 0;
	private ArrayList<String> fileList = new ArrayList<String>();

	public JavaCodeAnalyzer() {
		File file = new File(DIRECTORY);
		analyze(file.listFiles());
		showInfo(file);
	}

	private void showInfo(File file) {
		System.out.println(getClass().getSimpleName() + " v0.1 - David Tran");
		System.out.println("------------------------------------");
		System.out.println("Path: " + file.getAbsolutePath());
		System.out.println("Checkout: " + DIRECTORY);
		System.out.println("Lines: " + counterLine);
		System.out.println("Files: " + counterFiles);
		System.out.println("Directories: " + counterDir);
		System.out.println("Duplicate Classnames: " + counterDup);
		System.out.println("Imagefiles (jpg, jpeg, gif, png): " + counterImage);
	}
	
	public void analyze(File[] filelist) {
		for(int i=0;i<filelist.length;i++) {
			File file = filelist[i];
			if(isClassName(file)) {
				continue;
			}
			if(file.isDirectory()) {
				if(isPackageName(file)) {
					continue;
				}
				analyze(file.listFiles());
				counterDir++;
				continue;
			}
			if(isImage(file)) {
				counterImage++;
				continue;
			}
			if(!file.getName().endsWith("java")) {
				continue;
			}
			if(fileList.contains(file.getName())) {
				counterDup++;
			} else {
				fileList.add(file.getName());
			}
			readFile(file);
		}
	}

	private boolean isPackageName(File file) {
		if(getClass().getPackage() != null) {
			return file.getName().equalsIgnoreCase(getClass().getPackage().getName());
		}
		return false;
	}

	private boolean isClassName(File file) {
		return file.getName().equalsIgnoreCase(getClass().getSimpleName());
	}

	private boolean isImage(File file) {
		return file.getName().endsWith("png")
				|| file.getName().endsWith("gif")
				|| file.getName().endsWith("jpg")
				|| file.getName().endsWith("jpeg");
	}

	private void readFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim().toLowerCase();
				if(isIgnoredLine(line)) {
					counterLine++;
				}
			}
			counterFiles++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isIgnoredLine(String line) {
		return !(line.contains("package")
				|| line.contains("import")
				|| line.contains("class")
				|| line.equals("{")
				|| line.equals("}")
				|| line.equals(""));
//		return !(line.equalsIgnoreCase(""));
	}
	
	public static void main(String[] args) {
		new JavaCodeAnalyzer();
	}
}
