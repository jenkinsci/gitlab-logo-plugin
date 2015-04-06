package org.jenkinsci.plugins.gitlablogo.api

import co.freeside.betamax.Betamax
import co.freeside.betamax.MatchRule
import co.freeside.betamax.Recorder
import co.freeside.betamax.TapeMode
import co.freeside.betamax.tape.yaml.OrderedPropertyComparator
import co.freeside.betamax.tape.yaml.TapePropertyUtils
import org.junit.Rule
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.yaml.snakeyaml.introspector.Property
import spock.lang.Ignore
import spock.lang.Specification

@RunWith(Enclosed)
class GitlabApiSpec {
  static class getProject extends Specification {
    @Rule
    Recorder recorder = new Recorder()

    def setup(){
      // NOTE: https://github.com/robfletcher/betamax/issues/141
      TapePropertyUtils.metaClass.sort = {Set<Property> properties, List<String> names ->
        new LinkedHashSet(properties.sort(true, new OrderedPropertyComparator(names)))
      }
    }

    @Betamax(tape="getSingleProject", mode = TapeMode.READ_ONLY, match = [MatchRule.host, MatchRule.path])
    def "should return project"(){
      when:
      String endpointUrl = "https://gitlab.com/api/v3"
      String privateToken = "xxxxxxxxxxxxx"
      GitlabApi api = new GitlabApi(endpointUrl, privateToken)
      api.setProxyHost("localhost", recorder.getProxyPort())

      then:
      api.getProject("sue445/example").avatarUrl == "https://gitlab.com/uploads/project/avatar/219579/image.jpg"
    }

    @Ignore
    @Betamax(tape="getSingleProject", mode = TapeMode.WRITE_ONLY, match = [MatchRule.host, MatchRule.path])
    def "record gitlab response to yaml"(){
      when:
      String endpointUrl = "https://gitlab.com/api/v3"
      String privateToken = "INPUT_YOUR_TOKEN"
      GitlabApi api = new GitlabApi(endpointUrl, privateToken)
      api.setProxyHost("localhost", recorder.getProxyPort())

      then:
      api.getProject("sue445/example")
    }
  }
}
