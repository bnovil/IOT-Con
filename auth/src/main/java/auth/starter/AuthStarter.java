package auth.starter;

import auth.AuthLogicConnection;
import auth.AuthServer;
import org.apache.commons.cli.*;
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

/**
 * Created by Qzy on 2016/1/28.
 */

public class AuthStarter {
    private static final Logger logger = LoggerFactory.getLogger(AuthStarter.class);
    private static File cfg = null;
    private static File log = null;
    public static RedisPoolManager _redisPoolManager;
    public static int workNum = 1;
    public static auth.Worker[] workers;

    public static void main(String[] args) throws Exception {

        String filePath = AuthStarter.class.getResource("../../auth.xml").getPath();
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

            xPathExpression = xPath.compile("/auth/authserver");
            nodeList = (NodeList) xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element) nodeList.item(0);
            int authListenPort = Integer.parseInt(element.getAttribute("port"));
            workNum = Integer.parseInt(element.getAttribute("workNum"));
            auth.Worker.startWorker(workNum);
            logger.info("Authserver authListenPort " + authListenPort);

            xPathExpression = xPath.compile("/auth/redis");
            nodeList = (NodeList) xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element) nodeList.item(0);
            _redisPoolManager = new RedisPoolManager();
            _redisPoolManager.REDIS_SERVER = element.getAttribute("ip");
            _redisPoolManager.REDIS_PORT = Integer.parseInt(element.getAttribute("port"));

            _redisPoolManager.returnJedis(_redisPoolManager.getJedis());
            logger.info("Redis init successed");

            xPathExpression = xPath.compile("/auth/logic");
            nodeList = (NodeList) xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element) nodeList.item(0);
            String logicIp = element.getAttribute("ip");
            int logicPort = Integer.parseInt(element.getAttribute("port"));

            //Now Start Servers
            new Thread(() -> AuthServer.startAuthServer(authListenPort)).start();

            new Thread(() -> AuthLogicConnection.startAuthLogicConnection(logicIp, logicPort)).start();

        } catch (Exception e) {
            logger.error("init cfg error");
            e.printStackTrace();
        }
    }

}
