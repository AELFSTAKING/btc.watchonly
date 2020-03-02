package io.seg.kofo.bitcoinwo.common.util;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class BitcoinRpcURLWrapper {


    private URL rpcURL;

    private BitcoinRpcURLWrapper() {
    }

    public static URL rpcURL(String rpcUser, String rpcPassword, String host, String port) throws MalformedURLException {
        return new URL("http://" + rpcUser + ':' + rpcPassword + "@" + host + ":" + (port == null ? "18332" : port) + "/");
    }

    public static BitcoinJSONRPCClient bitcoinJSONRPCClient(String rpcUser, String rpcPassword, String host, String port) throws MalformedURLException {
        URL rpc = new URL("http://" + rpcUser + ':' + rpcPassword + "@" + host + ":" + (port == null ? "18332" : port) + "/");
        return new BitcoinJSONRPCClient(rpc);
    }

    public static BitcoinJSONRPCClient bitcoinJSONRPCClient(String rpcUser, String rpcPassword, String nodeUrlString) throws MalformedURLException {
        URL rpc = new URL("http://" + rpcUser + ':' + rpcPassword + "@" + nodeUrlString + "/");
        return new BitcoinJSONRPCClient(rpc);
    }
}
