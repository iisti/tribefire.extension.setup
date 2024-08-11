package tribefire.extension.setup.hiconic.sdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Updator {
	private static final int PROGRESS_WIDTH = 50;
	private static Map<String, Option<?>> optionByName = new HashMap<String, Option<?>>();
	private static Map<Character, Option<?>> optionByShortName = new HashMap<Character, Option<?>>();

	private static final Option<String> OPTION_VERSION = register(new Option<String>("version", 'v', String.class));
	
	private static final String EXT_DOWNLOAD = ".download";
	private static final String ENV_DEVROCK_SDK_HOME = "DEVROCK_SDK_HOME";
	private static final String ENV_HICONIC_SDK_HOME = "HICONIC_SDK_HOME";
	private static final String GITHUB_READ_PACKAGES_TOKEN = "GITHUB_READ_PACKAGES_TOKEN";
	private static final URI REPO_URL = URI.create("https://maven.pkg.github.com/hiconic-os/maven-repo-dev/");

	private static HttpClient httpClient = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).build();

	private static Options options = new Options();
	
	private static Pattern launchPattern = Pattern.compile("launch-[^\\d\\.]*\\.jar");
	
	private static <E> Option<E> register(Option<E> option) {
		String name = option.name();
		
		optionByName.put(name, option);
		
		Character shortName = option.shortName();
		
		if (shortName != null)
			optionByShortName.put(shortName, option);
		
		return option;
	}
	
	public static void main(String[] args) {
		try {
			readOptions(args);
			update();
		}
		catch (ErrorException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void readOptions(String[] args) {
		Option<?> option = null;
		for (String arg: args) {
			if (option == null) {
				if (arg.startsWith("--")) {
					String name = arg.substring(2);

					option = optionByName.get(name);
					
					if (option == null)
						throw new ErrorException("Unknown option " + arg);
				}
				else if (arg.startsWith("-")) {
					String name = arg.substring(2);
					
					if (name.length() != 1)
						throw new ErrorException("Invalid argument " + arg);
					
					Character c = name.charAt(0);
					
					option = optionByShortName.get(c);
					
					if (option == null)
						throw new ErrorException("Unknown option " + arg);
				}
			}
			else {
				options.putParsed(option, arg);
				option = null;
			}
		}
		
		if (option != null)
			throw new ErrorException("Missing argument for option " + option.name());
	}

	public static void update() {
		File sdkHome = getSdkHome();
		String version = getVersion();
		
		System.out.println("Updating hiconic-sdk in [" + sdkHome.getAbsolutePath() + "] to version " + version);
		
		File sharedLibs = new File(sdkHome, "tools/shared-lib");
		
		if (!sharedLibs.isDirectory()) {
			System.err.println(sharedLibs + " is not a directory.");
			System.exit(1);
		}
		
		File[] files = sharedLibs.listFiles();
		
		Set<String> exceeds = new HashSet<>();
		
		for (File file: files) {
			String name = file.getName();
			exceeds.add(name);
		}
		
		String sharedLibPrefix = "hiconic-sdk/tools/shared-lib/";
		List<String> skipped = new ArrayList<>();
		Map<File, File> downloadedFiles = new LinkedHashMap<>();
		Map<File, File> updateFiles = new LinkedHashMap<>();
		
		HttpResponse<InputStream> response = openResponse("tribefire/extension/setup/hiconic-sdk/" + version + "/hiconic-sdk-" + version + ".zip", BodyHandlers.ofInputStream());
		long length = response.headers().firstValue("Content-Length").map(Long::parseLong).get();
		
		ProgressPrinter progressPrinter = new ProgressPrinter(length, PROGRESS_WIDTH);
		
		CountingInputStream cin = null;
		try (ZipInputStream in = new ZipInputStream(cin = new CountingInputStream(response.body()))) {
			ZipEntry entry = null;
			
			System.out.println("\n            |" + "-".repeat(PROGRESS_WIDTH) + "|");
			System.out.print("Downloading |");
			
			while ((entry = in.getNextEntry()) != null) {
				String name = entry.getName();
				
				if (name.startsWith(sharedLibPrefix)) {
					String libName = name.substring(sharedLibPrefix.length());
					if (libName.isEmpty())
						continue;
					
					File file = new File(sharedLibs, libName);
					
					boolean launchJar = launchPattern.matcher(libName).find();
					
					boolean existed = exceeds.remove(libName);
					
					if (!existed || launchJar) {
						
						long expectedCrc32 = entry.getCrc();
						long actualCrc32;
						
						File tempFile = new File(file.getPath() + EXT_DOWNLOAD);
						try (OutputStream out = new FileOutputStream(tempFile)) {
							if (existed)
								updateFiles.put(tempFile, file);
							else 
								downloadedFiles.put(tempFile, file);
							
							actualCrc32 = pump(in, out);
						}
						
						if (expectedCrc32 != actualCrc32) {
							for (File downloadedFile: downloadedFiles.keySet()) {
								downloadedFile.delete();
							}
							throw new ErrorException("CRC32 check failed after extraction of " + entry.getName());
						}
					}
					else {
						consume(in);
						skipped.add(libName);
					}
				}
				
				in.closeEntry();
				progressPrinter.update(cin.getByteCount());
			}
			
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		progressPrinter.update(length);
		System.out.println("|");
		
		transfer(downloadedFiles, "Adding");
		
		transfer(updateFiles, "Updating");
		
		if (!exceeds.isEmpty()) {
			System.out.println();
			for (String exceeding: exceeds) {
				File file = new File(sharedLibs, exceeding);
				
				System.out.println("Removing " + exceeding);
				file.delete();
			}
		}
		
		if (!skipped.isEmpty())
			System.out.println("\n" + skipped.size() + " libraries were up to date");

	}

	private static void transfer(Map<File, File> fileMap, String operation) {
		if (fileMap.isEmpty())
			return;
		
		System.out.println();
		
		for (Map.Entry<File, File> entry: fileMap.entrySet()) {
			File target = entry.getValue();
			File source = entry.getKey();
			try {
				System.out.println(operation + " " + target.getName());
				Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	private static String getVersion() {
		String version = options.find(OPTION_VERSION);
		
		if (version != null)
			return version;
		
		return getLatestVersion();
	}
	
	private static void consume(InputStream in) throws IOException {
		byte[] buffer = new byte[64536];
        while (in.read(buffer) != -1) {
            // Just read to discard the data
        }
	}
	
	private static long pump(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[64536];
		int len = 0;
		
		CRC32 crc32 = new CRC32();
		
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
			crc32.update(buffer, 0, len);
		}
		
		return crc32.getValue();
	}
	
	private static String getLatestVersion() {
		String metaDataXml = open("tribefire/extension/setup/hiconic-sdk/maven-metadata.xml", BodyHandlers.ofString());

		try {
			Document document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(metaDataXml)));

			NodeList nodeList = document.getElementsByTagName("latest");
			if (nodeList.getLength() == 0) {
				throw new ErrorException("Missing latest version in maven-metadata.xml");
			}
			
			Node item = nodeList.item(0);
			String version = item.getTextContent();
			
			return version;
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T open(String path, BodyHandler<T> handler) {
		return openResponse(path, handler).body();
	}
	
	private static <T> HttpResponse<T> openResponse(String path, BodyHandler<T> handler) {
		URI uri = REPO_URL.resolve(path);

		String token = getAuthToken();
		byte[] authBytes = token.getBytes(StandardCharsets.UTF_8);

		String encodedAuth = Base64.getEncoder().encodeToString(authBytes);

		HttpRequest httpRequest = HttpRequest.newBuilder() //
				.uri(uri).header("Authorization", "Basic " + encodedAuth).GET().build();

		final HttpResponse<T> response;

		try {
			response = httpClient.send(httpRequest, handler);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}

		int statusCode = response.statusCode();

		if (statusCode >= 200 && statusCode < 300) {
			return response;
		} else
			throw new ErrorException("HTTP status error " + statusCode + " while accessing "
					+ httpRequest.uri().toString() + ": " + response.body());

	}

	private static File getSdkHome() {
		String hcHome = System.getenv(ENV_HICONIC_SDK_HOME);
		
		if (hcHome != null)
			return new File(hcHome);
		
		String drHome = System.getenv(ENV_DEVROCK_SDK_HOME);
		if (drHome != null)
			return new File(drHome);
		
		String launchScript = System.getProperty("reflex.launch.script");
		
		if (launchScript == null) {
			throw new ErrorException("HICONIC SDK HOME not detected");
		}
		
		return new File(launchScript).getParentFile().getParentFile().getParentFile().getParentFile();
	}
	
	private static String getAuthToken() {
		String token = System.getenv(GITHUB_READ_PACKAGES_TOKEN);

		if (token == null)
			throw new ErrorException("Missing environment variable " + GITHUB_READ_PACKAGES_TOKEN
					+ " that is required for authentication against githup maven packages");

		return token;
	}

}

record Option<E>(String name, Character shortName, Class<E> type) {
}

class Options {
	private static Map<Class<?>, Function<String, ?>> parsers = new IdentityHashMap<>();
	
	static {
		parsers.put(String.class, Function.identity());
		parsers.put(Boolean.class, Boolean::parseBoolean);
		parsers.put(Integer.class, Integer::parseInt);
		parsers.put(Long.class, Long::parseLong);
	}
	
	private Map<Option<?>, Object> options = new LinkedHashMap<>();
	
	public void putParsed(Option<?> option, String s) {
		Object value = convert(option, s);
		options.put(option, value);
	}
	
	private Object convert(Option<?> option, String value) {
		Function<String, ?> function = parsers.get(option.type());
		return function.apply(value);
	}
	
	public <E> E find(Option<E> option) {
		return find(option, null);
	}
	
	public <E> E find(Option<E> option, E defaultValue) {
		return (E)options.get(option);
	}
}

class ProgressPrinter {
	private long length;
	private int charLength;
	private long curLength;
	private int curCharLength;
	
	public ProgressPrinter(long length, int charLength) {
		super();
		this.length = length;
		this.charLength = charLength;
	}

	public void update(long newLength) {
		curLength = newLength;
		
		double factor = curLength / (double)length;
		
		int newCharLength = (int) (charLength * factor); 
		
		int charDelta = newCharLength - curCharLength;
		
		if (charDelta > 0)
			System.out.print("*".repeat(charDelta));
			
		curCharLength = newCharLength;
	}
}
