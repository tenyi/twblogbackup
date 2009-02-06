package tenyi.util.html;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * 
 * @author 鳥毅
 * @version 1.0
 */
public class XuiteExtractor extends AbstractBlogExtractor {

	public static void main(String args[])
	{
		String blogentry = "http://blog.xuite.net/efchang/network";
		XuiteExtractor xe = new XuiteExtractor();
		try {
			String aaa= xe.getBodyHtml(blogentry);
			//System.out.println(aaa);
			aaa= xe.findFirstLinkURL(xe.getSpanContentText(aaa,
			"titlename"));
			System.out.println(aaa);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void extractPages(String blogentry, int numberPerPage,
			String saveDirectory, String saveType) throws ParserException {
		int page = 0;
		this.saveDirectory = saveDirectory;
		if (blogentry.endsWith("/")) {
			// 不要最後的 '/'
			blogentry = blogentry.substring(0, blogentry.length() - 1);
		}
		String author = blogentry.substring("http://blog.xuite.net/".length());
		author = author.substring(0, author.indexOf('/'));
		String bodyHtml = getBodyHtml(blogentry);
		blogtitle = getLinkContentText(getDivContentText(bodyHtml, "blogname"));
		blogdescription = getDivContentText(bodyHtml, "description");
		String lastArticleURL = findLastArticle(blogentry);
		BlogPage bp = getBlogContent(lastArticleURL, blogentry);
		// 最後一則為
		while (bp.previousUrl != null) {
			bp = getBlogContent(bp.previousUrl, blogentry);
			blogList.add(bp);

			System.out.println("目前執行到:" + blogList.size() + " "
					+ bp.previousUrl);
			if (numberPerPage > 0) {
				if (blogList.size() % numberPerPage == 0) // 整除表示滿一頁
				{
					page++;
					outputPages(saveType, author, blogentry, blogList, Integer
							.toString(page));
					// mtOutPages(author, blogentry, blogList,
					// Integer.toString(page));
					blogList.clear();
				}
			}
		}

		System.out.println("共:" + blogList.size());
		if (numberPerPage == 0) {
			output(saveType, author, blogentry, blogList);
		} else {
			if (blogList.size() > 0) {
				outputPages(saveType, author, blogentry, blogList, Integer
						.toString(++page));
			}
		}
	}

	@Override
	protected BlogPage getBlogContent(String url, String blogentry)
			throws ParserException {
		BlogPage blogPage = new BlogPage();
		blogPage.url = url;
		String bodyHtml = getBodyHtml(url);

		// 文章 <div class="blogbody">
		blogPage.content = getDivContentHtml(bodyHtml, "blogbody");
		// blogPage.content = blogPage.content.substring(
		// "<div class=\"blogbody\"><br><br>".length(),
		// blogPage.content.length() - "<br><br></div>".length());
		// System.out.println(blogPage.content);
		// 標題 <span class="titlename">
		blogPage.title = getSpanContentText(bodyHtml, "titlename");
		// 分類 <span class="category">
		blogPage.category = getSpanContentText(bodyHtml, "category");
		// 時間 <span class="titledate"> //還要改格式
		blogPage.datetime = getSpanContentText(bodyHtml, "titledate");
		// 前一則
		// <div class="selectbar">
		String aaa = getDivContentHtml(bodyHtml, "selectbar");
		if (aaa != null) {
			blogPage.previousUrl = findFirstLinkURL(aaa);
			if (blogPage.previousUrl.startsWith("/")) {
				// 就是最後一篇了
				blogPage.previousUrl = null;
			} else {
				blogPage.previousUrl = blogentry + "/" + blogPage.previousUrl;
			}
		} else {
			blogPage.previousUrl = null;
		}

		return blogPage;
	}

	private String findFirstLinkURL(String stringHtml) throws ParserException {
		if(stringHtml == null)
			{
			return null;
			}
		String httpurl = null;
		Parser p = new Parser();
//		try
//		{
			p.setInputHTML(stringHtml);
//		}
//		catch(Exception ex)
//		{
//			System.out.println(ex);
//			System.out.println(stringHtml);
//		}
		NodeList linkList = p.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof LinkTag;
			}
		});
		LinkTag linkTag = (LinkTag) linkList.elementAt(0);
		httpurl = linkTag.getLink();
		return httpurl;
	}

	private String findLastArticle(String blogentry) throws ParserException {
		// <div class="singleArticle">... <span class="titlename"> <a href="....">
		return findFirstLinkURL(getSpanContentText(getBodyHtml(blogentry),
						"titlename"));
	}
}
