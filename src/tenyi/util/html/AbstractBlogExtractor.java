package tenyi.util.html;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public abstract class AbstractBlogExtractor {
	ArrayList<BlogPage> blogList = new ArrayList<BlogPage>();
	String blogtitle;
	String blogdescription;
	String saveDirectory;

	// public void extract(String blogentry) throws ParserException {
	// extractPages(blogentry, "rss", 0);
	// }

	public static String getNowTime(String timeFormat) {
		SimpleDateFormat lformat = new SimpleDateFormat(timeFormat);
		Calendar now = Calendar.getInstance();
		String nowstr = lformat.format(now.getTime());
		return nowstr;
	}

	public static String getNowTime() {
		return getNowTime("MM/dd/yyyy hh:mm:ss");
	}

	/**
	 * 
	 * @param blogentry
	 * @param numberPerPage
	 *            多少篇一個檔案 0表示只有一個檔
	 * @param saveDirectory
	 * @param saveType
	 * @throws org.htmlparser.util.ParserException
	 */
	public abstract void extractPages(String blogentry, int numberPerPage,
			String saveDirectory, String saveType) throws ParserException;

	protected abstract BlogPage getBlogContent(String url, String blogentry)
			throws ParserException;

	/**
	 * <a href="http://www.sixapart.com/movabletype/docs/mtimport">MT格式</a>
	 * 
	 * @param blogentry
	 * @param bpList
	 */
	protected void mtOut(String author, String blogentry,
			ArrayList<BlogPage> bpList) {
		mtOutPages(author, blogentry, bpList, "1");
	}

	protected void mtOutPages(String author, String blogentry,
			ArrayList<BlogPage> bpList, String page) {
		int size = bpList.size();
		int iPage = Integer.parseInt(page);
		int lastIndex = blogentry.lastIndexOf('/') + 1;
		String blogname = blogentry.substring(lastIndex, blogentry.length());
		String filename = String.format("%1$s\\%2$s%3$03d.txt", saveDirectory,
				blogname, iPage);
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(filename)),
					"UTF-8");
