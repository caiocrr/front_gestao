package eel418_2016_1.DAO;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import com.google.common.base.Function;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import eel418_2016_1.DTOs.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

public class ReferenciasBibliograficas extends BaseDAO {

//------------------------------------------------------------------------------    
    public ArrayList<Produto> buscarLista(RequestBusca dados) {
        System.out.println(dados.toString());
        ArrayList<Produto> lista = new ArrayList<>();
        Produto ref = null;

        String[] palavrasDoTituloBusca = extrairPalavrasTituloBusca(dados.getTitulo());

        String preparedStatement = prepararComandoSQL(palavrasDoTituloBusca);

        try (Connection conexao = getConnection()) {
            PreparedStatement comandoSQL = conexao.prepareStatement(preparedStatement);

            int k = 0;

            for (int i = 0; i < palavrasDoTituloBusca.length; i++) {
                comandoSQL.setString(i + k + 1, "%" + palavrasDoTituloBusca[i] + "%");
            }

            System.out.println(comandoSQL.toString());
            ResultSet rs = comandoSQL.executeQuery();
            while (rs.next()) {
                ref = new Produto();
                ref.setTitulo(rs.getString("nome"));
                ref.setURLFarmacia(rs.getString("url_farmacia"));
                ref.setUrl(rs.getString("link"));
                ref.setLinkimg(rs.getString("linkimg"));
                ref.setPreco(String.format("%.2f", rs.getDouble("preco")));
                ref.setNomeFarmacia(rs.getString("nome_farmacia"));

//                System.out.println(String.valueOf(rs.getDouble("preco")));
                lista.add(ref);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return lista;
    }
//------------------------------------------------------------------------------    

    private String[] extrairPalavrasTituloBusca(String busca) {
        busca = Utils.removeDiacriticals(busca);
        String[] temp = busca.split(" ");
        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].trim();
        }
        return temp;
    }

//------------------------------------------------------------------------------    
    private String prepararComandoSQL(String[] palavrasDoTituloBusca) {

        String inicioSelectExterno
                = "SELECT T1.nome, T1.preco, T1.link, T1.linkimg, T3.imgurl AS url_farmacia, T3.nome AS nome_farmacia, (count(*)) AS nrohits \n"
                + "FROM precos T1 \n"
                + "INNER JOIN palavrasprodutonormal T2 ON(T1.id = T2.pid) "
                + "INNER JOIN farmacias T3 ON(T1.id_farmacia = T3.id_farmacia)"
                + "WHERE \n";

        String finalSelectExterno
                = "GROUP BY T1.nome, T1.preco, T1.link, T1.linkimg, T3.imgurl, T3.nome ORDER BY nrohits DESC, preco ASC;";

        String baseComandoTitulo = "T2.palavra_produto_normal LIKE ? \n";
        String comando = "";

        for (int i = 0; i < palavrasDoTituloBusca.length; i++) {
            comando = comando + baseComandoTitulo;
            if (i < (palavrasDoTituloBusca.length - 1)) {
                comando = comando + "OR \n";
            }
        }

        String finalcomando = inicioSelectExterno + comando + finalSelectExterno;

        return finalcomando;
    }
//------------------------------------------------------------------------------
//
//    public String calcularFrete(String URL, String CEP, String nomefarmacia) {
//        switch (nomefarmacia) {
//            case "Ultra Farma":
//                return freteUltraFarma(URL, CEP);
//            case "Drogarias Pacheco":
//                return fretePacheco(URL, CEP);
//            case "Drogaria Venancio":
////                return freteUltraFarma(URL,CEP);
//            case "Farmagora":
////                return freteUltraFarma(URL,CEP);
//        }
//        return "";
//
//    }

//    public String freteUltraFarma(String URL, String CEP) {
//
//        JBrowserDriver driver = new JBrowserDriver(Settings.builder().
//                timezone(Timezone.AMERICA_LIMA).build());
//
//        driver.get(URL);
//
//        //ADD TO CART
//        ((JavascriptExecutor) driver).executeScript("$('form[name=frm_detalhes]').submit();");
//
////        waitForPageLoad(driver);
////      Esperar carregar o inputtxt
//        WebDriverWait wait = new WebDriverWait(driver, 20);
//        wait.until(presenceOfElementLocated(By.id("cep_pagina")));
//
//        //mudar o valor do input pro CEP do cliente
//        WebElement CEPText = driver.findElement(By.id("cep_pagina"));
//        CEPText.sendKeys(CEP);
//
//        //Calcular Frete
//        ((JavascriptExecutor) driver).executeScript("atualizar_cep('pagina');");
//
//        WebDriverWait wait1 = new WebDriverWait(driver, 20);
//        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("valor_frete_pagina")));
//
////        try {
////            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
////
////            // Now you can do whatever you need to do with it, for example copy somewhere
////            FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
////        } catch (IOException ex) {
////            Logger.getLogger(ReferenciasBibliograficas.class.getName()).log(Level.SEVERE, null, ex);
////        }
//        //pegar o texto da div;
//        WebElement divResp = driver.findElement(By.id("valor_frete_pagina"));
//        String resp = divResp.getText().split(":")[1].trim();
//        driver.quit();
//
//        return resp;
//    }
//    public String freteUltraFarma(String URL, String CEP) {
//
//        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
//        HtmlPage page;
//        try {
//            Long time1 = System.currentTimeMillis();
//
//            page = webClient.getPage(URL);
//
//            Long time2 = System.currentTimeMillis();
//
//            String javaScriptCode = "$('form[name=frm_detalhes]').submit();";
//            page.executeJavaScript(javaScriptCode);
//
//            Long time3 = System.currentTimeMillis();
//
//            HtmlPage result = webClient.getPage("http://ultrafarma.com.br/minha_cesta/ajax_cep/3/" + CEP);
//
//            Long time4 = System.currentTimeMillis();
//
//            String resultString = result.getBody().asText();
//            webClient.close();
//
//            Long a = time2 - time1;
//            Long b = time3 - time2;
//            Long c = time4 - time3;
//
//            System.out.println(a + " " + b + " " + c);
//            return resultString;
//
//        } catch (IOException ex) {
//            Logger.getLogger(ReferenciasBibliograficas.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (FailingHttpStatusCodeException ex) {
//            Logger.getLogger(ReferenciasBibliograficas.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return "";
//    }
//
//    public String fretePacheco(String URL, String CEP) {
//        try {
//            Document doc = Jsoup.connect(URL).get();
//
//            Element div = doc.select("div.buy_box.flt_right").first();
//            Element div2 = div.select("div.compra_segura.flt_right").first();
//            Element a = div2.select("a").first();
//            String href = a.attr("abs:href");
//            System.out.println(href);
//
//            WebClient webClient = new WebClient(BrowserVersion.CHROME);
//            webClient.getOptions().setJavaScriptEnabled(false);
//            HtmlPage page;
//            try {
//                page = webClient.getPage(href);
//                
//                page = webClient.getPage("https://www.drogariaspacheco.com.br/checkout/#/cart");
//                
//                System.out.println(page.getWebResponse().getContentAsString());
//                
//                HtmlInput intputBox = (HtmlInput) page.getHtmlElementById("summary-postal-code");
//                intputBox.setValueAttribute(CEP);
//                
//                HtmlElement button = page.getHtmlElementById("//*[@id=\'cart-shipping-calculate\']") ;
//                page = button.click();
//                
//                
//                HtmlElement result = page.getHtmlElementById("/html/body/div[4]/div[2]/div[1]/div[3]/div[1]/div[2]/div[1]/div[2]/div/table/tbody[1]/tr[3]/td[3]") ;
//                
//                System.out.println(result.getTextContent());
//                
//            } catch (IOException ex) {
//                Logger.getLogger(ReferenciasBibliograficas.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (FailingHttpStatusCodeException ex) {
//                Logger.getLogger(ReferenciasBibliograficas.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            System.out.println(href);
//
////            return href;
//        } catch (IOException ex) {
//            Logger.getLogger(ReferenciasBibliograficas.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return "";
//    }

    private class JsonResponseWebWrapper extends WebConnectionWrapper {

        public JsonResponseWebWrapper(WebClient webClient) {
            super(webClient);
        }

        String jsonResponse;

        @Override
        public WebResponse getResponse(WebRequest request) throws IOException {;
            WebResponse response = super.getResponse(request);
            //extract JSON from response
            jsonResponse = response.getContentAsString();
            return response;
        }

        public String getJsonResponse() {
            return jsonResponse;
        }
    };

    private static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        return new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        };
    }

    public void waitForPageLoad(JBrowserDriver driver) {

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                System.out.println("Current Window State       : "
                        + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });
    }
}
