package com.salt.YahooFinanceParse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;

public class YahooFinanceParseActivity extends Activity implements OnLongClickListener, android.view.View.OnClickListener
{

   // EXAMPLE XPATH QUERIES IN THE FORM OF STRINGS - WILL BE USED LATER

   // private static final String NAME_XPATH = "//div[@class='yfi_quote']/div[@class='hd']/h2";
   //    
   // private static final String TIME_XPATH = "//table[@id='time_table']/tbody/tr/td[@class='yfnc_tabledata1']";
   //    
   // private static final String PRICE_XPATH = "//table[@id='price_table']//tr//span";
   //    
   // example XPATH queries in the form of strings – will be used later
   private static final String NAME_XPATH  = "//div[@id='yfi_investing_head']/h1";
   // private static final String TIME_XPATH = "//div[@class='yfi_quote_summary']/p/span[@class='time']/span";
   private static final String TIME_XPATH  = "//div[@id='yfi_investing_head']/div[2]/small/span";
   private static final String PRICE_XPATH = "//div[@id='yfi_quote_summary_data']/table[@id='table1']/tbody//big/b/span";

   // TAGNODE OBJECT, ITS USE WILL COME IN LATER

   private static TagNode      node;

   private EditText            editResult  = null;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      Button button = (Button) findViewById(R.id.searchbutton);
      button.setOnClickListener(this);

      this.editResult = (EditText) findViewById(R.id.editTextStockSelected);

   }

   // A METHOD THAT HELPS ME RETRIEVE THE STOCK OPTION'S DATA BASED OFF THE NAME (I.E. GOUAA IS ONE OF GOOGLE'S STOCK OPTIONS)

   public static Option getOptionFromName(String name) throws XPatherException, ParserConfigurationException, SAXException, IOException, XPatherException
   {

      // THE URL WHOSE HTML I WANT TO RETRIEVE AND PARSE

      String option_url = "http://finance.yahoo.com/q?s=" + name.toUpperCase();

      // THIS IS WHERE THE HTMLCLEANER COMES IN, I INITIALIZE IT HERE

      HtmlCleaner cleaner = new HtmlCleaner();

      CleanerProperties props = cleaner.getProperties();

      props.setAllowHtmlInsideAttributes(true);

      props.setAllowMultiWordAttributes(true);

      props.setRecognizeUnicodeChars(true);

      props.setOmitComments(true);

      // OPEN A CONNECTION TO THE DESIRED URL

      URL url = new URL(option_url);

      URLConnection conn = url.openConnection();

      // USE THE CLEANER TO "CLEAN" THE HTML AND RETURN IT AS A TAGNODE OBJECT

      node = cleaner.clean(new InputStreamReader(conn.getInputStream()));

      // ONCE THE HTML IS CLEANED, THEN YOU CAN RUN YOUR XPATH EXPRESSIONS ON THE NODE, WHICH WILL THEN RETURN AN ARRAY OF TAGNODE OBJECTS (THESE ARE RETURNED AS OBJECTS BUT GET CASTED BELOW)

      Object[] info_nodes = node.evaluateXPath(NAME_XPATH);

      Object[] time_nodes = node.evaluateXPath(TIME_XPATH);

      Object[] price_nodes = node.evaluateXPath(PRICE_XPATH);

      // HERE I JUST DO A SIMPLE CHECK TO MAKE SURE THAT MY XPATH WAS CORRECT AND THAT AN ACTUAL NODE(S) WAS RETURNED

      Option o = new Option();

      if (info_nodes.length > 0)
      {

         // CASTED TO A TAGNODE

         TagNode info_node = (TagNode) info_nodes[0];

         // HOW TO RETRIEVE THE CONTENTS AS A STRING

         String info = info_node.getChildren().iterator().next().toString().trim();

         System.out.println("info:=" + info);

         // SOME METHOD THAT PROCESSES THE STRING OF INFORMATION (IN MY CASE, THIS WAS THE STOCK QUOTE, ETC)

         // processInfoNode(o, info);

      }

      if (time_nodes.length > 0)
      {

         TagNode time_node = (TagNode) time_nodes[0];

         String date = time_node.getChildren().iterator().next().toString().trim();

         // DATE RETURNED IN 15-JAN-10 FORMAT, SO THIS IS SOME METHOD I WROTE TO JUST PARSE THAT STRING INTO THE FORMAT THAT I USE

         // processDateNode(o, date);

      }

      if (price_nodes.length > 0)
      {

         TagNode price_node = (TagNode) price_nodes[0];

         double price = Double.parseDouble(price_node.getChildren().iterator().next().toString().trim());

         o.setPremium(price);
         System.out.println("price:=" + price);

      }

      return o;

   }

   @Override
   public boolean onLongClick(View paramView)
   {
      try
      {
         Option result = getOptionFromName("APBR.BA");
         System.out.println("Option.price:= " + result.getPrice());
         System.out.println("Option:= " + result);
         editResult.append(result.toString());

      }
      catch (XPatherException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (ParserConfigurationException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (SAXException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return false;

   }

   @Override
   public void onClick(View paramView)
   {
      try
      {
         Option result = getOptionFromName("APBR.BA");
         System.out.println("Option.price:= " + result.getPrice());
         System.out.println("Option:= " + result);
         editResult.append(result.toString());
      }
      catch (XPatherException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (ParserConfigurationException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (SAXException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}