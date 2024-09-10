package org.webcrawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static String URL = "https://unsplash.com/";

    static Map<String, String> data = new HashMap<>();

    public static void main(String[] args) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        crawl(1, URL, new ArrayList<>());

        try (FileWriter writer = new FileWriter("unsplash.json")) {
            // Write the map as a JSON file
            gson.toJson(data, writer);

            System.out.println("Successfully written to wikipedia.json");
        } catch (IOException e) {
            System.out.println("An error occurred while writing the JSON file.");
            e.printStackTrace();
        }

    }

    private static void crawl(int level, String url, ArrayList<String> visited)  {
        if(level <= 1) {

            Document doc = request(url, visited);
            data.put(doc.nodeName(), url);

            if(doc != null) {



                for (Element link : doc.select("img[src]")) {
                    String nextLink = link.absUrl("src");
                    if(!visited.contains(nextLink)) {
                        crawl(level++, nextLink, visited);
                    }
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> v) {
        try {
            Connection conn = Jsoup.connect(url);

            Document doc = conn.get();

            if(conn.response().statusCode() == 200) {
                System.out.println("Link    " + url);
                System.out.println(doc.nodeName());

                v.add(url);

                return doc;
            }
            return null;

        } catch (IOException e) {
            return null;
        }
    }

}