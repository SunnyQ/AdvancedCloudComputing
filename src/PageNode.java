import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wei on 9/13/14.
 */
public class PageNode {
	private String nid;
	private List<String> adjList;
	private double pageRank;
	private boolean isDangling;

	public PageNode (String inputLineStr) {
		this.isDangling = false;
		this.adjList = new ArrayList<String>();

		this.nid = inputLineStr.split("\t")[0];
		System.out.println("line: "+inputLineStr);
		System.out.println("nid: "+this.nid);
		this.parseLinkList(inputLineStr.substring(this.nid.length()));
	}

	private void parseLinkList (String jsonStr) {
		com.fasterxml.jackson.core.JsonParser jp;

		JsonFactory jf = new JsonFactory();

		try {
			jp = jf.createParser(jsonStr);
			jp.nextToken(); // Will return JsonToken.START_OBJECT.

			while (jp.nextToken() != JsonToken.END_OBJECT) {
				String fieldName = jp.getCurrentName();
				jp.nextToken();

				if ("content-type".equals(fieldName)){
					jp.nextToken(); // Skip END_OBJECT of http_headers
				} else if ("links".equals(fieldName)) {
					if (jp.nextToken() == JsonToken.END_ARRAY) {
						this.isDangling = true;
						break;
					} else {
						do {
							if ("href".equals(jp.getCurrentName())) {
								jp.nextToken();
								this.adjList.add(jp.getText());
							}
						} while (jp.nextToken() != JsonToken.END_ARRAY);
						break; // Finish parsing outgoing links.
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Getters and setter.
	 */
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public List<String> getAdjList() {
		return adjList;
	}

	public void setAdjList(List<String> adjList) {
		this.adjList = adjList;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public boolean isDangling() {
		return isDangling;
	}

	public void setDangling(boolean isDangling) {
		this.isDangling = isDangling;
	}

	public static void main(String[] args) {
		File file = new File("/Users/parasitew/Documents/DataSet/metadata");
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			String str = dis.readLine();
			PageNode node = new PageNode(str);
			System.out.println(node.getNid());

			int cnt = 1;
			for (String s : node.getAdjList()) {
				System.out.println(cnt + ": " + s);
				cnt++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
