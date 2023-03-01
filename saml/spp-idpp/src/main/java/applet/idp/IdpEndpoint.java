package applet.idp;

import applet.sp.SpConfig;
import applet.utils.OpenSAMLUtils;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.saml.common.messaging.context.SAMLBindingContext;
import org.opensaml.saml.common.messaging.context.SAMLEndpointContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPPostEncoder;
import org.opensaml.saml.saml2.core.ArtifactResponse;
import org.opensaml.saml.saml2.metadata.EmailAddress;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname IdpEndpoint
 * @Description TODO idp提供商的端点
 * @Date 2022/7/25 13:01
 * @Created by CoffeeAndIce
 */
@Controller
@RequestMapping("/idp")
public class IdpEndpoint {
    @Autowired
    private IdpConfig idpConfig;
    @Autowired
    private SpConfig spConfig;

    @RequestMapping("/logon")
    public void authorized(HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        String samlRequest = request.getParameter("SAMLRequest");
        String SigAlg = request.getParameter("SigAlg");
        String Signature = request.getParameter("Signature");

        MessageContext context = new MessageContext();
        //关于传输对端实体的信息，对于IDP就是SP，对于SP就是IDP；
        SAMLPeerEntityContext peerEntityContext =
                context.getSubcontext(SAMLPeerEntityContext.class, true);
        //端点信息；
        SAMLEndpointContext endpointContext =
                peerEntityContext.getSubcontext(SAMLEndpointContext.class, true);
        endpointContext.setEndpoint(getIPDEndpoint());
        peerEntityContext.setEntityId(idpConfig.idp_entity_id);
        //数据签名环境上线文
        SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
        //获得证书，其中包含公钥
        signatureSigningParameters.setSigningCredential(IdpCredentials.getCredential());
        //ALGO_ID_SIGNATURE_RSA_SHA256
        signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);


        HTTPPostEncoder httpPostEncoder = new HTTPPostEncoder();
        httpPostEncoder.setMessageContext(context);
        httpPostEncoder.setHttpServletResponse(response);
        EmailAddress emailAddress = OpenSAMLUtils.buildSAMLObject(EmailAddress.class);
        emailAddress.setAddress("demo@outlook.com");

        SAMLBindingContext baseContexts = new SAMLBindingContext();
        //用于给判断的时间而已
        baseContexts.setRelayState("60");
        baseContexts.setHasBindingSignature(true);
        baseContexts.setAutoCreateSubcontexts(true);
        baseContexts.setBindingUri(idpConfig.idp_sso_logon);
        context.addSubcontext(baseContexts);

        ArtifactResponse artifactResponse = OpenSAMLUtils.buildSAMLObject(ArtifactResponse.class);
        artifactResponse.setMessage(emailAddress);
//        artifactResponse.getOrderedChildren()
        context.setMessage(artifactResponse);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        httpPostEncoder.setVelocityEngine(velocityEngine);
        try {
            httpPostEncoder.initialize();
        } catch (ComponentInitializationException e) {
            e.printStackTrace();
        }
        try {
            //*encode*方法将会压缩消息，生成签名，添加结果到URL并从定向用户到Idp.
            //先使用RFC1951作为默认方法压缩数据，在对压缩后的数据信息Base64编码
            httpPostEncoder.encode();
        } catch (MessageEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Endpoint getIPDEndpoint() {
        SingleSignOnService endpoint = OpenSAMLUtils.buildSAMLObject(SingleSignOnService.class);
        endpoint.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        endpoint.setLocation(spConfig.sp_consumer);

        return endpoint;
    }
}
