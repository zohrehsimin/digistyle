package com.mycompany.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {


        try {
            PrintStream originalOut = System.out;
            PrintStream fileOut = new PrintStream("/Users/zohreh/my-app/src/main/java/reference/digiStyle.json");
            System.setOut(fileOut);

            List<String> allFishLinks = getAllLinks("/Users/zohreh/my-app/src/main/java/reference/mahi.xml");
            for (String link : allFishLinks) {
                Document doc = Jsoup.connect(link).get();
                Elements summaries = doc.select(".summary");
                for (Element summary : summaries) {
                    String item = summary.getElementsByTag("h1").text().trim();
                    String price = summary.getElementsByTag("bdi").text().replace(",", "").replace("تومان", "").trim();

                    originalOut.println(item + "\t" + price);
//                    System.out.println(item + "\t" + price);
                }
            }


            Product digiStyle = new Product();
            List<String> allLinks = getAllLinks("/Users/zohreh/my-app/src/main/java/reference/digi.xml");
            int limit = 0;
            for (String link : allLinks) {
                {
                    if (limit < 100) {
                        Document doc = Jsoup.connect(link).get();
                        Elements products = doc.select(".c-main");
                        for (Element product : products) {

                            digiStyle.productOriginalPrice = product.select(".c-price-container--quick-view-price-original").attr("data-price-value").trim();
                            digiStyle.productName = product.select(".c-product__details-subtitle").text().replace("لیست فروشندگان این محصول", "").trim();
                            digiStyle.productDiscountAmount = product.select(".c-price-container--quick-view-price-discount").attr("data-price-value").trim();
                            digiStyle.productFinalPrice = product.select(".c-price-container--quick-view-price-final").attr("data-price-value").trim();
                            digiStyle.productDiscountPercentage = product.select(".c-product__image-gallery-discount").text().trim();
                            digiStyle.productBreadcrumb = product.select(".c-breadcrumb li span").text().replace("بازگشت یه نتایج", "").trim();
                            digiStyle.productBrand = product.select(".c-product__details-title").text().trim();

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();

                            String output = gson.toJson(digiStyle);

                            originalOut.println(output);
                            System.out.println(output);
                        }
                    }
                }
                limit++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllLinks(String filePath) throws IOException {
        File anyFile = new File(filePath);
        FileInputStream fis = new FileInputStream(anyFile);
        Document parsedXml = Jsoup.parse(fis, null, "", Parser.xmlParser());
        Elements locs = parsedXml.getElementsByTag("loc");
        List<String> list = new ArrayList<>();
        for (Element loc : locs) {
            list.add(loc.text());
        }
        return list;
    }

}

