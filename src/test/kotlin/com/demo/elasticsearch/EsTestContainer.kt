package com.demo.elasticsearch

import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

class EsTestContainer : ElasticsearchContainer(DockerImageName.parse("elasticsearch:6.4.3")
    .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch")) {
  init {
    this.addFixedExposedPort(9300, 9300)
    this.addEnv( "cluster.name", "elasticsearch")
  }
}
