package org.shipkit.internal.gradle.git

import org.gradle.api.Action
import testutil.PluginSpecification

class GitAuthPluginTest extends PluginSpecification {

    def "should provide Git auth information"() {
        given:
        conf.gitHub.writeAuthToken = "token"
        conf.gitHub.writeAuthUser = "user"
        conf.gitHub.repository = "shipkit/example"
        def t = project.tasks.create("foo")

        when:
        GitAuthPlugin.GitAuth auth = null
        project.plugins.apply(GitAuthPlugin).provideAuthTo(t, new Action<GitAuthPlugin.GitAuth>() {
            @Override
            void execute(GitAuthPlugin.GitAuth gitAuth) {
                auth = gitAuth
            }
        })
        project.tasks[GitAuthPlugin.IDENTIFY_GIT_ORIGIN_TASK].execute()

        then:
        auth.secretValue == "token"
        auth.repositoryUrl == "https://user:token@github.com/shipkit/example.git"
        auth.repositoryName == "shipkit/example"
    }
}