//			out.write("AUTHOR: ");
//			out.write(author);
//			out.write("\r\n");
//			out.write("TITLE: ");
//			out.write(blogtitle);
//			out.write("\r\n");
//			out.write("DATE: ");
//			out.write(getNowTime());
//			out.write("\r\n");
//			out.write("-----\r\n");

			for (int i = 0; i < size; i++) {
				if(i >0 )
				{
				}				
				BlogPage bp = bpList.get(i);
				out.write("TITLE: ");
				out.write(bp.title);
				out.write("\r\n");
				out.write("AUTHOR: ");
				out.write(author);
				out.write("\r\n");
				out.write("DATE: ");
				out.write(bp.datetime);
				out.write("\r\n");
				out.write("-----\r\n");
				out.write("BODY:");
				out.write("\r\n");
				out.write(bp.content);
				//out.write(EncodeHTML.encodeHTML(bp.title));
				out.write("\r\n");
				//out.write("-----\r\n");
				//再來是COMMENT:
//				out.write("\r\n");

				//out.write(EncodeHTML.encodeHTML(bp.content));
				out.write("-----\r\n");
				out.write("--------\r\n");
			}

			out.flush();
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	public void output(String outputtype, String author, String blogentry,
			ArrayList<BlogPage> bpList) {
		if (outputtype.toLowerCase().equals("RSS")) {
			rssOut(blogentry, bpList);
		}

		if (outputtype.toLowerCase().equals("HTML")) {
			htmlOut(blogentry, bpList);
		}

		if (outputtype.toLowerCase().equals("MT")) {
			mtOut(author, blogentry, bpList);
		}
	}

	protected void rssOut(String blogentry, ArrayList<BlogPage> bpList) {
		rssOutPages(blogentry, bpList, "1");
	}

	protected void htmlOut(String blogentry, ArrayList<BlogPage> bpList) {
		htmlOutPages(blogentry, bpList, "1");
	}

	public void outputPages(String outputtype, String author, String blogentry,
			ArrayList<BlogPage> bpList, String page) {
		if (outputtype.toLowerCase().equals("rss")) {
			rssOutPages(blogentry, bpList, page);
		}
		if (outputtype.toLowerCase().equals("html")) {
			htmlOutPages(blogentry, bpList, page);
		}
		if (outputtype.toLowerCase().equals("mt")) {
			mtOutPages(author, blogentry, bpList, page);
		}
	}

	protected void rssOutPages(String blogentry, ArrayList<BlogPage> bpList,
			String page) {
		int size = bpList.size();
		int iPage = Integer.parseInt(page);
		int lastIndex = blogentry.lastIndexOf('/') + 1;
		String blogname = blogentry.substring(lastIndex, blogentry.length());
		String filename = String.format("%1$s\\%2$s%3$03d.rss", saveDirectory,
				blogname, iPage);

		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(filename)),
					"UTF-8");
			out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
			out.write("<rss version=\"2.0\">\r\n");
			out.write("<channel>\r\n");
			out.write("\t<title>");
			out.write(blogtitle);
			out.write("</title>\r\n");
			out.write("\t<link>");
			out.write(blogentry);
			out.write("</link>\r\n");
			out.write("\t<description>");
			out.write(blogdescription);
			out.write("\t</description>\r\n");

			for (int i = 0; i < size; i++) {
				BlogPage bp = bpList.get(i);
				out.write("<item>\r\n");
				out.write("<title>");
				out.write(EncodeHTML.encodeHTML(bp.title));
				// out.write("<![CDATA[" + bp.title + "]]>");
				out.write("</title>\r\n");
				out.write("<pubDate>");
				out.write(bp.datetime);
				out.write("</pubDate>\r\n");
				out.write("<description>");
				out.write(EncodeHTML.encodeHTML(bp.content));
				// out.write("<![CDATA[" + bp.content + "]]>");
				out.write("</description>\r\n");
				out.write("</item>\r\n");
			}
			out.write("</channel>\r\n");
			out.write("</rss>\r\n");
			out.flush();
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	protected void htmlOutPages(String blogentry, ArrayList<BlogPage> bpList,
			String page) {
		int size = bpList.size();
		int iPage = Integer.parseInt(page);
		int lastIndex = blogentry.lastIndexOf('/') + 1;
		String blogname = blogentry.substring(lastIndex, blogentry.length());
		String filename = String.format("%1s\\%2$s%3$03d.html", saveDirectory,
				blogname, iPage);

		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(filename)),
					"UTF-8");
			out.write("<html>\r\n");
			out.write("<head>\r\n");
			out.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />");
			out.write("\t<title>");
			out.write(blogtitle);
			out.write("</title>\r\n");
			out.write("\t<link>");
			out.write(blogentry);
			out.write("</link>\r\n");
			out.write("\t<description>");
			out.write(blogdescription);
			out.write("\t</description>\r\n");
			out.write("</head>\r\n");
			out.write("<body>\r\n");

			for (int i = 0; i < size; i++) {
				BlogPage bp = bpList.get(i);
				out.write("<h1><a href=\""+bp.url+"\">"+bp.title+"</a></h1>\r\n");
				out.write(bp.content);
			}
			out.write("</body>\r\n");
			out.write("</html>\r\n");
			out.flush();
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	public String getBodyHtml(String url) throws ParserException {
		String bodyhtml;
		// System.out.println(url);
		Parser parser = new Parser(url);
		NodeList body = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof BodyTag;
			}
		});

		BodyTag bodyTag = (BodyTag) body.elementAt(0);
		bodyhtml = bodyTag.getChildrenHTML();
		return bodyhtml;
	}

	public String getDivContentHtml(String stringHtml, String classname)
			throws ParserException {
		String content = null;
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);

		NodeList divList = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof Div;
			}
		});

		for (int i = 0; i < divList.size(); i++) {
			String divHtml = divList.elementAt(i).getText();

			if (divHtml.contains("class=\"" + classname + "\"")) {
				content = divList.elementAt(i).toHtml();
				break;
			}
		}
		return content;
	}

	public String getDivContentText(String stringHtml, String classname)
			throws ParserException {
		String content = null;
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);

		NodeList divList = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof Div;
			}
		});

		for (int i = 0; i < divList.size(); i++) {
			String divHtml = divList.elementAt(i).getText();

			if (divHtml.contains("class=\"" + classname + "\"")) {
				content = ((Div) (divList.elementAt(i))).getStringText();
				break;
			}
		}
		return content.trim();
	}

	public String getTitle(String stringHtml) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);
		org.htmlparser.visitors.HtmlPage htmlPage = new org.htmlparser.visitors.HtmlPage(
				parser);
		return htmlPage.getTitle();
	}

	/**
	 * Get Span Content Text
	 * 
	 * @param stringHtml
	 * @param classname
	 * @return
	 * @throws ParserException
	 */
	public String getSpanContentText(String stringHtml, String classname)
			throws ParserException {
		String content = null;
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);

		NodeList spanList = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof Span;
			}
		});

		for (int i = 0; i < spanList.size(); i++) {
			String spanHtml = spanList.elementAt(i).getText();

			if (spanHtml.contains("class=\"" + classname + "\"")) {
				content = ((Span) (spanList.elementAt(i))).getStringText();
				break;
			}
		}
		return content.trim();
	}

	public String getTableContentHtml(String stringHtml, String classname)
			throws ParserException {
		String content = null;
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);

		NodeList tableList = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof TableTag;
			}
		});

		for (int i = 0; i < tableList.size(); i++) {
			String tableHtml = tableList.elementAt(i).getText();

			if (tableHtml.contains("class=\"" + classname + "\"")) {
				content = ((TableTag) (tableList.elementAt(i))).toHtml();
				break;
			}
		}
		return content;
	}

	public String getLinkContentText(String stringHtml) throws ParserException {
		String content = null;
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);

		NodeList linkList = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof LinkTag;
			}
		});
		content = ((LinkTag) (linkList.elementAt(0))).getStringText();
		return content.trim();
	}

	public String[] getImageURLs(String stringHtml) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);
		NodeList images = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof ImageTag;
			}
		});
		String[] urls = new String[images.size()];
		for (int i = 0; i < images.size(); i++) {
			ImageTag imageTag = (ImageTag) images.elementAt(i);
			urls[i] = imageTag.getImageURL();
		}
		return urls;
	}

	public String[] getLinkURLs(String stringHtml) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);
		NodeList links = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof LinkTag;
			}
		});
		String[] urls = new String[links.size()];
		for (int i = 0; i < links.size(); i++) {
			LinkTag linkTag = (LinkTag) links.elementAt(i);
			urls[i] = linkTag.getLink();
		}
		return urls;
	}

	public String[] getTableContents(String stringHtml) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(stringHtml);
		NodeList tables = parser.extractAllNodesThatMatch(new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				return node instanceof TableTag;
			}
		});
		String[] strTables = new String[tables.size()];
		for (int i = 0; i < tables.size(); i++) {
			TableTag tableTag = (TableTag) tables.elementAt(i);
			strTables[i] = tableTag.toHtml();
		}
		return strTables;
	}
}
