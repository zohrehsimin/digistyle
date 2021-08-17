package com.mycompany.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.parser.Parser;

import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

public class App {
    public static void main(String[] args) {

        try {
            File file = new File(System.getProperty("user.dir") + "//target//mahi.xml");
            FileInputStream fis = new FileInputStream(file);

            PrintStream originalOut = System.out;
            PrintStream fileOut = new PrintStream("./out.tsv");
            System.setOut(fileOut);

            Document mainDoc = Jsoup.parse(fis, null, "", Parser.xmlParser());

            for (Element e : mainDoc.getElementsByTag("loc")) {
                Document doc = Jsoup.connect(e.text()).get();
                Elements seaFoods = doc.select(".summary");
                for (Element seaFood : seaFoods) {
                    originalOut.println(seaFood.getElementsByTag("h1").text() + " " + seaFood.getElementsByTag("bdi").text() + "\n");
                    System.out.println(seaFood.getElementsByTag("h1").text() + " " + seaFood.getElementsByTag("bdi").text() + "\n");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

