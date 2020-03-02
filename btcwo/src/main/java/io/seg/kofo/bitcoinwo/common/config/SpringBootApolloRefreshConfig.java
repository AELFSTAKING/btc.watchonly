package io.seg.kofo.bitcoinwo.common.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;


/**
 *
 * @author  gin
 */
@Component
@Slf4j
public class SpringBootApolloRefreshConfig {
    @Autowired
    private WatchOnlyProperties watchOnlyProperties;

    @Autowired
    private RefreshScope refreshScope;
    @Autowired
    private FullNodeCache fullNodeCache;

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean personCacheKeysChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("watch-only.")) {
                personCacheKeysChanged = true;
                break;
            }
        }
        if (!personCacheKeysChanged) {
            return;
        }


        log.info("before refresh {}", watchOnlyProperties.toString());
        refreshScope.refresh("watchOnlyProperties");
        //刷新缓存
        fullNodeCache.getFullNodeCache().invalidateAll();
        log.info("after refresh {}", watchOnlyProperties.toString());
    }
}
