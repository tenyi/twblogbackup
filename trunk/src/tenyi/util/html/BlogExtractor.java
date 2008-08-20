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
//			new BlogExtractor().go(...);
//		} catch (ParserException e) {
//			e.printStackTrace();
//		}
//	}
    public void go(String blogtype, String blogentry, int numberPerPage, String saveDirectory, String saveType)
            throws ParserException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        AbstractBlogExtractor extractor = null;
        extractor = (AbstractBlogExtractor) Class.forName("tenyi.util.html."+blogtype + "Extractor").newInstance();
        extractor.extractPages(blogentry, numberPerPage, saveDirectory, saveType);
    }
}
