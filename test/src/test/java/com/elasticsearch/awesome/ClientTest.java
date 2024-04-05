package com.elasticsearch.awesome;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.elastisearch.awesome.model.Product;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.SSLContext;

/**
 * @author xuandu.wj
 * @date 2024/4/6
 **/


public class ClientTest {
    public ElasticsearchClient esClient() {
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
        return new ElasticsearchClient(transport);
    }
    @Test
    public void testIndex() throws Exception {
        ElasticsearchClient esClient = esClient();
        Product product = new Product("bk-1", "City bike", 123.0);
        IndexRequest.Builder<Product> indexReqBuilder = new IndexRequest.Builder<>();
        indexReqBuilder.index("product");
        indexReqBuilder.id(product.getSku());
        indexReqBuilder.document(product);
        IndexResponse response = esClient.index(indexReqBuilder.build());
        Assert.assertTrue(Result.Created == response.result() || Result.Updated == response.result());
    }

    @Test
    public void testReadIndex() throws Exception {
        ElasticsearchClient esClient = esClient();
        GetResponse<Product> response = esClient.get(g -> g
                        .index("products")
                        .id("bk-1"), Product.class
        );
        Assert.assertTrue(response.found());
    }
}
