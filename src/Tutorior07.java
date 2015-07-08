import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;


public class Tutorior07 {
	public static String readFile2(String filePath) {
		String fileContent = "";
                //目标地址
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				try {
                                        //循环，每次读一行
					while ((line = reader.readLine()) != null) {
						fileContent += line;
					}
					reader.close();
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return fileContent;
	}
	public static void main (String args[]) {
	// Make a TDB-backed dataset
		
	  String directory = "D:/freebase/" ;
	  Dataset dataset = TDBFactory.createDataset(directory) ;
	  dataset.begin(ReadWrite.READ) ;
	  // Get model inside the transaction
	  Model model = dataset.getDefaultModel() ;
	 /*model.write(System.out,"N-TRIPLES");
	 // String queryString = "SELECT ?x "
	 // 		+ "WHERE {<http://dbpedia.org/resource/List_of_S.W.A.T._episodes>  <http://dbpedia.org/property/writtenby> ?x }" ;*/
	  String queryString = readFile2("Query.txt");
	  System.out.println(queryString);
	  Query query = QueryFactory.create(queryString) ;
	  ResultSet results;
	  double start1 = System.currentTimeMillis();
	  double start2, end1, end2, end3;
	  OutputStream outS = null;
	  try {
		outS = new FileOutputStream("result.txt");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
	    results = qexec.execSelect() ;
		end1 = System.currentTimeMillis();
	    for(;results.hasNext();) results.next();
	      end2 = System.currentTimeMillis();
	  }
	  start2 = System.currentTimeMillis();
	  try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
		    results = qexec.execSelect() ;
		      ResultSetFormatter.out(outS, results, query);
		    end3 = System.currentTimeMillis();
		  }
	  dataset.end() ;
	  System.out.println("查询时间 "+(end1-start1)/1000+"s");
	  System.out.println("只遍历时间 "+(end2-start1)/1000+"s");
	  System.out.println("写文件时间 "+(end3-start2)/1000+"s");
	}
}
