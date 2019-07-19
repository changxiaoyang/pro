package com.cxy890.elasticsearch.client;

import com.cxy890.config.annotation.AutoScan;
import com.cxy890.config.annotation.Level;
import com.cxy890.config.annotation.Stuff;
import com.cxy890.config.annotation.Value;
import com.cxy890.config.util.Strings;
import com.cxy890.elasticsearch.annotations.EsIndex;
import com.cxy890.server.runner.Runner;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Changxiaoyang
 */
@Slf4j
@AutoScan(level = Level.CONFIGURATION)
public class Clients implements Runner {

    private final String clusterName;

    private final List<String> clusterNodes;

    private final Boolean clientSniff;

    @Getter
    private TransportClient transportClient;

    public Clients(@Value("elasticsearch.cluster.name") String clusterName,
                   @Value("elasticsearch.cluster.nodes") List<String> clusterNodes,
                   @Value("elasticsearch.client.transport.sniff") Boolean clientSniff) {
        this.clientSniff = clientSniff;
        this.clusterNodes = clusterNodes;
        this.clusterName = clusterName;

    }

    @Stuff
    public TransportClient transportClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", clientSniff).build();
        TransportClient transportClient = new PreBuiltTransportClient(settings);
        for (String node : clusterNodes) {
            String[] n = node.split(":");
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(n[0]), Integer.valueOf(n[1])));
        }

        this.transportClient = transportClient;
        return transportClient;
    }

    public IndicesAdminClient adminClient() {
        return transportClient.admin().indices();
    }

    public void create(EsIndex esIndex, String source) {
        if (!exist(esIndex.value())) {
            adminClient().prepareCreate(esIndex.value())
                    .setSettings(Settings.builder()
                            .put("index.number_of_shards", esIndex.shards())
                            .put("index.number_of_replicas", esIndex.replicas())
                    )
                    .addMapping(Strings.isNull(esIndex.type()) ? esIndex.value() : esIndex.type(),
                            source, XContentType.JSON)
                    .get();
        }
    }

    public boolean exist(String index) {
        IndicesExistsResponse actionFuture = adminClient()
                .exists(new IndicesExistsRequest(index)).actionGet(1000);
        return actionFuture.isExists();
    }

    @Override
    public void run() {
        ClusterAdminClient cluster = transportClient.admin().cluster();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()-> cluster.health(new ClusterHealthRequest(), new ActionListener<ClusterHealthResponse>() {
            @Override
            public void onResponse(ClusterHealthResponse clusterHealthResponse) {
                log.debug(clusterHealthResponse.getClusterName() + " health " + clusterHealthResponse.getStatus().toString());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Elasticsearch health check error : ", e);
            }
        }), 0L, 30L, TimeUnit.SECONDS);
    }
}
