package com.digitalsanctum.recon.job;

import com.digitalsanctum.recon.Utils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DownloadJavascriptJob implements Job {

    protected final String userId;
    protected final String url;
    private String slug = null;
    private final String jobName;

    public DownloadJavascriptJob(String userId, String url) {
        this.userId = userId;
        this.url = url;
        this.jobName = this.getClass().getSimpleName();
        if (url != null) {
            this.slug = Utils.slugify(url);
        }
    }

    public String getJobName() {
        return this.jobName;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    public String getSlug() {
        return this.slug;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Optional<List<Job>> execute() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch()) {
            Page page = browser.newPage();
            page.onResponse(this::downloadJs);
            page.navigate(this.url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void downloadJs(Response response) {
        Map<String, String> headers = response.headers();
        if (headers.containsKey("content-type") && headers.get("content-type").contains("application/javascript")) {
            String content = response.text();
            Optional<String> maybe_filename = Utils.extractFileNameFromURL(response.url());
            if (maybe_filename.isPresent()) {
                Path filePath = Paths.get(this.userId, this.getSlug(), "js", maybe_filename.get());
                try {
                    Utils.saveToFile(content, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return getJobName() + "{" + "userId='" + this.getUserId() + '\'' + ", url='" + this.getUrl() + '\'' + ", slug='" + this.getSlug() + '\'' + '}';
    }
}
