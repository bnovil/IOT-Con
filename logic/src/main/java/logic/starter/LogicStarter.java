package logic.starter;


import logic.LogicServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import thirdparty.redis.utils.RedisPoolManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class LogicStarter {
    private static final Logger logger = LoggerFactory.getLogger(LogicStarter.class);
    private static File cfg = null;
    private static File log = null;
    public static RedisPoolManager _redisPoolManager;
    public static int workNum = 1;

    public static void main(String[] args) throws Exception {
        String filePath = LogicStarter.class.getResource("../../logic.xml").getPath();
        cfg = new File(filePath);

        try {
            //parse xml File and apply it
            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            Document doc = builder.parse(cfg);
            Element rootElement = doc.getDocumentElement();

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = null;
            NodeList nodeList = null;
            Element element = null;


            xPathExpression  = xPath.compile("/logic/logicserver");
            nodeList = (NodeList)xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element)nodeList.item(0);
            int logicListenPort = Integer.parseInt(element.getAttribute("port"));
            logger.info("Logicserver logicListenPort " + logicListenPort);
            workNum = Integer.parseInt(element.getAttribute("workNum"));
            logic.Worker.startWorker(workNum);

            xPathExpression  = xPath.compile("/logic/redis");
            nodeList = (NodeList)xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element)nodeList.item(0);
            _redisPoolManager = new RedisPoolManager();
            _redisPoolManager.REDIS_SERVER = element.getAttribute("ip");
            _redisPoolManager.REDIS_PORT = Integer.parseInt(element.getAttribute("port"));

            _redisPoolManager.returnJedis(_redisPoolManager.getJedis());
            logger.info("Redis init successed");

            //Now Start Servers
            new Thread(() -> LogicServer.startLogicServer(logicListenPort)).start();

        } catch (Exception e) {
            logger.error("init cfg error");
            e.printStackTrace();
        }

    }
}
