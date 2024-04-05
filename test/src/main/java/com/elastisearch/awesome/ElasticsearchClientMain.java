package com.elastisearch.awesome;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;

/**
 * @author xuandu.wj
 * @date 2024/4/5
 **/
public class ElasticsearchClientMain {


    public static void  main(String[] args) throws Exception{
            String host="localhost";
            int port=9200;
            String login ="elastic";
            String password = "J+LIHmgkio0peilHA2EI";
            String fingerprint = "93b0d04c39da1eba7ae0164968ed0e4ee084f4a2fb627f61594054eb7412db83";
            SSLContext sslContext = TransportUtils
                    .sslContextFromCaFingerprint(fingerprint);

            BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
            credsProv.setCredentials(
                    AuthScope.ANY, new UsernamePasswordCredentials(login, password)
            );

            RestClient restClient = RestClient
                    .builder(new HttpHost(host, port, "https"))
                    .setHttpClientConfigCallback(hc -> hc
                            .setSSLContext(sslContext)
                            .setDefaultCredentialsProvider(credsProv)
                    )
                    .build();

    // Create the transport and the API client
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient esClient = new ElasticsearchClient(transport);
            CreateIndexResponse res = esClient.indices().create(c -> c.index("products"));
            System.out.println(res.toString());
        }


}
