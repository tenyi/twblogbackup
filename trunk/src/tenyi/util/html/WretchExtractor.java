package tenyi.util.html;

import java.util.ArrayList;
import org.htmlparser.util.ParserException;

public class WretchExtractor extends AbstractBlogExtractor {


	@Override
	public void extractPages(String blogentry, int numberPerPage, String saveDirectory, String saveType) throws ParserException {
		// TODO: 未完成
		if (blogentry.endsWith("/")) {
			// 不要最後的 '/'
			blogentry = blogentry.substring(0, blogentry.length() - 1);
		}

		String bodyHtml = getBodyHtml(blogentry);
		blogtitle = getLinkContentText(getDivContentText(bodyHtml, "blogname"));
		blogdescription = getDivContentText(bodyHtml, "description");

		// Wretch提供列表功能
		//ArrayList<String> list = getBlogLists(blogentry);

		//BlogPage bp = getBlogContent(lastArticleURL, blogentry);

		// System.out.println(lastArticleURL);
		// System.out.println(content);
		System.out.println("共:" + blogList.size()+" 篇");
		
		rssOut(blogentry, blogList);
	}

	private ArrayList<String> getBlogLists(String blogentry)
			throws ParserException {
		ArrayList<String> list = new ArrayList<String>();
		String listEntry = blogentry + "&list=1";
		boolean hasnextpage = false;
		int page = 1;
		do {
			String bodyhtml = getBodyHtml(listEntry);
			String tablehtml = getTableContents(bodyhtml)[0];
			String[] links = getLinkURLs(tablehtml);
			for (String s : links) {
				list.add(s);
			}
			page++;
			String divhtml = getDivContentHtml(bodyhtml, "list-linkcontrol");
			listEntry = blogentry + "&list=1&page=" + page;
			if (divhtml.contains(listEntry)) {
				hasnextpage = true;
			}
		} while (hasnextpage);

		return list;
	}

	@Override
	protected BlogPage getBlogContent(String url, String blogentry)
			throws ParserException {
		// TODO Auto-generated method stub
		return null;
	}





}
