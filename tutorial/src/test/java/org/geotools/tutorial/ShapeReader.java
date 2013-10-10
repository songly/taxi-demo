package org.geotools.tutorial;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;


public class ShapeReader {
	  private static URL getResource(String path) {
	        return ShapeReader.class.getClassLoader().getResource(path);
	        
	    }

	    public static void main(String[] args) {
	    	try {
	    		 
	            String newStoreURLString = ""; //url for output shapefile
	 
	            // get the shapefile URL by either loading it from the file system
	            // or from the classpath
	            URL shapeURL = null;
	            JFileChooser fileChooser = new JFileChooser();
	            FileNameExtensionFilter filter = new FileNameExtensionFilter(
	                    "shp","shp");
	            fileChooser.setFileFilter(filter);
	 
	            int result = fileChooser.showOpenDialog(null);
	 
	            if (result == JFileChooser.APPROVE_OPTION) {
	                File f = fileChooser.getSelectedFile();
	                shapeURL = f.toURL();
	                // generate new shapefile filename by prepending "new_"
	                newStoreURLString =
	                    shapeURL.toString().substring(0,shapeURL.toString().lastIndexOf("/") + 1)
	                    + "new_" +
	                    shapeURL.toString().substring(shapeURL.toString().lastIndexOf("/") + 1);
	            }
	 
	            // get feature results
	            ShapefileDataStore store = new ShapefileDataStore(shapeURL);
	            // feature type name is defaulted to the name of shapefile (without extension)
	            String name = store.getTypeNames()[0];
	            FeatureSource source = store.getFeatureSource(name);
	            FeatureCollection collection = source.getFeatures();
	 
	            // print out total number of features
	            System.out.println(collection.size() + " features found in shapefile.");
	            
	            FeatureIterator iterator=collection.features();
	 
	            // get feature type to create new shapefile
	            FeatureType ft = source.getSchema();
	            
	            System.out.println(ft.toString());	            
	            
	            while(iterator.hasNext()){
	            	Feature feature=(Feature)iterator.next();

	            	Collection<Property> properties=feature.getProperties();
	            	Iterator<Property> it=properties.iterator();
	            	while(it.hasNext()){
	            		Property p=it.next();
	            		if(p.getValue() instanceof MultiLineString){
	            			MultiLineString line=(MultiLineString) p.getValue();
	            			System.out.println("Segment: MultiLineString (Centroid: X "+line.getCentroid().getX()+"  Y "+line.getCentroid().getY() +")");
	            			
	            			int pointNums=line.getNumPoints();
	            			for(int i=0;i<pointNums;i++){
	            				Coordinate[] points=line.getCoordinates();
	            				for(Coordinate point : points){
	            					//Do sth. with the point on the line
	            					//System.out.println("Point:"+point.x+"  "+point.y);
	            				}
	            			}
	            		}
	            		else{
	            			String names=p.getName().toString();
	            			String value=p.getValue().toString();
	            			System.out.println(names+" "+value);
	            		}
	            	}
	            }
	            iterator.close();
	 
	            System.out.println();
	 
	       
	        } catch (Exception e) {
	            System.out.println("Ops! Something went wrong :-(");
	            e.printStackTrace();
	        }
	 
	        System.exit(0);
	    }
	}