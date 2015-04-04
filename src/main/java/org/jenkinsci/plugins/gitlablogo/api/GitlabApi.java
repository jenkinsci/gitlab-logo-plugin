package org.jenkinsci.plugins.gitlablogo.api;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GitlabApi {
  private final String endpoint;
  private final String privateToken;
  private final HttpClient httpClient = new HttpClient();

  public GitlabApi(String endpoint, String privateToken){
    this.endpoint     = endpoint;
    this.privateToken = privateToken;
  }

  public Project getProject(String repositoryName) throws IOException {
    String url = endpoint + "/projects/" + urlEncode(repositoryName);
    String json = getContent(url);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, Project.class);
  }

  private String urlEncode(String str) throws UnsupportedEncodingException {
    return URLEncoder.encode(str, "UTF-8");
  }

  private String getContent(String url) throws IOException {
    HttpMethod method = new GetMethod(url);
    method.addRequestHeader("PRIVATE-TOKEN", privateToken);

    int statusCode = httpClient.executeMethod(method);

    if (statusCode != HttpStatus.SC_OK) {
      throw new RuntimeException("statusCode is " + statusCode);
    }

    try {
      byte[] responseBody = method.getResponseBody();
      return new String(responseBody);

    } finally {
      method.releaseConnection();
    }
  }

  public void setProxyHost(String hostname, int port){
    httpClient.getHostConfiguration().setProxyHost(new ProxyHost(hostname, port));
  }
}
