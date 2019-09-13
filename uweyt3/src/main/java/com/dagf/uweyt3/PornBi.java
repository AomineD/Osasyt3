package com.dagf.uweyt3;

import android.os.AsyncTask;
import android.util.Log;

import com.dagf.uweyt3.xvideos.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author papapa
 *
 */
public class PornBi extends AbstractCrawler {

    private static final Logger log = LoggerFactory.getLogger(PornBi.class);

    private static final String BASE_URL = "https://www.xvideos.com";

    /**
     * 获取视频高清地址
     *
     * @param viewUrl
     * @return
     * @throws IOException
     */
    @Override
    public String getVideoUrl(String viewUrl) {
        String videoUrl = null;
        Document doc;
        try {
            doc = HttpUtil.getDocument(viewUrl);
            if (doc == null) {
                return null;
            }
            Elements scripts = doc.getElementsByTag("script");
            if (scripts == null || scripts.size() == 0) {
                return null;
            }
            for (Element script : scripts) {
                String scriptStr = script.html();
                if (StringUtils.contains(scriptStr, "html5player.setVideoUrlHigh('")) {
                    videoUrl = StringUtils.substringBetween(scriptStr, "html5player.setVideoUrlHigh('", "');");
                    break;
                }
            }
        } catch (IOException e) {
            log.error("MAIN " + viewUrl + "]", e);
        }

        return videoUrl;
    }

    /**
     * 获取该分类地址下的所有播放地址
     *
     * @param href
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getViewUrls(String href) {
        List<String> viewUrls = new ArrayList<>();
        Document doc;
        try {
            doc = HttpUtil.getDocument(href);
            if (doc == null) {
                return null;
            }
            List<String> pageUrls = getPageUrls(doc);
            if (pageUrls != null) {
                viewUrls.addAll(pageUrls);
            }

        //    Log.e("MAIN", "getViewUrls: "+doc.html());
            Element nextPageElement = doc.select("div.pagination > ul > li > a.next-page").first();
            //Log.e("MAIN", "getViewUrls: "+nextPageElement.attr("href"));
            if (nextPageElement != null) {
                String nextPageHref = BASE_URL + nextPageElement.attr("href");// 下一页地址
                if (nextPageHref.contains("/1") || nextPageHref.contains("p=1")) {// 为了测试只获取2页数据
                    return viewUrls;
                }
                viewUrls.addAll(getViewUrls(nextPageHref));// 递规获取每一页中的播放地址
            }
        } catch (IOException e) {
            log.error("MAIN" + href + "]", e);
            Log.e("MAIN", "getViewUrls: "+e.getMessage());
        }

        return viewUrls;
    }

    /**
     * 获取当前页中每个的播放地址
     *
     * @param doc
     * @return
     */
    public List<String> getPageUrls(Document doc) {
        //Elements links = doc.select("div#content > div.mozaique > div div.thumb > a");
        Elements insides = doc.select("div#content > div.mozaique > div div.thumb-inside");
   Element incubo = doc.select("div.thumb-under").first();



        if (insides == null || insides.size() == 0) {
            return null;
        }

      /*  int maxLogSize = 1000;
        for(int i = 0; i <= doc.html().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > doc.html().length() ? doc.html().length() : end;
           // Log.e("MAIN", doc.html().substring(start, end));
        }*/


        List<String> urls = new ArrayList<>();
        for (Element inside : insides) {
            Element hdElement = inside.select("span.video-hd-mark").first();//获取高清(720p)视频
            if (hdElement != null) {
                Element linkElement = inside.select("div.thumb > a").first();
                String link = linkElement.attr("href");
              //  Log.e("MAIN", "getPageUrls: "+inside.html() );
                urls.add(BASE_URL + link);
            }
        }
        return urls;
    }

    @Override
    public Map<String, String> getCategories() {
        Map<String, String> categories = new HashMap<>();
        //  categories.put("丝袜",BASE_URL+"/c/Stockings-28");
        //    categories.put("喷水",BASE_URL+"/c/Squirting-56");
        categories.put("Lesbian", BASE_URL + "/?k=lesbian");

        return categories;
    }

public static void getXvideo(final String urlto, loadInterfaceXvi loadInterfaceXvi){
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            try {
             String url = new PornBi().getVideoUrl(urlto);

             loadInterfaceXvi.onLoadUrl(url);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MAIN", "run: "+e.getMessage() );
                loadInterfaceXvi.onLoadUrl("error");
            }
        }
    });

}


public interface loadInterfaceXvi{
        void onLoadUrl(String ur);
}

}
