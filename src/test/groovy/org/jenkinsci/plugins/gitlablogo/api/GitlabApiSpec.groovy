package org.jenkinsci.plugins.gitlablogo.api

import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import spock.lang.Specification

@RunWith(Enclosed)
class GitlabApiSpec {
  static class getProject extends Specification {
    static class StubGitlabApi extends GitlabApi{
      StubGitlabApi(String endpointUrl, String privateToken) {
        super(endpointUrl, privateToken)
      }

      @Override
      String getContent(String url) {
        if (url == "https://gitlab.com/api/v3/projects/sue445%2Fexample") {
          new File("src/test/resources/getSingleProject.json").text
        } else {
          throw "${url} is invalid"
        }
      }
    }

    def "should return project"(){
      when:
      String endpointUrl = "https://gitlab.com/api/v3"
      String privateToken = "xxxxxxxxxxxxx"
      GitlabApi api = new StubGitlabApi(endpointUrl, privateToken)

      then:
      api.getProject("sue445/example").avatarUrl == "https://gitlab.com/uploads/project/avatar/219579/image.jpg"
    }
  }
}
