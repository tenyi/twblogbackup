package tenyi.util.html;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class EncodeHTML {
	//	public static String encodeHTML(String s)
	//	{
	//		if(s == null) {return null;}
	//	    StringBuffer out = new StringBuffer();
	//	    for(int i=0; i<s.length(); i++)
	//	    {
	//	        char c = s.charAt(i);
	//	        if(c > 127 || c=='"' || c=='<' || c=='>')
	//	        {
	//	           out.append("&#"+(int)c+";");
	//	        }
	//	        else
	//	        {
	//	            out.append(c);
	//	        }
	//	    }
	//	    return out.toString();
	//	}

	public static String encodeHTML(String source) {
		final StringBuffer result = new StringBuffer();

		final StringCharacterIterator iterator = new StringCharacterIterator(
				source);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '&') {
				result.append("&amp;");
//			} else if (character == '\"') {
//				result.append("&quot;");
//			} else if (character == '\'') {
//				result.append("&#039;");
//			} else if (character == '\\') {
//				result.append("&#092;");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
}
