package ai.reclaim.interview.job;

import ai.reclaim.interview.Utils;
import com.microsoft.playwright.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.microsoft.playwright.Page.ScreenshotOptions;

public class UrlReconJob implements Job {

    protected final String userId;
    protected final String url;
    private final String jobName;
    private String slug = null;

    private Path requestAuditFilePath = null;

    public UrlReconJob(String userId, String url) {
        this.userId = userId;
        this.url = url;
        this.jobName = this.getClass().getSimpleName();
        if (url != null) {
            this.slug = Utils.slugify(url);
            this.requestAuditFilePath = Paths.get(this.userId, this.slug, "audit.txt");
        }
    }

    public String getJobName() {
        return this.jobName;
    }

    public String getSlug() {
        return this.slug;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public Optional<List<Job>> execute() {
        Path harPath = Paths.get(this.userId, this.getSlug(), "audit.har");
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setRecordHarPath(harPath);
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch();
             BrowserContext context = browser.newContext(contextOptions);
        ) {
            Page page = context.newPage();

            page.onDOMContentLoaded(this::handleOnDOMContentLoaded);
            Response resp = page.navigate(url);

            if (resp.ok()) {
                saveScreenshot(page);
                return Optional.of(List.of(new DownloadJavascriptJob(this.userId, this.url)));
            } else {
                System.out.println("Error: " + resp.status() + " " + resp.statusText() + ", url: " + this.url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private void saveScreenshot(Page page) {
        Path path = Paths.get(this.userId, this.getSlug(), "screenshot.png");
        ScreenshotOptions screenshotOptions = new ScreenshotOptions()
                .setPath(path)
                .setFullPage(true);
        page.screenshot(screenshotOptions);
    }

    private void handleOnDOMContentLoaded(Page page) {
        String html = page.content();
        Path filePath = Paths.get(this.userId, this.getSlug(), "index.html");
        try {
            Utils.saveToFile(html, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getJobName() + "{" + "userId='" + this.getUserId() + '\'' + ", url='" + this.getUrl() + '\'' + ", slug='" + this.getSlug() + '\'' + '}';
    }
}
