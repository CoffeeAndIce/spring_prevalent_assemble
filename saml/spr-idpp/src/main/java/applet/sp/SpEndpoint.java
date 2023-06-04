package applet.sp;

import applet.idp.IdpConfig;
import applet.utils.OpenSAMLUtils;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import org.apache.commons.lang.StringUtils;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.decoder.MessageDecodingException;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.saml2.binding.decoding.impl.HTTPPostDecoder;
import org.opensaml.saml.saml2.core.Artifact;
import org.opensaml.saml.saml2.metadata.EmailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * @Classname SpEndpoint
 * @Description TODO  服务提供商的端点
 * @Date 2022/7/25 13:01
 * @Created by CoffeeAndIce
 */
@Controller
@RequestMapping("/sp")
public class SpEndpoint {
    private static Logger logger = LoggerFactory.getLogger(SpEndpoint.class);
    @Autowired
    private IdpConfig idpConfig;
    @Autowired
    private SpConfig spConfig;

    @GetMapping("/dest")
    @ResponseBody
    public String authorized() {
        return "you are Authorized";
    }

    @RequestMapping("/consumer")
    @ResponseBody
    public String consumer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("Artifact received");
        if (StringUtils.isNotEmpty(req.getParameter("SAMLResponse"))) {
            buildArtifactFromRequest(req);
        }
        return "you are consumer";
    }

    private Artifact buildArtifactFromRequest(HttpServletRequest req) {
        try {

            String samlResponse = req.getParameter("SAMLResponse");
            if (StringUtils.isNotEmpty(samlResponse)) {
                byte[] decodedBytes = Base64.getDecoder().decode(samlResponse);
                samlResponse = new String(decodedBytes, "utf-8");
                logger.info("ArtifactResponse received");
                logger.info("ArtifactResponse: {}", samlResponse);
            }
        } catch (IOException e) {
            logger.info("解析异常", e);
        }

        HTTPPostDecoder httpPostDecoder = new HTTPPostDecoder();
        httpPostDecoder.setHttpServletRequest(req);
        try {
            httpPostDecoder.initialize();
        } catch (ComponentInitializationException e) {
            e.printStackTrace();
        }
        try {
            httpPostDecoder.decode();
        } catch (MessageDecodingException e) {
            e.printStackTrace();
        }
        final MessageContext<SAMLObject> messageContext = httpPostDecoder.getMessageContext();
        System.out.println(messageContext);
        List<XMLObject> encryptedAssert = messageContext.getMessage().getOrderedChildren();
        for (int i = 0, len = encryptedAssert.size(); i < len; i++) {
            XMLObject xmlObject = encryptedAssert.get(i);
            if (xmlObject instanceof EmailAddress) {
                EmailAddress emailAddress = ((EmailAddress) xmlObject);
                logger.info("内容:{}", emailAddress.getAddress());
                //附带的其他内容
//                final List<XMLObject> orderedChildren = emailAddress.getOrderedChildren();
//                for (int j = 0, leng = encryptedAssert.size(); j < len; j++) {
//                    XMLObject xmlObject1 = orderedChildren.get(i);
//                }
            }
        }
        Artifact artifact = OpenSAMLUtils.buildSAMLObject(Artifact.class);
        artifact.setArtifact(req.getParameter("SAMLResponse"));
        System.out.println(req.getAttribute("SAMLResponse"));
        System.out.println(req.getParameter("SAMLResponse"));
        req.getSession().setAttribute(spConfig.AUTHENTICATED_SESSION_ATTRIBUTE,true);
        return artifact;
    }

}
