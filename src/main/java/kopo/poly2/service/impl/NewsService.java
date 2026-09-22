package kopo.poly2.service.impl;

import kopo.poly2.dto.NewsDTO;
import kopo.poly2.service.iNewsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class NewsService implements iNewsService {

    @Override
    public NewsDTO getNews() throws Exception {

        String url = "https://n.news.naver.com/mnews/article/020/0003749524";

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(5000)
                .get();

        NewsDTO dto = new NewsDTO();

        Element titleElement = doc.selectFirst(".media_end_head_headline");
        Element storyElement = doc.selectFirst("#dic_area");

        if (titleElement != null) {
            dto.setNewsTitle(titleElement.text());
        }

        if (storyElement != null) {
            storyElement.select("script, style, button").remove();
            dto.setNewsStory(storyElement.text());
        }

        return dto;
    }
}
