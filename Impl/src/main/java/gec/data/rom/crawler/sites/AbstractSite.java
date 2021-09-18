package gec.data.rom.crawler.sites;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public abstract class AbstractSite {
    private static final String CACHE_DB = "cacheDb";
    private static final Logger log = LoggerFactory.getLogger(AbstractSite.class);
    protected HTreeMap diskCache;

    protected void initCache(String cachePath) {
        DB dbDisk = DBMaker
                .fileDB(cachePath)
                .checksumHeaderBypass()
                .make();

        if (!dbDisk.exists(CACHE_DB)) {
            diskCache = dbDisk
                    .hashMap(CACHE_DB)
                    .create();
        } else {
            diskCache = dbDisk
                    .hashMap(CACHE_DB)
                    .open();
        }
        log.info("Cache loaded");
    }

    protected List<String> getHrefsForSite(String site, String cacheName) throws IOException, InterruptedException {
        if (diskCache == null) {
            log.error("Calling getHrefsForSite({}, {}) before calling createCache({})", site,
                    cacheName, cacheName);
            throw new RuntimeException("getHrefsForSite() called before used cache have been initiated");
        }

        if (diskCache.get(cacheName) == null) {
            log.info("No cache found for site, indexing...");

            List<String> masterList = new ArrayList<>();
            Queue<String> queue = new LinkedList<>();
            queue.add(site);

            while (!queue.isEmpty()) {
                log.info("Sites left to index: {}", queue.size());
                List<String> newHrefs = pullHrefsFromPage(site, queue.poll())
                        .filter(hrefValue -> !masterList.contains(hrefValue))
                        .collect(Collectors.toList());
                masterList.addAll(newHrefs);
                queue.addAll(newHrefs);
                // Give host some time between requests
                Thread.sleep(200);
            }
            diskCache.put(cacheName, masterList);
            diskCache.close();

            return masterList;
        }

        log.debug("Cached site, returning cache");
        List<String> hrefs = (List<String>) diskCache.get(cacheName);
        diskCache.close();
        return hrefs;
    }

    private Stream<String> pullHrefsFromPage(String originUrl, String url) throws IOException {
        log.debug("Caching site: {}", url);

        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        Page page = webClient.getPage(url);
        if (page instanceof HtmlPage) {
            return ((HtmlPage) page).getByXPath("//a[contains(@href,'http')]")
                    .stream()
                    .filter(o -> o instanceof HtmlAnchor)
                    .map(o -> ((HtmlAnchor) o))
                    .map(HtmlAnchor::getHrefAttribute)
                    .filter(hrefValue -> hrefValue.startsWith(originUrl))
                    .distinct();
        } else {
            return Stream.empty();
        }
    }
}
