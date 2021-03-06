package com.dagf.uweyt3;


import com.dagf.uweyt3.xvideos.Crawler;
import com.dagf.uweyt3.xvideos.HttpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author papapa
 *
 */
public abstract class AbstractCrawler implements Crawler {

    private static final Logger log = LoggerFactory.getLogger(AbstractCrawler.class);

    @Override
    public void execute(String dir) throws IOException {
        if (StringUtils.isBlank(dir)) {
            throw new IOException("NO SE");
        }
        Map<String, String> categories = getCategories();
        if (categories != null) {
            for (Map.Entry<String, String> entry : categories.entrySet()) {
                String title = entry.getKey();
                String href = entry.getValue();
                List<String> viewUrls = getViewUrls(href);
                log.info("MAIN", title, viewUrls);
                List<String> videoUrls = getVideoUrls(viewUrls);
                log.info("MAIN", title, videoUrls);

                String downloadFileName = dir + File.separatorChar + "download_" + title + ".txt";
                FileUtils.writeLines(new File(downloadFileName), videoUrls, null);
                log.info("MAIN",title,downloadFileName);
            }
        }
    }

    public List<String> getVideoUrls(List<String> viewUrls) {
        List<String> videoUrls = new ArrayList<>();
        if (viewUrls != null && viewUrls.size() > 0) {
            for (String viewUrl : viewUrls) {
                videoUrls.add(getVideoUrl(viewUrl));
            }
        }
        return videoUrls;
    }

    /**
     * 下载视频
     *
     * @param videoUrl
     * @param dir
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void downloadVideo(String videoUrl, String dir) throws FileNotFoundException, IOException {
        String fileName = StringUtils.substringBefore(videoUrl, ".mp4?");
        fileName = StringUtils.substringAfterLast(fileName, "/");

        String filePath = dir + File.separatorChar + fileName + ".mp4";

        log.info("MAIN", videoUrl, filePath);
        HttpUtil.downloadDirect(videoUrl, new FileOutputStream(new File(filePath)));
    }

}
