package com.example.stuart.testingremotedatasource;

import android.util.Log;

import java.io.IOException;
import java.util.Queue;

import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

//Allows logging with logcat
import android.util.Log;

/**
 * Created by Stuart on 06/03/2015.
 */
public class RemoteDataSource {

	//Stores a reference to itself to implement the singleton design pattern
	private static RemoteDataSource instance = null;

	//The defualt URL where this RemoteDataSource will go to first
	private String baseUrl;
	//A Queue of String URL requests from apps
	private Queue resourceQueue;
	//A list of Documents requested and returned for apps
	private ArrayList<Document> files;
	//An array containing the most recently requested resources
	private String[] currentUrl;
	//A HTTP request that is used to connect to remote data sources
	private DefaultHttpClient httpClient;
	//A poster that posts a request to a resource though a DefaultHttpRequest
	private HttpPost httpPoster;
	//Returns the http request from a remote source
	private HttpResponse httpResponse;
	//The Content of the response of the request
	private HttpEntity responseContent;

	//The factory that creates concrete Document objects
	private DocumentBuilderFactory documentFactory;
	private DocumentBuilder documentBuilder;

	//The current xml file that is being received
	private Document currentXml;



	/**
	 * Default constructor. Constructs a new RemoteDataSource Singleton
	 * with a baseUrl of "https://www.uea.ac.uk/~fqv12suu/default.rss"
	 */
	protected RemoteDataSource(){
		try{
			this.baseUrl = "http://stuartfrost.me/wp-content/uploads/2015/02/ActivityFinderData.xml";
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Gets current instanciation of the RemoteDataSource
	 * @return			the current RemoteDataSourceSingleton
	 */
	public static RemoteDataSource getInstance(){
		if(instance == null){
			instance = new RemoteDataSource();
		}
		return instance;
	}

	/**
	 * Takes a single URL, retrieves the XML file from that location,
	 * wraps it up in a Document and returns it. Also sets currentUrl
	 * and currentXml in the RemoteDataSource
	 *
	 * @param url			A String representing the URL of the data source
	 * @return				A Document wrapping the XML file.
	 */
	public Document getResource(String url){
		try {
			if (documentFactory == null) {
				documentFactory = DocumentBuilderFactory.newInstance();
			}
			if(httpClient == null){
				httpClient = new DefaultHttpClient();
			}
			//Get the XML file from remote
			httpPoster = new HttpPost(url);
			httpResponse = httpClient.execute(httpPoster);
			responseContent = httpResponse.getEntity();
			currentUrl[0] = EntityUtils.toString(responseContent);

			//Wrap it in a Document
			currentXml = null;
			documentBuilder = documentFactory.newDocumentBuilder();
			InputSource input = new InputSource();
			input.setCharacterStream(new StringReader(currentUrl[0]));
			currentXml = documentBuilder.parse(input);
		}
		//Catches exceptions from the DocumentBuilder instansiation
		catch(ParserConfigurationException e){
			Log.d("RDS getResource Error: ", e.getMessage());
			return null;
		}
		//IO and SAX exceptions from creating the Document and Parsing
		catch(SAXException e){
			Log.d("RDS getResource Error: ", e.getMessage());
			return null;
		}
		catch(IOException e){
			Log.d("RDS getResource Error", e.getMessage());
			return null;
		}
		return currentXml;
	}

	public final String getElementValue(Node element){
		Node child;
		if(element != null){
			if(element.hasChildNodes()){
				for(child = element.getFirstChild(); child != null; child = child.getNextSibling()){
					if(child.getNodeType() == Node.TEXT_NODE){
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public String getValue(Element item, String str){
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

}