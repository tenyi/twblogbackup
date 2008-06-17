package tenyi.util.html;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s="看到：<a href=\"http://www.vixual.net/wikka/wikka.php?wakka=Archive2005041201\">學程式設計的人不能不看的好文章</a><br />";
		String ret;
		ret = tenyi.util.html.EncodeHTML.encodeHTML(s);
		System.out.println(ret);
	}

}
