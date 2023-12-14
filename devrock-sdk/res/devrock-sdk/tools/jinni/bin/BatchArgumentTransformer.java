import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BatchArgumentTransformer {
	public static void main(String[] args) {
		List<String> escapedTransformedArgs = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.length() > 1 && arg.charAt(0) == '+') {
				String name = arg.substring(1);
				int n = ++i;
				String value = n < args.length? args[n]: "";
				arg = "-P." + name + "=" + value;
			}

			escapedTransformedArgs.add(escapeAndQuote(arg));
		}
		
		System.out.println(escapedTransformedArgs.stream().collect(Collectors.joining(" ")));
	}

	private static String escapeAndQuote(String arg) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("\"");
		
		for (int i = 0; i < arg.length(); i++) {
			char c = arg.charAt(i);
			
			switch (c) {
			case '"':
				builder.append("\\\"");
				break;
			case '%':
				builder.append("%%");
				break;
			case '\\':
				int n = i + 1;
				if (n < arg.length() && arg.charAt(n) == '"') {
					builder.append("\\\"");
					i++;
				}
				else
					builder.append('\\');
				break;
			default:
				builder.append(c);
			}
		}
		
		builder.append("\"");
		return builder.toString();
	}
}
