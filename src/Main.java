import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static String ROOT_SITE = "https://skillbox.ru/";
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        LinkFetcher sitemapRoot = new LinkFetcher(ROOT_SITE);
        new ForkJoinPool().invoke(new LinkFetcherRecursive(sitemapRoot));

        FileOutputStream stream = new FileOutputStream("sitemap.txt");
        String result = createSitemapString(sitemapRoot, 0);
        stream.write(result.getBytes());
        stream.flush();
        stream.close();
        System.out.println((System.currentTimeMillis() - start)/1000 + " s.");
    }

    public static String createSitemapString(LinkFetcher node, int depth) {
        String tabs = String.join("", Collections.nCopies(depth, "\t"));
        StringBuilder result = new StringBuilder(tabs + node.getUrl());
        node.getChildren().forEach(child -> {
            result.append("\n").append(createSitemapString(child, depth + 1));
        });
        return result.toString();
    }
}