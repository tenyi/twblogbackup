package tenyi.util.html;

import org.htmlparser.util.ParserException;

/**
 * 
 * @author Tenyi 鳥毅
 * @version 1.0
 */
public class BlogExtractor {
	
//	public static void main(String[] args) {
//		try {
//			new BlogExtractor().go(
//					"http://blog.xuite.net/efchang/network");
//					//"http://blog.xuite.net/anti_ms/index");
//					//"http://blog.xuite.net/tenyi/program");
//		} catch (ParserException e) {
//			e.printStackTrace();
//		}
//	}

	public void go(String blogtype, String blogname, String blogentry) throws ParserException {
 		if (blogentry.toLowerCase().startsWith("http://blog.xuite.net/")) {
			XuiteExtractor xe = new XuiteExtractor();
			xe.extractPages( blogentry, "rss", 20);
		}
	}
	
}
