package tenyi.util.html;
import java.net.URL;
import java.util.Vector;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * 
 * @author 王建興qing 
 * 十月 11 2006, 01:44:33 
 * modified by 鳥毅 2006/10/13
 */
public class RollerReader
{
	public static void main(String argv[])
	{
		try
		{
			if (argv.length < 4)
			{
				System.out.println("usages: <blogid> <username> <password> <numberOfPosts>");
				System.exit(0);
			}
			//       
			Vector <Object>params = new Vector<Object>();
			params.addElement(argv[0]);
			params.addElement(argv[1]);
			params.addElement(argv[2]);
			params.addElement(new Integer(Integer.parseInt(argv[3])));

			//

			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

			config.setServerURL(new URL(
					//"http://www.javaworld.com.tw/roller/xmlrpc"
					"http://www.blogger.com/feeds/10940209/posts/full"
					));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			//

			Object result[] = (Object[]) client.execute(
					"metaWeblog.getRecentPosts", params);
			System.out.println("Total: " + result.length + " Posts");

			for (Object element : result) {
				System.out.println(element);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
