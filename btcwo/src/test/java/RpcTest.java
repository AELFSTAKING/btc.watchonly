import io.seg.kofo.bitcoinwo.BtcWoApplication;
import io.seg.kofo.bitcoinwo.common.util.BitcoinRpcURLWrapper;
import com.alibaba.fastjson.JSON;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = BtcWoApplication.class)
public class RpcTest {
    volatile List<Integer> integerList;

    @Test
    public void usdtRpcTest() throws MalformedURLException {
        String user= "xxx";
        String pwd = "xxx";
        String host = "xxx";
        String port = "28332";
        BitcoinJSONRPCClient bitcoin = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(user,pwd,host,port);
        String address = "xxx";
        String propertyId = "1111";
        try {
           Bitcoin.OmniBalance balance = bitcoin.omniGetBalance(address, Long.parseLong(propertyId));
            System.out.println(JSON.toJSONString(balance.balance()));
        } catch (BitcoinException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        List<? super String> list = new ArrayList<>();
        list.add("vdsgfdgdga");
        List temp = list;
        temp.add(new HashMap<String, Object>(){{put("aa", "bbb");}});
        System.out.println(temp);
        integerList = temp;
        System.out.println(integerList.get(1) instanceof Integer);
    }

    @Test
    public void test3() throws MalformedURLException {
        String user= "xxx";
        String pwd = "xxx";
        String host = "xxx";
        String port = "18332";
        BitcoinJSONRPCClient bitcoin = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(user,pwd,host,port);
        String address = "xxxx";
        String propertyId = "1111";
        try {
            String result = bitcoin.getRawTransactionHex("xxx");
            System.out.println(JSON.toJSONString(result));
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
    }
}
